package com.spending.tracker.jackson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class PageDeserializer extends JsonDeserializer<Page<?>> {

	@Override
	public Page<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws
			IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);
		Class<? extends Collection> contentClass = ArrayList.class;
		if (node.has("content") && node.get("content").isArray()) {
			ArrayNode contentNode = (ArrayNode) node.get("content");
			if (contentNode.size() > 0) {
				JsonNode firstElement = contentNode.get(0);
				contentClass = (Class<? extends Collection>) mapper.getTypeFactory().constructCollectionType(List.class, firstElement.getClass()).getRawClass();
			}
		}
		JavaType contentType = mapper.getTypeFactory().constructCollectionType(contentClass, Object.class);
		List<?> content = mapper.readValue(node.get("content").traverse(), contentType);
		long totalElements = node.get("totalElements").asLong();
		int size = node.get("size").asInt();
		int number = node.get("number").asInt();
		return new PageImpl<>(content, PageRequest.of(number, size), totalElements);
	}
}

