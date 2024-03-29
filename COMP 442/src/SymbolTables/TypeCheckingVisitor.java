package SymbolTables;

import ASTNodes.*;
import LexerComponents.*;

import java.io.PrintWriter;

public class TypeCheckingVisitor{
    public PrintWriter semanticErrorWriter;
    public String p_error="";
    public boolean foundMain = false;
    public int countMain = 0;

    public TypeCheckingVisitor(PrintWriter semanticErrorWriter) {
        this.semanticErrorWriter = semanticErrorWriter;
    }

    public void visit(ASTNode node) {
        //System.out.println("type checking visit");
    }

    public void visit(ProgNode node) {
        for (ASTNode child : node.childrenNodes) {
            // 15.2 non-existing main function
            // 15.3 duplicate main function
            String checkForMain = ((Token) child.childrenNodes.get(0).semanticConcept).getLexeme();
            if (child instanceof FuncDefNode && checkForMain.equals("main")) {
                foundMain = true;
                countMain++;
                if (countMain > 1) {
                    p_error += "15.3 Duplicate main function "
                            + ", line " + ((Token) child.childrenNodes.get(0).semanticConcept).getPosition() + "\n";

                }
            }
            child.accept(this);
        }
            if(foundMain == false && countMain ==0){
            p_error += "15.2 Non-existing main function "+"\n";
        }

        System.out.println("Type Check complete");
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
        if(node.foundMember == false && !((Token) node.childrenNodes.get(0).semanticConcept).getLexeme().equals("main")){
            p_error += "6.1 Undeclared member function definition: function "
                    +((Token) node.childrenNodes.get(0).semanticConcept).getLexeme()
                    +", line "+((Token) node.childrenNodes.get(0).semanticConcept).getPosition()+"\n";
        }
        // 8.2 Multiply Declared Functions and 9.1 Overload free function
        int cntFound = 0;
        int overloadFound =0;
        for(ASTNode childOfProgNode : node.parentNode.childrenNodes){
            if(childOfProgNode instanceof FuncDefNode){
                String funcnamefromlist = ((Token)childOfProgNode.childrenNodes.get(0).semanticConcept).getLexeme();
                String nodefuncname = ((Token)node.childrenNodes.get(0).semanticConcept).getLexeme();
                if(nodefuncname.equals(funcnamefromlist) && !nodefuncname.equals("main")){
                    String funcDefParamList = childOfProgNode.childrenNodes.get(1).m_symtabentry.toString();
                    String nodeParamList =node.childrenNodes.get(1).m_symtabentry.toString();
                    if(funcDefParamList.equals(nodeParamList)){
                        cntFound++;
                    } else{
                        overloadFound++;
                    }
                }
            }
        }
        if(cntFound > 1){
            p_error += "8.2 Multiply declared function: function "
                    +((Token) node.childrenNodes.get(0).semanticConcept).getLexeme()
                    +", line "+((Token) node.childrenNodes.get(0).semanticConcept).getPosition()+"\n";
        }
        if(overloadFound > 0){
            p_error += "[warning] 9.1 Overload free function: function "
                    +((Token) node.childrenNodes.get(0).semanticConcept).getLexeme()
                    +", line "+((Token) node.childrenNodes.get(0).semanticConcept).getPosition()+"\n";
        }
    }

    public void visit(VarDeclNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }

        // 8.4 Mulitply declared identifier in function
        int cnt =0;
        for(ASTNode child : node.parentNode.childrenNodes){
            if(child instanceof VarDeclNode){
                String nodememberName = ((Token) node.childrenNodes.get(0).semanticConcept).getLexeme();
                String nodecomparedmemberName = ((Token) child.childrenNodes.get(0).semanticConcept).getLexeme();
                if(nodememberName.equals(nodecomparedmemberName)){
                    cnt++;
                }
            }
        }
        if (cnt >1){
            p_error+="8.4 Mulitply declared identifier in function: identifier "
                    + ((Token) node.childrenNodes.get(0).semanticConcept).getLexeme()
                    +", line "+((Token) node.childrenNodes.get(0).semanticConcept).getPosition()+"\n";
        }

