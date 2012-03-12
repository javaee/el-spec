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
 *
 * @author Kin-man Chung
 */

package com.sun.el.query;

import java.util.Iterator;
import java.beans.FeatureDescriptor;

import javax.el.ELContext;
import javax.el.ELResolver;

/*
 * This ELResolver handles the operators (implemented with build-in
 * methods in Iterable's) in LINQ, .NET Language Integrated Query.  See
 * http://msdn.microsoft.com/en-us/library/bb394939.aspx
 */

public class QueryOperatorELResolver extends ELResolver {

    public Object invoke(ELContext context,
                         Object base,
                         Object method,
                         Class<?>[] paramTypes,
                         Object[] params) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (! (base instanceof Iterable && method instanceof String)) {
            return null;
        }

        QueryOperator operator =
            QueryOperator.getQueryOperator(method.toString());
        if (operator == null) {
            return null;
        }

        context.setPropertyResolved(true);
        @SuppressWarnings("unchecked")
        Iterable<Object> iterable = (Iterable<Object>) base;
        return operator.invoke(context, iterable, params);
    }

    public Object getValue(ELContext context, Object base, Object property) {
        return null;
    }

    public Class<?> getType(ELContext context, Object base, Object property) {
        return null;
    }

    public void setValue(ELContext context, Object base, Object property,
                                  Object value) {
    }

    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return false;
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(
                                            ELContext context,
                                            Object base) {
        return null;
    }

    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return String.class;
    }
}
