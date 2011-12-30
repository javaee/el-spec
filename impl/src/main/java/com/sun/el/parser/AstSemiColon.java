package com.sun.el.parser;

import javax.el.ELException;
import com.sun.el.lang.EvaluationContext;

public
class AstSemiColon extends SimpleNode {
    public AstSemiColon(int id) {
      super(id);
    }

    public Object getValue(EvaluationContext ctx)
            throws ELException {
        this.children[0].getValue(ctx);
        return this.children[1].getValue(ctx);
    }

    public void setValue(EvaluationContext ctx, Object value)
            throws ELException {
        this.children[0].getValue(ctx);
        this.children[1].setValue(ctx, value);
    }
}
