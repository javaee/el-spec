package javax.el;

/**
 * The abstract class coerces an object to a specific type.  The
 * {@link ELContext} contains an extension of this interface, and
 * is used for type coercion in expression evaluations.
 *
 * <p>{@link StandardTypeConverter} extends this class to implement the
 * conversion rules specified in the specification and is used in
 * {@link StandardELContext}.  The easiest way to plug in a TypeConverter is to
 * subclass <code>StandardTypeConverter</code> to provide a conversion for a
 * specific source and target type, while leaving other conversions to its
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
