package javax.el;

/**
 * <p>A runtime representation of the sytax <code>T(full-class-Name)</code>
 * that encapsulates the name of the specified class.</p>
 * 
 * <p>This class is used only in {@link StaticFieldELResolver} and will
 * probably only be of interest to EL implementors, and not EL users.
 *
 * @since EL 3.0
 */

public class ELClass {

    private String className;

    /**
     * Constructor
     * @param className The name of the class specified in <code>T(...)</code>.
     */
    public ELClass(String className) {
        this.className = className;
    }

    /**
     * Returns the class name for the specified class.
     * @return The class name for the specified class.
     */
    public String getClassName() {
        return className;
    }
}
