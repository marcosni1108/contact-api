package br.com.contact.contactapi.configuration;


import br.com.contact.contactapi.annotations.LogExecutionAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Bean for aspect configuration with spring
 *
 * @author Marcos Silva
 */
@Configuration
@EnableAspectJAutoProxy
public class AspectConfiguration {

    @Bean
    public LogExecutionAspect logExecutionAspect() {
        return new LogExecutionAspect();
    }

}
