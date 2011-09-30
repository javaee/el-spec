package javax.el;

/**
 * An event associated with the evaluation of EL expressions.
 *
 * @since EL 3.0
 */

public class EvaluationEvent extends java.util.EventObject {

    private String expression;

    /**
     * Constructor
     *
     * @param context The <code>ELContext</code> used in the evaluation of the
     *     expression.
     * @param expression The original expression String
     */
    public EvaluationEvent(ELContext context, String expression) {
        super(context);
        this.expression = expression;
    } 

    /**
     * Retruns the <code>ELContext</code> used in the evaluation of the
     * expression.
     *
     * @return The ELContext
     */
    public ELContext getELContext() {
        return (ELContext) getSource();
    }

    /**
     * Returns the original expression String
     *
     * @return The original expression String
     */
    public String getExpressionString() {
        return expression;
    }
}
