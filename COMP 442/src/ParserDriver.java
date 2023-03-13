import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ParserDriver {

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
            PrintWriter pwError = new PrintWriter(new File("COMP 442/input&output/" + fileName+ ".outerrors"));

            // Open Lexer
            Lexer lexer = new Lexer(fileInputStream, printWriterErrors);

            Token token;

            //Open Parser
            Parser p = new Parser();
            p.Parser(fileName);

            p.parse(pwError, lexer);
            System.out.println(p.output);


        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }


}
