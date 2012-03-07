package javax.el;

import java.util.Iterator;
import java.beans.FeatureDescriptor;

/**
 * <p>An <code>ELResolver</code> for resolving user or container managed beans.</p>
 * <p>A {@link BeanNameResolver} is required for its proper operation.
 * The following example creates an <code>ELResolver</code> that 
 * reoslves the name "bean" to an instance of MyBean.
 * <blockquote>
 * <pre>
 * ELResovler elr = new BeanNameELResolver(new BeanNameResolver {
 *    public Object getBean(String beanName) {
 *       if ("bean".equals(beanName))
 *          return new MyBean();
 *       return null;
 *    }
 * });
 * </pre>
 * </blockquote>
 * </p>
 * @since EL 3.0
 */
public class BeanNameELResolver extends ELResolver {

    private BeanNameResolver beanNameResolver;

    /**
     * Constructor
     * @param beanNameResolver The {@link BeanNameResolver} that resolves a bean name.
     */
    public BeanNameELResolver(BeanNameResolver beanNameResolver) {
        this.beanNameResolver = beanNameResolver;
    }

    /**
     * If the base object is <code>null</code>and the property is a name
     * that is resolvable by the BeanNameResolver, returns the value
     * resolved by the BeanNameResolver.
     *
     * <p>If name is resolved by the BeanNameResolver, the
     * <code>propertyResolved</code> property of the <code>ELContext</code>
     * object must be set to <code>true</code> by this resolver, before
     * returning. If this property is not <code>true</code> after this
     * method is called, the caller should ignore the return value.</p>
     *
     * @param context The context of this evaluation.
     * @param base <code>null</code>
     * @param property The name of the bean.
     * @return If the <code>propertyResolved</code> property of
     *     <code>ELContext</code> was set to <code>true</code>, then
     *     the value of the bean with the given name. Otherwise, undefined.
     * @throws NullPointerException if context is <code>null</code>.
     * @throws ELException if an exception was thrown while performing
     *     the property or variable resolution. The thrown exception
     *     must be included as the cause property of this exception, if
     *     available.
     */
    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base == null && property instanceof String) {
            Object bean = beanNameResolver.getBean((String) property);
            if (bean != null) {
                context.setPropertyResolved(true);
                return bean;
            }
        }
        return null;
    }

    /**
     * If the base is null and the property is a name that is resolvable by
     * the BeanNameResolver, the bean in the BeanNameResolver is set to the
     * given value.
     *
     * <p>If the name is resolvable by the BeanNameResolver,
     * the <code>propertyResolved</code> property of the
     * <code>ELContext</code> object must be set to <code>true</code>
     * by the resolver, before returning. If this property is not
     * <code>true</code> after this method is called, the caller can
     * safely assume no value has been set.</p>
     *
     * @param context The context of this evaluation.
     * @param base <code>null</code>
     * @param property The name of the bean
     * @param value The value to set the bean with the given name to.
     * @throws NullPointerException if context is <code>null</code>
     * @throws PropertyNotWritableException if the BeanNameResolver does not
     *     allow the bean to be modified.
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

        if (base == null && property instanceof String) {
            if (beanNameResolver.isReadOnly((String) property)) {
                throw new PropertyNotWritableException("The bean " +
                    property + " is not writable.");
            }
            Object bean = beanNameResolver.getBean((String) property);
            if (bean != null) {
                context.setPropertyResolved(true);
                beanNameResolver.setBeanValue((String) property, value);
            }
        }
    }

    /**
     * If the base is null and the property is a name resolvable by
     * the BeanNameResolver, return the type of the bean.
     *
     * <p>If the name is resolvable by the BeanNameResolver,
     * the <code>propertyResolved</code> property of the
     * <code>ELContext</code> object must be set to <code>true</code>
     * by the resolver, before returning. If this property is not
     * <code>true</code> after this method is called, the caller can
     * safely assume no value has been set.</p>
     *
     * @param context The context of this evaluation.
     * @param base <code>null</code>
     * @param property The name of the bean.
     * @return If the <code>propertyResolved</code> property of
     *     <code>ELContext</code> was set to <code>true</code>, then
     *     the type of the bean with the given name. Otherwise, undefined.
     * @throws NullPointerException if context is <code>null</code>.
     * @throws ELException if an exception was thrown while performing
     *     the property or variable resolution. The thrown exception
     *     must be included as the cause property of this exception, if
     *     available.
     */
    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (base == null && property instanceof String) {
            Object bean = beanNameResolver.getBean((String) property);
            if (bean != null) {
                context.setPropertyResolved(true);
                return bean.getClass();
            }
        }
        return null;
    }

    /**
     * If the base is null and the property is a name resolvable by
     * the BeanNameResolver, attempts to determine if the bean is writable.
     *
     * <p>If the name is resolvable by the BeanNameResolver,
     * the <code>propertyResolved</code> property of the
     * <code>ELContext</code> object must be set to <code>true</code>
     * by the resolver, before returning. If this property is not
     * <code>true</code> after this method is called, the caller can
     * safely assume no value has been set.</p>
     *
     * @param context The context of this evaluation.
     * @param base <code>null</code>
     * @param property The name of the bean.
     * @return If the <code>propertyResolved</code> property of
     *     <code>ELContext</code> was set to <code>true</code>, then
     *     <code>true</code> if the property is read-only or
     *     <code>false</code> if not; otherwise undefined.
     * @throws NullPointerException if context is <code>null</code>.
     * @throws ELException if an exception was thrown while performing
     *     the property or variable resolution. The thrown exception
     *     must be included as the cause property of this exception, if
     *     available.
     */
    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }

        if (base == null && property instanceof String) {
            Object bean = beanNameResolver.getBean((String) property);
            if (bean != null) {
                context.setPropertyResolved(true);
                return beanNameResolver.isReadOnly((String) property);
            }
        }
        return false;
    }

    /**
     * Always returns <code>null</code>, since there is no reason to 
     * iterate through a list of one element: bean name.
     * @param context The context of this evaluation.
     * @param base <code>null</code>.
     * @return <code>null</code>.
     */
    public Iterator<FeatureDescriptor> getFeatureDescriptors(
                                   ELContext context, Object base) {
        return null;
    }

    /**
     * Always returns <code>String.class</code>, since a bean name is a String.
     * @param context The context of this evaluation.
     * @param base <code>null</code>.
     * @return <code>String.class</code>.
     */
    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return String.class;
    }
}
