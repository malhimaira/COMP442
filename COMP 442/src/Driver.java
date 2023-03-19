import ASTNodes.ASTNode;
import LexerComponents.Token;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Stack;

public class Driver {

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

            // Parse Tokens and fill ASTNodes.AST
            p.parse(pwError, lexer);
            // System.out.println(p.output);

            // AST Stack
            Stack<ASTNode> ASTstack = p.ASTstack;

            // Generate ASTNodes.AST's text file
            p.writeASTTreeToFile();

            //Generate Symbol Tables and write to file
            SymbolTableCreation stc = new SymbolTableCreation();
            Stack<ASTNode> ASTStackWithSymbolTables = stc.generateSymbolTables(ASTstack);
            stc.writeSymblTablesToFile(fileName, ASTStackWithSymbolTables);

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }


}
