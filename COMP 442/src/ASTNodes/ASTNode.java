package ASTNodes;

import CodeGeneration.TagsBasedCodeGenerationVisitor;
import SymbolTables.*;
import java.util.ArrayList;

public class ASTNode {

    public ASTNode parentNode;
    public ArrayList<ASTNode> childrenNodes;
    public Object semanticConcept;
    public int treeDepth;

    // Symbol Tables created by SymbolTableCreation
    public  SymbolTable      m_symtab             = null;
    public  SymbolTableEntry m_symtabentry        = null;

    public ASTNode(ASTNode parentNode, ArrayList<ASTNode> childrenNodes, Object semanticConcept, int treeDepth){
        this.parentNode = parentNode;
        this.childrenNodes = childrenNodes;
        this.semanticConcept = semanticConcept;
        this.treeDepth = treeDepth;
    }

    public void setChildrenNodes(ArrayList<ASTNode> childrenNodes) {
        this.childrenNodes = childrenNodes;
    }

    public void fixTreeDepth(){
        if(this.childrenNodes == null)
            return;
        for (var child: this.childrenNodes){
            child.setTreeDepth(child.getTreeDepth()+1);
            child.fixTreeDepth();
        }
    }

    public void setParentNode(ASTNode parentNode) {this.parentNode = parentNode;}

    public int getTreeDepth() {return treeDepth;}

    public void setTreeDepth(int treeDepth) {this.treeDepth = treeDepth;}

    @Override
    public String toString() {
        StringBuilder tree = new StringBuilder();
        for(int i=0;i<treeDepth; i++){
            tree.append("|  ");
        }
        tree.append(semanticConcept).append("\n");
        if(childrenNodes != null){
            for(var subtree: childrenNodes){
                tree.append(subtree.toString());
            }
        }
        return tree.toString();
    }

    public void accept(SymbolTableVisitor stv){stv.visit(this);}
    public void accept(TypeCheckingVisitor tcv){tcv.visit(this);}
    public void accept(ComputeMemorySizeVisitor cmsv){cmsv.visit(this);}
    public void accept(TagsBasedCodeGenerationVisitor tbcgv) {tbcgv.visit(this);}
}