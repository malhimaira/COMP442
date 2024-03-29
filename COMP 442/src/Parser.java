import ASTNodes.*;
import LexerComponents.Token;
import LexerComponents.TokenType;
import SymbolTables.SymbolTable;

import java.io.*;
import java.util.*;

public class Parser {

    private Map<String, ArrayList<TokenType>> followSet = new HashMap<>();
    private Map<String, ArrayList<TokenType>> firstSet = new HashMap<>();

    Map<String, String> hash = new HashMap<>();
    String output = "";

    Stack<String> s1 = new Stack<>();
    private ArrayList<String> nullable = new ArrayList<>();
    private ArrayList<String> endable = new ArrayList<>();

    String filename = "";

    FileWriter ASTFileWriter;
    Stack<ASTNode> ASTstack = new Stack<>();

    String[] terminals = {
            "id",
            "integerType",
            "floatType",
            "doubleEqual",
            "angleBrackets",
            "lessThan",
            "greaterThan",
            "lessThanOrEqualTo",
            "greaterThanOrEqualTo",
            "plus",
            "minus",
            "multiply",
            "divide",
            "equals",
            "or",
            "and",
            "not",
            "openBracketRound",
            "closeBracketRound",
            "openBracketCurly",
            "closedBracketCurly",
            "openBracketSquare",
            "closeBracketSquare",
            "semicolon",
            "comma",
            "period",
            "colon",
            "lambdaExpression",
            "doubleColon",
            "integerKeyWord",
            "floatKeyWord",
            "voidKeyWord",
            "classKeyWord",
            "selfKeyWord",
            "isaKeyWord",
            "whileKeyWord",
            "ifKeyWord",
            "thenKeyWord",
            "elseKeyWord",
            "readKeyWord",
            "writeKeyWord",
            "returnKeyWord",
            "localvarKeyWord",
            "constructorKeyWord",
            "attributeKeyWord",
            "functionKeyWord",
            "publicKeyWord",
            "privateKeyWord",
            "blockComment",
            "inlineComment",
            "errorTokenId",
            "errorTokenNumber",
            "errorTokenChar",
            "errorTokenUnendingBlock",
            "epsilon",
            "EOF"
    };


