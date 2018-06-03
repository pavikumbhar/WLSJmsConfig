package com.pavikumbhar.util;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 
 * @author pavikumbhar
 *
 */
public class RestClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestClient.class);

	/**
	 * 
	 * @param uri
	 * @param request
	 * @param bodyType
	 * @return
	 */
    public static <T,E>  E syncCall(String uri, T request, Class<E> bodyType ) {
        LOGGER.debug("RestClient.syncCall -Making sync rest call...  uri : {} request :{} " ,uri,request.toString());
        E response = WebClient.create().
        						post()
        						.uri(URI.create(uri))
        						.body(BodyInserters.fromObject(request))
        						.retrieve().
        						 bodyToMono(bodyType) //The method bodyToMono() extracts the body to a Mono instance
        						.block(); //The method Mono.block() subscribes to this Mono instance and blocks until the response is received. 
        LOGGER.debug("RestClient.makeRestCall() Sync Rest call completed...");
        LOGGER.debug(response.toString());
        return response;
    }

    /**
     * 
     * @param uri
     * @param request
     * @param bodyType
     */
    public static   <T,E> void asyncCall(String uri, T request, Class<E> bodyType ) {
    	LOGGER.debug("RestClient.asyncCall() -Making async rest call...uri  : {} request :{} " ,uri, request.toString());
    	WebClient.create()
    	            .post()
    	            .uri(URI.create(uri))
    	            .body(BodyInserters.fromObject(request))
    	            .retrieve()
                    .bodyToMono(bodyType)
                    .subscribe();   //Mono.subscribe() returns immediately (as opposed to block() method which waits for the full response)
    	           // .subscribe(RestClient::handleResponse);
    	LOGGER.debug("RestClient.asyncCall() Async Rest call completed...");
       
/*     try {
			java.util.concurrent.TimeUnit.SECONDS.sleep(6);
		} catch (InterruptedException e) {
			e.printStackTrace();
}
   */
    }

    
    private static <E>void handleResponse(E response) {
        LOGGER.debug(response.toString());
    }


    public static void main(String[] args) {
        String requestXml = "<sample>Pravin Kumbhar </sample>";
        String uri = "http://localhost:8080/msdResponse";
        RestClient.asyncCall(uri,requestXml,String.class);
    }


}
