package ai.maum.beframework.conf.serialization;

import ai.maum.beframework.conf.serialization.deserializer.LocalDateDeserializer;
import ai.maum.beframework.conf.serialization.deserializer.LocalDateTimeDeserializer;
import ai.maum.beframework.conf.serialization.deserializer.LocalTimeDeserializer;
import ai.maum.beframework.conf.serialization.serializer.LocalDateSerializer;
import ai.maum.beframework.conf.serialization.serializer.LocalDateTimeSerializer;
import ai.maum.beframework.conf.serialization.serializer.LocalTimeSerializer;
import ai.maum.beframework.conf.serialization.serializer.ObjectIdSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Configuration
public class SerializationConfig {
    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder.build()
                .registerModule(new SimpleModule()
                        .addSerializer(ObjectId.class, new ObjectIdSerializer())
                        .addSerializer(LocalTime.class, new LocalTimeSerializer())
                        .addSerializer(LocalDate.class, new LocalDateSerializer())
                        .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer())
                        .addDeserializer(LocalTime.class, new LocalTimeDeserializer())
                        .addDeserializer(LocalDate.class, new LocalDateDeserializer())
                        .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer()));
    }
}
