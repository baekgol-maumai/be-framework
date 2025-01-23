package ai.maum.beframework.util;

import ai.maum.beframework.vo.BaseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 파싱 유틸
 * @author baekgol@maum.ai
 */
@SuppressWarnings("unchecked")
public class ParsingUtil {
    private static final ObjectMapper om = new ObjectMapper();

    private ParsingUtil() {}

    /**
     * URL에 있는 Query String 정보를 Map으로 반환한다.
     * @param url URL
     * @return Query String 정보가 담긴 Map, 존재하지 않을 경우 null
     */
    public static Map<String, Object> getQueryString(String url) {
        if(url == null) return null;
        String[] info = url.split("\\?");
        return Arrays.stream(URLDecoder.decode(info.length > 1 ? info[1] : info[0], StandardCharsets.UTF_8).split("&"))
                .map(param -> param.split("="))
                .filter(param -> param.length == 2)
                .collect(Collectors.toMap(param -> param[0], param -> param[1]));
    }

    /**
     * URL에서 주소 정보를 반환한다.
     * @param url URL
     * @return 주소 정보, 존재하지 않을 경우 null
     */
    public static String getAddress(String url) {
        if(url == null) return null;
        String[] info = url.split("\\?");
        return info.length > 1 ? info[0] : null;
    }

    /**
     * 해당 객체의 각 필드에 대한 값을 Map으로 반환한다.
     * @param object 객체 정보
     * @return 객체 정보가 담긴 Map
     */
    public static Object parseObject(Object object) throws IllegalAccessException {
        if(object == null) return null;

        Map<String, Object> info = new HashMap<>();
        Class<?> classInfo = object.getClass();

        if(!classInfo.getPackage().getName().startsWith("ai.maum")
                || Arrays.stream(classInfo.getInterfaces()).anyMatch(clazz -> clazz.isAssignableFrom(Map.class))
                || classInfo.isEnum()) return object;

        for(Field field: classInfo.getDeclaredFields()) {
            field.setAccessible(true);
            Object target = field.get(object);
            info.put(StringUtil.convertNaming(field.getName(), true),
                    target instanceof List ? convertToListByList((List<Object>)target) : parseObject(target));
        }

        return info;
    }

