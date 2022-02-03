package edu.ufl.cise.plc;

import edu.ufl.cise.plc.Token;

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
    Map<String, Kind> reservered_map;
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
