package javax.el;

/**
 * The interface for coercing an object to a specific type.  The
 * {@link ELResolver} contains an implementation of this interface, and
 * is used to coerce an object to a type, in expression evaluations.
 *
 * <p>{@link StandardTypeConverter} extends this interface and implements the
 * conversion rules specified in the specification and is used in
 * {@link StandardELContext}.  The easiest way to plug in a TypeConverter is to
 * subclass <code>StandardTypeConverter</code> to provide a conversion for a
 * specific source and target type, and leave other conversions to its
 * super class.
 *
 * @since EL 3.0
 */

public abstract class TypeConverter {

    /**
     * Coerces an object to a specific type.
     *
     * <p>An <code>ELException</code> is thrown if an error results from
     * applying the conversion rules.
     * </p>
     *
     * @param obj The object to coerce.
     * @param targetType The target type for the coercion.
     * @throws ELException thrown if an error results from applying the
     *     conversion rules.
     */
    public abstract Object coerceToType(Object obj, Class<?> targetType);

}
