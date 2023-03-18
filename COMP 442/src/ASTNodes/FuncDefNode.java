package ASTNodes;

import java.util.ArrayList;

public class FuncDefNode extends AST {

    public FuncDefNode(AST parentNode, ArrayList<AST> childrenNodes, Object semanticConcept, int treeDepth) {
        super(parentNode, childrenNodes, semanticConcept, treeDepth);
    }

    @Override
    public String symbolTablePrint() {
        return"class decl entry";
    }
}
