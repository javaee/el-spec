package javax.el;

import java.lang.reflect.Method;

/**
 * This class provides an API for using EL stand-alone, outside of a web
 * container.  It provides a direct and simple interface for creating and
 * evaluating EL expressions, while hiding low level details from the users.
 * This API is not indenpendent from the EL 2.2 API, but rather builds
 * on top of it.  
 * 
 * This API operates on a level higher that those provided in EL 2.2, in that
 * it hides implementation details (such as ELResolver and ValueExpression)
 * from the users.
 *
 * The EL processing environment is handled by the use of ELManager.
 *
 * @since EL 3.0
 */

public class ELProcessor {

    private ELManager elManager = new ELManager();
    private ExpressionFactory factory = elManager.getExpressionFactory();

    /**
     * Return the ELManager used for EL porcessing.
     * @return The ELManager used for EL porcessing.
     */
    public ELManager getELManager() {
        return elManager;
    }

    /*
     * Evaluate an EL expression, without coercion.
     * @param expression The EL expression to be evaluated.
     * @return The result of the expression evaluation.
     */
    public Object getValue(String expression) {
        return getValue(expression, Object.class);
    }

    /*
     * Evaluate an EL expression, with coercion.
     * @param expression The EL expression to be evaluated.
     * @param exprectedType Specifies the type that the resultant evaluation
     *        will be coerced to.
     * @return The result of the expression evaluation.
     */
    public Object getValue(String expression, Class<?> expectedType) {
        ValueExpression exp = factory.createValueExpression(
                                  elManager.getELContext(),
                                  expression, expectedType);
        return exp.getValue(elManager.getELContext());
    }

    /*
     * Evaluates the expression, and sets the result to the provided value.
     * @param expression The expression, to be evaluated.  
     * @param value The new value to be set.
     * @throws PropertyNotFoundException if one of the property
     *     resolutions failed because a specified variable or property
     *     does not exist or is not readable.
     * @throws PropertyNotWritableException if the final variable or
     *     property resolution failed because the specified
     *     variable or property is not writable.
     * @throws ELException if an exception was thrown while attempting to
     *     set the property or variable. The thrown exception
     *     must be included as the cause property of this exception, if
     *     available.
     */
    public void setValue(String expression, Object value) {
        ValueExpression exp = factory.createValueExpression(
                                  elManager.getELContext(),
                                  expression, Object.class);
        exp.setValue(elManager.getELContext(), value);
    }

    /**
     * Assign an EL expression to an EL variable, replacing
     * any previously assignment to the same variable.
     * The assignment for the variable is removed if
     * the expression is <code>null</code>.
     * @param var The name of the variable.
     * @param expression The EL expression to be assigned to the variable.
     */
    public void setVariable(String var, String expression) {
        ValueExpression exp = factory.createValueExpression(
                                  elManager.getELContext(),
                                  expression, Object.class);
        elManager.setVariable(var, exp);
    }

    /*
     * Define an EL function.
     * @param prefix The prefix of the function, or "" if prefix not used.
     * @param localName The short name of the function.
     * @param className The name of the Java class that implements the function
     * @param method The name (without the parenthese) or the signature 
     *    (as in the Java Language Spec) of the method that implements the
     *    function.  If the name (e.g. "sum") is given, the first declared
     *    method in class that matches the name is selected.  If the signature
     *    (e.g. "int sum(int, int)" ) is given, then the declared method
     *    with the signature is selected.
     *    
     * @throws ClassNoFoundException if the specified class does not exists.
     * @throws NoSuchMethodException if the method (with or without the
     *    signature) is not a declared method of the class, or if the method
     *    signature is not valid.
     */
    public void defineFunction(String prefix, String localName,
                               String className,
                               String method)
            throws ClassNotFoundException, NoSuchMethodException {

        Method meth = null;
        Class<?> klass = Class.forName(className);
        int j = method.indexOf('(');
        if (j < 0) {
            // Just a name is given
            for (Method m: klass.getMethods()) {
                if (m.getName().equals(method)) {
                    meth = m;
                }
            }
            if (meth == null) {
                throw new NoSuchMethodException();
            }
        } else {
            // TODO: get meth from signature
        }
        elManager.mapFunction(prefix, localName, meth);
    }

    /**
     * Define a bean in a local bean repository
     * @name The name of the bean
     * @bean The bean instance to be defined
     */
    public void defineBean(String name, Object bean) {
        elManage.defineBean(name, bean);
    }
}

