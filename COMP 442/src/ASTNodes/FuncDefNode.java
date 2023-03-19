package ASTNodes;

import SymbolTables.SymbolTableVisitor;
import SymbolTables.TypeCheckingVisitor;

import java.util.ArrayList;

public class FuncDefNode extends ASTNode {

    public boolean foundMember = false;

    public FuncDefNode(ASTNode parentNode, ArrayList<ASTNode> childrenNodes, Object semanticConcept, int treeDepth) {
        super(parentNode, childrenNodes, semanticConcept, treeDepth);
    }


    @Override
    public void accept(SymbolTableVisitor stv) {
        stv.visit(this);
    }
    public void accept(TypeCheckingVisitor tcv){
        tcv.visit(this);
    }
}
