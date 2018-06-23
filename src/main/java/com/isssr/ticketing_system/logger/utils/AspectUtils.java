package it.uniroma2.ticketingsystem.logger.utils;

import java.lang.annotation.Annotation;

public abstract class AspectUtils {

    /**
     * Check if the option of the annotation has the default value
     *
     * @param annotation the class of the annotation
     * @param optionName the name of the option
     * @param option the annotated object
     * @return true if the option has the default value, false otherwise
     */
    public static boolean defaultOption(Class<?> annotation, String optionName, Object option) {
        Object defaultValue = null;
        try {
            defaultValue = annotation.getDeclaredMethod(optionName).getDefaultValue();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return option.equals(defaultValue);
    }

    private static void printStringArray(String[] array) {
        for (String s : array)
            System.err.println(s);
    }

    /**
     * Check if the object has the annotation
     *
     * @param object the object to be checked
     * @param annotation the annotation class
     * @return the annotation instance, null if there is not such annotation
     */
    public static Annotation hasAnnotation(Object object, Class<?> annotation) {
        Annotation[] annotations = object.getClass().getAnnotations();
        for (Annotation a : annotations)
            if (a.annotationType().equals(annotation))
                return a;
        return null;
    }


}