    public void Parser(String filetoRead) {

        filename = filetoRead;

        addFirstFollow();

        String table = "COMP 442/parsing/parsingTable.csv";
        String line;
        String commaDel = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(table))) {
            int row = 0;
            var headers = br.readLine().split(",");
            while ((line = br.readLine()) != null) {
                String[] values = line.split(commaDel);
                for (int col = 0; col < values.length; col++) {
                    hash.put(convertTerminals(values[0]) + "," + convertTerminals(headers[col]), values[col]);
                }
                row++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean parse(PrintWriter pwError, Lexer lex) {
        try {
            PrintWriter pwDerivations = new PrintWriter("COMP 442/input&output/" + filename + ".outderivation");

            s1.push("$");
            s1.push("START");

            Token token = lex.getNextToken();
            Token previousToken = token;

            String top;
            String[] lookahead;

            var line = "START";
            while (!s1.peek().equals("$") && !s1.peek().equals("EOF")) {

                while (token.getTokenType() == TokenType.blockComment || token.getTokenType() == TokenType.inlineComment) {
                    previousToken = token;
                    token = lex.getNextToken();
                }
                top = s1.peek();

                if (top.equals("epsilon")) {
                    s1.pop();
                    top = s1.peek();
                }

                if (top.equals("$") || top.equals("EOF")) {
                    break;
                }

                while (top.startsWith("SACT")) {
                    switch (top) {
                        case "SACT1" -> this.makeNode(previousToken);
                        case "SACT2" -> this.makeNull();
                        case "SACT3" -> this.makeFamily(new ArraySizeNode(null,null, "array Size", 0));
                        case "SACT4" -> this.makeFamily(new VarDeclNode(null,null, "local var", 0));
                        case "SACT5" -> this.makeFamily(new ClassDeclNode(null,null, "class decl", 0));
                        case "SACT6" -> this.makeFamily(new MemberVarDeclNode(null,null, "memberVar decl", 0));
                        case "SACT7" -> this.makeFamily(new MemberFuncDefNode(null,null, "memberFunc decl", 0));
                        case "SACT8" -> this.makeFamily(new FuncDefNode(null,null, "func def", 0));
                        case "SACT9" -> this.makeFamily(new InheritListNode(null,null, "inherit lst", 0));
                        case "SACT10" -> this.makeFamily(new ParamListNode(null,null, "func params", 0));
                        case "SACT11" -> this.makeFamily(new StatBlockNode(null,null, "local var + stat block", 0));
                        case "SACT12" -> this.makeFamily("scope res");
                        case "SACT13" -> this.makeFamily(new StatNode(null,null, "stat", 0));
                        case "SACT14" -> this.makeFamily("if block");
                        case "SACT15" -> this.makeFamily("while block");
                        case "SACT16" -> this.makeFamily("read block");
                        case "SACT17" -> this.makeFamily(new WriteNode(null,null, "write block", 0));
                        case "SACT18" -> this.makeFamily("return stat");
                        case "SACT19" -> this.makeFamily("then block");
                        case "SACT20" -> this.makeFamily("else stat");
                        case "SACT21" -> this.makeFamily(new ProgNode(null,null, "prog", 0));
                        case "SACT22" -> this.makeFamily(new AddOpNode(null,null, "add op", 0));
                        case "SACT23" -> this.makeFamily(new AssignOpNode(null,null, "assign op", 0));
                        case "SACT24" -> this.makeFamily(new MultOpNode(null,null, "multi op", 0));
                        case "SACT25" -> this.makeFamily(new ArithmNode(null,null, "arithm expr", 0));
                        case "SACT26" -> this.makeFamily(new ExprNode(null,null, "expr", 0));
                        case "SACT27" -> this.makeFamily("factor");
                        case "SACT28" -> this.makeFamily(new IndiceNode(null,null, "indice", 0));
                        case "SACT29" -> this.makeFamily(new RelExprNode(null,null, "rel expr", 0));
                        case "SACT30" -> this.makeFamily(new NumNode(null,null, "num", 0));
                        case "SACT31" -> this.makeFamily(new IdNode(null,null, "id", 0));
                    }
                    s1.pop();
                    top = s1.peek();
                }
                if (top.equals("$") || top.equals("EOF")) {
                    break;
                }

                // gets value of the key in the parsing table and stores it in templookahead for the not terminal
                var tempLookahead = hash.get(top + "," + token.getTokenType());


                if (tempLookahead != null && !tempLookahead.equals("") ) {
                    lookahead = tempLookahead.split("→")[1].trim().split(" ");

                } else {
                    lookahead = new String[]{};
                }

                // terminal token case
                if (Arrays.asList(terminals).contains(top)) {
                    if (top.equals(token.getTokenType().name())) {

                        s1.pop();
                        previousToken = token;
                        token = lex.getNextToken();

                        while (token.getTokenType() == TokenType.blockComment || token.getTokenType() == TokenType.inlineComment) {
                            previousToken = token;
                            token = lex.getNextToken();
                        }
                    } else {

                        // handle error
                        skipError(token, pwError, lex);
                        token = lex.getNextToken();

                    }
                // non terminal token case found
                } else if (lookahead.length > 0) {

                    var nT = s1.pop(); //pop nonterminal

                    //push onto stack backwards
                    Collections.reverse(Arrays.asList(lookahead));
                    for (var i : lookahead) {
                        s1.push(convertTerminals(i));

                    }
                    // reverse again to print
                    Collections.reverse(Arrays.asList(lookahead));

                    // create string to print
                    if (nT == null) {
                        nT = "!";
                    }

                    var tempLine = "";

                    for (var i : lookahead) {
                        if(!i.contains("SACT")){
                            tempLine += " " + i;
                        }

                    }
                    String regexTempNT = "\\b"+nT+"\\b";
                    line = line.replaceAll(regexTempNT, tempLine).replace("&epsilon", "");

                    // append yo string for print
                    output += "START => " + line + "\n";

                } else {
                    skipError(token, pwError, lex);
                    token = lex.getNextToken();
                }

            }

            pwError.close();
            pwDerivations.write(output);
            pwDerivations.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public ASTNode makeNull() {
        ASTstack.push(null);
        return null;
    }

    public ASTNode makeNode(Token semanticConcept) {
        ASTNode node = new ASTNode(null, null, semanticConcept, 0);
        ASTstack.push(node);
        return node;
    }

    public ASTNode makeFamily(ASTNode parentNode) {
        ArrayList<ASTNode> childrenNodes = new ArrayList<>();

        // if no childen left to pop
        while (ASTstack.peek() != null) {
            // build the tree
            childrenNodes.add(ASTstack.pop());
        }
        ASTstack.pop();

       parentNode.setChildrenNodes(childrenNodes);

        // set the parent node in each of the children nodes of a specific node
        for (var child : parentNode.childrenNodes) {
            child.setParentNode(parentNode);
        }
        parentNode.fixTreeDepth();

        // stack requires reverse ordering
        Collections.reverse(childrenNodes);

        // add
        ASTstack.push(parentNode);

        return parentNode;
    }

    public ASTNode makeFamily(Object semanticConcept) {
        ArrayList<ASTNode> childrenNodes = new ArrayList<>();

        // if no childen left to pop
        while (ASTstack.peek() != null) {
            // build the tree
            childrenNodes.add(ASTstack.pop());
        }
        ASTstack.pop();

        ASTNode parentNode = new ASTNode(null, childrenNodes, semanticConcept, 0);

        // set the parent node in each of the children nodes of a specific node
        for (var child : parentNode.childrenNodes) {
            child.setParentNode(parentNode);
        }
        parentNode.fixTreeDepth();

        // stack requires reverse ordering
        Collections.reverse(childrenNodes);

        // add
        ASTstack.push(parentNode);

        return parentNode;
    }

    public void writeASTTreeToFile(){
        // Generate ASTNodes.AST File
        try {
            this.ASTFileWriter = new FileWriter("COMP 442/input&output/" + filename + ".outast");
            ASTFileWriter.write(this.printTree());
            ASTFileWriter.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public String printTree() {
        return ASTstack.toString();
    }

    public String convertTerminals(String value) {
        try {
            value = value.toUpperCase();
            switch (value) {
                case "COLON" -> {
                    return "colon";
                }
                case "RPAR" -> {
                    return "closeBracketRound";
                }
                case "LPAR" -> {
                    return "openBracketRound";
                }
                case "RCURBR" -> {
                    return "closedBracketCurly";
                }
                case "LCURBR" -> {
                    return "openBracketCurly";
                }
                case "NEQ" -> {
                    return "angleBrackets";
                }
                case "NOT" -> {
                    return "not";
                }
                case "RSQBR" -> {
                    return "closeBracketSquare";
                }
                case "LSQBR" -> {
                    return "openBracketSquare";
                }
                case "FLOATLIT" -> {
                    return "floatType";
                }
                case "INTLIT" -> {
                    return "integerType";
                }
                case "ARROW" -> {
                    return "lambdaExpression";
                }
                case "EQUAL" -> {
                    return "equals";
                }
                case "SR" -> {
                    return "doubleColon";
                }
                case "PUBLIC" -> {
                    return "publicKeyWord";
                }
                case "DOT" -> {
                    return "period";
                }
                case "SEMI" -> {
                    return "semicolon";
                }
                case "RETURN" -> {
                    return "returnKeyWord";
                }
                case "WRITE" -> {
                    return "writeKeyWord";
                }
                case "READ" -> {
                    return "readKeyWord";
                }
                case "WHILE" -> {
                    return "whileKeyWord";
                }
                case "ELSE" -> {
                    return "elseKeyWord";
                }
                case "THEN" -> {
                    return "thenKeyWord";
                }
                case "IF" -> {
                    return "ifKeyWord";
                }
                case "MINUS" -> {
                    return "minus";
                }
                case "PLUS" -> {
                    return "plus";
                }
                case "VOID" -> {
                    return "voidKeyWord";
                }
                case "GEQ" -> {
                    return "greaterThanOrEqualTo";
                }
                case "LEQ" -> {
                    return "lessThanOrEqualTo";
                }
                case "GT" -> {
                    return "greaterThan";
                }
                case "LT" -> {
                    return "lessThan";
                }
                case "EQ" -> {
                    return "equals";
                }
                case "ISA" -> {
                    return "isaKeyWord";
                }
                case "DIV" -> {
                    return "divide";
                }
                case "MULT" -> {
                    return "multiply";
                }
                case "ATTRIBUTE" -> {
                    return "attributeKeyWord";
                }
                case "CONSTRUCTORKEYWORD" -> {
                    return "constructorKeyWord";
                }
                case "FUNCTION" -> {
                    return "functionKeyWord";
                }
                case "LOCALVAR" -> {
                    return "localvarKeyWord";
                }
                case "CLASS" -> {
                    return "classKeyWord";
                }
                case "PRIVATE" -> {
                    return "privateKeyWord";
                }
                case "&EPSILON" -> {
                    return "epsilon";
                }
                case "INTEGER" -> {
                    return "integerKeyWord";
                }
                case "FLOAT" -> {
                    return "floatKeyWord";
                }
                case "COMMA" -> {
                    return "comma";
                }
                case "ID" -> {
                    return "id";
                }
                default -> {
                    return value;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "";
    }

    private Token skipError(Token lookahead, PrintWriter pwError, Lexer lex) {
        pwError.write("Syntax Error : " + lookahead.getTokenType() +" token on line "+ lookahead.getPosition() + "\n");
        String top = s1.peek();
        if (followSet.get(top).contains(lookahead.getTokenType())) {
            s1.pop();
        }
        return lookahead;
    }

    private void addFirstFollow() {
        String firstSetFile = "COMP 442/parsing/firstFollow.csv";
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(firstSetFile))) {
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] initialSplit = line.split(",");
                String key = initialSplit[0];
                if (initialSplit[3].contains("yes")) {
                    nullable.add(key.toUpperCase());
                }
                if (initialSplit[3].contains("no")) {
                    endable.add(key.toUpperCase());
                }
                String[] firstSplit = initialSplit[1].split(" ");
                String[] followSplit = initialSplit[2].split(" ");

                ArrayList<TokenType> firstVal = new ArrayList<>();
                for (int i = 0; i < firstSplit.length & i < followSplit.length; i++) {
                    if (firstSplit[i].contains("∅") || followSplit[i].contains("EOF")) {
                        firstVal.add(TokenType.epsilon);
                    } else {
                        String valueToAdd = firstSplit[i].toUpperCase();
                        switch (valueToAdd) {
                            case "RPAR" -> firstVal.add(TokenType.closeBracketRound);

                            case "LPAR" -> firstVal.add(TokenType.openBracketRound);

                            case "RCURBR" -> firstVal.add(TokenType.closedBracketCurly);

                            case "LCURBR" -> firstVal.add(TokenType.openBracketCurly);

                            case "NEQ" -> firstVal.add(TokenType.angleBrackets);

                            case "NOT" -> firstVal.add(TokenType.not);

                            case "RSQBR" -> firstVal.add(TokenType.closeBracketSquare);

                            case "LSQBR" -> firstVal.add(TokenType.openBracketSquare);

                            case "FLOATLIT" -> firstVal.add(TokenType.floatType);

                            case "INTLIT" -> firstVal.add(TokenType.integerType);

                            case "INTEGER" -> firstVal.add(TokenType.integerKeyWord);

                            case "FLOAT" -> firstVal.add(TokenType.floatKeyWord);

                            case "ARROW" -> firstVal.add(TokenType.lambdaExpression);

                            case "EQUAL" -> firstVal.add(TokenType.equals);

                            case "SR" -> firstVal.add(TokenType.doubleColon);

                            case "PUBLIC" -> firstVal.add(TokenType.publicKeyWord);

                            case "DOT" -> firstVal.add(TokenType.period);

                            case "SEMI" -> firstVal.add(TokenType.semicolon);

                            case "RETURN" -> firstVal.add(TokenType.returnKeyWord);

                            case "WRITE" -> firstVal.add(TokenType.writeKeyWord);

                            case "READ" -> firstVal.add(TokenType.readKeyWord);

                            case "WHILE" -> firstVal.add(TokenType.whileKeyWord);

                            case "ELSE" -> firstVal.add(TokenType.elseKeyWord);

                            case "THEN" -> firstVal.add(TokenType.thenKeyWord);

                            case "IF" -> firstVal.add(TokenType.ifKeyWord);

                            case "MINUS" -> firstVal.add(TokenType.minus);

                            case "PLUS" -> firstVal.add(TokenType.plus);

                            case "VOID" -> firstVal.add(TokenType.voidKeyWord);

                            case "GEQ" -> firstVal.add(TokenType.greaterThan);

                            case "LEQ" -> firstVal.add(TokenType.lessThan);

                            case "GT" -> firstVal.add(TokenType.greaterThan);

                            case "LT" -> firstVal.add(TokenType.lessThan);

                            case "EQ" -> firstVal.add(TokenType.equals);

                            case "ISA" -> firstVal.add(TokenType.isaKeyWord);

                            case "DIV" -> firstVal.add(TokenType.divide);

                            case "MULT" -> firstVal.add(TokenType.multiply);

                            case "ATTRIBUTE" -> firstVal.add(TokenType.attributeKeyWord);

                            case "CONSTRUCTORKEYWORD" -> firstVal.add(TokenType.constructorKeyWord);

                            case "FUNCTION" -> firstVal.add(TokenType.functionKeyWord);

                            case "LOCALVAR" -> firstVal.add(TokenType.localvarKeyWord);

                            case "CLASS" -> firstVal.add(TokenType.classKeyWord);

                            case "PRIVATE" -> firstVal.add(TokenType.privateKeyWord);

                            case "OR" -> firstVal.add(TokenType.or);

                            case "AND" -> firstVal.add(TokenType.and);

                            case "ID" -> firstVal.add(TokenType.id);

                            case "COMMA" -> firstVal.add(TokenType.comma);

                            case "COLON" -> firstVal.add(TokenType.colon);

                            case " " -> firstVal.add(TokenType.epsilon);

                            default -> firstVal.add(TokenType.valueOf(valueToAdd.toUpperCase()));

                        }

                    }
                }
                firstSet.put(key, firstVal);

                ArrayList<TokenType> followVal = new ArrayList<>();
                for (String s : followSplit) {
                    if (s.contains("∅") || s.contains("EOF")) {
                        followVal.add(TokenType.epsilon);
                    } else {
                        String valueToAdd = s.toUpperCase();
                        switch (valueToAdd) {
                            case "RPAR" -> followVal.add(TokenType.closeBracketRound);

                            case "LPAR" -> followVal.add(TokenType.openBracketRound);

                            case "RCURBR" -> followVal.add(TokenType.closedBracketCurly);

                            case "LCURBR" -> followVal.add(TokenType.openBracketCurly);

                            case "NEQ" -> followVal.add(TokenType.angleBrackets);

                            case "NOT" -> followVal.add(TokenType.not);

                            case "RSQBR" -> followVal.add(TokenType.closeBracketSquare);

                            case "LSQBR" -> followVal.add(TokenType.openBracketSquare);

                            case "FLOATLIT" -> followVal.add(TokenType.floatType);

                            case "INTLIT" -> followVal.add(TokenType.integerType);

                            case "INTEGER" -> followVal.add(TokenType.integerKeyWord);

                            case "FLOAT" -> followVal.add(TokenType.floatKeyWord);

                            case "ARROW" -> followVal.add(TokenType.lambdaExpression);

                            case "EQUAL" -> followVal.add(TokenType.equals);

                            case "SR" -> followVal.add(TokenType.doubleColon);

                            case "PUBLIC" -> followVal.add(TokenType.publicKeyWord);

                            case "DOT" -> followVal.add(TokenType.period);

                            case "SEMI" -> followVal.add(TokenType.semicolon);

                            case "RETURN" -> followVal.add(TokenType.returnKeyWord);

                            case "WRITE" -> followVal.add(TokenType.writeKeyWord);

                            case "READ" -> followVal.add(TokenType.readKeyWord);

                            case "WHILE" -> followVal.add(TokenType.whileKeyWord);

                            case "ELSE" -> followVal.add(TokenType.elseKeyWord);

                            case "THEN" -> followVal.add(TokenType.thenKeyWord);

                            case "IF" -> followVal.add(TokenType.ifKeyWord);

                            case "MINUS" -> followVal.add(TokenType.minus);

                            case "PLUS" -> followVal.add(TokenType.plus);

                            case "VOID" -> followVal.add(TokenType.voidKeyWord);

                            case "GEQ" -> followVal.add(TokenType.greaterThanOrEqualTo);

                            case "LEQ" -> followVal.add(TokenType.lessThanOrEqualTo);

                            case "GT" -> followVal.add(TokenType.greaterThan);

                            case "LT" -> followVal.add(TokenType.lessThan);

                            case "EQ" -> followVal.add(TokenType.equals);

                            case "ISA" -> followVal.add(TokenType.isaKeyWord);

                            case "DIV" -> followVal.add(TokenType.divide);

                            case "COMMA" -> followVal.add(TokenType.comma);

                            case "MULT" -> followVal.add(TokenType.multiply);

                            case "ATTRIBUTE" -> followVal.add(TokenType.attributeKeyWord);

                            case "CONSTRUCTORKEYWORD" -> followVal.add(TokenType.constructorKeyWord);

                            case "FUNCTION" -> followVal.add(TokenType.functionKeyWord);

                            case "LOCALVAR" -> followVal.add(TokenType.localvarKeyWord);

                            case "CLASS" -> followVal.add(TokenType.classKeyWord);

                            case "OR" -> followVal.add(TokenType.or);

                            case "AND" -> followVal.add(TokenType.and);

                            case "ID" -> followVal.add(TokenType.id);

                            case "COLON" -> followVal.add(TokenType.colon);

                            case "PRIVATE" -> followVal.add(TokenType.privateKeyWord);

                            case " " -> followVal.add(TokenType.epsilon);

                            default -> followVal.add(TokenType.valueOf(valueToAdd.toUpperCase()));
                        }
                    }
                    followSet.put(key, followVal);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
