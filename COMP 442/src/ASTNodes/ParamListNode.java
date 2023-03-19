package ASTNodes;

import SymbolTables.SymbolTableVisitor;
import java.util.ArrayList;

public class ParamListNode extends ASTNode {
	
	public ParamListNode(ASTNode parentNode, ArrayList<ASTNode> childrenNodes, Object semanticConcept, int treeDepth) {
		super(parentNode, childrenNodes, semanticConcept, treeDepth);
	}
	
	public void accept(SymbolTableVisitor p_visitor) {
		p_visitor.visit(this);
	}
}
