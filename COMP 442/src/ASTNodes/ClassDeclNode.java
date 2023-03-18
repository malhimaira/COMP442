package ASTNodes;

import java.util.ArrayList;

public class ClassDeclNode extends ASTNode {

    public ClassDeclNode(ASTNode parentNode, ArrayList<ASTNode> childrenNodes, Object semanticConcept, int treeDepth) {
        super(parentNode, childrenNodes, semanticConcept, treeDepth);
    }

    @Override
    public String symbolTablePrint() {
        return"class decl entry";
    }
}
