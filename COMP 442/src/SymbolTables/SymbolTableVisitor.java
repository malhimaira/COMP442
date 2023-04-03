package SymbolTables;

import ASTNodes.*;
import LexerComponents.*;

import java.util.Vector;

public class SymbolTableVisitor{

    public Integer m_tempVarNum     = 0;

    public SymbolTableVisitor() {}

    public String moonCodeTempVarRegname(){
        m_tempVarNum++;
        return "t" + m_tempVarNum.toString();
    }


    public void visit(ASTNode node) {
//        System.out.println("symbol table entry string");
    }

    public void visit(ProgNode node) {
        //System.out.println("prog entry");
        node.m_symtab = new SymbolTable(0, "global", null);
        for (ASTNode child : node.childrenNodes) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(ClassDeclNode node) {
        //System.out.println("class decl entry");

        String classname = ((Token) node.childrenNodes.get(0).semanticConcept).getLexeme();

        SymbolTable localtable = new SymbolTable(1, classname, node.m_symtab);
        node.m_symtabentry = new ClassEntry(classname,  localtable);
        node.m_symtab.addEntry(node.m_symtabentry);
        node.m_symtab = localtable;

        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(InheritListNode node){
        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }
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
//        System.out.println("func def entry");

        String ftype = "";
        String fname =  ((Token) node.childrenNodes.get(0).semanticConcept).getLexeme();
        SymbolTable localtable = new SymbolTable(1,fname, node.m_symtab);

        String paramlist = "";
        boolean returntypeneeded = false;
        int cnt =0;
        for (ASTNode child : node.childrenNodes) {
            if(child instanceof ParamListNode){
                paramlist = "(";
                for (ASTNode child2 : child.childrenNodes) {
                    if(cnt%2==1) {
                        paramlist += ((Token) child2.semanticConcept).getLexeme() + ", ";
                    }
                    cnt++;
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

        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }

    }

    public void visit(ParamListNode node) {
        //System.out.println("pln  entry");
        int cnt=0;
        for (ASTNode child : node.childrenNodes) {
            if(cnt%2==0){
                child.m_symtab = node.m_symtab;
                node.m_symtabentry = new VarEntry("param", ""+((Token)node.childrenNodes.get(cnt+1).semanticConcept).getLexeme(), ((Token)child.semanticConcept).getLexeme());
                child.m_symtabentry =  node.m_symtabentry;
                child.m_symtab.addEntry(node.m_symtabentry);
                child.accept(this);
            }

            cnt++;
        }
    }

    public void visit(StatBlockNode node){
//        System.out.println("stat block");
        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(StatNode node){
//        System.out.println("stat block");
        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(VarDeclNode node){
        //System.out.println("var decl node");
        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }
        // get array dimensions, if multi dim  vector size >1
        Vector<Integer> dimlist = new Vector<Integer>();
        for (ASTNode dim : node.childrenNodes.get(2).childrenNodes){
            Integer dimval = Integer.parseInt(((Token)dim.semanticConcept).getLexeme());
            dimlist.add(dimval);
        }
            node.m_symtabentry = new VarEntry(""+node.semanticConcept, ""+((Token)node.childrenNodes.get(1).semanticConcept).getLexeme(), ((Token)node.childrenNodes.get(0).semanticConcept).getLexeme(), dimlist);
            node.m_symtab.addEntry(node.m_symtabentry);
    }

    public void visit(MemberVarDeclNode node){
        //System.out.println("var decl node");
        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }
        // get array dimensions, if multi dim  vector size >1
        Vector<Integer> dimlist = new Vector<Integer>();
        for (ASTNode dim : node.childrenNodes.get(3).childrenNodes){
            Integer dimval = Integer.parseInt(((Token)dim.semanticConcept).getLexeme());
            dimlist.add(dimval);
        }

        node.m_symtabentry = new VarEntry("data", ((Token)node.childrenNodes.get(2).semanticConcept).getLexeme(), ""+((Token)node.childrenNodes.get(1).semanticConcept).getLexeme(), ((Token)node.childrenNodes.get(0).semanticConcept).getLexeme(), dimlist);
        node.m_symtab.addEntry(node.m_symtabentry);
    }

    public void visit(MemberFuncDefNode node) {
//        System.out.println("member func def entry");

        String ftype = "";
        String fname =  ((Token) node.childrenNodes.get(1).semanticConcept).getLexeme();
        String fencap = ((Token) node.childrenNodes.get(0).semanticConcept).getLexeme();
        SymbolTable localtable = new SymbolTable(2,fname, node.m_symtab);

        String paramlist = "";
        boolean returntypeneeded = false;
        int cnt = 0;
        for (ASTNode child : node.childrenNodes) {
            if(child instanceof ParamListNode){
                paramlist = "(";
                for (ASTNode child2 : child.childrenNodes) {
                    if(cnt%2==1) {
                        paramlist += ((Token) child2.semanticConcept).getLexeme() + ", ";
                    }
                    cnt++;
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

        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }

    }

    public void visit(WriteNode node){
        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(ArraySizeNode node){
        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(IdNode node){
        node.m_moonVarName = ((Token)node.childrenNodes.get(0).semanticConcept).getLexeme();
        node.m_symtabentry = new VarEntry("idvalLoad", ""+((Token) node.childrenNodes.get(0).semanticConcept).getTokenType(), node.m_moonVarName);
        node.m_symtab.addEntry(node.m_symtabentry);
    }

    public void visit(NumNode node){
        node.m_moonVarName =this.moonCodeTempVarRegname();
        node.m_symtabentry = new VarEntry("litvalLoad", ""+((Token) node.childrenNodes.get(0).semanticConcept).getTokenType(), node.m_moonVarName);
        node.m_symtab.addEntry(node.m_symtabentry);
    }

    public void visit(ArithmNode node){
        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(ExprNode node){
        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(AssignOpNode node){
        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }
    }

    public void visit(MultOpNode node){
        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }
        String tempvarname = this.moonCodeTempVarRegname();
        node.m_moonVarName = tempvarname;
        node.m_symtabentry = new VarEntry("tempRegStr", ((Token)node.childrenNodes.get(1).childrenNodes.get(0).semanticConcept).getTokenType().toString(), node.m_moonVarName);
        node.m_symtab.addEntry(node.m_symtabentry);
    }

    public void visit(AddOpNode node){
        for (ASTNode child : node.childrenNodes ) {
            child.m_symtab = node.m_symtab;
            child.accept(this);
        }
        String tempvarname = this.moonCodeTempVarRegname();
        node.m_moonVarName = tempvarname;
        node.m_symtabentry = new VarEntry("tempRegStr", ((Token)node.childrenNodes.get(1).childrenNodes.get(0).semanticConcept).getTokenType().toString(), node.m_moonVarName);
        node.m_symtab.addEntry(node.m_symtabentry);
    }

}
