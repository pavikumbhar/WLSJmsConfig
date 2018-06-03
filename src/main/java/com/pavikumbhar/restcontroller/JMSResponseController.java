package com.pavikumbhar.restcontroller;

import java.util.concurrent.TimeUnit;

import javax.jms.Destination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pavikumbhar.jms.sender.JMSMessageSender;


/**
 * 
 * @author pavikumbhar
 *
 */

@RestController
public class JMSResponseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JMSResponseController.class);

    @Autowired
    private JMSMessageSender jmsMessageSender;

    @Autowired
    private Destination mpiResponseQueue;
    
    @Autowired
    private Destination msdResponseQueue;
 
    
    /**
     * 
     * @param mpiResponseXml
     * @return
     */
    @RequestMapping(value = "/mpiResponse", method = RequestMethod.POST)
    ResponseEntity<String> responseMPIXml(@RequestBody String mpiResponseXml) {
    	 LOGGER.debug("Reciveing mpiResponseXml: {} " , mpiResponseXml);
    	 jmsMessageSender.sendMPIMessage(mpiResponseQueue, mpiResponseXml);
        return ResponseEntity.ok("Success");
    }
    
    /***
     * 
     * @param msdResponseXml
     * @return
     */
    @RequestMapping(value = "/msdResponse", method = RequestMethod.POST)
    ResponseEntity<String> responseMSDXml(@RequestBody String msdResponseXml) {
    	LOGGER.debug("Reciveing msdResponseXml : {} " ,msdResponseXml);
    	jmsMessageSender.sendMSDMessage(msdResponseQueue, msdResponseXml);
    
    	
        return ResponseEntity.ok("Success");
    }
    
    
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    ResponseEntity<String> responseTestXml(@RequestBody String responseTestXml) {
    	LOGGER.debug("###################### Reciveing xml :{}  " + responseTestXml);
    	   try {
   			TimeUnit.SECONDS.sleep(15);
   			LOGGER.debug("######################PROCESS COMPLETE : ");
   		} catch (InterruptedException e) {
   			e.printStackTrace();
   }
        return ResponseEntity.ok("Success");
    }
    
    
   
}