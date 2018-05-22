package com.isssr.ticketing_system.response_entity;

import com.isssr.ticketing_system.response_entity.response_serializator.IncludeInResponse;
import com.isssr.ticketing_system.response_entity.response_serializator.VariableResponseSelector;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class ObjectResponseEntityBuilder<T> extends HashMapResponseEntityBuilder {
    public ObjectResponseEntityBuilder(T object) {
        this(object, "base");
    }

    @SuppressWarnings("unchecked")
    public ObjectResponseEntityBuilder(T object, String responseType) {
        if (object != null) {
            Class<?> objectClass = object.getClass();
            for (Field field : objectClass.getDeclaredFields())
                for (Annotation annotation : field.getDeclaredAnnotations())
                    if (annotation instanceof IncludeInResponse)
                        for (String string : ((IncludeInResponse) annotation).value())
                            if (string.equals(responseType))
                                try {
                                    Object subObject = new PropertyDescriptor(field.getName(), object.getClass()).getReadMethod().invoke(object);
                                    if (field.getType().isAnnotationPresent(VariableResponseSelector.class))
                                        this.setBuilder(field.getName(), new ObjectResponseEntityBuilder<>(subObject, responseType));
                                    else if (field.getType().isAssignableFrom(Collection.class))
                                        this.setBuilder(field.getName(), new ListObjectResponseEntityBuilder((Collection) subObject, responseType));
                                    else
                                        this.set(field.getName(), subObject);
                                } catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
                                    e.printStackTrace();
                                }
        }
    }
}