    /**
     * List 요소들의 속성명을 Snake 형식으로 변환한다.
     * @param target List 객체
     * @return Snake 형식으로 변환된 List
     */
    public static List<Object> convertToListByList(List<Object> target) {
        return target.stream()
                .map(e -> {
                    try {
                        return Arrays.stream(e.getClass().getInterfaces()).anyMatch(clazz -> clazz.isAssignableFrom(Map.class))
                                ? convertToMapByMap((Map<String, Object>)e)
                                : convertToObjectByObject(e);
                    } catch(IllegalAccessException ex) {
                        throw BaseException.of(ex);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Map 요소들의 속성명을 Snake 형식으로 변환한다.
     * @param target Map 객체
     * @return Snake 형식으로 변환된 Map
     */
    public static Map<String, Object> convertToMapByMap(Map<String, Object> target) {
        return target.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> StringUtil.convertNaming(e.getKey(), true),
                        e -> {
                            Object value = e.getValue();
                            if(Arrays.stream(value.getClass().getInterfaces()).anyMatch(clazz -> clazz.isAssignableFrom(Map.class)))
                                return convertToMapByMap((Map<String, Object>)value);
                            return value; }));
    }

    /**
     * Object 형식 그대로 형변환한다.
     * @param object 객체
     * @return object
     */
    public static <T> T convertType(T object) {
        return object;
    }

    /**
     * JSON 형식의 문자열을 원하는 객체로 변환한다.
     * @param target JSON 형식 문자열
     * @param targetClass 클래스 정보
     * @return 변환된 객체
     */
    public static <T> T convertToObjectByJsonString(String target, Class<T> targetClass) throws JsonProcessingException {
        return om.readValue(target, targetClass);
    }

    /**
     * 객체를 JSON 형식의 문자열로 변환한다.
     * @param object 객체 정보
     * @return 변환된 JSON 형식 문자열
     */
    public static String convertToJsonStringByObject(Object object) throws JsonProcessingException, IllegalAccessException {
        return om.writeValueAsString(parseObject(object));
    }

    /**
     * Map을 특정 객체로 변환한다.
     * @param target Map 객체
     * @param clazz 객체로 변환할 클래스
     * @return 변환된 객체
     */
    public static <T> T convertToObjectByMap(Map<String, Object> target, Class<T> clazz) {
        return om.convertValue(target.entrySet()
                .stream()
                .filter(e -> e.getValue() != null)
                .map(e -> {
                    Object v = e.getValue();
                    if(v instanceof Collection) v = convertToElementByCollection((Collection<Object>)v);
                    else if(!(v instanceof String) && !v.getClass().isPrimitive()) {
                        try {
                            if(v instanceof Map) v = convertToObjectByMap((Map<String, Object>)v, Map.class);
                            v = convertToObjectByObject(v);
                        } catch(IllegalAccessException ex) {
                            throw BaseException.of(ex);
                        }
                    }
                    return Map.entry(e.getKey().contains("_") ? StringUtil.convertNaming(e.getKey(), false) : e.getKey(), v);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)), clazz);
    }

    /**
     * List의 요소들을 특정 객체로 변환한다.
     * 객체 요소에 대해서만 속성명을 Camel 또는 Snake 형식으로 변환한다.
     * @param target List 객체
     * @return 변환된 요소들이 담긴 List 객체
     */
    public static Collection<Object> convertToElementByCollection(Collection<Object> target) {
        return target.stream()
                .map(e -> {
                    if(e instanceof Map) return convertToObjectByMap((Map<String, Object>)e, Object.class);
                    else if(!(e instanceof String) && !e.getClass().isPrimitive()) {
                        try {
                            return convertToObjectByObject(e);
                        } catch(IllegalAccessException ex) {
                            throw BaseException.of(ex);
                        }
                    }
                    return e;
                })
                .collect(Collectors.toList());
    }

    /**
     * 객체의 속성명을 Camel 또는 Snake 형식으로 변환한다.
     * @param target 객체
     * @return 변환된 객체
     */
    public static Object convertToObjectByObject(Object target) throws IllegalAccessException {
        return parseObject(target);
    }

    /**
     * JSONObject 객체를 Map 객체로 변환한다.
     * @param target JSONObject 객체
     * @return Map 객체
     */
    public static Map<String, Object> convertToMapByJsonObject(JSONObject target) {
        Map<String, Object> res = new HashMap<>();

        target.keys().forEachRemaining(e -> {
            try {
                String key = (String)e;
                Object value = target.get(key);
                res.put(key, value instanceof JSONObject
                        ? convertToMapByJsonObject((JSONObject)value)
                        : (value instanceof JSONArray
                        ? convertToListByJsonArray((JSONArray)value)
                        : value));
            } catch(JSONException ex) {
                throw BaseException.of(ex);
            }
        });

        return res;
    }

    /**
     * JSONArray 객체를 List 객체로 변환한다.
     * @param target JSONArray 객체
     * @return List 객체
     */
    public static List<Object> convertToListByJsonArray(JSONArray target) {
        int size = target.length();
        List<Object> result = new ArrayList<>();

        for(int i=0; i<size; i++) {
            try {
                result.add(convertToMapByJsonObject(target.getJSONObject(i)));
            } catch(JSONException e) {
                throw BaseException.of(e);
            }
        }

        return result;
    }

    /**
     * Properties 정보를 조회한다.
     * @param clazz 현재 클래스
     * @param env 실행 환경
     * @return Properties 정보
     */
    public static Map<String, Object> getProperties(Class<?> clazz, String env) {
        return new Yaml().load(clazz.getClassLoader().getResourceAsStream("application-" + env + ".yml"));
    }
}
