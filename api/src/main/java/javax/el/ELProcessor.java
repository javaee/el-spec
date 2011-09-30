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
 * The EL expressions allowed in the methods getValue, setValue, and 
 * setVariable are limited to non-composite expressions, i.e. expressions
 * of the form ${...} or #{...}.  Also, it is not necessary (in fact not
 * allowed) to bracket the expression strings with ${ or #{ and } in these
 * methods: they will be automatically bracketed.  This reduces the visual
 * cluster, without lost of functionalities (thanks to the addition of the
 * concatenation operator).
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
                                  bracket(expression), expectedType);
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
                                  bracket(expression), Object.class);
        exp.setValue(elManager.getELContext(), value);
    }

    /**
     * Assign an EL expression to an EL variable, without evaluation, and
     * replace any previously assign expression to the same variable.
     * The assignment for the variable is removed if
     * the expression is <code>null</code>.
     * @param var The name of the variable.
     * @param expression The EL expression to be assigned to the variable.
     */
    public void setVariable(String var, String expression) {
        ValueExpression exp = factory.createValueExpression(
                                  elManager.getELContext(),
                                  bracket(expression), Object.class);
        elManager.setVariable(var, exp);
    }

    /*
     * Define an EL function.
     * @param function The name of the function, with optional namespace prefix
     *    (e.g. "func" or "ns:func").  Can be null or empty (""), in which case
     *    the method name is used as the function name.
     * @param className The name of the Java class that implements the function
     * @param method The name (specified without parenthesis) or the signature 
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
    public void defineFunction(String function,
                               String className,
                               String method)
            throws ClassNotFoundException, NoSuchMethodException {

        Method meth = null;
        ClassLoader loader = getClass().getClassLoader();
        Class<?> klass = Class.forName(className, false, loader);
        int j = method.indexOf('(');
        if (j < 0) {
            // Just a name is given
            for (Method m: klass.getDeclaredMethods()) {
                if (m.getName().equals(method)) {
                    meth = m;
                }
            }
            if (meth == null) {
                throw new NoSuchMethodException();
            }
        } else {
            // method is the signature
            // First get the method name, ignore the return type
            int p = method.indexOf(' ');
            if (p < 0) {
                throw new NoSuchMethodException(
                    "Bad method singnature: " + method);
            }
            String methodName = method.substring(p+1, j).trim();
            // Extract parameter types
            p = method.indexOf(')', j+1);
            if (p < 0) {
                throw new NoSuchMethodException(
                    "Bad method singnature: " + method);
            }
            String[] params = method.substring(j+1, p).split(",");
            Class<?>[] paramTypes = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++) {
                paramTypes[i] = toClass(params[i], loader);
            }
            meth = klass.getDeclaredMethod(methodName, paramTypes);
        }
        elManager.mapFunction(function, meth);
    }

    /**
     * Define an EL function
     * @param function The name of the function, with optional namespace prefix
     *    (e.g. "func" or "ns:func").  Can be null or empty (""), in which case
     *    the method name is used as the function name.
     * @param method The java.lang.reflect.Method instance of the method that
     *    implements the function.
     */
    public void defineFunction(String function, Method method) {
        elManager.mapFunction(function, method);
    }

    /**
     * Define a bean in a local bean repository
     * @param name The name of the bean
     * @param bean The bean instance to be defined
     */
    public void defineBean(String name, Object bean) {
        elManager.defineBean(name, bean);
    }

    /**
     * Return the Class object associated with the class or interface with
     * the given name.
     */
    private static Class<?> toClass(String type, ClassLoader loader)
            throws ClassNotFoundException {

        Class<?> c = null;
        int i0 = type.indexOf('[');
        int dims = 0;
        if (i0 > 0) {
            // This is an array.  Count the dimensions
            for (int i = 0; i < type.length(); i++) {
                if (type.charAt(i) == '[')
                    dims++;
            }
            type = type.substring(0, i0);
        }

        if ("boolean".equals(type))
            c = boolean.class;
        else if ("char".equals(type))
            c = char.class;
        else if ("byte".equals(type))
            c =  byte.class;
        else if ("short".equals(type))
            c = short.class;
        else if ("int".equals(type))
            c = int.class;
        else if ("long".equals(type))
            c = long.class;
        else if ("float".equals(type))
            c = float.class;
        else if ("double".equals(type))
            c = double.class;
        else
            c = loader.loadClass(type);

        if (dims == 0)
            return c;

        if (dims == 1)
            return java.lang.reflect.Array.newInstance(c, 1).getClass();

        // Array of more than i dimension
        return java.lang.reflect.Array.newInstance(c, new int[dims]).getClass();
    }

    private String bracket(String expression) {
        return "${" + expression + '}';
    }
}

