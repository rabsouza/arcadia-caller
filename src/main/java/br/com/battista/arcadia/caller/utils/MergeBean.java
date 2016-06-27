package br.com.battista.arcadia.caller.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * Created by rabsouza on 12/04/16.
 */
public class MergeBean {

    public <M> void merge(M target, M destination) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());

        // Iterate over all the attributes
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {

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
}
