import java.io.FileInputStream;
import java.util.ArrayList;

public class Lexer {

    private ArrayList<String> TokenSequence;

    public Lexer(FileInputStream fileInputStream) {
        //Read char by char
        try {
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
                        //create a token for lastcharread and '=' and reset lastcharsread
                    } else {
                        //create a token for lastcharread and then token for '='
                        // and clear string for lastcharsread and set it to the nextChar
                    }
                } else if ((char) currentPointerChar == ':') {
                    nextChar = fileInputStream.read();

                    // Case of double colon
                    if ((char) nextChar == ':') {
                        // check if lastcharsread is not empty and make its token
                        // and then make a token for ':'
                    }
                    else if ((char) nextChar == ' ') {
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
                    }
                    else if ((char) nextChar == ' ') {
                        //create a token for lastcharread and '>' and reset lastcharsread
                    } else {
                        // create a token for lastcharread and then token for '>'
                        // and clear string for lastcharsread and set it to the nextChar
                    }
                } else if ((char) currentPointerChar == '/') {
                    nextChar = fileInputStream.read();

                    // Case of Inline Comment
                    if ((char) nextChar == '/') {
                       lastCharsRead = "//";
                        while((nextChar = fileInputStream.read())!= '\n' && (nextChar = fileInputStream.read())!= '\r'){
                            lastCharsRead += (char)nextChar;
                        }
                        //create token for inline
                    }
                    // Case of Block Comment
                    else if ((char) nextChar == '*') {
                        lastCharsRead = "/*";
                        boolean checkIfBlockEnded = false;
                        while(!checkIfBlockEnded){
                            nextChar = fileInputStream.read();
                            // finds a *
                            if((char)nextChar =='*'){
                                lastCharsRead += "*";
                                nextChar = fileInputStream.read();
                                // finds the '*/'
                                if((char)nextChar =='/'){
                                    // found end of comment
                                    lastCharsRead += "/";
                                    //make token for comment and add to arraylist
                                    break;
                                }
                            }
                            lastCharsRead += (char)nextChar;
                        }
                    }
                    // Case of division
                    else if ((char) nextChar == ' ') {
                        //create a token for lastcharread and '/' and reset lastcharsread
                    } else {
                        // create a token for lastcharread and then token for '/'
                        // and clear string for lastcharsread and set it to the nextChar
                    }
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
}
