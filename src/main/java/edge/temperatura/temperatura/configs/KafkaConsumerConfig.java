package edge.temperatura.temperatura.configs;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;




@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${APP_NAME}")
    private String APP_NAME;

    @Value("${USERNAME}")
    private String USERNAME;

    @Value("${API_KEY}")
    private String API_KEY;

    @Value("${bootstrapServerAddress}")
    private String bootstrapServerAddress;

    @Value("${consumerGroupId}")
    private String consumerGroupId;



    @Bean
    public Map<String,Object> consumerConfigs(){

        Map<String, Object> props = new HashMap<>();

        //Can Change String deserializer to json
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerAddress);
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(SslConfigs.SSL_PROTOCOL_CONFIG, "TLSv1.2");
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "es-cert.jks");
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "password");
        props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        String saslJaasConfig = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\""
            + USERNAME + "\" password=" + API_KEY + ";";
        props.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);

        return props;
    }


    @Bean
    public ConsumerFactory<String, String> consumerFactory(){
    return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,String>> kafkaListenerContainerFactory(){

        ConcurrentKafkaListenerContainerFactory<String, String> factory= new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}