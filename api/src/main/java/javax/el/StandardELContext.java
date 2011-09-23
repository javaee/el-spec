package javax.el;

import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Method;

/*
 * A stadard ELContext suitable for use in stand alone EL.
 * This class provides a default implementation of an ELResolver, FunctionMapper
 * and a VariableMapper.  
 *
 * @since EL 3.0
 */

public class StandardELContext extends ELContext {

    /*
     * The ELResolver for this ELContext.
     */
    private ELResolver elResolver;

    /*
     * The list of the custom ELResolvers added to the ELResolvers.
     * An ELResolver is added to the list when addELResolver is called.
     */
    private CompositeELResolver customResolvers;

    /*
     * The ImportHandler for this ELContext.
     */
    private ImportHandler importHandler;

    /*
     * The FunctionMapper for this ELContext.
     */
    private FunctionMapper functionMapper;

    /*
     * The VariableMapper for this ELContext.
     */
    private VariableMapper variableMapper;

    /*
     * The TypeConverter for this ELContext.
     */
    private TypeConverter typeConverter;

    /*
     * If non-null, indicates the presence of a delegate ELContext.
     * When a Standard is constructed from another ELContext, there is no
     * easy way to get its private context map, therefore delegation is needed.
     */
    private ELContext delegate = null;
 
    /**
     * A bean repository local to this context
     */
    private Map<String, Object> beans = new HashMap<String, Object>();

    /**
     * Default Constructor
     */
    public StandardELContext() {
    }

    /**
     * Construct a StandardELContext from another ELContext
     * @param context The ELContext that acts as a delegate in most cases
     */
    public StandardELContext(ELContext context) {
        this.delegate = context;
        // Copy all attributes except map and resolved
        elResolver = context.getELResolver();
        functionMapper = context.getFunctionMapper();
        variableMapper = context.getVariableMapper();
        setLocale(context.getLocale());
    }

    @Override
    public void putContext(Class key, Object contextObject) {
        if (delegate !=null) {
            delegate.putContext(key, contextObject);
        } else {
            super.putContext(key, contextObject);
        }
    }

    @Override
    public Object getContext(Class key) {
        if (delegate !=null) {
            return delegate.getContext(key);
        } else {
            return super.getContext(key);
        }
    }

    /**
     * Construct (if needed) and return a default ELResolver
     * Retrieves the <code>ELResolver</code> associated with this context.
     * @return The ELResolver for this context.
     */
    @Override
    public ELResolver getELResolver() {
        if (elResolver == null) {
            CompositeELResolver resolver = new CompositeELResolver();
            resolver.add(new BeanNameELResolver(new LocalBeanNameResolver()));
            customResolvers = new CompositeELResolver();
            resolver.add(customResolvers);
            resolver.add(new StaticFieldELResolver());
            resolver.add(new MapELResolver());
            resolver.add(new ResourceBundleELResolver());
            resolver.add(new ListELResolver());
            resolver.add(new ArrayELResolver());
            resolver.add(new BeanELResolver());
            elResolver = resolver;
        }
        return elResolver;
    }

    /**
     * Add a custom ELResolver to the context.  The list of the custom
     * ELResolvers will be accessed in the order they are added.
     * A custom ELResolver added to the context cannot be removed.
     * @param cELResolver The new ELResolver to be added to the context
     */
    public void addELResolver(ELResolver cELResolver) {
        getELResolver();  // make sure elResolver is constructed
        customResolvers.add(cELResolver);
    }

    /**
     * Construct (if need) and return an ImportHandler {
     */
    public ImportHandler getImportHandler() {
        if (importHandler == null) {
            importHandler = new ImportHandler();
        }
        return importHandler;
    }

    /**
     * Get the local bean repository
     * @return the bean repository
     */
    public Map<String, Object> getBeans() {
        return beans;
    } 

    /**
     * Construct (if needed) and return a default FunctionMapper.
     * @return The default FunctionMapper
     */
    @Override
    public FunctionMapper getFunctionMapper() {
        if (functionMapper == null) {
            functionMapper = new DefaultFunctionMapper();
        }
        return functionMapper;
    }

    /**
     * Construct (if needed) and return a default VariableMapper() {
     * @return The defualt Variable
     */
    @Override
    public VariableMapper getVariableMapper() {
        if (variableMapper == null) {
            variableMapper = new DefaultVariableMapper();
        }
        return variableMapper;
    }

    /**
     * Set the TypeConverter for expression evaluation
     * @param typeConverter The TypeConverter to be used for expression
     *     evaluations.
     * @return The previous TypeConverter
     */
    public TypeConverter setTypeConverter(TypeConverter typeConverter) {
        TypeConverter prev = this.typeConverter;
        this.typeConverter = typeConverter;
        return prev;
    }

    /**
     * Construct (if needed) and return a default TypeConverter() {
     */
    @Override
    public TypeConverter getTypeConverter() {
        if (typeConverter == null) {
            typeConverter = new StandardTypeConverter();
        }
        return typeConverter;
    }

    private static class DefaultFunctionMapper extends FunctionMapper {

        private Map<String, Method> functions = null;
        
        @Override
        public Method resolveFunction(String prefix, String localName) {
            if (functions == null) {
                return null;
            }
            return functions.get(prefix + ":" + localName);
        }

    
        @Override
        public void mapFunction(String prefix, String localName, Method meth){
            if (functions == null) {
                functions = new HashMap<String, Method>();
            }
            functions.put(prefix + ":" + localName, meth);
        }
    }

    private static class DefaultVariableMapper extends VariableMapper {

        private Map<String, ValueExpression> variables = null;

        @Override
        public ValueExpression resolveVariable (String variable) {
            if (variables == null) {
                return null;
            }
            return variables.get(variable);
        }

        @Override
        public ValueExpression setVariable(String variable,
                                           ValueExpression expression) {
            if (variables == null) {
                variables = new HashMap<String, ValueExpression>();
            }
            ValueExpression prev = null;
            if (expression == null) {
                prev = variables.remove(variable);
            } else {
                prev = variables.put(variable, expression);
            }
            return prev;
        }
    }

    private class LocalBeanNameResolver extends BeanNameResolver {

        @Override
        public Object getBean(String beanName) {
            return beans.get(beanName);
        }

        @Override
        public void setBeanValue(String beanName, Object value) {
            beans.put(beanName, value);
        }

        @Override
        public boolean isReadOnly(String beanName) {
            return false;
        }
    }
}
  
