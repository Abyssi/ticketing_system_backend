package it.uniroma2.ticketingsystem.logger.utils;

import it.uniroma2.ticketingsystem.logger.aspect.LogClass;
import it.uniroma2.ticketingsystem.logger.exception.ObjNotFoundException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.hibernate.proxy.HibernateProxyHelper.getClassWithoutInitializingProxy;

public abstract class ReflectUtils {


    // TODO: da cancellare perché se l'id non è string non riesce a fare il cast
    public static String fieldToString(Object instance, String fieldName) {

        String result = null;

        try {
            Field field = FieldUtils.getField(instance.getClass(), fieldName, true);
            result = (String) field.get(instance);
        } catch (IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
        }

        if (result == null)
            //throw new AttrNotFoundException("Attributo \"" + fieldName + "\" non trovato nella classe \"" + instance.getClass().getName() + "\"");
            System.err.println("Attributo \"" + fieldName + "\" non trovato nella classe \"" + instance.getClass().getName() + "\"");
        return result;
    }


    public static Object getMethodParameter(String objectName, MethodSignature signature, Object[] args)
            throws ObjNotFoundException {

        String[] argNames = signature.getParameterNames();

        int index = 0;
        boolean found = false;
        for (String s : argNames) {
            if (objectName.equals(s)) {
                found = true;
                break;
            }
            index++;
        }

        if (!found)
            throw new ObjNotFoundException(
                    "Oggetto \"" + objectName + "\" non trovato nella lista dei parametri del metodo");

        return args[index];
    }


    // TODO: Non è una classe utils, perché si riferisce ad un'annotazione specifica -> renderla generica
    public static String[] getIDParameters(Object target){

        Class objectClass = getClassWithoutInitializingProxy(target);
        String[] params = null;

        for(Annotation annotation : objectClass.getAnnotations()){
            if(annotation.annotationType().equals(LogClass.class)){
                LogClass myAnn = (LogClass) annotation;
                params = ((LogClass) annotation).idAttrs();

                break;
            }
        }
        return params;
    }

    // TODO: Non è una classe utils, perché si riferisce ad un'annotazione specifica -> renderla generica
    //controllare se la classe dell'oggetto è annotata
    //se si, controllare se ha inserito dei parametri rilevanti
    //restituire i parametri rilevanti
    public static String[] getParameters(Object targetObject){
        Class objectClass = getClassWithoutInitializingProxy(targetObject);
        String[]params = null;

        //analizzo tutte le annotazioni della classe dell'oggetto
        for (Annotation annotation : objectClass.getAnnotations()){

            if (annotation.annotationType().equals(LogClass.class)) {
                LogClass myAnnotation = (LogClass) annotation;
                params = myAnnotation.logAttrs();
                if (params.length == 1 && params[0].equals(""))
                    return null;
                break;
            }
        }
        return params;
    }

}

