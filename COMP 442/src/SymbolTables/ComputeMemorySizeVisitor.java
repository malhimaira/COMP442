package SymbolTables;

import ASTNodes.*;
import LexerComponents.Token;

public class ComputeMemorySizeVisitor {

    public ComputeMemorySizeVisitor() {}

    public int sizeOfEntry(ASTNode p_node) {
        int size = 0;
        if(p_node.m_symtabentry.m_type.equals("integer"))
            size = 4;
        else if(p_node.m_symtabentry.m_type.equals("float"))
            size = 8;
        else if (p_node.m_symtabentry.m_type.equals("id")) {
            size = 4;
        }
        return size;
    }

    public void visit(ASTNode node) {
        // System.out.println("memory string computed");
    }

    public void visit(ProgNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
        System.out.println("here");
    }

    public void visit(ClassDeclNode node) {
        for (ASTNode child : node.childrenNodes ) {
            child.accept(this);
        }
    }

    public void visit(FuncDefNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
    }

    public void visit(VarDeclNode node) {
        node.m_symtabentry.m_size = this.sizeOfEntry(node);
    }

    public void visit(StatNode node) {
        for (ASTNode child : node.childrenNodes ) {
            child.accept(this);
        }
    }

    public void visit(StatBlockNode node){
        for (ASTNode child : node.childrenNodes ) {
            child.accept(this);
        }
    }

    public void visit(ParamListNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
    }

    public void visit(MemberVarDeclNode node){

    }

    public void visit(MemberFuncDefNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
        node.m_symtabentry.m_size = this.sizeOfEntry(node);
    }

}
