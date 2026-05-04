package fr.horizion.savoir.api;

import fr.horizion.savoir.models.Etudiant;
import fr.horizion.savoir.models.formation.Formartion;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface FormationRepository {

    List<Formartion> findAll() throws SQLException;

    Optional<Formartion> findById(int id) throws SQLException;

    Formartion create(ApiServer.CreateFormationRequest request) throws SQLException;

    Formartion addStudent(int formationId, Etudiant student) throws SQLException;

    Formartion addContent(int formationId, ApiServer.AddContentRequest request) throws SQLException;

    Formartion completeContent(int formationId, String title) throws SQLException;

    PurchaseResult recordPurchase(int formationId, Etudiant student, String paymentType, long amountCents, String currency, String orderId) throws SQLException;
}