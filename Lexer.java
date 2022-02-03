package edu.ufl.cise.plc;

import edu.ufl.cise.plc.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import edu.ufl.cise.plc.IToken.Kind;


public class Lexer implements ILexer
{
    ArrayList<Token> tokens;
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

    private State state = State.START;
    Map<String, Kind> reserved_map = new HashMap<String, Kind>()
    {
        {
            put("string", Kind.TYPE);
            put("int", Kind.TYPE);
            put("float", Kind.TYPE);
            put("boolean", Kind.TYPE);
            put("color", Kind.TYPE);
            put("image", Kind.TYPE);
            put("void", Kind.KW_VOID);
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
        char[] chars = source.toCharArray();
        boolean finished = false;
        int pos = 0;
        int startPos = 0;
        IToken.SourceLocation srcLoc = new IToken.SourceLocation(1, 1);
        while (finished)
        {
            char ch = chars[pos];
            switch(state)
            {
                case START -> {
                    Token newTok;
                    startPos = pos;
                    switch (ch) {
                        case '&' -> {
                            newTok = new Token(Kind.AND, "&", startPos, pos - startPos);
                            tokens.add(newTok);
                        }
                        case '=' -> {
                            newTok = new Token(Kind.ASSIGN, "=", startPos, pos - startPos);
                            tokens.add(newTok);
                        }
                        case '!' -> {
                            newTok = new Token(Kind.BANG, "!", startPos, pos - startPos);
                            tokens.add(newTok);
                        }
                        case ',' -> {
                            newTok = new Token(Kind.COMMA, ",", startPos, pos - startPos);
                            tokens.add(newTok);
                        }
                        case '/' -> {
                            newTok = new Token(Kind.DIV, "/", startPos, pos - startPos);
                            tokens.add(newTok);
                        }
                        case '>' -> {
                            newTok = new Token(Kind.GT, ">", startPos, pos - startPos);
                            tokens.add(newTok);
                        }
                        case '<' -> {
                            newTok = new Token(Kind.LT, "<", startPos, pos - startPos);
                            tokens.add(newTok);
                        }
                        case '(' -> {
                            newTok = new Token(Kind.LPAREN, "(", startPos, pos - startPos);
                            tokens.add(newTok);
                        }
                        case '[' -> {
                            newTok = new Token(Kind.EOF, ")", startPos, pos - startPos);
                            tokens.add(newTok);
                        }
                        case '-' -> {
                            newTok = new Token(Kind.EOF, "-", startPos, pos - startPos);
                            tokens.add(newTok);
                        }
                        case '%' -> {
                            newTok = new Token(Kind.EOF, "%", startPos, pos - startPos);
                            tokens.add(newTok);
                        }
                        case '|' -> {
                            newTok = new Token(Kind.EOF, "|", startPos, pos - startPos);
                            tokens.add(newTok);
                        }
                        case '+' -> {
                            newTok = new Token(Kind.EOF, "+", startPos, pos - startPos);
                            tokens.add(newTok);
                        }
                        case '^' -> {
                            newTok = new Token(Kind.RETURN, "^", startPos, pos - startPos);
                            tokens.add(newTok);
                        }
                        case ')' -> {
                            newTok = new Token(Kind.RPAREN, ")", startPos, pos - startPos);
                            tokens.add(newTok);
                        }
                        case ']' -> {
                            newTok = new Token(Kind.RSQUARE, "]", startPos, pos - startPos);
                            tokens.add(newTok);
                        }
                        case ';' -> {
                            newTok = new Token(Kind.SEMI, ";", startPos, pos - startPos);
                            tokens.add(newTok);
                        }
                        case '*' -> {
                            newTok = new Token(Kind.TIMES, "*", startPos, pos - startPos);
                            tokens.add(newTok);
                        }
                        case 0 -> {
                            newTok = new Token(Kind.EOF, null, startPos, pos - startPos);
                            tokens.add(newTok);
                            finished = true;
                        }
                    }
                }
                case HAVE_EQUALS -> {

                }
                case HAVE_LARRAOW -> {

                }
                case HAVE_RARROW -> {

                }
                case HAVE_EXCLAM -> {

                }
                case HAVE_ZERO -> {

                }
                case HAS_DOT -> {

                }
                case IN_FLOAT -> {

                }
                case IN_NUM -> {

                }
                case IN_IDENT -> {

                }
            }
        }


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
