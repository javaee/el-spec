package javax.el;

import java.lang.reflect.Method;

/**
 * <p>Manages EL parsing and evaluation enviroment.  The ELManager maintains an
 * instance of ExpressionFactory and StandardELContext, for
 * parsing and evaluating EL expressions.</p>
 *
 * @since EL 3.0
 */
public class ELManager {

    private ExpressionFactory expressionFactory;
    private StandardELContext elContext;

    /**
     * Return the ExpressionFactory instance used for EL evaluations.  If
     * there is currently no ExpressionFactory, an instance is created.
     * @return The ExpressionFactory
     */
    public ExpressionFactory getExpressionFactory() {
        if (expressionFactory == null) {
            expressionFactory = ExpressionFactory.newInstance();
        }
        return expressionFactory;
    }

    /**
     * Return the ELContext used for parsing and evaluating EL expressions.
     * If there is currently no ELContext, a default instance of
     * StandardELContext is returned.
     *
     * @return The ELContext used for parsing and evaluating EL expressions..
     */
    public StandardELContext getELContext() {
        if (elContext == null) {
            elContext = new StandardELContext(
                            expressionFactory.getQueryOperatorELResolver());
        }
        return elContext;
    }

    /**
     * Set the ELContext used for parsing and evaluating EL expressions.
     * The supplied ELContext will not be modified, except for the context
     * object map.
     * @param context The new ELContext.
     * @return The previous ELContext, null if none.
     */
    public ELContext setELContext(ELContext context) {
        ELContext prev = elContext;
        elContext = new StandardELContext(context);
        return prev;
    }

    /**
     * Register a BeanNameResolver.
     * Construct a BeanNameELResolver with the BeanNameResolver and add it
     * to the list of ELResolvers.
     * Once reigstered, the BeanNameResolver cannot be removed.
     * @param bnr The BeanNameResolver to be registered.
     */
    public void addBeanNameResolver(BeanNameResolver bnr) {
        getELContext().addELResolver(new BeanNameELResolver(bnr));
    }

    /**
     * Add an user defined ELResolver to the list of ELResolvers.
     * Can be called multiple times.  The new ELResolver is
     * placed ahead of the default ELResolvers.  The list of the ELResolvers
     * added this way are ordered chronologically.
     * 
     * @param elr The ELResolver to be added to the list of ELResolvers in
     *     ELContext.
     * @see StandardELResolver#addELResolver
     */
    public void addELResolver(ELResolver elr) {
        getELContext().addELResolver(elr);
    }

    /**
     * Maps a static method to an EL function.
     * @param prefix The namespace of the functions, can be "".
     * @param function The name of the function.  
     * @param meth The static method to be invoked when the function is used.
     */
    public void mapFunction(String prefix, String function, Method meth) {
        getELContext().getFunctionMapper().mapFunction(prefix, function, meth);
    }

    /** 
     * Assign a ValueExpression to an EL variable, replacing
     * any previous assignment to the same variable.
     * The assignment for the variable is removed if
     * the expression is <code>null</code>.
     *
     * @param variable The variable name
     * @param expression The ValueExpression to be assigned
     *        to the variable.
     */
    public void setVariable(String variable, ValueExpression expression) {
        getELContext().getVariableMapper().setVariable(variable, expression);
    }

    /**
     * Import a class.  The imported class must be loadable from the
     * classloader, at class resolution time.
     * @param className The full class name of the class to be imported
     * @throws ELException if the name is not a full class name.
     */
    public void importClass(String className) throws ELException {
        getELContext().getImportHandler().importClass(className);
    }

    /**
     * Import a package.  At the class resolution time, the imported package
     * name will be used to construct the full class name, which will then be
     * used to load the class.  Inherently, this is less efficient than
     * importing a class.
     * @param packageName The package name to be imported
     */
    public void importPackage(String packageName) {
        getELContext().getImportHandler().importPackage(packageName);
    }

    /**
     * Define a bean in the local bean repository
     * @param name The name of the bean
     * @param bean The bean instance to be defined.  If null, the defination
     *        of the bean is removed.
     */
    public Object defineBean(String name, Object bean) {
        Object ret = getELContext().getBeans().get(name);
        getELContext().getBeans().put(name, bean);
        return ret;
    }

    /**
     * Set the TypeConverter for expression evaluation
     * @param typeConverter The TypeConverter to be used for expression 
     *     evaluations.
     * @return The previous TypeConverter
     */
    public TypeConverter setTypeConverter(TypeConverter typeConverter) {
        return getELContext().setTypeConverter(typeConverter);
    }

    /**
     * Register a listener.  Used only for {@link EvaluationListener}s in
     * 3.0, but possibly to used for other listeners.
     *
     * @param listener The listener to be added.
     */
    public <T extends java.util.EventListener> void addListener(T listener) {
        getELContext().addListener(listener);
    }
}
