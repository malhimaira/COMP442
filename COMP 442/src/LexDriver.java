import com.sun.source.util.SourcePositions;

import java.io.FileInputStream;

public class LexDriver {

    public static void main(String[] args) {
        // Open Stream to File
        try {
            FileInputStream fileInputStream = new FileInputStream("/Users/mairamalhi/IdeaProjects/COMP442/COMP 442/example-bubblesort.src");
            Lexer lexer = new Lexer(fileInputStream);
            Token token;
            while((token = lexer.getNextToken()) != null){
                System.out.println(token.getLexeme());
            }

            lexer.getNextToken();
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }


}
