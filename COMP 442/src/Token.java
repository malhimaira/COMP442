
public class Token {
    private TokenType tokenType;
    private String lexeme;
    private Position p;

    public Token() {
        this.lexeme = "";
    }

    // Setters
    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    // Getters
    public TokenType getTokenType() {
        return tokenType;
    }

    public String getLexeme() {
        return lexeme;
    }

}
