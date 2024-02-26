package com.in28minutes.microservices.currencyconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {

	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversion(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {
		
		/*
		 * > SPRING CLOUD gives you 2 classes, RestTemplate & ResponseEntity that you
		 * can use to to call a MicroService from another MicroService.
		 * 
		 * Here, we will call currency-exchange service from inside the call of
		 * currency-conversion service. For this, we need a RestTemplate object & a
		 * ResponseEntity object. 
		 * 
		 * The RestTemplate will call the currency-exchange API
		 * and the ResponseEntity will store the response of the API call. 
		 * 
		 * The response body in the ResponseEntity object can be stored in a currencyConversion
		 * object.
		 * 
		 * This currencyConversion object can then be returned as a response of this
		 * GetMapping method.
		 * 
		 * > HOWEVER, This introduces too many lines of code. Hence, 
		 * SPRING CLOUD introduces FEIGN REST CLIENT, that makes the job easier, with less LOC. 
		 * Check out CurrencyExchangeProxy interface and below method that does the same job with almost no LOC.
		 */
		 

		HashMap<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		
		// RestTemplate is used to make API calls
		ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables);

		CurrencyConversion currencyConversion = responseEntity.getBody();

		return new CurrencyConversion(currencyConversion.getId(), from, to, quantity,
				currencyConversion.getConversionMultiple(),
				quantity.multiply(currencyConversion.getConversionMultiple()), currencyConversion.getEnvironment()
				+ " -> " + " from Rest Template");
	}
	
	@Autowired
	private CurrencyExchangeProxy proxy; 
	
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {
		
		CurrencyConversion currencyConversion = proxy.retrieveExchangeValue(from, to);
		
		return new CurrencyConversion(currencyConversion.getId(), from, to, quantity,
				currencyConversion.getConversionMultiple(),
				quantity.multiply(currencyConversion.getConversionMultiple()), currencyConversion.getEnvironment()
				+ " -> " + " from Feign");
		
	}
	
}
