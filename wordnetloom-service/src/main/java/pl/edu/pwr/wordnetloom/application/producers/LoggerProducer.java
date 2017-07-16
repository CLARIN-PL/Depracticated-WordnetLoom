package pl.edu.pwr.wordnetloom.application.producers;

import java.util.logging.Logger;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * @author Tomasz Naskręt <naskret.tomasz@gmail.com>
 */
public class LoggerProducer {

    @Produces  
    public Logger produceLogger(InjectionPoint injectionPoint) {  
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());  
    } 
}
