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

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * <p>Encapsulates a parameterized {@link ValueExpression}.</p>
 *
 * <p>A <code>LambdaExpression</code> is a representation of the EL Lambda
 * expression syntax.  It consists of a list of the formal parameters and a
 * body, represented by a {@link ValueExpression}.
 * The body can be any valid <code>Expression</code>, including another
 * <code>LambdaExpression</code>.</p>
 * A <code>LambdaExpression</code> is created when an EL expression containing
 * a Lambda expression is evaluated.</p>
 * <p>A <code>LambdaExpression</code> can be invoked by calling
 * {@link LambdaExpression#invoke}, with
 * an {@link javax.el.ELContext} and a list of the actual arguments.  The
 * evaluation of the <code>ValueExpression</code> in the body uses the
 * {@link ELContext} to resolve references to the parameters.
 * The result of the evaluation is returned.</p>
 * @see ELContext#getLambdaArgument
 * @see ELContext#enterLambdaScope
 * @see ELContext#exitLambdaScope
 */

public class LambdaExpression {

    private List<String> formalParameters = new ArrayList<String>();
    private ValueExpression expression;

    /**
     * Creates a new LambdaExpression.
     * @param formalParameters The list of String representing the formal
     *        parameters.
     * @param expression The <code>ValueExpression</code> representing the
     *        body.
     */
    public LambdaExpression (List<String> formalParameters,
                             ValueExpression expression) {
        this.formalParameters = formalParameters;
        this.expression = expression;
    }

    /**
     * Retrieves the formal parameters of the Lambda expression
     * @return The list of the parameter names.
     */
    public List<String> getFormalParameters() {
        return this.formalParameters;
    }

    /**
     * Invoke the encapsulated Lambda expression.
     * <p> The supplied arguments are matched, in
     * the same order, to the formal parameters.  If there are more arguments
     * than the formal parameters, the extra arguments are ignored.  If there
     * are less arguments than the formal parameters, an
     * <code>ELException</code> is thrown.</p>
     *
     * <p>The actual Lambda arguments are added to the ELContext and are
     * available during the evaluation of the Lambda expression.  They are
     * removed after the evaluation.</p>
     *
     * @param elContext The ELContext used for the evaluation of the expression
     * @param args The arguments for the Lambda expression
     * @return The result of invoking the Lambda expression
     * @throws ELException if not enough arguments are provided
     */
    public Object invoke(ELContext elContext, Object... args) 
            throws ELException {
        int i = 0;
        Map<String, Object> lambdaArgs = new HashMap<String, Object>();
        for (String fParam: formalParameters) {
            if (i >= args.length) {
                throw new ELException("Expected Argument " + fParam +
                            " missing in Lambda Expression");
            }
            lambdaArgs.put(fParam, args[i++]);
        }
        elContext.enterLambdaScope(lambdaArgs);
        Object ret = expression.getValue(elContext);
        elContext.exitLambdaScope();
        return ret;
    }
}
