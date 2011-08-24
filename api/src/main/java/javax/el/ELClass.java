package javax.el;

/**
 * A runtime representation of the sytax T(full-class-Name).  Used only in
 * StaticFieldELResolver.
 *
 * @since EL 3.0
 */

public class ELClass {

    private String className;

    /**
     * Constructor
     * @param className The name of the class specified in T(...).
     */
    public ELClass(String className) {
        this.className = className;
    }

    /**
     * Return the class name for the specified class.
     * @return The class name for the specified class.
     */
    public String getClassName() {
        return className;
    }
}
