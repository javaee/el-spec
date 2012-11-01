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

package com.sun.el.streams;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Collection;
import java.util.Map;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.LambdaExpression;
import javax.el.streams.Stream;
import com.sun.el.lang.ELSupport;

/*
 */

public class StreamImpl implements Stream {

    private Iterator<Object> source;
    private Stream upstream;
    private Operator op;

    StreamImpl(Iterator<Object> source) {
        this.source = source;
    }

    StreamImpl(Stream upstream, Operator op) {
        this.upstream = upstream;
        this.op = op;
    }

    @Override
    public Iterator<Object> iterator() {
        if (source != null) {
            return source;
        }

        return op.iterator(upstream.iterator());
    }

    @Override
    public Stream filter(final LambdaExpression predicate) {
        return new StreamImpl(this, new Operator() {
            @Override
            public Iterator<Object> iterator(final Iterator<Object> up) {
                return new BaseIterator(up) {
                    @Override
                    public void doItem(Object item) {
                        if ((Boolean) predicate.invoke(item)) {
                            yield(item);
                        }
                    }
                };
            }
        });
    }

    @Override
    public Stream map(final LambdaExpression mapper) {
        return new StreamImpl(this, new Operator() {
            @Override
            public Iterator<Object> iterator(final Iterator<Object> up) {
                return new BaseIterator(up) {
                    @Override
                    public void doItem(Object item) {
                        yield(mapper.invoke(item));
                    }
                };
            }
        });
    }

    @Override
    public Stream sorted(final LambdaExpression comparator) {
        return new StreamImpl(this, new Operator() {

            private PriorityQueue<Object> queue = null;

            @Override
            public Iterator<Object> iterator(final Iterator<Object> up) {
        if (queue == null) {
            queue = new PriorityQueue<Object>(16,
                new Comparator<Object>() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        return (Integer) ELSupport.coerceToType(
                            comparator.invoke(o1, o2),
                            Integer.class);
                    }
                });

            while(up.hasNext()) {
                queue.add(up.next());
            }
        }

                return new Iterator<Object>() {

                @Override
                public boolean hasNext() {
                    return !queue.isEmpty();
                }

                @Override
                public Object next() {
                     return queue.remove();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
            }
        });
    }

    @Override
    public Stream flatMap(LambdaExpression mapper) {
        return new StreamImpl(this, new FlatMap(mapper));
    }

    @Override
    public Object reduce(Object base, LambdaExpression op) {
        Iterator<Object> iter = iterator();
        while (iter.hasNext()) {
            base = op.invoke(base, iter.next());
        }
        return base;
    }

    @Override
    public void forEach(LambdaExpression sink) {
        Iterator<Object> iter = iterator();
        while (iter.hasNext()) {
            sink.invoke(iter.next());
        }
    }

    @Override
    public Map<Object,Collection<Object>> groupBy(LambdaExpression classifier) {
        Map<Object, Collection<Object>> map =
                        new HashMap<Object, Collection<Object>>();
        Iterator<Object> iter = iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            Object key = classifier.invoke(item);
            if (key == null) {
                throw new ELException("null key");
            }
            Collection<Object> c = map.get(key);
            if (c == null) {
                c = new ArrayList<Object>();
                map.put(key, c);
            }
            c.add(item);
        }
        return map;
    }

/*
    @Override
    public Object[] toArray() {
        Iterator<Object> iter = iterator();
        while (iter.hasNext()) {
        }
    }        
*/
}
