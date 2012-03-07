package javax.el;

/**
 * Resolves a bean by its known name.
 * This class can be extended to return a bean object given its name,
 * or to set a value to an existing bean.
 * @see BeanNameELResolver
 *
 * @since EL 3.0
 */
public abstract class BeanNameResolver {
    /**
     * Returns the bean known by its name.
     * @param beanName The name of the bean.
     * @return The bean with the given name.  A <code>null</code> indicates
     *     that a bean with the given name is not found.
     */
    public Object getBean(String beanName) {
        return null;
    }

    /**
     * Sets a value to an existing bean of the given name.
     * @param beanName The name of the bean
     * @param value The new bean for the given name.
     * @throws PropertyNotWritableException if setting a new bean for the given
     *    name is not allowed.
     */
    public void setBeanValue(String beanName, Object value)
             throws PropertyNotWritableException {
        throw new PropertyNotWritableException();
    }

    /**
     * Indicates if the bean of the given name is read-only or writable
     * @param beanName The name of the bean
     * @return <code>true</code> if a new bean can be set for the given name.
     *    <code>false</code> otherwise.
     */
    public boolean isReadOnly(String beanName) {
        return true;
    }
}
