package br.com.battista.arcadia.caller.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import br.com.battista.arcadia.caller.exception.AppException;

public final class MergeBeanUtils {

    private MergeBeanUtils() {
    }

    public static <M> void merge(M target, M destination) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());

            // Iterate over all the attributes
            for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
                processDescriptor(target, destination, descriptor);
            }
        } catch (Exception e) {
            throw new AppException("Error to merge entities.", e);
        }
    }

    private static <M> void processDescriptor(M target, M destination, PropertyDescriptor descriptor) throws IllegalAccessException, InvocationTargetException {
        // Only copy writable attributes
        if (descriptor.getWriteMethod() != null) {
            Object newValue = descriptor.getReadMethod().invoke(destination);

            // Only copy values values where the destination values is not null
            if (newValue != null) {
                descriptor.getWriteMethod().invoke(target, newValue);
            }

        }
    }
}
