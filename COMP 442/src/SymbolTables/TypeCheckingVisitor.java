package SymbolTables;

import ASTNodes.*;
import LexerComponents.*;

import java.io.PrintWriter;

public class TypeCheckingVisitor{
    public PrintWriter semanticErrorWriter;
    public String p_error="";

    public TypeCheckingVisitor(PrintWriter semanticErrorWriter) {
        this.semanticErrorWriter = semanticErrorWriter;
    }

    public void visit(ASTNode node) {
        System.out.println("type checking visit");
    }

    public void visit(ProgNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
        semanticErrorWriter.write(p_error);

    }

    public void visit(ClassDeclNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }

        // 8.1 Check for multiple declared classes
        int cntFound = 0;
        for(ASTNode childOfProgNode : node.parentNode.childrenNodes){
            if(childOfProgNode instanceof ClassDeclNode){
                String classnamefromlist = ((Token)childOfProgNode.childrenNodes.get(0).semanticConcept).getLexeme();
                   if(((Token)node.childrenNodes.get(0).semanticConcept).getLexeme().equals(classnamefromlist)){
                       cntFound++;
                   }
            }
        }
        if(cntFound > 1){
            p_error += "8.1 Multiply declared class: class "
                    +((Token) node.childrenNodes.get(0).semanticConcept).getLexeme()
                    +", line "+((Token) node.childrenNodes.get(0).semanticConcept).getPosition()+"\n";
        }
    }

    public void visit(FuncDefNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }

        // 6.1 check for undeclared member function definition
        if(node.foundMember == false){
            p_error += "6.1 Undeclared member function definition: function "
                    +((Token) node.childrenNodes.get(0).semanticConcept).getLexeme()
                    +", line "+((Token) node.childrenNodes.get(0).semanticConcept).getPosition()+"\n";
        }
        // 8.2 Multiply Declared Functions
        int cntFound = 0;
        for(ASTNode childOfProgNode : node.parentNode.childrenNodes){
            if(childOfProgNode instanceof FuncDefNode){
                String funcnamefromlist = ((Token)childOfProgNode.childrenNodes.get(0).semanticConcept).getLexeme();
                if(((Token)node.childrenNodes.get(0).semanticConcept).getLexeme().equals(funcnamefromlist)){
                    cntFound++;
                }
            }
        }
        if(cntFound > 1){
            p_error += "8.2 Multiply declared function: function "
                    +((Token) node.childrenNodes.get(0).semanticConcept).getLexeme()
                    +", line "+((Token) node.childrenNodes.get(0).semanticConcept).getPosition()+"\n";
        }
    }

    public void visit(VarDeclNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
    }

    public void visit(ParamListNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
    }

    public void visit(MemberFuncDefNode node) {
        boolean isdeclared_6_2 = false;
        for(ASTNode childofProgNode : node.parentNode.parentNode.childrenNodes){
            if(childofProgNode.semanticConcept.equals("func def")){
                String funcDef = (((Token) childofProgNode.childrenNodes.get(0).semanticConcept).getLexeme());
                String nodeDef = ((Token) node.childrenNodes.get(1).semanticConcept).getLexeme();
                if(funcDef.equals(nodeDef)){
                    String funcDefParamList = childofProgNode.childrenNodes.get(1).m_symtabentry.toString();
                    String nodeParamList =node.childrenNodes.get(2).m_symtabentry.toString();
                    if(funcDefParamList.equals(nodeParamList)){
                        String funcDefReturn = (((Token) childofProgNode.childrenNodes.get(2).semanticConcept).getLexeme());
                        String nodeDefReturn = ((Token) node.childrenNodes.get(3).semanticConcept).getLexeme();
                        if(funcDefReturn.equals(nodeDefReturn)){
                            ((FuncDefNode)childofProgNode).foundMember = true;
                            isdeclared_6_2 = true;
                            break;
                        }
                    }
                }
            }
        }
        if(isdeclared_6_2 == false){
            p_error+="6.2 Undefined member function definition: function "
                    + ((Token) node.childrenNodes.get(1).semanticConcept).getLexeme()
                    +", line "+((Token) node.childrenNodes.get(1).semanticConcept).getPosition()+"\n";
        }
    }

}