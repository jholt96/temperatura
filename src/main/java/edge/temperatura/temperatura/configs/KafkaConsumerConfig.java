/*
Author: Josh Holt
Temperatura Backend 
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: Configuration for Kafka Consumer. Creates the consumer listener and sends the messages along to the kafkaConsumerService class. 

*/
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
import org.springframework.kafka.support.serializer.JsonDeserializer;

import edge.temperatura.temperatura.payloads.KafkaMessage;

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

        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerAddress);
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
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
    public ConsumerFactory<String, KafkaMessage> consumerFactory(){
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(),
                                                                    new JsonDeserializer<>(KafkaMessage.class));
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,KafkaMessage>> kafkaListenerContainerFactory(){

        ConcurrentKafkaListenerContainerFactory<String, KafkaMessage> factory= new ConcurrentKafkaListenerContainerFactory<String, KafkaMessage>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}