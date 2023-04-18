import ASTNodes.ASTNode;
import SymbolTables.SymbolTableVisitor;
import SymbolTables.TypeCheckingVisitor;

import java.io.PrintWriter;
import java.util.Stack;

public class SymbolTableCreation {


    public SymbolTableCreation() {}

    public Stack<ASTNode> generateSymbolTables(Stack<ASTNode> ASTStack) {
        SymbolTableVisitor stv = new SymbolTableVisitor();
        ASTStack.firstElement().accept(stv);
        return ASTStack;
    }

    public void typeCheckSymbolTables(String filename, Stack<ASTNode> ASTStack, PrintWriter printErrorWriter) {
        try {
            TypeCheckingVisitor tcv = new TypeCheckingVisitor(printErrorWriter);
            ASTStack.firstElement().accept(tcv);
            printErrorWriter.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void writeSymblTablesToFile(String filename, Stack<ASTNode> ASTStackWithSymbolTables) {
        try {
            PrintWriter symbolTablesWriter = new PrintWriter("COMP 442/input&output/" + filename + ".outsymboltables");
            symbolTablesWriter.write(ASTStackWithSymbolTables.get(0).m_symtab.toString());
            symbolTablesWriter.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
