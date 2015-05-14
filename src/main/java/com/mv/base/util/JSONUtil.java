package com.mv.base.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.type.JavaType;

public class JSONUtil {
	private static Logger LOG = Logger.getLogger(JSONUtil.class.getName());

	public static String toJson(Object o) {
		if (o == null) {
			return null;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		String result = "";
		
		try {
			result = mapper.writeValueAsString(o);
		} catch (JsonGenerationException e) {
			LOG.error("Error parsing to JSON String.");
		} catch (JsonMappingException e) {
			LOG.error("Error parsing to JSON String.");
		} catch (IOException e) {
			LOG.error("Error parsing to JSON String.");
		}
		return result;
	}
	
	public static <K, V> Map<K, V> readMap(String json, Class<?> K, Class<?> V) {
		if (isEmpty(json)) {
			return null;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		JavaType keyType = mapper.getTypeFactory().constructType(K);
		JavaType valueType = mapper.getTypeFactory().constructType(V);
		MapType mapType = mapper.getTypeFactory().constructMapType(HashMap.class, keyType, valueType);
		
		Map<K, V> result = null;
		try {
			result = mapper.readValue(json, mapType);
		} catch (JsonParseException e) {
			LOG.error("Error parsing from JSON String:" + json);
		} catch (JsonMappingException e) {
			LOG.error("Error parsing from JSON String:" + json);
		} catch (IOException e) {
			LOG.error("Error parsing from JSON String:" + json);
		}
		return result;
	}
	
	public static <T> List<T> readList(String json, Class<?> T) {
		if (isEmpty(json)) {
			return null;
		}

		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, T);

		List<T> result = null;
		try {
			result = mapper.readValue(json, type);
		} catch (JsonParseException e) {
			LOG.error("Error parsing from JSON String:" + json);
		} catch (JsonMappingException e) {
			LOG.error("Error parsing from JSON String:" + json);
		} catch (IOException e) {
			LOG.error("Error parsing from JSON String:" + json);
		}
		return result;
	}
	
	public static <T> T readObject(String json, Class<T> clazz) {
		if (isEmpty(json)) {
			return null;
		}

		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		T result = null;

		try {
			result = mapper.readValue(json, clazz);
		} catch (JsonParseException e) {
			LOG.error("Error parsing object from:" + json);
		} catch (JsonMappingException e) {
			LOG.error("Error extracting object from:" + json);
		} catch (IOException e) {
			LOG.error("IO Error range object json:" + json);
		}
		return result;
	}
	
	public static <T> T readObject(String json, Class<T> clazz, ObjectMapper mapper) {
		if (isEmpty(json)) {
			return null;
		}

		T result = null;

		try {
			result = mapper.readValue(json, clazz);
		} catch (JsonParseException e) {
			LOG.error("Error parsing object from:" + json);
		} catch (JsonMappingException e) {
			LOG.error("Error extracting object from:" + json);
		} catch (IOException e) {
			LOG.error("IO Error range object json:" + json);
		}
		return result;
	}

	private static boolean isEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}
}
