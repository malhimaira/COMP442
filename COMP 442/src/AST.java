import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class AST {
    AST parentNode;
    ArrayList<AST> childrenNodes;
    Object concept;
    int treeDepth;

    static Stack<AST> astStack = new Stack<>();

    public void setParentNode(AST parentNode) {
        this.parentNode = parentNode;
    }

    public int getTreeDepth() {
        return treeDepth;
    }

    public void setTreeDepth(int treeDepth) {
        this.treeDepth = treeDepth;
    }

    public AST(AST parentNode, ArrayList<AST> childrenNodes,Object concept, int treeDepth){
        this.parentNode = parentNode;
        this.childrenNodes = childrenNodes;
        this.concept = concept;
        this.treeDepth = treeDepth;
    }

    static public AST makeNull(){
        astStack.push(null);
        return null;
    }

    static public AST makeNode(Token concept){
        AST node = new AST(null, null, concept,  0);
        astStack.push(node);
        return node;
    }

    static public AST makeFamily(Object concept, int numOfPops){
        ArrayList<AST> childrenNodes = new ArrayList<>();
        if(numOfPops != -1){
            for(int i = 0; i < numOfPops; i++){
                childrenNodes.add(astStack.pop());
            }
        }
        else {
            while(astStack.peek() != null){
                childrenNodes.add(astStack.pop());
            }
            astStack.pop();
        }
        AST parentNode = new AST(null, childrenNodes, concept,  0);

        for (var child: parentNode.childrenNodes){
            child.setParentNode(parentNode);
        }
        parentNode.updateTreeDepth();

        Collections.reverse(childrenNodes);

        astStack.push(parentNode);

        return parentNode;
    }

    public void updateTreeDepth(){
        if(this.childrenNodes == null)
            return;
        for (var child: this.childrenNodes){
            child.setTreeDepth(child.getTreeDepth()+1);
            child.updateTreeDepth();
        }
    }

    public static String treeToString(){
        return astStack.peek().toString();
    }

    @Override
    public String toString() {
        StringBuilder tree = new StringBuilder();
        for(int i=0;i<treeDepth; i++){
            tree.append("|  ");
        }
        tree.append(concept).append("\n");
        if(childrenNodes != null){
            for(var subtree: childrenNodes){
                tree.append(subtree.toString());
            }
        }

        return tree.toString();
    }
}