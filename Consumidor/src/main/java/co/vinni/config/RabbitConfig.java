package co.vinni.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String QUEUE_PEDIDOS = "pedido-queue";
    public static final String QUEUE_FACTURAS = "factura-queue";
    public static final String QUEUE_NOTIFICACIONES = "notificacion-queue";

    @Bean
    public Queue pedidoQueue() {
        return new Queue(QUEUE_PEDIDOS, true);
    }

    @Bean
    public Queue facturaQueue() {
        return new Queue(QUEUE_FACTURAS, true);
    }

    @Bean
    public Queue notificacionQueue() {
        return new Queue(QUEUE_NOTIFICACIONES, true);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}