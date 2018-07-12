/*
 * Created on 18-nov-2004
 *
 * 
 */
package it.finsiel.siged.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Almaviva sud
 */
public final class ObjectCreator {
    /**
     * This class cannot be instantiated
     * 
     */
    private ObjectCreator() {
    }

    /**
     * Instantiate an Object from a given class name
     * 
     * @param className
     * @return
     * @throws Exception
     */
    public static Object createObject(String className) throws Exception {
        return createObject(Class.forName(className));
    }

    /**
     * Instantiate an Object instance
     * 
     * @param classObject
     * @return
     * @throws Exception
     */
    public static Object createObject(Class classObject) throws Exception {
        return classObject.newInstance();
    }

    /**
     * Instantiate an Object instance, requires a constructor with parameters
     * 
     * @param className
     * @param params
     *            an array including the required parameters to instantiate the
     *            object
     * @return
     * @throws Exception
     */
    public static Object createObject(String className, Object[] params)
            throws Exception {

        return createObject(Class.forName(className), params);
    }

    /**
     * @param classObject
     * @param params
     *            an array including the required parameters to instantiate the
     *            object
     * @return Exception
     */
    private static Object createObject(Class classObject, Object[] params)
            throws Exception {

        Constructor[] constructors = classObject.getConstructors();
        Object object = null;
        for (int i = 0; i < constructors.length; i++) {
            try {
                object = constructors[i].newInstance(params);
            } catch (Exception e) {

                if (e instanceof InvocationTargetException) {
                    ((InvocationTargetException) e).getTargetException()
                            .printStackTrace();
                }
                // Do nothing, try the next constructor
            }
        }
        if (object == null) {
            throw new InstantiationException();
        }
        return object;
    }

}
