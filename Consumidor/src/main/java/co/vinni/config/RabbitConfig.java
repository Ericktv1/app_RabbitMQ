package co.vinni.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@Configuration
@EnableRabbit
public class RabbitConfig {
    // Colas “legacy”
    public static final String QUEUE_FACTURAS = "factura-queue";
    public static final String QUEUE_NOTIFICACIONES = "notificacion-queue";

    // DIRECT para cocinas
    public static final String EXCHANGE_KITCHEN = "kitchen-exchange";
    public static final String QUEUE_ITALIANA = "kitchen-italiana-queue";
    public static final String QUEUE_ASIATICA = "kitchen-asiatica-queue";
    public static final String RK_ITALIANA = "italiana";
    public static final String RK_ASIATICA = "asiatica";

    @Bean DirectExchange kitchenExchange() { return new DirectExchange(EXCHANGE_KITCHEN, true, false); }
    @Bean
    public FanoutExchange promoFanout() {
        return new FanoutExchange("promo-fanout",true, false);
    }


    @Bean Queue italianaQueue() { return new Queue(QUEUE_ITALIANA, true); }
    @Bean Queue asiaticaQueue() { return new Queue(QUEUE_ASIATICA, true); }

    @Bean Binding italianaBinding() { return BindingBuilder.bind(italianaQueue()).to(kitchenExchange()).with(RK_ITALIANA); }
    @Bean Binding asiaticaBinding() { return BindingBuilder.bind(asiaticaQueue()).to(kitchenExchange()).with(RK_ASIATICA); }

    // Colas de salida al cliente
    @Bean public Queue facturaQueue() { return new Queue(QUEUE_FACTURAS, true); }
    @Bean public Queue notificacionQueue() { return new Queue(QUEUE_NOTIFICACIONES, true); }

    @Bean public Jackson2JsonMessageConverter jsonMessageConverter() { return new Jackson2JsonMessageConverter(); }
}
