package ASTNodes;

import java.util.ArrayList;

public class AST {
    public AST parentNode;
    public ArrayList<AST> childrenNodes;
    public Object semanticConcept;
    public int treeDepth;

    public AST(AST parentNode, ArrayList<AST> childrenNodes,Object semanticConcept, int treeDepth){
        this.parentNode = parentNode;
        this.childrenNodes = childrenNodes;
        this.semanticConcept = semanticConcept;
        this.treeDepth = treeDepth;
    }

    public void fixTreeDepth(){
        if(this.childrenNodes == null)
            return;
        for (var child: this.childrenNodes){
            child.setTreeDepth(child.getTreeDepth()+1);
            child.fixTreeDepth();
        }
    }

    public void setParentNode(AST parentNode) {this.parentNode = parentNode;}

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

    public String symbolTablePrint(){
        return "symbol table entry string";
    }
}