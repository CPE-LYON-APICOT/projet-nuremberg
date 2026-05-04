package fr.horizion.savoir.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import fr.horizion.savoir.models.Etudiant;
import fr.horizion.savoir.models.formation.Formartion;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.sql.SQLException;

public class ApiServer {

    private final HttpServer server;
    private final ObjectMapper objectMapper;
    private final FormationRepository repository;

    public ApiServer(int port) throws IOException {
        this.objectMapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.repository = new MysqlFormationRepository(new DatabaseConfig());
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.server.createContext("/", this::handleUiRequest);
        this.server.createContext("/api", this::handleRequest);
        this.server.setExecutor(Executors.newCachedThreadPool());
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    private void handleUiRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.startsWith("/api")) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
            return;
        }

        String resourcePath;
        if ("/".equals(path) || "/web".equals(path) || "/web/".equals(path)) {
            resourcePath = "/web/index.html";
        } else if (path.startsWith("/web/")) {
            resourcePath = path;
        } else {
            resourcePath = "/web/index.html";
        }

        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                sendText(exchange, 200, loadIndexFallback());
                return;
            }

            byte[] bytes = inputStream.readAllBytes();
            String contentType = URLConnection.guessContentTypeFromName(resourcePath);
            if (contentType == null) {
                contentType = resourcePath.endsWith(".js") ? "application/javascript; charset=utf-8"
                        : resourcePath.endsWith(".css") ? "text/css; charset=utf-8"
                        : "text/html; charset=utf-8";
            }

            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(bytes);
            }
        }
    }

    private String loadIndexFallback() {
        return "<!doctype html><html><head><meta charset='utf-8'><meta name='viewport' content='width=device-width,initial-scale=1'><title>Horizon Savoir</title></head><body><h1>Horizon Savoir</h1><p>Le frontend n'a pas encore été compilé.</p></body></html>";
    }

    private void sendText(HttpExchange exchange, int statusCode, String content) throws IOException {
        byte[] bytes = content.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(bytes);
        }
    }

    private void handleRequest(HttpExchange exchange) throws IOException {
        applyCors(exchange);

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendNoContent(exchange);
            return;
        }

        String[] segments = extractSegments(exchange.getRequestURI().getPath());
        try {
            if (segments.length == 1 && "health".equals(segments[0]) && "GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJson(exchange, 200, Map.of("status", "ok"));
                return;
            }

            if (segments.length == 1 && "formations".equals(segments[0])) {
                handleFormationsCollection(exchange);
                return;
            }

            if (segments.length >= 2 && "formations".equals(segments[0])) {
                handleFormationResource(exchange, segments);
                return;
            }

            sendJson(exchange, 404, Map.of("error", "Route introuvable"));
        } catch (IllegalArgumentException exception) {
            sendJson(exchange, 400, Map.of("error", exception.getMessage()));
        } catch (Exception exception) {
            sendJson(exchange, 500, Map.of("error", "Erreur serveur", "details", exception.getMessage()));
        }
    }

    private void handleFormationsCollection(HttpExchange exchange) throws IOException {
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                sendJson(exchange, 200, repository.findAll());
            } catch (SQLException exception) {
                sendJson(exchange, 500, Map.of("error", exception.getMessage()));
            }
            return;
        }

        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            CreateFormationRequest request = readBody(exchange, CreateFormationRequest.class);
            try {
                Formartion formation = repository.create(request);
                sendJson(exchange, 201, formation);
            } catch (SQLException exception) {
                sendJson(exchange, 500, Map.of("error", exception.getMessage()));
            }
            return;
        }

        sendMethodNotAllowed(exchange, "GET, POST, OPTIONS");
    }

    private void handleFormationResource(HttpExchange exchange, String[] segments) throws IOException {
        if (segments.length < 2) {
            sendJson(exchange, 404, Map.of("error", "Formation introuvable"));
            return;
        }

        int formationId = Integer.parseInt(segments[1]);
        try {
            if (segments.length == 2 && "GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                Formartion formation = repository.findById(formationId)
                        .orElseThrow(() -> new IllegalArgumentException("Formation introuvable : " + formationId));
                sendJson(exchange, 200, formation);
                return;
            }

            if (segments.length == 3 && "progress".equals(segments[2]) && "GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                Formartion formation = repository.findById(formationId)
                        .orElseThrow(() -> new IllegalArgumentException("Formation introuvable : " + formationId));
                sendJson(exchange, 200, Map.of(
                        "formationId", formation.getId(),
                        "progression", formation.getTauxProgression(),
                        "contenusTotal", formation.getContenuePedagogiques().size()
                ));
                return;
            }

            if (segments.length == 3 && "students".equals(segments[2]) && "POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                StudentRequest request = readBody(exchange, StudentRequest.class);
                Etudiant student = request.toEtudiant();
                Formartion formation = repository.addStudent(formationId, student);
                sendJson(exchange, 200, formation);
                return;
            }

            if (segments.length == 3 && "contents".equals(segments[2]) && "POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                AddContentRequest request = readBody(exchange, AddContentRequest.class);
                Formartion formation = repository.addContent(formationId, request);
                sendJson(exchange, 200, formation);
                return;
            }

            if (segments.length == 4 && "contents".equals(segments[2]) && "complete".equals(segments[3]) && "POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                CompleteContentRequest request = readBody(exchange, CompleteContentRequest.class);
                Formartion formation = repository.completeContent(formationId, request.titre());
                sendJson(exchange, 200, Map.of(
                        "updated", true,
                        "progression", formation.getTauxProgression()
                ));
                return;
            }

            if (segments.length == 3 && "purchase".equals(segments[2]) && "POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                PurchaseRequest request = readBody(exchange, PurchaseRequest.class);
                Etudiant student = request.student().toEtudiant();
                PurchaseResult purchaseResult = repository.recordPurchase(
                        formationId,
                        student,
                        request.paymentType(),
                        request.amount(),
                        request.currency(),
                        request.orderId()
                );
                sendJson(exchange, 200, Map.of(
                    "success", purchaseResult.success(),
                    "transactionId", purchaseResult.transactionId(),
                    "formation", purchaseResult.formation()
                ));
                return;
            }
        } catch (SQLException exception) {
            sendJson(exchange, 500, Map.of("error", exception.getMessage()));
            return;
        }

        sendMethodNotAllowed(exchange, "GET, POST, OPTIONS");
    }

    private String[] extractSegments(String path) {
        String relativePath = path.startsWith("/api") ? path.substring(4) : path;
        return Arrays.stream(relativePath.split("/"))
                .filter(segment -> !segment.isBlank())
                .toArray(String[]::new);
    }

    private void sendNoContent(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(204, -1);
        exchange.close();
    }

    private void sendMethodNotAllowed(HttpExchange exchange, String allowMethods) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Allow", allowMethods);
        sendJson(exchange, 405, Map.of("error", "Méthode non autorisée"));
    }

    private void applyCors(HttpExchange exchange) {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        headers.add("Content-Type", "application/json; charset=utf-8");
    }

    private <T> T readBody(HttpExchange exchange, Class<T> type) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Corps de requête manquant");
            }

            return objectMapper.readValue(inputStream, type);
        }
    }

    private void sendJson(HttpExchange exchange, int statusCode, Object payload) throws IOException {
        byte[] responseBody = objectMapper.writeValueAsBytes(payload);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, responseBody.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBody);
        }
    }

    public record CreateFormationRequest(String titre, String description, float prix) {
    }

    public record AddContentRequest(String type, String titre, Boolean estTermine, int value) {
    }

    public record CompleteContentRequest(String titre) {
    }

    public record StudentRequest(String nom, String prenom, String adresse, String email, String dateNaissance) {
        private static final String DATE_PATTERN = "yyyy-MM-dd";

        public Etudiant toEtudiant() {
            try {
                return new Etudiant(nom, prenom, adresse, parseDate(dateNaissance), email);
            } catch (ParseException exception) {
                throw new IllegalArgumentException("Format de date invalide, attendu: yyyy-MM-dd", exception);
            } catch (Exception exception) {
                throw new IllegalArgumentException(exception.getMessage(), exception);
            }
        }

        private Date parseDate(String rawDate) throws ParseException {
            if (rawDate == null || rawDate.isBlank()) {
                return new Date();
            }

            SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN, Locale.FRANCE);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            return formatter.parse(rawDate);
        }
    }

    public record PurchaseRequest(String paymentType, long amount, String currency, String orderId, StudentRequest student) {
    }
}