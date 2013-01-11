/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

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

    {
        importPackage("java.lang");
    }

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
     * @param name The name of the class to be resolved.
     *     It is assumed that this is a name without a package.
     * @return  If the class has been imported previously, with
     *     {@link #importClass} or {@link #importPackage}, then its
     *     Class instance. Otherwise <code>null</code>.
     */
    public Class<?> resolve(String name) {

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
