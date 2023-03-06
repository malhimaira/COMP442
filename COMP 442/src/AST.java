import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class AST {
    AST parentNode;
    ArrayList<AST> childrenNodes;
    Object semanticConcept;
    int treeDepth;
    static Stack<AST> semanticStack = new Stack<>();


    public AST(AST parentNode, ArrayList<AST> childrenNodes,Object semanticConcept, int treeDepth){
        this.parentNode = parentNode;
        this.childrenNodes = childrenNodes;
        this.semanticConcept = semanticConcept;
        this.treeDepth = treeDepth;
    }

    static public AST makeNull(){
        semanticStack.push(null);
        return null;
    }

    static public AST makeNode(Token semanticConcept){
        AST node = new AST(null, null, semanticConcept,  0);
        semanticStack.push(node);
        return node;
    }

    static public AST makeFamily(Object semanticConcept, int countPop){
        ArrayList<AST> childrenNodes = new ArrayList<>();

        // pop as many nodes as there are children to form tree
        if(countPop != -1){
            for(int i = 0; i < countPop; i++){
                childrenNodes.add(semanticStack.pop());
            }
        }
        else {
            // if no childen left to pop
            while(semanticStack.peek() != null){
                // build the tree
                childrenNodes.add(semanticStack.pop());
            }
            semanticStack.pop();
        }

        AST parentNode = new AST(null, childrenNodes, semanticConcept,  0);

        // set the parent nide in each of the children nodes of a specific node
        for (var child: parentNode.childrenNodes){
            child.setParentNode(parentNode);
        }
        parentNode.fixTreeDepth();

        // stack requires reverse ordering
        Collections.reverse(childrenNodes);

        // add
        semanticStack.push(parentNode);

        return parentNode;
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

    public static String printTree(){
        return semanticStack.toString();
    }

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
}