        // 11.5 Undeclared class
        if(((Token) node.childrenNodes.get(1).semanticConcept).getTokenType().equals(TokenType.id)){
            boolean foundClass = false;
            String classToFind = ((Token) node.childrenNodes.get(1).semanticConcept).getLexeme();
            for(ASTNode childOfProgNode : node.parentNode.parentNode.parentNode.childrenNodes){
                if(childOfProgNode instanceof ClassDeclNode){
                    String classNameFromProgChild = ((Token) childOfProgNode.childrenNodes.get(0).semanticConcept).getLexeme();
                    if(classToFind.equals(classNameFromProgChild)){
                        foundClass = true;
                        break;
                    }
                }
            }
            if(foundClass == false){
                p_error+="11.5 Undeclared Class: class "
                        + ((Token) node.childrenNodes.get(1).semanticConcept).getLexeme()
                        +", line "+((Token) node.childrenNodes.get(1).semanticConcept).getPosition()+"\n";
            }
        }

    }

    public void visit(StatNode node) {
        //System.out.println("stat node");
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

    public void visit(MemberFuncDefNode node) {
        // 6.2 Undefined member function definition: function
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

        // 9.2 Overloading member function
        int cntoverloaded =0;
        for(ASTNode child : node.parentNode.childrenNodes){
            if(child instanceof MemberFuncDefNode){
                String funcnamefromlist = ((Token)child.childrenNodes.get(1).semanticConcept).getLexeme();
                String nodefuncname = ((Token)node.childrenNodes.get(1).semanticConcept).getLexeme();
                if(nodefuncname.equals(funcnamefromlist) && child.childrenNodes.size()>=4){
                    String funcDefParamList = child.childrenNodes.get(2).m_symtabentry.toString();
                    String nodeParamList =node.childrenNodes.get(2).m_symtabentry.toString();
                    if(funcDefParamList.equals(nodeParamList)){

                    }else{
                        cntoverloaded ++;
                    }
                }
            }
        }
        if(cntoverloaded >0 ){
            p_error+="[warning] 9.2 Overloading member function "
                    + ((Token) node.childrenNodes.get(1).semanticConcept).getLexeme()
                    +", line "+((Token) node.childrenNodes.get(1).semanticConcept).getPosition()+"\n";
        }

        // 9.3 Overridden member function
        boolean overridefound = false;
        if(node.parentNode.childrenNodes.get(1).childrenNodes.size()!=0){
            String inheritedClassName = ((Token)node.parentNode.childrenNodes.get(1).childrenNodes.get(0).semanticConcept).getLexeme();
            if(!inheritedClassName.equals("none")){
                for(ASTNode childOfProgNode : node.parentNode.parentNode.childrenNodes){
                    String childofprognodeName = ((Token) childOfProgNode.childrenNodes.get(0).semanticConcept).getLexeme();
                    if(inheritedClassName.equals(childofprognodeName)){
                        for(ASTNode child2 : childOfProgNode.childrenNodes){
                            if(child2 instanceof MemberFuncDefNode){
                                if(child2.m_symtabentry.toString().equals(node.m_symtabentry.toString())){
                                    overridefound = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(overridefound == true){
            p_error+="[warning] 9.3 Overridding member function "
                    + ((Token) node.childrenNodes.get(1).semanticConcept).getLexeme()
                    +", line "+((Token) node.childrenNodes.get(1).semanticConcept).getPosition()+"\n";
        }
    }

    public void visit(MemberVarDeclNode node){
        // 8.3 Mulitply declared data member in class
        int cnt =0;
        for(ASTNode child : node.parentNode.childrenNodes){
            if(child instanceof MemberVarDeclNode){
                String nodememberName = ((Token) node.childrenNodes.get(1).semanticConcept).getLexeme();
                String nodecomparedmemberName = ((Token) child.childrenNodes.get(1).semanticConcept).getLexeme();
                if(nodememberName.equals(nodecomparedmemberName)){
                    cnt++;
                }
            }
        }
        if (cnt >1){
            p_error+="8.3 Mulitply declared data member in class: data "
                    + ((Token) node.childrenNodes.get(1).semanticConcept).getLexeme()
                    +", line "+((Token) node.childrenNodes.get(1).semanticConcept).getPosition()+"\n";
        }

        // 8.5 [warning] Shadowed inherited data member
        //check if it inherits
        String inheritedClassName = ((Token)node.parentNode.childrenNodes.get(1).childrenNodes.get(0).semanticConcept).getLexeme();
        if(!inheritedClassName.equals("none")){}
    }

    public void visit(InheritListNode node) {

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
    }

    public void visit(AddOpNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
    }

    public void visit(MultOpNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
    }

    public void visit(WriteNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
    }

    public void visit(NumNode node) {
        for (ASTNode child : node.childrenNodes) {
            child.accept(this);
        }
    }

    public void visit(IdNode node) {

    }

}
