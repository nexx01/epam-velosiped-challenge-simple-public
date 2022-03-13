package dev.abarmin.velosiped.task3;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class VelosipedJsonAdapterImpl implements VelosipedJsonAdapter {
    @Override
    public <T> T parse(String json, Class<T> targetClass) {
        String str =
                json.substring(json.indexOf("{") + 1).replace("}", "").trim();
        String[] fieldStr = str.split(",");
        Map<String, String> fieldMap = Arrays.stream(fieldStr)
                .map(field -> field.trim().split("\\:"))
                .collect(Collectors.toMap(field -> field[0].replace("\"",""),
                        field -> field[1].trim()));
        T instanse ;
        try {
            instanse  = targetClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        Field[] fields = targetClass.getDeclaredFields();
        Arrays.stream(fields)
                .forEach(field->{
                    String fieldValue = fieldMap.get(field.getName());
                    if (fieldValue == null) {
                        return;
                    }
                    field.setAccessible(true);
                    try {
                        field.set(instanse, mapValue(field.getType(), fieldValue));
                    }catch (IllegalAccessException e){
                        throw new RuntimeException();
                    }
                    
                });

        return instanse;
    }

    private Object mapValue(Class<?> valueClass, String value) {
        if (valueClass.equals(int.class)) {
            return Integer.parseInt(value);
        } else {
            throw new RuntimeException("Unsupported class");
        }
    }

    @Override
    public String writeAsJson(Object instance) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        Class<?> instanceClass = instance.getClass();
        Field[] fields=instanceClass.getDeclaredFields();
        String fieldsStr = Arrays.stream(fields)
                .map(field -> {
                    StringBuilder fieldStr = new StringBuilder();
                    field.setAccessible(true);
                    String value;
                    try {
                        value = field.get(instance).toString();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException();
                    }

                    fieldStr.append("\"").append(field.getName()).append("\"");
                    fieldStr.append(":");
                    fieldStr.append(value);
                    return fieldStr;

                }).collect(Collectors.joining(","));

        json.append(fieldsStr);
        json.append("}");
        return json.toString();

    }
}
