import LexerComponents.Token;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;

public class LexDriver {

    public static void main(String[] args) {
        // Open Stream to File
        try {
            FileInputStream fileInputStream = new FileInputStream("COMP 442/input&output/" + args[0]);

            //Extract filename
            String filetoRead = args[0];
            int fileExtensionIndex = filetoRead.indexOf(".");
            String fileName = filetoRead.substring(0, fileExtensionIndex);

            // Open print writer for errors
            PrintWriter printWriterErrors = new PrintWriter(new File("COMP 442/input&output/"+fileName + ".outlexerrors"));
            PrintWriter printWriterTokens = new PrintWriter(new File("COMP 442/input&output/"+fileName + ".outlextokens"));

            // Open Lexer
            Lexer lexer = new Lexer(fileInputStream, printWriterErrors);

            Token token;
            int lastRowChecked = 1;
            boolean firstcheck = true;
            // Loop to print tokens
            while ((token = lexer.getNextToken()) != null) {
                if (firstcheck == true) {
                    printWriterTokens.write("" + token);
                    firstcheck = false;
                } else if (lastRowChecked == token.getPosition().getRow()) {
                    printWriterTokens.write(" " + token);
                } else {
                    lastRowChecked= token.getPosition().getRow();
                    printWriterTokens.write("\n" + token);
                }
            }
            lexer.getNextToken();
            printWriterTokens.close();
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }


}
