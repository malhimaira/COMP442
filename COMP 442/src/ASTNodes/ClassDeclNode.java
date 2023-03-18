package ASTNodes;

import SymbolTables.SymbolTableVisitor;

import java.util.ArrayList;

public class ClassDeclNode extends ASTNode {

    public ClassDeclNode(ASTNode parentNode, ArrayList<ASTNode> childrenNodes, Object semanticConcept, int treeDepth) {
        super(parentNode, childrenNodes, semanticConcept, treeDepth);
    }

    @Override
    public void accept(SymbolTableVisitor stv){
        System.out.println("class decl entry");
        stv.visit(this);
    }
}
