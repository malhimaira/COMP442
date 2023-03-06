import java.util.ArrayList;
import java.util.Stack;
import java.util.Collections;

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

    public void setParentNode(AST parentNode) {
        this.parentNode = parentNode;
    }

    public int getTreeDepth() {
        return treeDepth;
    }

    public void setTreeDepth(int treeDepth) {
        this.treeDepth = treeDepth;
    }

    public void updateTreeDepth(){
        if(this.childrenNodes == null)
            return;
        for (var child: this.childrenNodes){
            child.setTreeDepth(child.getTreeDepth()+1);
            child.updateTreeDepth();
        }
    }

    static public AST createSubtree(Object concept, int countPop){

        ArrayList<AST> childrens = new ArrayList<>();
        if(countPop != -1){
            for(int i = 0; i < countPop; i++){
                childrens.add(astStack.pop());
            }
        }
        else {
            while(astStack.peek() != null){
                childrens.add(astStack.pop());
            }
            astStack.pop();
        }
        AST parent = new AST(null, childrens, concept,  0);

        for (var childNode: parent.childrenNodes){
            childNode.setParentNode(parent);
        }
        parent.updateTreeDepth();

        Collections.reverse(childrens);

        astStack.push(parent);

        return parent;

    }

    static public AST pushEmptyNode(){
        astStack.push(null);
        return null;
    }

    static public AST pushNode(Token conceptToken){
        AST node = new AST(null, null, conceptToken,  0);
        astStack.push(node);
        return node;
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