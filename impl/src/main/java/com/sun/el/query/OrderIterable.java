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
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

import javax.el.LambdaExpression;
import javax.el.ELContext;

/*
 * This implements the operators orderedBy and thenBy, orderByDescending and
 * thenByDescending operators.
 *
 * To avoid unnecessary intermediate sortings, the key selectors and the key
 * comparators are kept in a list and only applied on the last operator, when
 * the iterator of the final Iterable is needed.
 *
 * A Comparator is used to handle the sorting of the primary and secondary
 * keys.
 */

class OrderIterable implements Iterable<Object> {

    final private List<OrderParam> orderParams =
                new ArrayList<OrderParam>();

    ELContext context;

    private List<Object> base;
    private boolean sorted = false;

    public OrderIterable(ELContext context, List<Object> base,
                         LambdaExpression keySelector, Comparator<Object> cmp,
                         boolean descending) {
        this.context = context;
        this.base = base;
        orderParams.add(new OrderParam(keySelector, cmp, descending));
    }

    private void sort() {
        if (sorted) {
            return;
        }
        Collections.sort(base, new ItemComparator());
        sorted = true;
    }
        
    void addOrder(LambdaExpression keySelector, Comparator<Object> cmp,
                  boolean descending) {
        orderParams.add(new OrderParam(keySelector, cmp, descending));
    }

    @Override
    public Iterator<Object> iterator() {
        sort();
        return base.iterator();
    }

    private class ItemComparator implements Comparator<Object> {

        @Override
        public int compare(Object item, Object item2) {
            for (OrderParam orderParam: orderParams) {
                Object key1 = orderParam.getSelector().invoke(context, item);
                Object key2 = orderParam.getSelector().invoke(context, item2);
                Comparator<Object> comparator = orderParam.getComparator();
                int result = 0;
                if (comparator != null) {
                    result = comparator.compare(key1, key2);
                } else if (key1 instanceof Comparable) {
                    @SuppressWarnings("unchecked")
                    int temp = ((Comparable<Object>)key1).compareTo(key2);
                    result = temp;
                }
                if (result != 0) {
                    if (orderParam.isDescending()) {
                        return -result;
                    }
                    return result;
                }
            }
            return 0;
        }
    }

    private class OrderParam {
        private LambdaExpression selector;
        private Comparator<Object> comparator;
        private boolean descending;

        OrderParam(LambdaExpression selector,
                   Comparator<Object> comparator,
                   boolean descending) {
            this.selector = selector;
            this.comparator = comparator;
            this.descending = descending;
        }

        LambdaExpression getSelector() {
            return this.selector;
        }

        Comparator<Object> getComparator() {
            return this.comparator;
        }

        boolean isDescending() {
            return this.descending;
        }
    }
}


