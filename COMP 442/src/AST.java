import java.util.ArrayList;
import java.util.Stack;

public class AST {
    AST parentNode;
    ArrayList<AST> childrenNodes;
    Object concept;
    int treeDepth;

    static Stack<AST> astStack = new Stack<>();

    public AST (AST parentNode, ArrayList<AST> childrenNodes,Object concept, int treeDepth){
        this.parentNode = parentNode;
        this.childrenNodes = childrenNodes;
        this.concept = concept;
        this.treeDepth = treeDepth;
    }

    static public void createSubtree(Object concept, int countPop){

    }

    static public AST pushEmptyNode(){
        astStack.push(null);
        return null;
    }

    static public AST pushNode(Token t){
        AST node = new AST(null, null, t,  0);
        astStack.push(node);
        return node;
    }

    @Override
    public String toString() {
        return "";
    }

}