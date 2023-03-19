package SymbolTables;

import ASTNodes.*;
import LexerComponents.*;

import java.util.Vector;

public class SymbolTableVisitor {

    public SymbolTableVisitor() {}

    public void visit(ASTNode node) {
        System.out.println("symbol table entry string");
    }

    public void visit(ProgNode node) {
        //System.out.println("prog entry");
        node.m_symtab = new SymbolTable(0, "global", null);
        for (ASTNode child : node.childrenNodes) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }

        // TODO print to file
    }

    public void visit(ClassDeclNode node) {
        //System.out.println("class decl entry");

        String classname = ((Token) node.childrenNodes.get(0).semanticConcept).getLexeme();

        SymbolTable localtable = new SymbolTable(1, classname, node.m_symtab);
        node.m_symtabentry = new ClassEntry(classname,  localtable);
        node.m_symtab.addEntry(node.m_symtabentry);
        node.m_symtab = localtable;

        //TODO: children node loop
        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(InheritListNode node){
        if(node.childrenNodes.size()!=0){
            String fname =  ((Token) node.childrenNodes.get(0).semanticConcept).getLexeme();
            node.m_symtabentry = new InheritListEntry("","",fname,null);
            node.m_symtab.addEntry(node.m_symtabentry);
        }else{
            node.m_symtabentry = new InheritListEntry("","","none",null);
            node.m_symtab.addEntry(node.m_symtabentry);
        }
    }

    public void visit(FuncDefNode node) {
        System.out.println("func def entry");

        String ftype = "";
        String fname =  ((Token) node.childrenNodes.get(0).semanticConcept).getLexeme();
        SymbolTable localtable = new SymbolTable(1,fname, node.m_symtab);

        String paramlist = "";
        boolean returntypeneeded = false;
        for (ASTNode child : node.childrenNodes) {
            if(child instanceof ParamListNode){
                paramlist = "(";
                for (ASTNode child2 : child.childrenNodes) {
                    paramlist += ((Token)child2.semanticConcept).getLexeme()+", ";
                }
                paramlist = paramlist.substring(0,paramlist.length()-2)+")";
                returntypeneeded = true;
                continue;
            }
            if(returntypeneeded == true){
                returntypeneeded = false;
                paramlist += " "+((Token)child.semanticConcept).getLexeme();
            }

        }
        node.m_symtabentry = new FuncEntry(ftype, fname, paramlist, localtable);
        node.m_symtab.addEntry(node.m_symtabentry);
        node.m_symtab = localtable;

        // TODO children loop
        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }

    }

    public void visit(ParamListNode node) {
        //System.out.println("pln  entry");
        for (ASTNode child : node.childrenNodes) {
            child.m_symtab = node.m_symtab;
            node.m_symtabentry = new VarEntry("param", ""+((Token)child.semanticConcept).getTokenType(), ((Token)child.semanticConcept).getLexeme());
            child.m_symtabentry =  node.m_symtabentry;
            child.m_symtab.addEntry(node.m_symtabentry);
            child.accept(this);
        }
    }

    public void visit(StatBlockNode node){
        System.out.println("stat block");
        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(VarDeclNode node){
        //System.out.println("var decl node");
            node.m_symtabentry = new VarEntry(""+node.semanticConcept, ""+((Token)node.childrenNodes.get(1).semanticConcept).getLexeme(), ((Token)node.childrenNodes.get(0).semanticConcept).getLexeme());
            node.m_symtab.addEntry(node.m_symtabentry);
    }

    public void visit(MemberVarDeclNode node){
        //System.out.println("var decl node");
        node.m_symtabentry = new VarEntry("data", ((Token)node.childrenNodes.get(2).semanticConcept).getLexeme(), ""+((Token)node.childrenNodes.get(1).semanticConcept).getLexeme(), ((Token)node.childrenNodes.get(0).semanticConcept).getLexeme());
        node.m_symtab.addEntry(node.m_symtabentry);
    }

    public void visit(MemberFuncDefNode node) {
        System.out.println("member func def entry");

        String ftype = "";
        String fname =  ((Token) node.childrenNodes.get(1).semanticConcept).getLexeme();
        String fencap = ((Token) node.childrenNodes.get(0).semanticConcept).getLexeme();
        SymbolTable localtable = new SymbolTable(2,fname, node.m_symtab);

        String paramlist = "";
        boolean returntypeneeded = false;
        for (ASTNode child : node.childrenNodes) {
            if(child instanceof ParamListNode){
                paramlist = "(";
                for (ASTNode child2 : child.childrenNodes) {
                    paramlist += ((Token)child2.semanticConcept).getLexeme()+", ";
                }
                paramlist = paramlist.substring(0,paramlist.length()-2)+")";
                returntypeneeded = true;
                continue;
            }
            if(returntypeneeded == true){
                returntypeneeded = false;
                paramlist += " "+((Token)child.semanticConcept).getLexeme();
            }

        }
        node.m_symtabentry = new FuncEntry(ftype, fname, paramlist, localtable, fencap);
        node.m_symtab.addEntry(node.m_symtabentry);
        node.m_symtab = localtable;

        // TODO children loop
        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }

    }

}
