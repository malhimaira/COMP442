package SymbolTables;

import ASTNodes.*;

public class SymbolTableVisitor {

    public SymbolTableVisitor(){}

    public void visit(ASTNode node){
        System.out.println("symbol table entry string");
    };

    public void visit(ProgNode node){
        System.out.println("prog entry");
        for (ASTNode child : node.childrenNodes ) {
            child.accept(this);
        }

    };

    public void visit(ClassDeclNode node){
        System.out.println("class decl entry");
    };

    public void visit(FuncDefNode node){
        System.out.println("func def entry");
    };

}
