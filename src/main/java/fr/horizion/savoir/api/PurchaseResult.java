package fr.horizion.savoir.api;

import fr.horizion.savoir.models.formation.Formartion;

public record PurchaseResult(boolean success, String transactionId, Formartion formation) {
}