package cz.martinbayer.toggletest.vatvalidator.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.reficio.ws.builder.SoapBuilder;
import org.reficio.ws.builder.SoapOperation;
import org.reficio.ws.builder.core.Wsdl;
import org.reficio.ws.client.core.SoapClient;

import cz.martinbayer.toggletest.vatvalidator.exceptions.InvalidResponseException;

public class VatValidatorServiceImpl implements VatValidatorService {
	private static final String WSDL_DESCRIPTOR_ADDRESS = "http://ec.europa.eu/taxation_customs/vies/checkVatService.wsdl";
	private static final String CHECK_VAT_SERVICE_ADDRESS = "http://ec.europa.eu/taxation_customs/vies/services/checkVatService";
	private static final String CHECK_VAT_BINDING = "checkVatBinding";
	private static final String CHECK_VAT = "checkVat";
	private static final String COUNTRY_CODE_START_TAG = "<urn:countryCode>";
	private static final String COUNTRY_CODE_END_TAG = "</urn:countryCode>";
	private static final String COUNTRY_CODE_REPLACE_QUERY = COUNTRY_CODE_START_TAG + ".+" + COUNTRY_CODE_END_TAG;
	private static final String VAT_NUMBER_START_TAG = "<urn:vatNumber>";
	private static final String VAT_NUMBER_END_TAG = "</urn:vatNumber>";
	private static final String VAT_NUMBER_REPLACE_QUERY = VAT_NUMBER_START_TAG + ".+" + VAT_NUMBER_END_TAG;
	private static final String VALID_TAG_IN_RESPONSE = "\\<valid\\>(\\w+)\\</valid\\>";
	private static final String TRUE_STRING = "true";
	private static final String FALSE_STRING = "false";

	@Override
	public boolean isVatValid(final String vatValue) throws InvalidResponseException {
		final String countryCode = vatValue.substring(0, 2);
		final String vatNumber = vatValue.substring(2);
		final Wsdl wsdl = Wsdl.parse(WSDL_DESCRIPTOR_ADDRESS);

		final SoapBuilder builder = wsdl.binding().localPart(CHECK_VAT_BINDING).find();
		final SoapOperation operation = builder.operation().name(CHECK_VAT).soapAction("").find();

		String request = builder.buildInputMessage(operation);
		request = request.replaceFirst(COUNTRY_CODE_REPLACE_QUERY,
				COUNTRY_CODE_START_TAG + countryCode + COUNTRY_CODE_END_TAG);
		request = request.replaceFirst(VAT_NUMBER_REPLACE_QUERY, VAT_NUMBER_START_TAG + vatNumber + VAT_NUMBER_END_TAG);

		final SoapClient client = SoapClient.builder().endpointUri(CHECK_VAT_SERVICE_ADDRESS).build();
		final String response = client.post(request);
		final Pattern p = Pattern.compile(VALID_TAG_IN_RESPONSE);
		final Matcher m = p.matcher(response);
		if (m.find()) {
			final String validValue = m.group(1);
			if (TRUE_STRING.equals(validValue)) {
				return true;
			} else if (FALSE_STRING.equals(validValue)) {
				return false;
			} else {
				throw new InvalidResponseException("true nor false value returned in the response: " + response);
			}
		}
		return false;
	}

}
