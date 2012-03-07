package javax.el;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * <p>Encapsulates a parameterized {@link ValueExpression}.</p>
 *
 * <p>A <code>LambdaExpression</code> is a representation of the EL Lambda
 * expression syntax.  It consists of a list of the formal parameters and a
 * body, represented by a {@link ValueExpression}.
 * The body can be any valid <code>Expression</code>, including another
 * <code>LambdaExpression</code>.</p>
 * A <code>LambdaExpression</code> is created when an EL expression containing
 * a Lambda expression is evaluated.</p>
 * <p>A <code>LambdaExpression</code> can be invoked by calling
 * {@link LambdaExpression#invoke}, with
 * an {@link javax.el.ELContext} and a list of the actual arguments.  The
 * evaluation of the <code>ValueExpression</code> in the body uses the
 * {@link ELContext} to resolve references to the parameters.
 * The result of the evaluation is returned.</p as the
 * @see ELContext#getLambdaArgument
 * @see ELContext#enterLambdaScope
 * @see ELContext#exitLambdaScope
 */

public class LambdaExpression {

    private List<String> formalParameters = new ArrayList<String>();
    private ValueExpression expression;

    /**
     * Creates a new LambdaExpression.
     * @param formalParameters The list of String representing the formal
     *        parameters.
     * @param expression The <code>ValueExpression</code> representing the
     *        body.
     */
    public LambdaExpression (List<String> formalParameters,
                             ValueExpression expression) {
        this.formalParameters = formalParameters;
        this.expression = expression;
    }

    /**
     * Retrieves the formal parameters of the Lambda expression
     * @return The list of the parameter names.
     */
    public List<String> getFormalParameters() {
        return this.formalParameters;
    }

    /**
     * Invoke the encapsulated Lambda expression.
     * <p> The supplied arguments are matched, in
     * the same order, to the formal parameters.  If there are more arguments
     * than the formal parameters, the extra arguments are ignored.  If there
     * are less arguments than the formal parameters, an
     * <code>ELException</code> is thrown.</p>
     *
     * <p>The actual Lambda arguments are added to the ELContext and are
     * available during the evaluation of the Lambda expression.  They are
     * removed after the evluation.</p>
     *
     * @param elContext The ELContext used for the evaluation of the expression
     * @param args The arguments for the Lambda expression
     * @return The result of invoking the Lambda expression
     * @throws ELException if not enough arguments are provided
     */
    public Object invoke(ELContext elContext, Object... args) 
            throws ELException {
        int i = 0;
        Map<String, Object> lambdaArgs = new HashMap<String, Object>();
        for (String fParam: formalParameters) {
            if (i >= args.length) {
                throw new ELException("Expected Argument " + fParam +
                            " missing in Lambda Expression");
            }
            lambdaArgs.put(fParam, args[i++]);
        }
        elContext.enterLambdaScope(lambdaArgs);
        Object ret = expression.getValue(elContext);
        elContext.exitLambdaScope();
        return ret;
    }
}
