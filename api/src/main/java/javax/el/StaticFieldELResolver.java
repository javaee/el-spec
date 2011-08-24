package javax.el;

import java.util.Iterator;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.beans.FeatureDescriptor;

/*
 * An ELResolver for resolving user or container managed beans.
 *
 * @since EL 3.0
 */
public class StaticFieldELResolver extends ELResolver {

    private BeanNameResolver beanNameResolver;

    /**
     * If the base object is an instance of <code>ELClass</code>and the
     * property is String, the
     * <code>propertyResolved</code> property of the <code>ELContext</code>
     * object must be set to <code>true</code> by this resolver, before
     * returning. If this property is not <code>true</code> after this
     * method is called, the caller should ignore the return value.</p>
     *
     * If the property is the string "class", return the java.lang.Class 
     * instance of the class specified in ELClass.
     * If the property is a public static field of class specified in
     * ELClass, return the value of the static field.  A Enum constant is a
     * public static field of a Enum object, and is a special case of this.
     * @param context The context of this evaluation.
     * @param base <code>null</code>
     * @param property The string "class", or a static field name.
     * @return If the <code>propertyResolved</code> property of
     *     <code>ELContext</code> was set to <code>true</code>, then the Class
     *     instance for the class or the static field value.
     * @throws NullPointerException if context is <code>null</code>.
     * @throws PropertyNotFoundException if the specified class does not exist,
     *         or if the field is not a public static filed of the class,
     *         or if the field is inacessible.
     */
    @Override
    public Object getValue(ELContext context, Object base, Object property) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (base instanceof ELClass && property instanceof String) {
            Class<?> klass = getClassClass(context, (ELClass)base);
            String fieldName = (String) property;
            try {
                context.setPropertyResolved(true);
                if ("class".equals(fieldName)) {
                    return klass;
                }
                Field field = klass.getField(fieldName);
                int mod = field.getModifiers();
                if (Modifier.isPublic(mod) && Modifier.isStatic(mod)) {
                    return field.get(null);
                }
            } catch (NoSuchFieldException ex) {
            } catch (IllegalAccessException ex) {
            }
            throw new PropertyNotFoundException(
                        ELUtil.getExceptionMessageString(context,
                            "staticFieldReadError",
                            new Object[] { klass.getName(), fieldName}));
        }
        return null;
    }

    /**
     * If the base object is an instance of <code>ELClass</code>and the
     * property is String, the
     * <code>propertyResolved</code> property of the <code>ELContext</code>
     * object must be set to <code>true</code> by this resolver, before
     * returning. If this property is not <code>true</code> after this
     * method is called, the caller should ignore the return value.</p>
     *
     * If the property string is a public static and non-final field of the
     * class specified in ELClass, the field is set to the given value.
     * @param context The context of this evaluation.
     * @param base <code>null</code>
     * @param property The name of the field
     * @param value The value to set the field of the class to.
     * @throws NullPointerException if context is <code>null</code>
     * @throws PropertyNotWritableException if field is not a public static
     *         non-final filed of the class, or if the field is inacessible.
     * @throws ELException if an exception was thrown while attempting to
     *     set the bean with the given name.  The thrown exception
     *     must be included as the cause property of this exception, if
     *     available.
     */
    @Override
    public void setValue(ELContext context, Object base, Object property,
                         Object value) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base instanceof ELClass  && property instanceof String) {
            Class<?> klass = getClassClass(context, (ELClass)base);
            String fieldName = (String) property;
            try {
                context.setPropertyResolved(true);
                Field field = klass.getField(fieldName);
                int mod = field.getModifiers();
                if (Modifier.isPublic(mod) && Modifier.isStatic(mod) &&
                        ! Modifier.isFinal(mod)) {
                    field.set(null, value);
                }
            } catch (NoSuchFieldException ex) {
            } catch (IllegalAccessException ex) {
            }
            throw new PropertyNotWritableException(
                        ELUtil.getExceptionMessageString(context,
                            "staticFieldWriteError",
                            new Object[] { klass.getName(), fieldName}));
        }
    }

    /**
     * If the base object is an instance of <code>ELClass</code>and the
     * property is a String,
     * the <code>propertyResolved</code> property of the
     * <code>ELContext</code> object must be set to <code>true</code>
     * by the resolver, before returning. If this property is not
     * <code>true</code> after this method is called, the caller can
     * safely assume no value has been set.</p>
     *
     * If the property is the string "class", return java.lang.Class object
     * of java.lang.Class.
     * If the property string is a public static field of class specified in
     * ELClass, return the type of the static field.
     * @param context The context of this evaluation.
     * @param base The ELClass instance
     * @param property The name of the field.
     * @return If the <code>propertyResolved</code> property of
     *     <code>ELContext</code> was set to <code>true</code>, then
     *     the type of the type of the field.
     * @throws NullPointerException if context is <code>null</code>.
     * @throws PropertyNotFoundException if field is not a public static
     *         filed of the class, or if the field is inacessible.
     */
    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (base instanceof ELClass  && property instanceof String) {
            Class<?> klass = getClassClass(context, (ELClass)base);
            String fieldName = (String) property;
            try {
                context.setPropertyResolved(true);
                if ("class".equals(fieldName)) {
                    return Class.class;
                }
                Field field = klass.getField(fieldName);
                int mod = field.getModifiers();
                if (Modifier.isPublic(mod) && Modifier.isStatic(mod)) {
                    return field.getType();
                }
            } catch (NoSuchFieldException ex) {
            }
            throw new PropertyNotFoundException(
                        ELUtil.getExceptionMessageString(context,
                            "staticFieldReadError",
                            new Object[] { klass.getName(), fieldName}));
        }
        return null;
    }

    /**
     * If the base object is an instance of <code>ELClass</code>and the
     * property is a String,
     * the <code>propertyResolved</code> property of the
     * <code>ELContext</code> object must be set to <code>true</code>
     * by the resolver, before returning. If this property is not
     * <code>true</code> after this method is called, the caller can
     * safely assume no value has been set.</p>
     *
     * If the property string is a public static and non-final field of the
     * class specified in ELClass, return false;
     * @param context The context of this evaluation.
     * @param base <code>null</code>
     * @param property The name of the bean.
     * @return If the <code>propertyResolved</code> property of
     *     <code>ELContext</code> was set to <code>true</code>, then
     *     <code>true</code> if the field is read-only or
     *     <code>false</code> if not; otherwise undefined.
     * @throws NullPointerException if context is <code>null</code>.
     */
    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }

        if (base instanceof ELClass  && property instanceof String) {
            try {
                Class<?> klass = getClassClass(context, (ELClass)base);
                String fieldName = (String) property;
                context.setPropertyResolved(true);
                Field field = klass.getField(fieldName);
                int mod = field.getModifiers();
                if (Modifier.isPublic(mod) && Modifier.isStatic(mod) &&
                        ! Modifier.isFinal(mod)) {
                    return false;
                }
            } catch (NoSuchFieldException ex) {
            }
        }
        return true;
    }

    /**
     * Always returns <code>null</code>, since there is no reason to 
     * iterate through a list of one element: field name.
     * @param context The context of this evaluation.
     * @param base <code>null</code>.
     * @return <code>null</code>.
     */
    public Iterator<FeatureDescriptor> getFeatureDescriptors(
                                   ELContext context, Object base) {
        return null;
    }

    /**
     * Always returns <code>String.class</code>, since a field name is a String.
     * @param context The context of this evaluation.
     * @param base <code>null</code>.
     * @return <code>String.class</code>.
     */
    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return String.class;
    }

    /**
     * Return the Class object for the specified class
     */
    private Class<?> getClassClass(ELContext context, ELClass elClass) {
        try {
            return Class.forName(elClass.getClassName(), false,
                                 getClass().getClassLoader());
        } catch (ClassNotFoundException ex) {
            throw new PropertyNotFoundException(
                        ELUtil.getExceptionMessageString(context,
                        "classNotFound",
                        new Object[] {elClass.getClassName()}));
        }
    }
}
