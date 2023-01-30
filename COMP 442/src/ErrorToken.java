public class ErrorToken extends Token{

    private String toPrint;
    public ErrorToken(String lexeme, TokenType tokenType, Position position) {
        super(lexeme, tokenType, position);
        if (tokenType == TokenType.errorTokenChar ) {
            this.toPrint = "invalidchar";
        } else if (tokenType == TokenType.errorTokenNumber) {
            this.toPrint = "invalidnum";
        } else if (tokenType == TokenType.errorTokenId) {
            this.toPrint = "invalidid";
        }
    }
    public String toString(){
        return "["+this.toPrint+", "+super.getLexeme()+", "+ super.getPosition() +"]";
    }
}
