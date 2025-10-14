package co.vinni.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Configuration
@EnableRabbit
public class RabbitConfig {

    // --- DIRECT (si lo usas para enrutar pedidos por cocina) ---
    public static final String EXCHANGE_KITCHEN = "kitchen-exchange";
    public static final String RK_ITALIANA = "italiana";
    public static final String RK_ASIATICA  = "asiatica";

    // --- FANOUT (promociones) ---
    public static final String PROMO_FANOUT_NAME   = "promo-fanout";   // único exchange
    public static final String PROMO_QUEUE_NAME    = "promo-queue";    // cola local del cliente

    // --- “legacy” (respuestas del restaurante) ---
    public static final String QUEUE_FACTURAS       = "factura-queue";
    public static final String QUEUE_NOTIFICACIONES = "notificacion-queue";

    // ============ Exchanges ============
    @Bean
    public DirectExchange kitchenExchange() {
        // el Productor publica a este exchange si enrutas por cocina; no crees colas aquí
        return new DirectExchange(EXCHANGE_KITCHEN, true, false);
    }

    @Bean
    public FanoutExchange promoFanout() {
        // ÚNICO fanout para promos
        return new FanoutExchange(PROMO_FANOUT_NAME, true, false);
    }

    // ============ Cola + binding de promociones (fanout) ============
    @Bean(name = "promoClientQueue")
    public Queue promoClientQueue() {
        // única cola local para escuchar promociones
        return new Queue(PROMO_QUEUE_NAME, true);
    }

    @Bean
    public Binding promoBinding(@Qualifier("promoClientQueue") Queue promoClientQueue,
                                FanoutExchange promoFanout) {
        // IMPORTANTÍSIMO: @Qualifier elimina la ambigüedad de múltiples Queue beans
        return BindingBuilder.bind(promoClientQueue).to(promoFanout);
    }

    // ============ Colas para facturas y notificaciones ============
    @Bean
    public Queue facturaQueue() {
        return new Queue(QUEUE_FACTURAS, true);
    }

    @Bean
    public Queue notificacionQueue() {
        return new Queue(QUEUE_NOTIFICACIONES, true);
    }

    // ============ Converter JSON ============
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
