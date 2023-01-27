import java.io.FileInputStream;

public class LexDriver {

    public static void main(String[] args) {
        // Open Stream to File
        try {
            FileInputStream fileInputStream = new FileInputStream("/Users/mairamalhi/IdeaProjects/COMP442/COMP 442/example-bubblesort.src");
            Lexer lexer = new Lexer(fileInputStream);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }


}
