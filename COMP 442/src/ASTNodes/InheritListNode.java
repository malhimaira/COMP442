package ASTNodes;

import SymbolTables.SymbolTableVisitor;

import java.util.ArrayList;

public class InheritListNode extends ASTNode {

	public InheritListNode(ASTNode parentNode, ArrayList<ASTNode> childrenNodes, Object semanticConcept, int treeDepth) {
			super(parentNode, childrenNodes, semanticConcept, treeDepth);
		}
	
	public void accept(SymbolTableVisitor p_visitor) {
		p_visitor.visit(this);
	}
}
