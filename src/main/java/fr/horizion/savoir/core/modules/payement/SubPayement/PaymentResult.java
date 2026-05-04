package fr.horizion.savoir.core.modules.payement.SubPayement;

public class PaymentResult {
    private boolean success;
    private String transactionId;

    public PaymentResult(boolean success, String transactionId) {
        this.success = success;
        this.transactionId = transactionId;
    }

    public boolean isSuccess() { return success; }
    public String getTransactionId() { return transactionId; }

    @Override
    public String toString() {
        return "PaymentResult{" +
                "success=" + success +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
