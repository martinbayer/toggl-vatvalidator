package cz.martinbayer.toggletest.vatvalidator;

import java.security.InvalidParameterException;

import cz.martinbayer.toggletest.vatvalidator.exceptions.InvalidResponseException;
import cz.martinbayer.toggletest.vatvalidator.service.VatValidatorService;
import cz.martinbayer.toggletest.vatvalidator.service.VatValidatorServiceImpl;

/**
 *
 */
public class App {
	private static final VatValidatorService VAT_VALIDATOR_SERVICE = new VatValidatorServiceImpl();

	public static void main(String[] args) {
		if (args.length > 0) {
			for (final String vatValue : args) {
				try {
					if (VAT_VALIDATOR_SERVICE.isVatValid(vatValue)) {
						System.out.println(vatValue + ": Valid");
					} else {
						System.out.println(vatValue + ": Invalid");
					}
				} catch (InvalidResponseException e) {
					e.printStackTrace();
				}
			}
		} else {
			throw new InvalidParameterException("No VAT provided as a parameter");
		}
	}
}
