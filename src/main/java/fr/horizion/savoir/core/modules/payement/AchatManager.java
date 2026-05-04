package fr.horizion.savoir.core.modules.payement;

import fr.horizion.savoir.core.modules.payement.SubPayement.PaymentFactory;
import fr.horizion.savoir.core.modules.payement.SubPayement.PaymentResult;
import fr.horizion.savoir.models.Etudiant;
import fr.horizion.savoir.models.PaymentProcessor;
import fr.horizion.savoir.models.formation.Formartion;

public class AchatManager {

	private final PaymentFactory paymentFactory;

	public AchatManager() {
		this(new PaymentFactory());
	}

	public AchatManager(PaymentFactory paymentFactory) {
		this.paymentFactory = paymentFactory;
	}

	public PaymentResult acheter(Formartion formation, Etudiant etudiant, String paymentType, long amount, String currency, String orderId) {
		if (formation == null || etudiant == null) {
			return new PaymentResult(false, null);
		}

		try {
			PayementStrategy strategy = paymentFactory.createPaymentStrategy(paymentType);
			PaymentProcessor processor = new PaymentProcessor(strategy);
			PaymentResult result = processor.processWithResult(amount, currency, orderId);

			if (result.isSuccess()) {
				formation.inscrireEleve(etudiant);
			}

			return result;
		} catch (IllegalArgumentException exception) {
			return new PaymentResult(false, null);
		}
	}
}
