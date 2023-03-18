package LexerComponents;

import LexerComponents.Position;
import LexerComponents.TokenType;

public class Token {
    private TokenType tokenType;
    private String lexeme;
    private Position position;

    public Token(String lexeme, TokenType tokenType, Position position) {
        this.lexeme = lexeme;
        this.tokenType = tokenType;
        this.position = position;
    }

    // Setters
    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public void setPosition(int row) {
        this.position = new Position(row);
    }

    public int getIntPosition(){
        return this.position.getRow();
    }

    // Getters
    public TokenType getTokenType() {
        return tokenType;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Position getPosition() {
        return position;
    }

    public String toString(){
        return "["+this.getTokenType()+", "+this.getLexeme()+", "+this.position+"]";
    }
}
