package CodeGeneration;

import ASTNodes.*;
import SymbolTables.*;
import LexerComponents.*;

import java.io.PrintWriter;
import java.util.Stack;

public class TagsBasedCodeGenerationVisitor {
    public PrintWriter moonCodePrintWriter ;
    public Integer m_tempVarNum = 0;
    public Stack<String> m_registerPool   = new Stack<String>();
    public String        m_moonExecCode   = "";               // moon code executable instructions
    public String        m_moonDataCode   = "";               // moon code data
    public String        m_mooncodeindent = new String("           ");

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
        moonCodePrintWriter.println(this.m_moonExecCode);
        moonCodePrintWriter.println(this.m_moonDataCode);

        moonCodePrintWriter.close();

        System.out.println("Moon Code Generated");
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
        if (((Token)node.childrenNodes.get(1).semanticConcept).getLexeme().equals("int") || ((Token)node.childrenNodes.get(1).semanticConcept).getLexeme().equals("integer")){
            m_moonDataCode += m_mooncodeindent + "% space for variable " + ((Token)node.childrenNodes.get(0).semanticConcept).getLexeme() + "\n";
            m_moonDataCode += ((Token)node.childrenNodes.get(0).semanticConcept).getLexeme() + " res 4\n";
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

    public void visit(IdNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
    }

    public void visit(InheritListNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
    }

    public void visit(ArraySizeNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
    }

    public void visit(IndiceNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
    }

    public void visit(ArithmNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
    }

    public void visit(ExprNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
    }

    public void visit(AssignOpNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
        String lhsVar = ((Token)node.parentNode.childrenNodes.get(0).semanticConcept).getLexeme();
        String oper = "";
        if(node.childrenNodes.get(1).childrenNodes.get(0).childrenNodes.size()>1){
            oper = node.childrenNodes.get(1).childrenNodes.get(0).childrenNodes.get(1).m_moonVarName;
        }
        else{
            oper = node.childrenNodes.get(1).childrenNodes.get(0).childrenNodes.get(0).m_moonVarName;
        }
        String localRegister = this.m_registerPool.pop();
        m_moonExecCode += m_mooncodeindent + "% processing: "  + lhsVar+ " := " +  oper+ "\n";
        m_moonExecCode += m_mooncodeindent + "lw " + localRegister + "," +oper+ "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "sw " + lhsVar + "(r0)," + localRegister + "\n";
        //deallocate local register
        this.m_registerPool.push(localRegister);
    }

    public void visit(AddOpNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (ASTNode child : node.childrenNodes )
            child.accept(this);
        // Then, do the processing of this nodes' visitor
        // create a local variable and allocate a register to this subcomputation
        String localRegister      = this.m_registerPool.pop();
        String leftChildRegister  = this.m_registerPool.pop();
        String rightChildRegister = this.m_registerPool.pop();
        // generate code
        int indexBefore = -1;
        String indexBeforeName = "";
        //before name of assign
        for (ASTNode child : node.parentNode.parentNode.parentNode.parentNode.childrenNodes) {
            indexBefore ++;
            if(child instanceof IndiceNode){
                indexBeforeName = ((Token)node.parentNode.childrenNodes.get(indexBefore-1).semanticConcept).getLexeme();
                break;
            }
            else if(child instanceof AssignOpNode){
                //ex: n from n = 1 + 1
                indexBeforeName = ((Token)node.parentNode.parentNode.parentNode.parentNode.childrenNodes.get(indexBefore-1).semanticConcept).getLexeme();

            }
        }
        String left = node.parentNode.childrenNodes.get(0).m_moonVarName;
        String right = node.childrenNodes.get(1).m_moonVarName;

        m_moonExecCode += m_mooncodeindent + "% processing: " + node.m_moonVarName + " := " + left + " + " + right + "\n";
        m_moonExecCode += m_mooncodeindent + "lw "  + leftChildRegister +  "," + left + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "lw "  + rightChildRegister + "," + right + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "add " + localRegister +      "," + leftChildRegister + "," + rightChildRegister + "\n";
        m_moonDataCode += m_mooncodeindent + "% space for " + left + " + " + right + "\n";
        m_moonDataCode += String.format("%-10s",node.m_moonVarName) + " res 4\n";
        m_moonExecCode += m_mooncodeindent + "sw " + node.m_moonVarName + "(r0)," + localRegister + "\n";
        // deallocate the registers for the two children, and the current node
        this.m_registerPool.push(leftChildRegister);
        this.m_registerPool.push(rightChildRegister);
        this.m_registerPool.push(localRegister);

    }

    public void visit(MultOpNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (ASTNode child : node.childrenNodes )
            child.accept(this);
        // Then, do the processing of this nodes' visitor
        // create a local variable and allocate a register to this subcomputation
        String localRegister      = this.m_registerPool.pop();
        String leftChildRegister  = this.m_registerPool.pop();
        String rightChildRegister = this.m_registerPool.pop();
        int indexBefore = -1;
        String indexBeforeName = "";
        //before name of assign
        for (ASTNode child : node.parentNode.parentNode.parentNode.parentNode.childrenNodes) {
            indexBefore ++;
            if(child instanceof IndiceNode){
                indexBeforeName = ((Token)node.parentNode.childrenNodes.get(indexBefore-1).semanticConcept).getLexeme();
                break;
            }
            else if(child instanceof AssignOpNode){
                //ex: n from n = 1 + 1
                indexBeforeName = ((Token)node.parentNode.parentNode.parentNode.parentNode.childrenNodes.get(indexBefore-1).semanticConcept).getLexeme();

            }
        }
        String left = node.parentNode.childrenNodes.get(0).m_moonVarName;
        String right = node.childrenNodes.get(1).m_moonVarName;
        // generate code
        m_moonExecCode += m_mooncodeindent + "% processing: " + node.m_moonVarName + " := " + left + " * " + right + "\n";
        m_moonExecCode += m_mooncodeindent + "lw "  + leftChildRegister  + "," + left + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "lw "  + rightChildRegister + "," + right + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "mul " + localRegister      + "," + leftChildRegister + "," + rightChildRegister + "\n";
        m_moonDataCode += m_mooncodeindent + "% space for " + left + " * " + right + "\n";
        m_moonDataCode += String.format("%-10s",node.m_moonVarName) + " res 4\n";
        m_moonExecCode += m_mooncodeindent + "sw " + node.m_moonVarName + "(r0)," + localRegister + "\n";
        // deallocate the registers for the two children, and the current node
        this.m_registerPool.push(leftChildRegister);
        this.m_registerPool.push(rightChildRegister);
        this.m_registerPool.push(localRegister);

    }

    public void visit(WriteNode node){
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (ASTNode child : node.childrenNodes){
            child.accept(this);
        }
        // Then, do the processing of this nodes' visitor
        // create a local variable and allocate a register to this subcomputation
        String localRegister      = this.m_registerPool.pop();
        //generate code
        String writeChar = node.childrenNodes.get(0).childrenNodes.get(0).childrenNodes.get(0).m_moonVarName;
//        String writeChar = p_node.m_moonVarName;
        m_moonExecCode += m_mooncodeindent + "% processing: put("  + writeChar + ")\n";
        m_moonExecCode += m_mooncodeindent + "lw " + localRegister + "," + writeChar + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "% put value on stack\n";
        m_moonExecCode += m_mooncodeindent + "sw -8(r14)," + localRegister + "\n";
        m_moonExecCode += m_mooncodeindent + "% link buffer to stack\n";
        m_moonExecCode += m_mooncodeindent + "addi " + localRegister + ",r0, buf\n";
        m_moonExecCode += m_mooncodeindent + "sw -12(r14)," + localRegister + "\n";
        m_moonExecCode += m_mooncodeindent + "% convert int to string for output\n";
        m_moonExecCode += m_mooncodeindent + "jl r15, intstr\n";
        m_moonExecCode += m_mooncodeindent + "sw -8(r14),r13\n";
        m_moonExecCode += m_mooncodeindent + "% output to console\n";
        m_moonExecCode += m_mooncodeindent + "jl r15, putstr\n";
        //deallocate local register
        this.m_registerPool.push(localRegister);
    }

    public void visit(NumNode node) {
        for (ASTNode child : node.childrenNodes){
            child.accept(this);
        }
        String constVal = ((Token)node.childrenNodes.get(0).semanticConcept).getLexeme();

        String localRegister = this.m_registerPool.pop();
        //generate code
        m_moonDataCode += m_mooncodeindent + "% space for constant " +  constVal + "\n";
        m_moonDataCode += String.format("%-10s",node.m_moonVarName) + " res 4\n";
        m_moonExecCode += m_mooncodeindent + "% processing: " + node.m_moonVarName + " := " + constVal + "\n";
        m_moonExecCode += m_mooncodeindent + "addi " + localRegister + ",r0," + constVal+ "\n";
        m_moonExecCode += m_mooncodeindent + "sw " + node.m_moonVarName + "(r0)," + localRegister + "\n";
        // deallocate the register for the current node
        this.m_registerPool.push(localRegister);
    }

}
