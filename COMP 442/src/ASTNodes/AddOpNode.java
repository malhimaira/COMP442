package ASTNodes;

import CodeGeneration.TagsBasedCodeGenerationVisitor;
import SymbolTables.ComputeMemorySizeVisitor;
import SymbolTables.SymbolTableVisitor;
import SymbolTables.TypeCheckingVisitor;

import java.util.ArrayList;

public class AddOpNode extends ASTNode{
    public AddOpNode(ASTNode parentNode, ArrayList<ASTNode> childrenNodes, Object semanticConcept, int treeDepth) {
        super(parentNode, childrenNodes, semanticConcept, treeDepth);
    }

    public void accept(SymbolTableVisitor p_visitor) {
        p_visitor.visit(this);
    }

    public void accept(TypeCheckingVisitor tcv) {
        tcv.visit(this);
    }

    public void accept(ComputeMemorySizeVisitor cmsv) {
        cmsv.visit(this);
    }

    public void accept(TagsBasedCodeGenerationVisitor tbcgv) {
        tbcgv.visit(this);
    }
}
