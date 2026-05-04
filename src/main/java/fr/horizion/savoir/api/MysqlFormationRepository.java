package fr.horizion.savoir.api;

import fr.horizion.savoir.core.modules.formation.ContentFactory;
import fr.horizion.savoir.core.modules.payement.AchatManager;
import fr.horizion.savoir.core.modules.payement.SubPayement.PaymentResult;
import fr.horizion.savoir.models.Etudiant;
import fr.horizion.savoir.models.formation.ContenuePedagogique;
import fr.horizion.savoir.models.formation.Formartion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MysqlFormationRepository implements FormationRepository {

    private final DatabaseConfig config;
    private final ContentFactory contentFactory = new ContentFactory();

    public MysqlFormationRepository(DatabaseConfig config) {
        this.config = config;
    }

    @Override
    public List<Formartion> findAll() throws SQLException {
        String sql = "SELECT id, titre, description, prix FROM formations ORDER BY id";
        try (Connection connection = config.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<Formartion> formations = new ArrayList<>();
            while (resultSet.next()) {
                formations.add(loadFormation(
                        connection,
                        resultSet.getInt("id"),
                        resultSet.getString("titre"),
                        resultSet.getString("description"),
                        resultSet.getFloat("prix")
                ));
            }
            return formations;
        }
    }

    @Override
    public Optional<Formartion> findById(int id) throws SQLException {
        String sql = "SELECT id, titre, description, prix FROM formations WHERE id = ?";
        try (Connection connection = config.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                return Optional.of(loadFormation(
                        connection,
                        resultSet.getInt("id"),
                        resultSet.getString("titre"),
                        resultSet.getString("description"),
                        resultSet.getFloat("prix")
                ));
            }
        }
    }

    @Override
    public Formartion create(ApiServer.CreateFormationRequest request) throws SQLException {
        String sql = "INSERT INTO formations (titre, description, prix) VALUES (?, ?, ?)";
        try (Connection connection = config.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, request.titre());
            statement.setString(2, request.description() != null ? request.description() : "");
            statement.setFloat(3, request.prix());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return findById(generatedKeys.getInt(1))
                            .orElseThrow(() -> new SQLException("Impossible de relire la formation créée"));
                }
            }
        }

        throw new SQLException("La création de la formation a échoué");
    }

    @Override
    public Formartion addStudent(int formationId, Etudiant student) throws SQLException {
        try (Connection connection = config.openConnection()) {
            int studentId = upsertStudent(connection, student);
            upsertEnrollment(connection, formationId, studentId);
            return findById(formationId).orElseThrow(() -> new SQLException("Formation introuvable"));
        }
    }

    @Override
    public Formartion addContent(int formationId, ApiServer.AddContentRequest request) throws SQLException {
        String sql = "INSERT INTO contenus_pedagogiques (formation_id, type_contenu, titre, est_termine, valeur_specifique, ordre_affichage) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = config.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, formationId);
            statement.setString(2, request.type());
            statement.setString(3, request.titre());
            statement.setBoolean(4, Boolean.TRUE.equals(request.estTermine()));
            statement.setInt(5, request.value());
            statement.setInt(6, nextContentOrder(connection, formationId));
            statement.executeUpdate();
        }

        return findById(formationId).orElseThrow(() -> new SQLException("Formation introuvable"));
    }

    @Override
    public Formartion completeContent(int formationId, String title) throws SQLException {
        String sql = "UPDATE contenus_pedagogiques SET est_termine = TRUE WHERE formation_id = ? AND titre = ?";
        try (Connection connection = config.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, formationId);
            statement.setString(2, title);
            statement.executeUpdate();
        }

        return findById(formationId).orElseThrow(() -> new SQLException("Formation introuvable"));
    }

    @Override
    public PurchaseResult recordPurchase(int formationId, Etudiant student, String paymentType, long amountCents, String currency, String orderId) throws SQLException {
        try (Connection connection = config.openConnection()) {
            connection.setAutoCommit(false);
            try {
                int studentId = upsertStudent(connection, student);

                AchatManager achatManager = new AchatManager();
                PaymentResult result = achatManager.acheter(
                        findById(formationId).orElseThrow(() -> new SQLException("Formation introuvable")),
                        student,
                        paymentType,
                        amountCents,
                        currency,
                        orderId
                );

                String paymentSql = "INSERT INTO paiements (formation_id, etudiant_id, order_id, provider, currency, amount_cents, transaction_id, success, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement paymentStatement = connection.prepareStatement(paymentSql)) {
                    paymentStatement.setInt(1, formationId);
                    paymentStatement.setInt(2, studentId);
                    paymentStatement.setString(3, orderId);
                    paymentStatement.setString(4, paymentType);
                    paymentStatement.setString(5, currency);
                    paymentStatement.setLong(6, amountCents);
                    paymentStatement.setString(7, result.getTransactionId());
                    paymentStatement.setBoolean(8, result.isSuccess());
                    paymentStatement.setString(9, result.isSuccess() ? "succeeded" : "failed");
                    paymentStatement.executeUpdate();
                }

                if (result.isSuccess()) {
                    upsertEnrollment(connection, formationId, studentId);
                }

                connection.commit();
                Formartion formation = findById(formationId).orElseThrow(() -> new SQLException("Formation introuvable"));
                return new PurchaseResult(result.isSuccess(), result.getTransactionId(), formation);
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private Formartion loadFormation(Connection connection, int formationId, String titre, String description, float prix) throws SQLException {
        return new Formartion(
                formationId,
                titre,
                description,
                prix,
                loadStudents(connection, formationId),
                loadContents(connection, formationId)
        );
    }

    private List<Etudiant> loadStudents(Connection connection, int formationId) throws SQLException {
        String sql = "SELECT e.nom, e.prenom, e.adresse, e.date_naissance, e.email FROM inscriptions i JOIN etudiants e ON e.id = i.etudiant_id WHERE i.formation_id = ? ORDER BY e.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, formationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Etudiant> students = new ArrayList<>();
                while (resultSet.next()) {
                    try {
                        students.add(new Etudiant(
                                resultSet.getString("nom"),
                                resultSet.getString("prenom"),
                                resultSet.getString("adresse"),
                                resultSet.getDate("date_naissance"),
                                resultSet.getString("email")
                        ));
                    } catch (Exception exception) {
                        throw new SQLException("Impossible de reconstruire un étudiant", exception);
                    }
                }
                return students;
            }
        }
    }

    private List<ContenuePedagogique> loadContents(Connection connection, int formationId) throws SQLException {
        String sql = "SELECT type_contenu, titre, est_termine, valeur_specifique FROM contenus_pedagogiques WHERE formation_id = ? ORDER BY ordre_affichage, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, formationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<ContenuePedagogique> contents = new ArrayList<>();
                while (resultSet.next()) {
                    ContenuePedagogique content = contentFactory.createContent(
                            resultSet.getString("type_contenu"),
                            resultSet.getString("titre"),
                            resultSet.getBoolean("est_termine"),
                            resultSet.getInt("valeur_specifique")
                    );
                    if (content != null) {
                        contents.add(content);
                    }
                }
                return contents;
            }
        }
    }

    private int upsertStudent(Connection connection, Etudiant student) throws SQLException {
        String selectSql = "SELECT id FROM etudiants WHERE email = ?";
        try (PreparedStatement select = connection.prepareStatement(selectSql)) {
            select.setString(1, student.getEmail());
            try (ResultSet resultSet = select.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        String insertSql = "INSERT INTO etudiants (nom, prenom, adresse, date_naissance, email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement insert = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, student.getNom());
            insert.setString(2, student.getPrenom());
            insert.setString(3, student.getAdresse());
            insert.setDate(4, new java.sql.Date(student.getDateNaissance().getTime()));
            insert.setString(5, student.getEmail());
            insert.executeUpdate();
            try (ResultSet generatedKeys = insert.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }

        throw new SQLException("Impossible d'enregistrer l'étudiant");
    }

    private void upsertEnrollment(Connection connection, int formationId, int studentId) throws SQLException {
        String sql = "INSERT INTO inscriptions (formation_id, etudiant_id, progression, statut) VALUES (?, ?, 0, 'active') ON DUPLICATE KEY UPDATE progression = progression";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, formationId);
            statement.setInt(2, studentId);
            statement.executeUpdate();
        }
    }

    private int nextContentOrder(Connection connection, int formationId) throws SQLException {
        String sql = "SELECT COALESCE(MAX(ordre_affichage), 0) + 1 AS next_order FROM contenus_pedagogiques WHERE formation_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, formationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("next_order");
                }
            }
        }
        return 1;
    }
}