package ASTNodes;

import java.util.ArrayList;

public class ClassDeclNode extends AST {

    public ClassDeclNode(AST parentNode, ArrayList<AST> childrenNodes, Object semanticConcept, int treeDepth) {
        super(parentNode, childrenNodes, semanticConcept, treeDepth);
    }

    @Override
    public String symbolTablePrint() {
        return"class decl entry";
    }
}
