package com.pavikumbhar.jms.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.pavikumbhar.util.RestClient;

/**
 * 
 * @author pavikumbhar
 *
 */
@Component
public class JMSMessageListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(JMSMessageListener.class);

	@Autowired
	private Environment environment;

	/**
	 * 
	 * @param requestXml
	 */
	@JmsListener(destination = "${mpi.request.queue}")
	public void mpiMesageRequest(String requestXml) {
		LOGGER.info("START OF mpiMesageRequest");
		LOGGER.debug("Reciveing request xml on MPI inbound queue: {} ", requestXml);
		LOGGER.debug("Calling MPI RestService ");
		String mpiRestBaseUrl = environment.getProperty("mpiRestBaseUrl") + "test";
		RestClient.asyncCall(mpiRestBaseUrl, requestXml, String.class);
		LOGGER.info("END OF mpiMesageRequest");
	}

	/**
	 * 
	 * @param requestXml
	 */
	@JmsListener(destination = "${msd.request.queue}")
	public void msdMesageRequest(String requestXml) {
		LOGGER.info("START OF msdMesageRequest");
		LOGGER.debug("Reciveing request xml on MSD inbound queue: {} ", requestXml);
		LOGGER.debug("Calling MSD RestService ");
		String msdRestBaseUrl = environment.getProperty("msdRestBaseUrl") + "msdRequest";
		RestClient.asyncCall(msdRestBaseUrl, requestXml, String.class);
		LOGGER.info("END OF msdMesageRequest");

	}

}
