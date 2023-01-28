import java.io.FileInputStream;
import java.util.ArrayList;

public class Lexer {

    private ArrayList<Token> TokenSequence;
    public static String reservedWords[] = {"integer", "float", "void", "class", "self",
            "isa", "while", "if", "then", "else", "read", "write", "return", "localvar", "constructor",
            "attribute", "function", "public", "private"};
    public static String operatorWords[] = {"and", "or", "not"};

    public Lexer(FileInputStream fileInputStream) {
        //Read char by char
        try {
            int countRowLine = 1;
            int nextChar;
            int currentPointerChar;
            String lastCharsRead = "";

            while ((currentPointerChar = fileInputStream.read()) != -1) {

                // Case of Operator or Punctuation
                if ((char) currentPointerChar == '=') {
                    nextChar = fileInputStream.read();

                    // Case of Equal or Greater to
                    if ((char) nextChar == '>') {
                        //create a => token
                    }
                    // Case of Equal Equal
                    else if ((char) nextChar == '=') {
                        //create a == token
                    }
                    // Case of Equal Token
                    else if ((char) nextChar == ' ') {
                        //validate and create a token for lastcharread and '=' and reset lastcharsread
                    } else {
                        //validate and create a token for lastcharread and then token for '='
                        // and clear string for lastcharsread and set it to the nextChar
                    }
                } else if ((char) currentPointerChar == ':') {
                    nextChar = fileInputStream.read();

                    // Case of double colon
                    if ((char) nextChar == ':') {
                        // check if lastcharsread is not empty and make its token
                        // and then make a token for ':'
                    } else if ((char) nextChar == ' ') {
                        //create a token for lastcharread and ':' and reset lastcharsread
                    } else {
                        //create a token for lastcharread and then token for ':'
                        // and clear string for lastcharsread and set it to the nextChar
                    }
                } else if ((char) currentPointerChar == '<') {
                    nextChar = fileInputStream.read();

                    // Case of '<>'
                    if ((char) nextChar == '>') {
                        // check if lastcharsread is not empty and make its token
                        // and then make a token for '<>'
                    }
                    // Case of '<='
                    else if ((char) nextChar == '=') {
                        // check if lastcharsread is not empty and make its token
                        // and then make a token for '<='
                    } else if ((char) nextChar == ' ') {
                        //create a token for lastcharread and '<' and reset lastcharsread
                    } else {
                        // create a token for lastcharread and then token for '<'
                        // and clear string for lastcharsread and set it to the nextChar
                    }
                } else if ((char) currentPointerChar == '>') {
                    nextChar = fileInputStream.read();

                    // Case of '>='
                    if ((char) nextChar == '=') {
                        // check if lastcharsread is not empty and make its token
                        // and then make a token for '>='
                    } else if ((char) nextChar == ' ') {
                        //create a token for lastcharread and '>' and reset lastcharsread
                    } else {
                        // create a token for lastcharread and then token for '>'
                        // and clear string for lastcharsread and set it to the nextChar if not space
                    }
                } else if ((char) currentPointerChar == '/') {
                    nextChar = fileInputStream.read();

                    // Case of Inline Comment
                    if ((char) nextChar == '/') {
                        lastCharsRead = "//";
                        while ((nextChar = fileInputStream.read()) != '\n' && (nextChar) != '\r') {
                            lastCharsRead += (char) nextChar;
                        }
                        //create token for inline
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
                    }
                    // Case of division
                    else if ((char) nextChar == ' ') {
                        //create a token for lastcharread and '/' and reset lastcharsread
                    } else {
                        // create a token for lastcharread and then token for '/'
                        // and clear string for lastcharsread and set it to the nextChar
                    }
                } else if ((char) currentPointerChar == '.') {

                    boolean isAId = validateId(lastCharsRead);

                    if (isAId == true) {
                        //make token for lastCharRead and token for '.' and reset last char read
                    } else {
                        lastCharsRead += '.';
                        while (((nextChar = fileInputStream.read()) >= 48 && nextChar <= 57) || (char) nextChar == 'e' || (char) nextChar == '+' || (char) nextChar == '-') {
                            lastCharsRead += (char) nextChar;
                        }
                        boolean isAFloat = validateFloat(lastCharsRead);
                        if (isAFloat == true) {
                            //create token for float
                            // reset lastcharsread to empty, but add back the nextchar just read if it is not a space
                            lastCharsRead = "" + nextChar;

                        } else {
                            // error case invalid period
                        }
                    }
                } else if ((char) currentPointerChar == '*') {
                    // validate and create token for lastcharsread and then create token for *
                    // reset lastcharsread to "" empty
                } else if ((char) currentPointerChar == '+') {
                    // validate and create token for lastcharsread and then create token for +
                    // reset lastcharsread to "" empty
                } else if ((char) currentPointerChar == '-') {
                    // validate and create token for lastcharsread and then create token for -
                    // reset lastcharsread to "" empty
                } else if ((char) currentPointerChar == ',') {
                    // validate and create token for lastcharsread and then create token for ,
                    // reset lastcharsread to "" empty
                } // Case of Semicolon
                else if ((char) currentPointerChar == ';') {
                    // backtrack to check if no token created from characters before
                    if (lastCharsRead != "") {
                        // create and validate ALE (Atomic lexical element) and word token
                        addALETokenToArrayList(lastCharsRead, countRowLine);
                    }
                    TokenSequence.add(new Token(";", TokenType.semicolon, new Position(countRowLine)));
                    lastCharsRead = "";

                } else if ((char) currentPointerChar == '(') {
                    // validate and create token for lastcharsread and then create token for ,
                    // reset lastcharsread to "" empty
                } else if ((char) currentPointerChar == ')') {
                    // validate and create token for lastcharsread and then create token for ,
                    // reset lastcharsread to "" empty
                } else if ((char) currentPointerChar == '}') {
                    // validate and create token for lastcharsread and then create token for ,
                    // reset lastcharsread to "" empty
                } else if ((char) currentPointerChar == '{') {
                    // validate and create token for lastcharsread and then create token for ,
                    // reset lastcharsread to "" empty
                } else if ((char) currentPointerChar == ']') {
                    // validate and create token for lastcharsread and then create token for ,
                    // reset lastcharsread to "" empty
                } else if ((char) currentPointerChar == '[') {
                    // validate and create token for lastcharsread and then create token for ,
                    // reset lastcharsread to "" empty
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
                    }
                }


                if (lastCharsRead.equals(";") || lastCharsRead.equals(")") || lastCharsRead.equals("]") || lastCharsRead.equals("}")) {
                    // do if stateents and make a token
                    // reset lastcharsread to "" empty
                }
//                lastCharsRead += (char)currentPointerChar;
//                previousChar = currentPointerChar;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // read char by char
        // if statements to determine the token type
        // create the token
        // add token to the end of array list
        //while loop restarts

    }

    public void addALETokenToArrayList(String lastCharsRead, int position) {
        TokenType tokenType = tokenValidation(lastCharsRead);
        TokenSequence.add(new Token(lastCharsRead, tokenType, new Position(position)));
    }

    public TokenType tokenValidation(String lastCharsRead) {

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

}
