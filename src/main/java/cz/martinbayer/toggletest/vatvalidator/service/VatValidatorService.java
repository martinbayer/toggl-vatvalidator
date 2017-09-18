package cz.martinbayer.toggletest.vatvalidator.service;

import cz.martinbayer.toggletest.vatvalidator.exceptions.InvalidResponseException;

public interface VatValidatorService {

	/**
	 * @param vatValue
	 *            to be validated
	 * @return <code>true</code> if VAT is valid, <code>false</code> if VAT is
	 *         invalid
	 * @throws InvalidResponseException
	 *             is throws if the response does not contain required value
	 */
	boolean isVatValid(final String vatValue) throws InvalidResponseException;
}
