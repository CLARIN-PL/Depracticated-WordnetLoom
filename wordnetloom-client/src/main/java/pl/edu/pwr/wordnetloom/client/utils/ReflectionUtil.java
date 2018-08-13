package pl.edu.pwr.wordnetloom.client.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionUtil {

    public static <T> void setValue(T object, String fieldName,Object value, Class parameterClass) {
        try {
            Method method = object.getClass().getMethod("set" + fieldName, parameterClass);
            method.invoke(object, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static <T> Object getValue(T object, String fieldName) {
        Method method = null;
        try {
            method = object.getClass().getMethod("get" + fieldName);
            return method.invoke(object);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> String getStringValue(T object, String fieldName) {
        return (String)getValue(object, fieldName);
    }

    public static <T> Long getLongValue(T object, String fieldName) {
        return (Long)getValue(object, fieldName);
    }

    public static <T>List<Method> getMethods(T object) {
        Method[] methods = object.getClass().getMethods();
        List<Method> methodList = Arrays.stream(methods).filter(
                x->x.getName().startsWith("get")
                        && !x.getName().equals("getClass")
                        && !x.getName().equals("getId"))
                .collect(Collectors.toList());Collectors.toList();
        return methodList;
    }
}
