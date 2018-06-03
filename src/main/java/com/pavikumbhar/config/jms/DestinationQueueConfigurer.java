package com.pavikumbhar.config.jms;

import javax.jms.Destination;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;



/**
 * 
 * @author pavikumbhar
 *
 */
@Configuration
@ComponentScan(basePackages = {"com.pavikumbhar" })
public class DestinationQueueConfigurer {
   
	@Autowired
	private JndiTemplate jndiTemplate;
	
	@Value("${mpi.response.queue}")
    private String mpiResponseQueue;
	
	@Value("${msd.response.queue}")
    private String msdResponseQueue;

    @Bean
    public Destination mpiResponseQueue() {
        return lookupByJndiTemplate(mpiResponseQueue,Destination.class);
    }

    
    @Bean
    public Destination msdResponseQueue() {
        return lookupByJndiTemplate(msdResponseQueue,Destination.class);
    }
  

    
    /**
     * 
     * @param jndiName
     * @param requiredType
     * @return
     */
    @SuppressWarnings("unchecked")
  	private   <T> T lookupByJndiTemplate(String jndiName,  Class<T> requiredType)  {
         
          try {
              Object located = jndiTemplate.lookup(jndiName);
              if (located == null) {
                  throw new NameNotFoundException("JNDI object with [" + jndiName + "] not found");
              }
              return (T)located;
           } catch (NamingException e) {
              e.printStackTrace();
          }
          return null;
      }
}
