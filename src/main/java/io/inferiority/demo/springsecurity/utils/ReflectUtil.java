package io.inferiority.demo.springsecurity.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author cuijiufeng
 * @Class ReflectUtil
 * @Date 2023/5/25 13:30
 */
public class ReflectUtil {

    public static Stream<Field> getAnnotationFields(Class<?> clazz, Class<? extends Annotation> annoClazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(annoClazz));
    }

    public static Object invokeMethod(Method method, Object object, Object ... args) throws ReflectiveOperationException {
        return method.invoke(object, args);
    }

    public static Method getGetter(Class<?> clazz, Field field) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if (propertyDescriptor.getName().equals(field.getName())) {
                    return propertyDescriptor.getReadMethod();
                }
            }
            throw new IllegalArgumentException("no getter method found for " + field.getName());
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Method getSetter(Class<?> clazz, Field field) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if (propertyDescriptor.getName().equals(field.getName())) {
                    return propertyDescriptor.getWriteMethod();
                }
            }
            throw new IllegalArgumentException("no setter method found for " + field.getName());
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
