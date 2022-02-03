package edu.ufl.cise.plc;

import edu.ufl.cise.plc.IToken.Kind;

public class Token implements IToken {
    final Kind kind;
    final String input;
    final int pos;
    final int length;

    Token(Kind kind, String input, int pos, int length)
    {
        this.kind = kind;
        this.input = input;
        this.pos = pos;
        this.length = length;
    }

    @Override
    public Kind getKind()
    {
        return kind;
    }
    @Override

    public String getText()
    {
        return null;
    }

    @Override
    public int getIntValue()
    {
        return 0;
    }

    @Override
    public float getFloatValue() {
        return 0;
    }

    @Override
    public boolean getBooleanValue() {
        return false;
    }

    @Override
    public String getStringValue() {
        return null;
    }

    @Override public SourceLocation getSourceLocation() {
        return null;
    }

}
