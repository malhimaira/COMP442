import ASTNodes.ASTNode;
import CodeGeneration.TagsBasedCodeGenerationVisitor;
import LexerComponents.Token;
import SymbolTables.ComputeMemorySizeVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
            File f = new File("COMP 442/input&output/"+fileName + ".errors");
            PrintWriter printWriterLexErrors = new PrintWriter(new FileOutputStream(f));
            PrintWriter printWriterSyntaxErrors = new PrintWriter(new FileOutputStream(f, true));
            PrintWriter printWriterSemanticErrors = new PrintWriter(new FileOutputStream(f, true));
            PrintWriter printWriterTokens = new PrintWriter(new File("COMP 442/input&output/"+fileName + ".outlextokens"));

            // Open Lexer
            Lexer lexer = new Lexer(fileInputStream, printWriterLexErrors);

            Token token;
            int lastRowChecked = 1;
            boolean firstcheck = true;
            // Loop to print tokens
            while ((token = lexer.getNextTokenForPrint()) != null) {
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
            lexer.getNextTokenForPrint();
            printWriterTokens.close();

            //Open Parser
            Parser p = new Parser();
            p.Parser(fileName);

            // Parse Tokens and fill ASTNodes.AST
            p.parse(printWriterSyntaxErrors, lexer);
            // System.out.println(p.output);

            // AST Stack
            Stack<ASTNode> ASTstack = p.ASTstack;

            // Generate ASTNodes.AST's text file
            p.writeASTTreeToFile();

            //Generate Symbol Tables
            SymbolTableCreation stc = new SymbolTableCreation();
            Stack<ASTNode> ASTStackWithSymbolTables = stc.generateSymbolTables(ASTstack);

            // Compute Memory Size of Nodes
            ComputeMemorySizeVisitor cmsv = new ComputeMemorySizeVisitor();
            ASTStackWithSymbolTables.firstElement().accept(cmsv);

            // write symbol tables to file
            stc.writeSymblTablesToFile(fileName, ASTStackWithSymbolTables);

            //Type Check AST
            stc.typeCheckSymbolTables(fileName, ASTStackWithSymbolTables, printWriterSemanticErrors);

            // Code Generation Visitor
            PrintWriter tbcgPrinter = new PrintWriter(new File("COMP 442/mooncode/" + fileName+ ".m"));
            TagsBasedCodeGenerationVisitor tbcgv = new TagsBasedCodeGenerationVisitor(tbcgPrinter);
            ASTStackWithSymbolTables.firstElement().accept(tbcgv);

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }


}
