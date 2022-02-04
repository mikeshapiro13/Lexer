package edu.ufl.cise.plc;

import edu.ufl.cise.plc.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import edu.ufl.cise.plc.IToken.Kind;


public class Lexer implements ILexer
{
    ArrayList<Token> tokens;
    int tokenPos;
    private enum State
    {
        START,
        HAVE_EQUALS,
        HAVE_RARROW,
        HAVE_LARROW,
        HAVE_EXCLAM,
        HAVE_ZERO,
        HAVE_MINUS,
        HAS_DOT,
        IN_FLOAT,
        IN_NUM,
        IN_IDENT,
        IN_COMM,
        IN_STRING
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

    Lexer(String source) {
        Token newTok;
        tokens = new ArrayList<>();
        char[] chars = source.toCharArray();
        ArrayList<Character> text = new ArrayList<Character>();
        ArrayList<Character> rawText = new ArrayList<Character>();
        boolean finished = false;
        int pos = 0;
        int startPos = 0;
        int stringStart = 0;
        int line = 0;
        int column = 0;
        IToken.SourceLocation srcLoc = new IToken.SourceLocation(0, 0);
        while (!finished)
        {
            char ch;
            if (chars.length == pos) {
                ch = 0;
            }
            else {
                ch = chars[pos];
            }
            switch(this.state)
            {
                case START -> {
                    startPos = pos;

                    if (Character.isJavaIdentifierStart(ch))
                    {
                        state = State.IN_IDENT;
                        text.add(ch);
                        rawText.add(ch);
                        srcLoc = new IToken.SourceLocation(line, column);
                        ++pos;
                        ++column;
                        break;
                    }


                    switch (ch) {
                        case '"' -> {
                            state = State.IN_STRING;
                            stringStart = pos;
                            rawText.add(ch);
                            srcLoc = new IToken.SourceLocation(line, column);
                            ++pos;
                            ++column;
                        }
                        case '#' -> {
                            state = State.IN_COMM;
                            ++pos;
                            ++column;
                        }
                        case '0' -> {
                            state = State.HAVE_ZERO;
                            srcLoc = new IToken.SourceLocation(line, column);
                            text.add(ch);
                            rawText.add(ch);
                            ++pos;
                            ++column;
                        }
                        case '1','2','3','4','5','6','7','8','9' -> {
                            state = State.IN_NUM;
                            srcLoc = new IToken.SourceLocation(line, column);
                            text.add(ch);
                            rawText.add(ch);
                            ++pos;
                            ++column;
                        }
                        case ' ', '\t', '\r' -> {
                            ++pos;
                            ++column;
                            srcLoc = new IToken.SourceLocation(line, column);
                        }
                        case '\n' -> {
                            ++pos;
                            ++line;
                            column = 0;
//                            srcLoc = new IToken.SourceLocation(line, column);
                        }
                        case '&' -> {
                            srcLoc = new IToken.SourceLocation(line, column);
                            newTok = new Token(Kind.AND, "&", "&", startPos, pos - startPos, srcLoc);
                            tokens.add(newTok);
                            pos++;
                            column++;
                        }
                        case '=' -> {
                            state = State.HAVE_EQUALS;
                            srcLoc = new IToken.SourceLocation(line, column);
                            pos++;
                            column++;
                            ch = chars[pos];
                        }
                        case '!' -> {
                            state = State.HAVE_EXCLAM;
                            srcLoc = new IToken.SourceLocation(line, column);
                            pos++;
                            column++;
                        }
                        case ',' -> {
                            srcLoc = new IToken.SourceLocation(line, column);
                            newTok = new Token(Kind.COMMA, ",", ",", startPos, pos - startPos, srcLoc);
                            tokens.add(newTok);
                            pos++;
                            column++;
                        }
                        case '/' -> {
                            srcLoc = new IToken.SourceLocation(line, column);
                            newTok = new Token(Kind.DIV, "/", "/", startPos, pos - startPos, srcLoc);
                            tokens.add(newTok);
                            pos++;
                            column++;
                        }
                        case '>' -> {
                            state = State.HAVE_RARROW;
                            srcLoc = new IToken.SourceLocation(line, column);
                            pos++;
                            column++;
                        }
                        case '<' -> {
                            state = State.HAVE_LARROW;
                            srcLoc = new IToken.SourceLocation(line, column);
                            pos++;
                            column++;
                        }
                        case '(' -> {
                            srcLoc = new IToken.SourceLocation(line, column);
                            newTok = new Token(Kind.LPAREN, "(", "(" , startPos, pos - startPos, srcLoc);
                            tokens.add(newTok);
                            pos++;
                            column++;
                        }
                        case '[' -> {
                            srcLoc = new IToken.SourceLocation(line, column);
                            newTok = new Token(Kind.LSQUARE, "[", "[", startPos, pos - startPos, srcLoc);
                            tokens.add(newTok);
                            pos++;
                            column++;
                        }
                        case '-' -> {
                            state = State.HAVE_MINUS;
                            srcLoc = new IToken.SourceLocation(line, column);
                            pos++;
                            column++;
                        }
                        case '%' -> {
                            srcLoc = new IToken.SourceLocation(line, column);
                            newTok = new Token(Kind.MOD, "%", "%", startPos, pos - startPos, srcLoc);
                            tokens.add(newTok);
                            pos++;
                            column++;
                        }
                        case '|' -> {
                            srcLoc = new IToken.SourceLocation(line, column);
                            newTok = new Token(Kind.OR, "|", "|", startPos, pos - startPos, srcLoc);
                            tokens.add(newTok);
                            pos++;
                            column++;
                        }
                        case '+' -> {
                            srcLoc = new IToken.SourceLocation(line, column);
                            newTok = new Token(Kind.PLUS, "+", "+", startPos, pos - startPos, srcLoc);
                            tokens.add(newTok);
//                            srcLoc = new IToken.SourceLocation(line, column);
                            pos++;
                            column++;
                        }
                        case '^' -> {
                            srcLoc = new IToken.SourceLocation(line, column);
                            newTok = new Token(Kind.RETURN, "^", "^", startPos, pos - startPos, srcLoc);
                            tokens.add(newTok);
                            pos++;
                            column++;
                        }
                        case ')' -> {
                            srcLoc = new IToken.SourceLocation(line, column);
                            newTok = new Token(Kind.RPAREN, ")", ")", startPos, pos - startPos, srcLoc);
                            tokens.add(newTok);
                            pos++;
                            column++;
                        }
                        case ']' -> {
                            srcLoc = new IToken.SourceLocation(line, column);
                            newTok = new Token(Kind.RSQUARE, "]", "]", startPos, pos - startPos, srcLoc);
                            tokens.add(newTok);
                            pos++;
                            column++;
                        }
                        case ';' -> {
                            srcLoc = new IToken.SourceLocation(line, column);
                            newTok = new Token(Kind.SEMI, ";", ";", startPos, pos - startPos, srcLoc);
                            tokens.add(newTok);
                            pos++;
                            column++;
                        }
                        case '*' -> {
                            srcLoc = new IToken.SourceLocation(line, column);
                            newTok = new Token(Kind.TIMES, "*", "*", startPos, pos - startPos, srcLoc);
                            tokens.add(newTok);
                            pos++;
                            column++;
                        }
                        case 0 -> {
                            newTok = new Token(Kind.EOF, null, null, startPos, pos - startPos, srcLoc);
                            tokens.add(newTok);
                            srcLoc = new IToken.SourceLocation(line, column);
                            finished = true;
                            pos++;
                            column++;
                        }
                        default -> {
                            newTok = new Token(Kind.ERROR, Character.toString(ch), Character.toString(ch), startPos, pos - startPos, srcLoc);
                            tokens.add(newTok);
                            ++pos;
                            ++column;
                            srcLoc = new IToken.SourceLocation(line, column);
                        }
                    }
                }
                case HAVE_EQUALS -> {
                    if(ch == '=') {
                        newTok = new Token(Kind.EQUALS, "==", "==", startPos, pos - startPos, srcLoc);
                        tokens.add(newTok);
//                        srcLoc = new IToken.SourceLocation(line, column);
                        pos++;
                        column++;
                    }
                    else {
                        newTok = new Token(Kind.ASSIGN, "=" , "=", startPos, pos - startPos, srcLoc);
                        tokens.add(newTok);

                    }
                    state = State.START;
                }
                case HAVE_LARROW -> {
                    if(ch == '<') {
                        newTok = new Token(Kind.LANGLE, "<<", "<<", startPos, pos - startPos, srcLoc);
                        tokens.add(newTok);
//                        srcLoc = new IToken.SourceLocation(line, column);
                        pos++;
                        column++;
                    }
                    else if(ch == '-'){
                        newTok = new Token(Kind.LARROW, "<-", "<", startPos, pos - startPos, srcLoc);
                        tokens.add(newTok);
//                        srcLoc = new IToken.SourceLocation(line, column);
                        pos++;
                        column++;
                    }
                    else if (ch == '=') {
                        newTok = new Token(Kind.LE, "<=", "<=", startPos, pos - startPos, srcLoc);
                        tokens.add(newTok);
//                        srcLoc = new IToken.SourceLocation(line, column);
                        pos++;
                        column++;
                    }
                    else
                    {
                        newTok = new Token(Kind.LT, "<", "<", startPos, pos - startPos, srcLoc);
                        tokens.add(newTok);
                    }
                    state = State.START;
                }
                case HAVE_RARROW -> {
                    if(ch == '='){
                        newTok = new Token(Kind.GE, ">=",">=", startPos, pos - startPos, srcLoc);
                        tokens.add(newTok);
//                        srcLoc = new IToken.SourceLocation(line, column);
                        pos++;
                        column++;
                    }
                    else if(ch == '>') {
                        newTok = new Token(Kind.RANGLE, ">>", ">>", startPos, pos - startPos, srcLoc);
                        tokens.add(newTok);
//                        srcLoc = new IToken.SourceLocation(line, column);
                        pos++;
                        column++;
                    }
                    else {
                        newTok = new Token(Kind.GT, ">", ">", startPos, pos - startPos, srcLoc);
                        tokens.add(newTok);
//                        srcLoc = new IToken.SourceLocation(line, column);
                    }
                    state = State.START;
                }
                case HAVE_EXCLAM -> {
                    if (ch == '=') {
                        newTok = new Token(Kind.NOT_EQUALS, "!=", "!=", startPos, pos - startPos, srcLoc);
                        tokens.add(newTok);
                        pos++;
                        column++;
                    }
                    else {
                        newTok = new Token(Kind.BANG, "!", "!", startPos, pos - startPos, srcLoc);
                        tokens.add(newTok);
                    }
                    state = State.START;
                }
                case HAVE_ZERO -> {
                    if(ch == '.') {
                        state = State.HAS_DOT;
                        text.add(ch);
                        rawText.add(ch);
                        pos++;
                        column++;
                    } else {
                        newTok = new Token(Kind.INT_LIT, "0", "0", startPos, pos - startPos, srcLoc);
                        tokens.add(newTok);
//                        srcLoc = new IToken.SourceLocation(line, column);
//                        pos++;
//                        column++;
                        text.clear();
                        rawText.clear();
                        state = State.START;
                    }
                }
                case HAS_DOT -> {
                    if (Character.isDigit(ch))
                    {
                        state = State.IN_FLOAT;
                        text.add(ch);
                        rawText.add(ch);
                        pos++;
                        column++;
                    }
                    else
                    {
                        newTok = new Token(Kind.ERROR, null, null, startPos, pos - startPos, srcLoc);
                        tokens.add(newTok);
                        pos++;
                        column++;
                        state = State.START;
                        text.clear();
                        rawText.clear();
                    }
                }
                case HAVE_MINUS -> {
                    if (ch == '>')
                    {
                        newTok = new Token(Kind.RARROW, "->", "->", startPos, pos - startPos, srcLoc);
                        tokens.add(newTok);
                        ++pos;
                        ++column;
                    }
                    else
                    {
                        newTok = new Token(Kind.MINUS, "-", "-", startPos, pos - startPos, srcLoc);
                        tokens.add(newTok);
                    }
                    state = State.START;
                }
                case IN_FLOAT -> {
                    if (Character.isDigit(ch))
                    {
                        text.add(ch);
                        rawText.add(ch);
                        ++pos;
                        ++column;
                    }
                    else
                    {
                        StringBuilder temp = new StringBuilder();
                        for (int i = 0; i < text.size(); ++i)
                        {
                            temp.append(text.get(i));
                        }
                        double guy = Double.parseDouble(temp.toString());
                        if (guy < Float.MAX_VALUE)
                            newTok =  new Token(Kind.FLOAT_LIT, temp.toString(), temp.toString(), startPos, pos - startPos, srcLoc);
                        else
                            newTok = new Token(Kind.ERROR, null, null, startPos, pos - startPos, srcLoc);
                        if (ch == '\n')
                        {
                            column = 0;
                            ++line;
                            ++pos;
                        }
//                        else
//                        {
//                            ++column;
//                        }
//                        ++pos;
                        tokens.add(newTok);
                        state = State.START;
                        text.clear();
                        rawText.clear();
                    }
                }
                case IN_NUM -> {
                    StringBuilder temp = new StringBuilder();
                    for (int i = 0; i < text.size(); ++i)
                    {
                        temp.append(text.get(i));
                    }
                    long guy = Long.parseLong(temp.toString());
                    if (Character.isDigit(ch))
                    {
                        if (guy < Integer.MAX_VALUE) {
                            text.add(ch);
                            rawText.add(ch);
                            ++pos;
                            ++column;
                        }
                        else {
                            newTok = new Token(Kind.ERROR, null, null, startPos, pos - startPos, srcLoc);
                            tokens.add(newTok);
                            state = State.START;
                            text.clear();
                            rawText.clear();
                        }
                    }
                    else if (ch == '.')
                    {
                        state = State.HAS_DOT;
                        text.add(ch);
                        rawText.add(ch);
                        pos++;
                        column++;
                    }
                    else
                    {
                        newTok =  new Token(Kind.INT_LIT, temp.toString(), temp.toString(),  startPos, pos - startPos, srcLoc);
                        tokens.add(newTok);
                        state = State.START;
                        text.clear();
                        rawText.clear();
                    }
                }
                case IN_IDENT -> {
                    if (!Character.isJavaIdentifierPart(ch)) {
                        state = State.START;
                        StringBuilder temp = new StringBuilder();
                        for (int i = 0; i < text.size(); ++i)
                        {
                            temp.append(text.get(i));
                        }
                        if (reserved_map.containsKey(temp.toString())) {
                            Kind tempKind = reserved_map.get(temp.toString());
                            newTok = new Token(tempKind, text.toString(), text.toString(), startPos, pos - startPos, srcLoc);
                        }
                        else
                            newTok = new Token(Kind.IDENT, text.toString(), text.toString(), startPos, pos - startPos, srcLoc);
                        if (ch == '\n')
                        {
                            column = 0;
                            ++line;
                            ++pos;
                        }
//                        else
//                        {
//                            ++column;
//                        }
//                        ++pos;
                        tokens.add(newTok);
                        srcLoc = new IToken.SourceLocation(line, column);
                        text.clear();
                        rawText.clear();
                    }
                    else {
                        text.add(ch);
                        rawText.add(ch);
                        ++pos;
                        ++column;
                    }
                }
                case IN_COMM -> {
                    if (ch == '\r') {
                        ++pos;
                        ++column;
                        ch = chars[pos];
                        if (ch == '\n') {
                            ++pos;
                            ++line;
                            column = 0;
                        }
                        state = State.START;
                        srcLoc = new IToken.SourceLocation(line, column);
                    }
                    else if (ch == '\n') {
                        ++pos;
                        ++line;
                        column = 0;
                        state = State.START;
                        srcLoc = new IToken.SourceLocation(line, column);
                    }
                    else {
                        ++pos;
                        ++column;
                    }
                }
                case IN_STRING -> {
                    switch(ch)
                    {
                        case '\b','\t', '\f','\'','\\' -> {
                            text.add(ch);
                            rawText.add('\\');
                            rawText.add(ch);
                            ++pos;
                            ++column;
                        }
                        case '\n','\r' -> {
                            text.add(ch);
                            rawText.add('\\');
                            rawText.add(ch);
                            ++pos;
                            ++line;
                            column = 0;
                        }
                        case '\"' -> {
                            rawText.add('\\');
                            rawText.add(ch);
                            ++pos;
                            if (pos < chars.length) {
                                ch = chars[pos];
                            }
                            else {
                                newTok = new Token(Kind.STRING_LIT, text.toString(), rawText.toString(), startPos, pos - startPos, srcLoc);
                                tokens.add(newTok);
                                state = State.START;
                            }
                            if (Character.isWhitespace(ch)) {
//                                srcLoc = new IToken.SourceLocation(line, stringStart);
                                newTok = new Token(Kind.STRING_LIT, text.toString(), rawText.toString(), pos, startPos - pos, srcLoc);
                                tokens.add(newTok);
                                state = State.START;
                                ++pos;
                                ++line;
                                column = 0;
                                text.clear();
                                rawText.clear();
//                                srcLoc = new IToken.SourceLocation(line, column);
                            }

                        }
                        case 0 -> {
                            state = State.START;
                            newTok = new Token(Kind.ERROR, null, null, startPos, pos - startPos, srcLoc);
                            tokens.add(newTok);
                        }
                        default -> {
                            text.add(ch);
                            rawText.add(ch);
                            ++pos;
                            ++column;
                        }
                    }

                }
            }
        }

        tokenPos = 0;
    }

    @Override
    public IToken next() throws LexicalException {
        IToken temp = tokens.get(tokenPos);
        if (temp.getKind() == Kind.ERROR)
            throw new LexicalException("Lexer Error");
        tokenPos++;
        return temp;
    }

    @Override
    public IToken peek() throws LexicalException {
        IToken temp = tokens.get(tokenPos);
        if (temp.getKind() == Kind.ERROR)
            throw new LexicalException("Lexer Error");
        return temp;
    }
}
