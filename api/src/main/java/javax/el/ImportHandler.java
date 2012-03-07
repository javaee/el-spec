package javax.el;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Handles imports of class names and package names.  An imported package
 * name implicitly imports all the classes in the package.  A class that has
 * been imported can be used without its package name.
 * The name is resolved to its full (package and class) name
 * at evaluation time.
 */
public class ImportHandler {

    private Map<String, String> map = new HashMap<String, String>();
    private List<String> packages = new ArrayList<String>();

    /**
     * Import a class.
     * @param name The full class name of the class to be imported
     * @throws ELException if the name is not a full class name.
     */
    public void importClass(String name) throws ELException {
        int i = name.indexOf('.');
        if (i <= 0) {
            throw new ELException(
                "The name " + name + " is not a full class name");
        }
        String className = name.substring(i+1);
        map.put(className, name);
    }

    /**
     * Import all the classes in a package.
     * @param packageName The package name to be imported
     */
    public void importPackage(String packageName) {
        packages.add(packageName);
    }

    /**
     * Resolve a class name from its imports.
     *
     * @param name The name of the class taken from the expression T(name).
     *     It is assumed that this is a name without a package.
     * @return  If the class has been imported previously (either explicitly,
     *     or as part of an imported package), then its Class instance.
     *     Otherwise <code>null</code>.
     */
    public Class<?> resolve(String name) {

        // TODO: Precomfig and optimize for java.lang.*
        String className = map.get(name);
        if (className != null) {
            return getClassFor(className);
        }

        for (String packageName: packages) {
            String fullClassName = packageName + "." + name;
            Class<?> c = getClassFor(fullClassName);
            if (c != null) {
                map.put(name, fullClassName);
                return c;
            }
        }
        return null;
    }
            
    private Class<?> getClassFor(String className) {
        Class<?> c = null;
        try {
            c = Class.forName(className, false, getClass().getClassLoader());
        } catch (ClassNotFoundException ex) {
        }
        return c;
    }
}
