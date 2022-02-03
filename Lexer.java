package edu.ufl.cise.plc;

import edu.ufl.cise.plc.Token;

import java.util.HashMap;
import java.util.Map;
import edu.ufl.cise.plc.IToken.Kind;


public class Lexer implements ILexer
{

    private enum State
    {
        START,
        HAVE_EQUALS,
        HAVE_RARROW,
        HAVE_LARRAOW,
        HAVE_EXCLAM,
        HAVE_ZERO,
        HAS_DOT,
        IN_FLOAT,
        IN_NUM,
        IN_IDENT,
    }

    private State state;
    Map<String, Kind> reservered_map = new HashMap<String, Kind>()
    {
        {
            put("string", Kind.TYPE);
            put("int", Kind.TYPE);
            put("float", Kind.TYPE);
            put("boolean", Kind.TYPE);
            put("color", Kind.TYPE);
            put("image", Kind.TYPE);
            put("void", Kind.TYPE);
            put("getWidth", Kind.IMAGE_OP);
            put("getHeight", Kind.IMAGE_OP);
            put("getRed", Kind.COLOR_OP);
            put("getBlue", Kind.COLOR_OP);
            put("getGreen", Kind.COLOR_OP);
            put("BLACK", Kind.COLOR_CONST);
            put("BLUE", Kind.COLOR_CONST);
            put("CYAN", Kind.COLOR_CONST);
            put("DARK_GRAY", Kind.COLOR_CONST);
            put("GRAY", Kind.COLOR_CONST);
            put("GREEN", Kind.COLOR_CONST);
            put("LIGHT_GRAY", Kind.COLOR_CONST);
            put("MAGENTA", Kind.COLOR_CONST);
            put("ORANGE", Kind.COLOR_CONST);
            put("PINK", Kind.COLOR_CONST);
            put("RED", Kind.COLOR_CONST);
            put("WHITE", Kind.COLOR_CONST);
            put("YELLOW", Kind.COLOR_CONST);
            put("true", Kind.BOOLEAN_LIT);
            put("false", Kind.BOOLEAN_LIT);
            put("if", Kind.KW_IF);
            put("else", Kind.KW_ELSE);
            put("fi", Kind.KW_FI);
            put("write", Kind.KW_WRITE);
            put("console", Kind.KW_CONSOLE);
        }
    };
    Lexer(String source)
    {

    }

    @Override
    public IToken next() throws LexicalException {
        return null;
    }

    @Override
    public IToken peek() throws LexicalException {
        return null;
    }
}
