package CodeGeneration;

import ASTNodes.*;

import java.io.PrintWriter;
import java.util.Stack;

public class TagsBasedCodeGenerationVisitor {
    public PrintWriter moonCodePrintWriter ;
    public Stack<String> m_registerPool   = new Stack<String>();
    public String        m_moonExecCode   = "";               // moon code instructions part
    public String        m_moonDataCode   = "";               // moon code data part
    public String        m_mooncodeindent = new String("           ");
    public String        fileName = "";

    public TagsBasedCodeGenerationVisitor(PrintWriter moonCodePrintWriter) {
        this.moonCodePrintWriter = moonCodePrintWriter;
        // push registers available to stack
        for (Integer i = 12; i>=1; i--)
            m_registerPool.push("r" + i.toString());
    }

    public void visit(ASTNode node) {
        //System.out.println("tag node visited");
    }

    public void visit(ProgNode node) {
        // entry point
        m_moonExecCode += m_mooncodeindent + "entry\n";
        m_moonExecCode += m_mooncodeindent + "addi r14,r0,topaddr\n";

        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }

        // end point
        m_moonDataCode += m_mooncodeindent + "% buffer space used for console output\n";
        m_moonDataCode += String.format("%-11s", "buf") + "res 20\n";
        m_moonExecCode += m_mooncodeindent + "hlt\n";

        // print to file
        moonCodePrintWriter.write(this.m_moonExecCode);
        moonCodePrintWriter.write(this.m_moonDataCode);
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
    }

    public void visit(MemberFuncDefNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
    }



}
