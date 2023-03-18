import ASTNodes.ASTNode;
import SymbolTables.SymbolTableVisitor;

import java.util.Stack;

public class SymbolTableCreation {

    public SymbolTableCreation(){}

    public Stack<ASTNode> generateSymbolTables(Stack<ASTNode> ASTStack){

        SymbolTableVisitor stv = new SymbolTableVisitor();
        ASTStack.firstElement().accept(stv);

        return ASTStack;
    }

    public void writeSymblTablesToFile(){

    }
}
