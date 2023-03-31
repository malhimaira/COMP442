package ASTNodes;

import SymbolTables.ComputeMemorySizeVisitor;
import SymbolTables.SymbolTableVisitor;
import SymbolTables.TypeCheckingVisitor;

import java.util.ArrayList;

public class ArraySizeNode extends ASTNode {

    public ArraySizeNode(ASTNode parentNode, ArrayList<ASTNode> childrenNodes, Object semanticConcept, int treeDepth) {
        super(parentNode, childrenNodes, semanticConcept, treeDepth);
    }

    public void accept(SymbolTableVisitor stv) {
        stv.visit(this);
    }

    public void accept(TypeCheckingVisitor tcv) {
        tcv.visit(this);
    }

    public void accept(ComputeMemorySizeVisitor cmsv) {
        cmsv.visit(this);
    }

}
