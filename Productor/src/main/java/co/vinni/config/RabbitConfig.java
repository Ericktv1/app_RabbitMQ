package co.vinni.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // === Topic para enviar pedidos ===
    public static final String EXCHANGE_KITCHEN_TOPIC = "kitchen-topic";

    @Bean
    public TopicExchange kitchenTopicExchange() {
        return new TopicExchange(EXCHANGE_KITCHEN_TOPIC, true, false);
    }

    // === Fanouts que escucha el cliente ===
    @Bean(name = "notificacionesFanout")
    public FanoutExchange notificacionesFanout() {
        return new FanoutExchange("notificaciones.fanout", true, false);
    }

    @Bean(name = "facturasFanout")
    public FanoutExchange facturasFanout() {
        return new FanoutExchange("facturas.fanout", true, false);
    }

    @Bean(name = "promoFanout")
    public FanoutExchange promoFanout() {
        return new FanoutExchange("promo-fanout", true, false);
    }

    // === Colas para escuchar mensajes del restaurante ===
    @Bean(name = "facturaQueue")
    public Queue facturaQueue() {
        return new Queue("factura-queue", true);
    }

    @Bean(name = "notificacionQueue")
    public Queue notificacionQueue() {
        return new Queue("notificacion-queue", true);
    }

    @Bean(name = "promoQueue")
    public Queue promoQueue() {
        return new Queue("promo-queue", true);
    }

    // === Bindings ===
    @Bean
    public Binding bindFacturas(
            @Qualifier("facturasFanout") FanoutExchange facturasFanout,
            @Qualifier("facturaQueue") Queue facturaQueue) {
        return BindingBuilder.bind(facturaQueue).to(facturasFanout);
    }

    @Bean
    public Binding bindNotificaciones(
            @Qualifier("notificacionesFanout") FanoutExchange notificacionesFanout,
            @Qualifier("notificacionQueue") Queue notificacionQueue) {
        return BindingBuilder.bind(notificacionQueue).to(notificacionesFanout);
    }

    @Bean
    public Binding bindPromos(
            @Qualifier("promoFanout") FanoutExchange promoFanout,
            @Qualifier("promoQueue") Queue promoQueue) {
        return BindingBuilder.bind(promoQueue).to(promoFanout);
    }

    // === Converter JSON ===
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
