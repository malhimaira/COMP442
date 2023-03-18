package ASTNodes;


import java.util.ArrayList;

public class ProgNode extends AST {


    public ProgNode(AST parentNode, ArrayList<AST> childrenNodes, Object semanticConcept, int treeDepth) {
        super(parentNode, childrenNodes, semanticConcept, treeDepth);
    }
}
