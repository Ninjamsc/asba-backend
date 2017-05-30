package com.technoserv.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Hibernate;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * HibernateInitializer.
 * Initializer for properties of hibernate entity.
 * <p/>
 * Example:
 * <p/>
 * HibernateInitializer.initializeProperties(entity, "entity.property");
 * <p/>
 * <p/>
 */
public final class HibernateInitializer {

    // todo: it's possible to add optimization for properties[n] n>2, to avoid count to call reflection methods and hibernate.initialize.

    public static void initializeProperties(Object entity_or_collection, String... properties) {
        try {
            for (String complex_property : properties) {
                StringTokenizer tokenizer = new StringTokenizer(complex_property, ".");
                List<String> props = new LinkedList<>();
                while (tokenizer.hasMoreElements()) {
                    props.add((String) tokenizer.nextElement());
                }
                // entry point for recursion function!
                recursion_initializeProperties(entity_or_collection, props.toArray(new String[]{}), 0);
            }
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void recursion_initializeProperties(Object obj_property, String[] props, int idx) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (props.length == idx || obj_property == null) {
            return;
        }

        String property = props[idx];

        if (obj_property instanceof Collection) {

            for (Object item : (Collection) obj_property) {

                Object obj_property1 = PropertyUtils.getProperty(item, property);
                Hibernate.initialize(obj_property1);

                recursion_initializeProperties(obj_property1, props, idx + 1);
            }

            return;
        }

        Object obj_property_inner = PropertyUtils.getProperty(obj_property, property);
        if (obj_property_inner != null) {
            Hibernate.initialize(obj_property_inner);

            recursion_initializeProperties(obj_property_inner, props, idx + 1);
        }
    }

}
