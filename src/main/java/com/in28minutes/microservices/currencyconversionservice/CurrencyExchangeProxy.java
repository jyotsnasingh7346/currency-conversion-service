package com.in28minutes.microservices.currencyconversionservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name="currency-exchange", url="localhost:8000")
@FeignClient(name="currency-exchange")
public interface CurrencyExchangeProxy {
	
		/*
		 * SPRING CLOUD introduces FEIGN REST CLIENT, that makes the job easier, with less LOC. 
		 * 
		 * Step 01 : Start by including FEIGN dependency in the POM file. 
		 * Step 02 : Make a proxy (new interface - CurrencyExchangeProxy) 
		 * Step 03 : @EnableFeignClients for CurrencyConversionServiceApplication 
		 * Step 04 : In the Proxy class, @FeignClient(name="application-name", url="localhost:port on which 
		 * the desired application API is running") in the new interface CurrencyExchangeProxy.
		 * Step 05 : Copy the @GetMapping method API definition, 
		 * which needs to be called by our API, from the desired application and put it in the new interface.
		 * Step 06 : Launch both of the applications and hit our FEIGN API
		 */
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyConversion retrieveExchangeValue(@PathVariable String from, @PathVariable String to);
		
	/*
	 * However, there is still one problem. The URL.
	 * The URL is being hardcoded in the cide and its not the most optimal way to code.
	 * Suppose, there are multiple instances running on multiple ports, you would then need to 
	 * add all the urls. Everytime there is a new instance, you would have to add in the code, which is tedious.
	 * 
	 * For situations like this, we can introduce, a SERVICE REGISTRY (or a NAMING SERVER)
	 * 
	 * Now, all the instances of a Microservice, running on different ports, 
	 * would register themselves with the NAMING SERVER.
	 * All the microservices (in CurrencyExchangeMicroservice, CurrencyConversionMicroservice, etc) 
	 * will be registered with the NAMING SERVER.
	 * 
	 * > If the CurrencyConversionMicroservice wants to talk to the CurrencyExchangeMicroservice,
	 * it would ask the Naming Server, to provide the addresses of the CurrencyExchangeMicroservice.
	 * The Service Registry, returns those back to the CurrencyConversionMicroservice & then 
	 * the CurrencyConversionMicroservice can send a request to CurrencyExchangeMicroservice.
	 * 
	 * > If the CurrencyConversionMicroservice wants know about the active instances of the CurrencyExchangeMicroservice,
	 * It asks the Naming Server, and then LOAD BALANCES between them.
	 * 
	 */
	
	/*
	 * To Create a NAMING SERVER, one of the options that the Spring cloud provides is EUREKA.
	 * 
	 * Step 01 : On start.spring.io, 
	 * Create a project (NAMING SERVER) with the dependencies Devtools, Actuator, & 
	 * Eureka Server (spring-cloud-netflix Eureka Server). Generate & import in your workspace.
	 * (Make sure to put it on git)
	 * 
	 * Step 02 : @EnableEurekaServer on the naming-server.SpringBootApplication Launcher class.
	 * 
	 * Step 03 : Add required properties in application.properties
	 * 
	 * Step 04 : Launch the Application and in the browser (localhost:8761)
	 * 
	 */

}
