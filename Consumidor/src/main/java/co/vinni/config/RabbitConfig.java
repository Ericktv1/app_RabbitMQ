package co.vinni.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // === Topic para pedidos por cocina ===
    public static final String EXCHANGE_KITCHEN_TOPIC = "kitchen-topic";
    public static final String Q_PEDIDOS_ITALIANA = "pedidos.italiana";
    public static final String Q_PEDIDOS_ASIATICA = "pedidos.asiatica";

    // === Exchanges ===
    @Bean
    public TopicExchange kitchenTopicExchange() {
        return new TopicExchange(EXCHANGE_KITCHEN_TOPIC, true, false);
    }

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

    // === Colas del restaurante (escucha pedidos) ===
    @Bean(name = "italianaQueue")
    public Queue italianaQueue() {
        return new Queue(Q_PEDIDOS_ITALIANA, true);
    }

    @Bean(name = "asiaticaQueue")
    public Queue asiaticaQueue() {
        return new Queue(Q_PEDIDOS_ASIATICA, true);
    }

    // === Bindings para recibir pedidos ===
    @Bean
    public Binding italianaBinding(
            @Qualifier("kitchenTopicExchange") TopicExchange kitchenTopicExchange,
            @Qualifier("italianaQueue") Queue italianaQueue) {
        return BindingBuilder.bind(italianaQueue)
                .to(kitchenTopicExchange)
                .with("cocina.italiana");
    }

    @Bean
    public Binding asiaticaBinding(
            @Qualifier("kitchenTopicExchange") TopicExchange kitchenTopicExchange,
            @Qualifier("asiaticaQueue") Queue asiaticaQueue) {
        return BindingBuilder.bind(asiaticaQueue)
                .to(kitchenTopicExchange)
                .with("cocina.asiatica");
    }

    // === Converter JSON ===
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
