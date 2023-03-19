package ASTNodes;

import SymbolTables.*;

import java.util.ArrayList;

public class StatBlockNode extends ASTNode {
		
	public StatBlockNode(ASTNode parentNode, ArrayList<ASTNode> childrenNodes, Object semanticConcept, int treeDepth) {
			super(parentNode, childrenNodes, semanticConcept, treeDepth);
		}
	
	public void accept(SymbolTableVisitor p_visitor) {
		p_visitor.visit(this);
	}
}
