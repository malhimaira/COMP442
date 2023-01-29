import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Lexer {

    private ArrayList<Token> TokenSequence;
    private int countTokens;
    public static ArrayList<String> reservedWords = new ArrayList<String>(Arrays.asList("integer", "float", "void", "class", "self",
            "isa", "while", "if", "then", "else", "read", "write", "return", "localvar", "constructor",
            "attribute", "function", "public", "private"));
    public static ArrayList<String> operatorWords = new ArrayList<String>(Arrays.asList("and", "or", "not"));

    public Lexer(FileInputStream fileInputStream) {
        //Read char by char
        try {
            countTokens = 0;
            int countRowLine = 1;
            int nextChar;
            int currentPointerChar;
            String lastCharsRead = "";

            while ((currentPointerChar = fileInputStream.read()) != -1) {

                // Case of Operator or Punctuation

                if ((char) currentPointerChar == '=') {

                    // backtrack to check if no token created from characters before
                    if (lastCharsRead != "") {
                        // create and validate ALE (Atomic lexical element) and word token
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                        lastCharsRead = "";
                    }

                    // force read next char
                    nextChar = fileInputStream.read();

                    // Case of lambda to
                    if ((char) nextChar == '>') {
                        TokenSequence.add(new Token("=>", TokenType.lambdaExpression, new Position(countRowLine)));
                    }
                    // Case of Equal Equal
                    else if ((char) nextChar == '=') {
                        TokenSequence.add(new Token("==", TokenType.doubleEqual, new Position(countRowLine)));
                    }
                    // Case of Equal Token
                    else if ((char) nextChar == ' ') {
                        TokenSequence.add(new Token("=", TokenType.equals, new Position(countRowLine)));
                    } else {
                        TokenSequence.add(new Token("=", TokenType.equals, new Position(countRowLine)));
                        lastCharsRead = "" + (char) nextChar;
                    }
                } else if ((char) currentPointerChar == ':') {

                    // backtrack to check if no token created from characters before
                    if (lastCharsRead != "") {
                        // create and validate ALE (Atomic lexical element) and word token
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                        lastCharsRead = "";
                    }

                    // force read next char
                    nextChar = fileInputStream.read();

                    // Case of double colon
                    if ((char) nextChar == ':') {
                        TokenSequence.add(new Token("::", TokenType.doubleColon, new Position(countRowLine)));
                    } // Case of colon
                    else if ((char) nextChar == ' ') {
                        TokenSequence.add(new Token(":", TokenType.colon, new Position(countRowLine)));
                    } else {
                        TokenSequence.add(new Token(":", TokenType.colon, new Position(countRowLine)));
                        lastCharsRead = "" + (char) nextChar;
                    }
                } else if ((char) currentPointerChar == '<') {

                    // backtrack to check if no token created from characters before
                    if (lastCharsRead != "") {
                        // create and validate ALE (Atomic lexical element) and word token
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                        lastCharsRead = "";
                    }
                    // force read next char
                    nextChar = fileInputStream.read();

                    // Case of '<>'
                    if ((char) nextChar == '>') {
                        TokenSequence.add(new Token("<>", TokenType.angleBrackets, new Position(countRowLine)));
                    }
                    // Case of '<='
                    else if ((char) nextChar == '=') {
                        TokenSequence.add(new Token("<=", TokenType.lessThanOrEqualTo, new Position(countRowLine)));
                    } else if ((char) nextChar == ' ') {
                        TokenSequence.add(new Token("<", TokenType.lessThan, new Position(countRowLine)));
                    } else {
                        TokenSequence.add(new Token("<", TokenType.lessThan, new Position(countRowLine)));
                        lastCharsRead = "" + (char) nextChar;
                    }
                } else if ((char) currentPointerChar == '>') {

                    // backtrack to check if no token created from characters before
                    if (lastCharsRead != "") {
                        // create and validate ALE (Atomic lexical element) and word token
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                        lastCharsRead = "";
                    }
                    // force read next char
                    nextChar = fileInputStream.read();

                    // Case of greater than or equal to
                    if ((char) nextChar == '=') {
                        TokenSequence.add(new Token(">=", TokenType.greaterThanOrEqualTo, new Position(countRowLine)));
                    } // Case of greater than
                    else if ((char) nextChar == ' ') {
                        TokenSequence.add(new Token(">", TokenType.greaterThan, new Position(countRowLine)));
                    } else {
                        TokenSequence.add(new Token(">", TokenType.greaterThan, new Position(countRowLine)));
                        lastCharsRead = "";
                    }
                } else if ((char) currentPointerChar == '/') {

                    // backtrack to check if no token created from characters before
                    if (lastCharsRead != "") {
                        // create and validate ALE (Atomic lexical element) and word token
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                        lastCharsRead = "";
                    }
                    // force read next char
                    nextChar = fileInputStream.read();

                    // Case of Inline Comment
                    if ((char) nextChar == '/') {
                        lastCharsRead = "//";
                        while ((char) (nextChar = fileInputStream.read()) != '\n' && ((char) nextChar) != '\r') {
                            lastCharsRead += (char) nextChar;
                        }
                        TokenSequence.add(new Token(lastCharsRead, TokenType.inlineComment, new Position(countRowLine)));
                        lastCharsRead = "";
                    }
                    // Case of Block Comment
                    else if ((char) nextChar == '*') {
                        lastCharsRead = "/*";
                        boolean checkIfBlockEnded = false;
                        while (!checkIfBlockEnded) {
                            nextChar = fileInputStream.read();
                            if ((char) nextChar == '\n' || (char) nextChar == '\r') {
                                countRowLine++;
                            }
                            // finds a *
                            if ((char) nextChar == '*') {
                                lastCharsRead += "*";
                                nextChar = fileInputStream.read();
                                // finds the '*/'
                                if ((char) nextChar == '/') {
                                    // found end of comment
                                    lastCharsRead += "/";
                                    //make token for comment and add to arraylist
                                    break;
                                }
                            }
                            lastCharsRead += (char) nextChar;
                        }
                        TokenSequence.add(new Token(lastCharsRead, TokenType.blockComment, new Position(countRowLine)));
                        lastCharsRead = "";
                    }
                    // Case of division
                    else if ((char) nextChar == ' ') {
                        TokenSequence.add(new Token("/", TokenType.divide, new Position(countRowLine)));
                    } else {
                        TokenSequence.add(new Token("/", TokenType.divide, new Position(countRowLine)));
                        lastCharsRead = "" + (char) nextChar;
                    }
                } else if ((char) currentPointerChar == '.') {

                    boolean isAId = validateId(lastCharsRead);

                    // check if this a valid id
                    if (isAId == true) {
                        // backtrack to check if no token created from characters before
                        if (lastCharsRead != "") {
                            // create and validate ALE (Atomic lexical element) and word token
                            addALETokenToArrayList(lastCharsRead, countRowLine);
                            lastCharsRead = "";
                        }
                        TokenSequence.add(new Token(".", TokenType.period, new Position(countRowLine)));
                    } // check if its a float
                    else {
                        lastCharsRead += '.';
                        while (((nextChar = fileInputStream.read()) >= 48 && nextChar <= 57) || (char) nextChar == 'e' || (char) nextChar == '+' || (char) nextChar == '-') {
                            lastCharsRead += (char) nextChar;
                        }
                        boolean isAFloat = validateFloat(lastCharsRead);
                        if (isAFloat == true) {
                            TokenSequence.add(new Token(lastCharsRead, TokenType.floatType, new Position(countRowLine)));
                            if ((char) nextChar != ' ' || (char) nextChar != '\n' || (char) nextChar != '\r') {
                                lastCharsRead = "" + (char) nextChar;
                            } else if ((char) nextChar == '\n' || (char) nextChar == '\r') {
                                countRowLine++;
                            }

                        } else {
                            // call method print error message to file
                        }
                    }
                } else if ((char) currentPointerChar == '*') {
                    // backtrack to check if no token created from characters before
                    if (lastCharsRead != "") {
                        // create and validate ALE (Atomic lexical element) and word token
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                        lastCharsRead = "";
                    }
                    TokenSequence.add(new Token("*", TokenType.multiply, new Position(countRowLine)));
                } else if ((char) currentPointerChar == '+') {
                    // backtrack to check if no token created from characters before
                    if (lastCharsRead != "") {
                        // create and validate ALE (Atomic lexical element) and word token
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                        lastCharsRead = "";
                    }
                    TokenSequence.add(new Token("+", TokenType.plus, new Position(countRowLine)));
                } else if ((char) currentPointerChar == '-') {
                    // backtrack to check if no token created from characters before
                    if (lastCharsRead != "") {
                        // create and validate ALE (Atomic lexical element) and word token
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                        lastCharsRead = "";
                    }
                    TokenSequence.add(new Token("-", TokenType.minus, new Position(countRowLine)));
                } else if ((char) currentPointerChar == ',') {
                    // backtrack to check if no token created from characters before
                    if (lastCharsRead != "") {
                        // create and validate ALE (Atomic lexical element) and word token
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                        lastCharsRead = "";
                    }
                    TokenSequence.add(new Token(",", TokenType.comma, new Position(countRowLine)));
                } else if ((char) currentPointerChar == ';') {
                    // backtrack to check if no token created from characters before
                    if (lastCharsRead != "") {
                        // create and validate ALE (Atomic lexical element) and word token
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                        lastCharsRead = "";
                    }
                    TokenSequence.add(new Token(";", TokenType.semicolon, new Position(countRowLine)));
                } else if ((char) currentPointerChar == '(') {
                    // backtrack to check if no token created from characters before
                    if (lastCharsRead != "") {
                        // create and validate ALE (Atomic lexical element) and word token
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                        lastCharsRead = "";
                    }
                    TokenSequence.add(new Token("(", TokenType.openBracketRound, new Position(countRowLine)));
                } else if ((char) currentPointerChar == ')') {
                    // backtrack to check if no token created from characters before
                    if (lastCharsRead != "") {
                        // create and validate ALE (Atomic lexical element) and word token
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                        lastCharsRead = "";
                    }
                    TokenSequence.add(new Token(")", TokenType.closeBracketRound, new Position(countRowLine)));
                } else if ((char) currentPointerChar == '}') {
                    // backtrack to check if no token created from characters before
                    if (lastCharsRead != "") {
                        // create and validate ALE (Atomic lexical element) and word token
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                        lastCharsRead = "";
                    }
                    TokenSequence.add(new Token("}", TokenType.closedBracketCurly, new Position(countRowLine)));
                } else if ((char) currentPointerChar == '{') {
                    // backtrack to check if no token created from characters before
                    if (lastCharsRead != "") {
                        // create and validate ALE (Atomic lexical element) and word token
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                        lastCharsRead = "";
                    }
                    TokenSequence.add(new Token("{", TokenType.openBracketRound, new Position(countRowLine)));
                } else if ((char) currentPointerChar == ']') {
                    // backtrack to check if no token created from characters before
                    if (lastCharsRead != "") {
                        // create and validate ALE (Atomic lexical element) and word token
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                        lastCharsRead = "";
                    }
                    TokenSequence.add(new Token("]", TokenType.closeBracketSquare, new Position(countRowLine)));
                } else if ((char) currentPointerChar == '[') {
                    // backtrack to check if no token created from characters before
                    if (lastCharsRead != "") {
                        // create and validate ALE (Atomic lexical element) and word token
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                        lastCharsRead = "";
                    }
                    TokenSequence.add(new Token("[", TokenType.openBracketSquare, new Position(countRowLine)));
                } else if ((char) currentPointerChar == '$' || (char) currentPointerChar == '@' || (char) currentPointerChar == '#'
                        || (char) currentPointerChar == '\'' || (char) currentPointerChar == '\\' || (char) currentPointerChar == '~'
                        || (char) currentPointerChar == '!') {
                    // Case of invalid char
                    // backtrack to check if no token created from characters before
                    if (lastCharsRead != "") {
                        // create and validate ALE (Atomic lexical element) and word token
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                        lastCharsRead = "";
                    }
                    // and call method to then print invlaid token in file
                } else if ((currentPointerChar >= 48 && currentPointerChar <= 57)
                        || (currentPointerChar >= 65 && currentPointerChar <= 90)
                        || (currentPointerChar >= 97 && currentPointerChar <= 122)
                        || ((char) currentPointerChar == '_')) {
                    // case of reading a character from an atomic lexical element
                    lastCharsRead += (char) currentPointerChar;
                } else if ((char) currentPointerChar == ' ' || (char) currentPointerChar == '\n' || (char) currentPointerChar == '\r') {
                    if ((char) currentPointerChar == '\n' || (char) currentPointerChar == '\r') {
                        countRowLine++;
                    }
                    // case of definite ending of token
                    if (lastCharsRead != "") {
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                        lastCharsRead = "";
                    }
                }
                if (lastCharsRead.equals(";") || lastCharsRead.equals(",") || lastCharsRead.equals(")") || lastCharsRead.equals("]") || lastCharsRead.equals("}")) {
                    if (lastCharsRead.equals(";")) {
                        TokenSequence.add(new Token(";", TokenType.semicolon, new Position(countRowLine)));
                    } else if (lastCharsRead.equals(",")) {
                        TokenSequence.add(new Token(",", TokenType.comma, new Position(countRowLine)));
                    } else if (lastCharsRead.equals(")")) {
                        TokenSequence.add(new Token(")", TokenType.closeBracketRound, new Position(countRowLine)));
                    } else if (lastCharsRead.equals("]")) {
                        TokenSequence.add(new Token("]", TokenType.closeBracketSquare, new Position(countRowLine)));
                    } else if (lastCharsRead.equals("}")) {
                        TokenSequence.add(new Token("}", TokenType.semicolon, new Position(countRowLine)));
                    }
                    lastCharsRead = "";
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addALETokenToArrayList(String lastCharsRead, int position) {
        TokenType tokenType = tokenValidation(lastCharsRead);
        if (tokenType != TokenType.errorToken) {
            TokenSequence.add(new Token(lastCharsRead, tokenType, new Position(position)));
        } else {
            // call method to print error

        }
    }

    public TokenType tokenValidation(String lastCharsRead) {
        if (validateReservedWord(lastCharsRead) == true) {
            return TokenType.valueOf(lastCharsRead.toLowerCase() + "KeyWord");
        }
        if (validateOperatorWord(lastCharsRead) == true) {
            return TokenType.valueOf(lastCharsRead.toLowerCase());
        }
        if (validateId(lastCharsRead) == true) {
            return TokenType.id;
        }
        if (validateFloat(lastCharsRead) == true) {
            return TokenType.floatType;
        }
        if (validateInteger(lastCharsRead) == true) {
            return TokenType.integerType;
        }
        return TokenType.errorToken;
    }

    public boolean validateFloat(String lastCharRead) {
        Boolean b = false;
        return b;
    }

    public boolean validateInteger(String lastCharRead) {
        Boolean b = false;
        return b;
    }

    public boolean validateId(String lastCharRead) {
        Boolean b = false;
        return b;
    }

    public boolean validateReservedWord(String lastCharRead) {
        Boolean b = false;
        return b;
    }

    public boolean validateOperatorWord(String lastCharRead) {
        Boolean b = false;
        return b;
    }

    public Token getNextToken() {
        Token token = null;
        if (countTokens!= TokenSequence.size()){
            token = TokenSequence.get(countTokens);
            countTokens++;
            return token;
        }
        return token;
    }

}
