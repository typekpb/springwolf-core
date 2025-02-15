package io.github.stavshamir.springwolf.asyncapi;

import com.asyncapi.v2.binding.kafka.KafkaOperationBinding;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.ImmutableMap;
import io.github.stavshamir.springwolf.asyncapi.serializers.KafkaOperationBindingSerializer;
import io.github.stavshamir.springwolf.asyncapi.types.AsyncAPI;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class DefaultAsyncApiSerializerService implements AsyncApiSerializerService {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @PostConstruct
    void postConstruct() {
        jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        registerKafkaOperationBindingSerializer();
    }

    private void registerKafkaOperationBindingSerializer() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(KafkaOperationBinding.class, new KafkaOperationBindingSerializer());
        jsonMapper.registerModule(module);
    }

    @Override
    public String toJsonString(AsyncAPI asyncAPI) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(asTitleToDocMap(asyncAPI));
    }

    private Map<String, AsyncAPI> asTitleToDocMap(AsyncAPI asyncAPI) {
        String title = asyncAPI.getInfo().getTitle();
        return ImmutableMap.of(title, asyncAPI);
    }

}
