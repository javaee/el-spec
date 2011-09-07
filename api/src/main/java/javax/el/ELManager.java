package javax.el;

import java.lang.reflect.Method;

/*
 * Manages EL parsing and evaluation enviroment.  The ELManager maintains an
 * instance of ELContext and ExpressionFactory, for parsing and evaluating EL
 * expressions.  Both can be replaced.
 *
 * The ELManager manages the EL vironment and handles adding an ELResolver,
 * defining a function, and setting of a variable.
 *
 * @since EL 3.0
 */
public class ELManager {

    private ExpressionFactory expressionFactory;
    private StandardELContext elContext;

    /**
     * Return the ExpressionFactory instance used for EL evaluations
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
     * @return The ELContext used for parsing and evaluating EL expressions..
     */
    public StandardELContext getELContext() {
        if (elContext == null) {
            elContext = new StandardELContext();
        }
        return elContext;
    }

    /**
     * Set the ELContext used for parsing and evaluating EL expressions.
     * @param context The new ELContext.
     * @return The previous ELContext, null if none.
     */
    public ELContext setELContext(ELContext context) {
        ELContext prev = elContext;
        if (context instanceof StandardELContext) {
            elContext = (StandardELContext) context;
        } else {
            elContext = new StandardELContext(context);
        }
        return prev;
    }

    /**
     * Register a BeanNameResolver.
     * Implicitly adds a BeanNameELResolver to the list of ELResolvers
     * Once reigstered, it cannot be removed.
     * @param bnr The BeanNameResolver to be registered.
     */
    public void addBeanNameResolver(BeanNameResolver bnr) {
        getELContext().addELResolver(new BeanNameELResolver(bnr));
    }

    /**
     * Add an user defined ELResolver to the currect list of ELResolvers
     * Can be called multiple times with additive effect.
     * @param elr The ELResolver to be added to the list of ELResolvers in
     *     ELContext.
     */
    public void addELResolver(ELResolver elr) {
        getELContext().addELResolver(elr);
    }

    /**
     * Maps a static method to an EL function
     * @param function The name of the function
     * @param meth The static method to be invoked when the function is used.
     */
    public void mapFunction(String function, Method meth) {
        int i = function.indexOf(':');
        String prefix, localName;
        if (i < 0) {
            prefix = "";
            localName = function;
        } else {
            prefix = function.substring(0, i);
            localName = function.substring(i+1);
        }
        getELContext().getFunctionMapper().mapFunction(prefix, localName, meth);
    }

    /** 
     * Assign a ValueExpression to an EL variable, replacing
     * any previously assignment to the same variable.
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
     * Import a class.
     * @param className The full class name of the class to be imported
     * @throws ELException if the name is not a full class name.
     */
    public void importClass(String className) throws ELException {
        getELContext().getImportHandler().importClass(className);
    }

    /**
     * Import all the classes in a package.
     * @param packageName The package name to be imported
     */
    public void importPackage(String packageName) {
        getELContext().getImportHandler().importPackage(packageName);
    }

    /**
     * Define a bean in the local bean repository
     * @param name The name of the bean
     * @param bean The bean instance to be defined
     */
    public void defineBean(String name, Object bean) {
        getELContext().getBeans().put(name, bean);
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

}
