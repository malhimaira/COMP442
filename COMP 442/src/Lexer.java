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
        try {
            // initialize
            TokenSequence = new ArrayList<>(10);
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
                        while ((char) (nextChar = fileInputStream.read()) != '\n' && ((char) nextChar) != '\r'
                                && nextChar != 10) {
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
                            if ((char) nextChar == '\n' || (char) nextChar == '\r' || nextChar == 10) {
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
                            if ((char) nextChar != ' ' && (char) nextChar != '\n' && (char) nextChar != '\r' && nextChar != 10) {
                                lastCharsRead = "" + (char) nextChar;
                            } else if ((char) nextChar == '\n' || (char) nextChar == '\r' || nextChar == 10) {
                                countRowLine++;
                            }

                        } else {
                            if ((char) nextChar != ' ' && (char) nextChar != '\n' && (char) nextChar != '\r' && nextChar != 10) {
                                lastCharsRead = "" + (char) nextChar;
                            }
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
                } else if ((char) currentPointerChar == ' ' || (char) currentPointerChar == '\n' || (char) currentPointerChar == '\r' || currentPointerChar == 10) {
                    if ((char) currentPointerChar == '\n' || (char) currentPointerChar == '\r' || currentPointerChar == 10) {
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

    public boolean validateFloat(String f) {
        float floatToCheck;
        float x = 1.76e5f;

        // Case of Invalid leading trailing zeros
        if (f.startsWith("0") || f.endsWith("0") || f.contains("e0")) {
            //return false if leading or trailing zeros or eo
            return false;
        } else {
            // case of contains e+ or e-
            if (f.contains("e")) {
                //left side of e
                for (int i = 0; i < f.indexOf('e'); i++) {
                    if (f.charAt(i) == '0' || f.charAt(i) == '1' || f.charAt(i) == '2' || f.charAt(i) == '3'
                            || f.charAt(i) == '4' || f.charAt(i) == '5' || f.charAt(i) == '6'
                            || f.charAt(i) == '7' || f.charAt(i) == '8' || f.charAt(i) == '9' || f.charAt(i) == '.') {
                        continue;
                    }
                    return false;
                }

                // case of right side of e
                for (int i = f.indexOf('e') + 2; i < f.length() - f.indexOf('e'); i++) {
                    if (f.charAt(i) == '0' || f.charAt(i) == '1' || f.charAt(i) == '2' || f.charAt(i) == '3'
                            || f.charAt(i) == '4' || f.charAt(i) == '5' || f.charAt(i) == '6'
                            || f.charAt(i) == '7' || f.charAt(i) == '8' || f.charAt(i) == '9' || f.charAt(i) == '.') {
                        continue;
                    }
                    return false;
                }

                if (f.charAt((f.indexOf('e') + 1)) == '+' || f.charAt((f.indexOf('e') + 1)) == '-') {
                    return true;
                } else {
                    return false;
                }
            }

            // case of no 'e+' or 'e-' in float
            for (int i = 0; i < f.length(); i++) {
                if (f.charAt(i) == '0' || f.charAt(i) == '1' || f.charAt(i) == '2' || f.charAt(i) == '3'
                        || f.charAt(i) == '4' || f.charAt(i) == '5' || f.charAt(i) == '6'
                        || f.charAt(i) == '7' || f.charAt(i) == '8' || f.charAt(i) == '9' || f.charAt(i) == '.') {
                    continue;
                }
                return false;
            }
            return true;
        }
    }

    public boolean validateInteger(String integ) {
        // case of leading zero
        if (integ.startsWith("0") && integ.length() != 1) {
            return false;
        }
        // check each char to be a digit
        for (int i = 0; i < integ.length(); i++) {
            if (integ.charAt(i) == '0' || integ.charAt(i) == '1' || integ.charAt(i) == '2' || integ.charAt(i) == '3'
                    || integ.charAt(i) == '4' || integ.charAt(i) == '5' || integ.charAt(i) == '6'
                    || integ.charAt(i) == '7' || integ.charAt(i) == '8' || integ.charAt(i) == '9' || integ.charAt(i) == '.') {
                continue;
            }
            return false;
        }
        return true;
    }

    public boolean validateId(String lastCharRead) {
        try {
            if (Character.isDigit(lastCharRead.charAt(0)) || (char) (lastCharRead.charAt(0)) == '_') {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateReservedWord(String lastCharRead) {
        if (reservedWords.contains(lastCharRead)) {
            return true;
        }
        return false;
    }

    public boolean validateOperatorWord(String lastCharRead) {
        if (operatorWords.contains(lastCharRead)) {
            return true;
        }
        return false;
    }

    public Token getNextToken() {
        Token token = null;
        if (countTokens != TokenSequence.size()) {
            token = TokenSequence.get(countTokens);
            countTokens++;
            return token;
        }
        return token;
    }

}
