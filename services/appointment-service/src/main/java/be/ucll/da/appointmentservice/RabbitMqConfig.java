package be.ucll.da.appointmentservice;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(Jackson2JsonMessageConverter converter, CachingConnectionFactory cachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }

    @Bean
    public Declarables createDeadLetterSchema(){
        return new Declarables(
                new DirectExchange("x.notification-failure"),
                new Queue("q.fall-back-notification"),
                new Binding("q.fall-back-notification", Binding.DestinationType.QUEUE,"x.notification-failure", "fall-back", null)
        );
    }

    
    @Bean
    public Declarables declarables() {
        return new Declarables(
                new DirectExchange("x.appointment-patient"),
                new Queue("q.validate-patient"),
                new Binding("q.validate-patient", Binding.DestinationType.QUEUE, "x.appointment-patient", "patient-service", null),

                new FanoutExchange("x.patient-validated"),
                new Queue("q.appointment-validate-patient-reply"),
                new Binding("q.appointment-validate-patient-reply", Binding.DestinationType.QUEUE, "x.patient-validated", "appointment-service", null),

                new DirectExchange("x.appointment-doctor"),
                new Queue("q.check-doctors-employed"),
                new Binding("q.check-doctors-employed", Binding.DestinationType.QUEUE, "x.appointment-doctor", "doctor-service", null),

                new FanoutExchange("x.doctors-on-payroll-checked"),
                new Queue("q.appointment-doctors-on-payroll-reply"),
                new Binding("q.appointment-doctors-on-payroll-reply", Binding.DestinationType.QUEUE, "x.doctors-on-payroll-checked", "doctor-service", null)


        );
    }
}
