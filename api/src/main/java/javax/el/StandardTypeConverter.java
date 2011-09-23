package javax.el;

/**
 * The default implementation of TypeConverter.  Coerces an object to a type
 * as specified in the specification.
 */ 
public class StandardTypeConverter extends TypeConverter {

    private ExpressionFactory expressionFactory;
    private ExpressionFactory getExpressionFactory() {
        if (expressionFactory == null) {
            expressionFactory = ExpressionFactory.newInstance();
        }
        return expressionFactory;
    }

    /**
     * Coerces an object to a specific type according to the
     * EL type conversion rules.
     *
     * @param obj The object to coerce.
     * @param targetType The target type for the coercion.
     * @throws ELException thrown if an error results from applying the
     *     conversion rules.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T coerceToType(Object obj, Class<T> targetType) {
        return (T)getExpressionFactory().coerceToType(obj, targetType);
    }
}
