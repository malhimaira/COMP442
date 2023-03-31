package SymbolTables;

import ASTNodes.*;
import LexerComponents.Token;

public class ComputeMemorySizeVisitor {

    public ComputeMemorySizeVisitor() {}

    public int sizeOfEntry(ASTNode node) {
        int size = 0;
        if(node.m_symtabentry.m_type.equals("integer"))
            size = 4;
        else if(node.m_symtabentry.m_type.equals("float"))
            size = 8;
        else if (node.m_symtabentry.m_type.equals("id")) {
            size = 4;
        }
        VarEntry varEntry = (VarEntry) node.m_symtabentry;
        if(!varEntry.m_dims.isEmpty())
            for(Integer dim : varEntry.m_dims)
                size *= dim;

        return size;
    }

    public int sizeOfTypeNode(ASTNode node) {
        int size = 0;
        // id  case -> 32 bit
        if(node.semanticConcept == "")
            size = 4;
        else if(node.semanticConcept == "float")
            size = 8;
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
        for (ASTNode child : node.childrenNodes ) {
            child.accept(this);
        }
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
        for (ASTNode child : node.childrenNodes ) {
            child.accept(this);
        }
        node.m_symtabentry.m_size = this.sizeOfEntry(node);
    }

    public void visit(MemberFuncDefNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
//        node.m_symtabentry.m_size = this.sizeOfEntry(node);
    }

}
