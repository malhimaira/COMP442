package ASTNodes;

import java.util.ArrayList;

public class FuncDefNode extends ASTNode {

    public FuncDefNode(ASTNode parentNode, ArrayList<ASTNode> childrenNodes, Object semanticConcept, int treeDepth) {
        super(parentNode, childrenNodes, semanticConcept, treeDepth);
    }

    @Override
    public String symbolTablePrint() {
        return"class decl entry";
    }
}
