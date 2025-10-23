package co.vinni.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // === Topic (ya existente) ===
    public static final String EXCHANGE_KITCHEN_TOPIC = "kitchen-topic";
    public static final String Q_PEDIDOS_ITALIANA = "pedidos.italiana";
    public static final String Q_PEDIDOS_ASIATICA = "pedidos.asiatica";

    @Bean
    public TopicExchange kitchenTopicExchange() {
        return new TopicExchange(EXCHANGE_KITCHEN_TOPIC, true, false);
    }

    @Bean(name = "italianaQueue")
    public Queue italianaQueue() { return new Queue(Q_PEDIDOS_ITALIANA, true); }

    @Bean(name = "asiaticaQueue")
    public Queue asiaticaQueue() { return new Queue(Q_PEDIDOS_ASIATICA, true); }

    @Bean
    public Binding italianaBinding(
            @Qualifier("kitchenTopicExchange") TopicExchange ex,
            @Qualifier("italianaQueue") Queue q) {
        return BindingBuilder.bind(q).to(ex).with("cocina.italiana");
    }

    @Bean
    public Binding asiaticaBinding(
            @Qualifier("kitchenTopicExchange") TopicExchange ex,
            @Qualifier("asiaticaQueue") Queue q) {
        return BindingBuilder.bind(q).to(ex).with("cocina.asiatica");
    }

    // === Fanouts (ya existentes) ===
    @Bean public FanoutExchange notificacionesFanout() { return new FanoutExchange("notificaciones.fanout", true, false); }
    @Bean public FanoutExchange facturasFanout()       { return new FanoutExchange("facturas.fanout", true, false); }
    @Bean public FanoutExchange promoFanout()          { return new FanoutExchange("promo-fanout", true, false); }

    // === NUEVO: Direct para ventas ===
    @Bean(name = "ventasDirect")
    public DirectExchange ventasDirect() {
        return new DirectExchange("ventas.direct", true, false);
    }

    @Bean(name = "ventasAdminQueue")
    public Queue ventasAdminQueue() {
        return new Queue("ventas-admin", true);
    }

    @Bean
    public Binding bindVentasAdmin(
            @Qualifier("ventasDirect") DirectExchange ventasDirect,
            @Qualifier("ventasAdminQueue") Queue ventasAdminQueue
    ) {
        // Todas las ventas pasan con routingKey "ventas.registro"
        return BindingBuilder.bind(ventasAdminQueue).to(ventasDirect).with("ventas.registro");
    }
    @Bean(name = "promoRepaQueue")
    public Queue promoRepaQueue() {
        return new Queue("promo-repa-queue", true);
    }

    @Bean
    public Binding bindPromoRepa(
            @Qualifier("promoFanout") FanoutExchange promoFanout,
            @Qualifier("promoRepaQueue") Queue promoRepaQueue) {
        return BindingBuilder.bind(promoRepaQueue).to(promoFanout);
    }
    // Converter JSON
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() { return new Jackson2JsonMessageConverter(); }
}
