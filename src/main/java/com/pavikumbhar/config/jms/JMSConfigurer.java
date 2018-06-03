package com.pavikumbhar.config.jms;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.QueueConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.JndiDestinationResolver;
import org.springframework.jndi.JndiTemplate;

import weblogic.jndi.WLInitialContextFactory;

/**
 * 
 * @author pavikumbhar
 *
 */
@Configuration
@EnableJms
@PropertySource(value="classpath:jms-${spring.profiles.active}.properties",ignoreResourceNotFound=true)
@PropertySource(value="classpath:restcallurl-${spring.profiles.active}.properties",ignoreResourceNotFound=true)
public  class JMSConfigurer  {

    /**
     * provider url
     * Constant that holds the name of the environment property
     * for specifying configuration information for the service provider
     * to use. The value of the property should contain a URL string
     * (e.g. "t3://localhost:8001").
     * 
     */
	@Value("${wls.jms.url}")
    private String url;
    
	/**
     * username of weblogic server using which JNDI connection will be
     * established
     @note here i am using : for default null value if ${wls.jms.username} key
     *not present any of properties file
     */
	@Value("${wls.jms.username:}")
    private String username;
    /**
     * password of weblogic server using which JNDI connection will be
     * established
     *@note here i am using : for default null value if ${wls.jms.password} key
     *not present any of properties file
     */
	@Value("${wls.jms.password:}")
    private String password;
    /**
     * JMS Connection factory name configured in weblogic server
     */
	@Value("${wls.jms.connectionFactoryName}")
    private String connectionFactoryName;
    
    

	/**
     *  Specify the number of concurrent consumers to create. Default is 1.
     */
	@Value("${wls.jms.concurrentConsumers}")
    private String concurrentConsumers;
    

    /**
   	 * Specify the level of caching that this listener container is allowed to apply,
   	 * in the form of the name of the corresponding constant: e.g. "CACHE_CONNECTION".
   	 * @see #setCacheLevel
   	 */
	@Value("${wls.jms.cacheLevelName}")
    private String cacheLevelName;

  



    /**
     * Create connection factory.
     * 
     * @return
     */
    @Bean
    public QueueConnectionFactory queueConnectionFactory() {
        // JNDI connection factory name stored in weblogic.
        return lookupByJndiTemplate(connectionFactoryName,QueueConnectionFactory.class);
    }



    
    @Autowired
    @Bean
    public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory(ConnectionFactory connectionFactory, DestinationResolver destination) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDestinationResolver(destination);
        factory.setConcurrency(concurrentConsumers);
        factory.setCacheLevelName(cacheLevelName);
        factory.setSessionAcknowledgeMode(javax.jms.Session.AUTO_ACKNOWLEDGE);
        return factory;
    }
    /**
     * Create InitialContext.
     * 
     * @return
     */
    @Bean
    public Properties jndiProperties() {
        Properties properties = new Properties();
        //weblogic.jndi.WLInitialContextFactory"
        properties.put(Context.INITIAL_CONTEXT_FACTORY, WLInitialContextFactory.class.getName());
        properties.put(Context.PROVIDER_URL, url);
        if (username != null && !username.isEmpty()) {
        	properties.setProperty(Context.SECURITY_PRINCIPAL, username);
        }
        if (password != null && !password.isEmpty()) {
        	properties.setProperty(Context.SECURITY_CREDENTIALS, password);
        }
        return properties;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(queueConnectionFactory());
    }

    /**
     * Create DestinationResolver
     * 
     * @return
     */
    @Bean
    public JndiDestinationResolver jndiDestinationResolver() {
        JndiDestinationResolver jndiDestinationResolver = new JndiDestinationResolver();
        jndiDestinationResolver.setJndiTemplate(jndiTemplate());
        jndiDestinationResolver.setCache(true);
        return jndiDestinationResolver;
    }

    /**
     * Create InitialContext.
     * 
     * @return
     */
    @Bean
    public JndiTemplate jndiTemplate() {
        JndiTemplate jndiTemplate = new JndiTemplate();
        jndiTemplate.setEnvironment(jndiProperties());
        return jndiTemplate;
    }

 


    
    
    /**
     * 
     * @param jndiName
     * @param requiredType
     * @return
     */
    @SuppressWarnings("unchecked")
  	protected  <T> T lookupByJndiTemplate(String jndiName,  Class<T> requiredType)  {
         
          try {
              Object located = jndiTemplate().lookup(jndiName);
              if (located == null) {
                  throw new NameNotFoundException("JNDI object with [" + jndiName + "] not found");
              }
              return (T)located;
           } catch (NamingException e) {
              e.printStackTrace();
          }
          return null;
      }
    /**
     * 
     * @param jndiName
     * @param requiredType
     * @return
     */
    @SuppressWarnings("unchecked")
	protected final  <T> T lookup(String jndiName,  Class<T> requiredType)  {
       
        try {
        	InitialContext initialContext = new InitialContext(jndiProperties());
            Object located = initialContext.lookup(jndiName);
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
