package ASTNodes;

import SymbolTables.SymbolTableVisitor;

import java.util.ArrayList;

public class ProgNode extends ASTNode {

    public ProgNode(ASTNode parentNode, ArrayList<ASTNode> childrenNodes, Object semanticConcept, int treeDepth) {
        super(parentNode, childrenNodes, semanticConcept, treeDepth);
    }

    @Override
    public void accept(SymbolTableVisitor stv){
        stv.visit(this);
    }
}
