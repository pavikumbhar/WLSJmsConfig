package com.pavikumbhar.jms.sender;

import javax.jms.Destination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * 
 * @author pavikumbhar
 *
 */
@Component
public class JMSMessageSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(JMSMessageSender.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMPIMessage(Destination mpiResponseQueue, String message) {
    	LOGGER.debug("Sending response message to  mpi outbound queue:{} " , message);
        jmsTemplate.convertAndSend(mpiResponseQueue, message);
    }
    
    public void sendMSDMessage(Destination mpiResponseQueue, String message) {
    	LOGGER.debug("Sending response message to  msd outbound queue: {}" , message);
        jmsTemplate.convertAndSend(mpiResponseQueue, message);
    }


}
