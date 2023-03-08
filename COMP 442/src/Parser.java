import java.io.*;
import java.util.*;

public class Parser {

    private Map<String, ArrayList<TokenType>> followSet = new HashMap<>();
    private Map<String, ArrayList<TokenType>> firstSet = new HashMap<>();

    Map<String, String> hash = new HashMap();
    String output = "";
    boolean hasError = false;

    Stack<String> s1 = new Stack<>();
    private ArrayList<String> nullable = new ArrayList<>();
    private ArrayList<String> endable = new ArrayList<>();

    String filename = "";

    FileWriter ASTFileWriter;
    Stack<AST> ASTstack = new Stack();

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
            "epsilon"
    };


    public void Parser(String filetoRead) {

        filename = filetoRead;

        addFirstFollow();

        String table = "COMP 442/parsing/parsingTable.csv";
        String line = "";
        String commaDel = ",";

        try {
            this.ASTFileWriter = new FileWriter("COMP 442/input&output/" + filename + ".outast");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try (BufferedReader br = new BufferedReader(new FileReader(table))) {
            int row = 0;
            var headers = br.readLine().split(",");
            while ((line = br.readLine()) != null) {
                String[] values = line.split(commaDel);
                //nonvalues[0]);
                for (int col = 0; col < values.length; col++) {
                    hash.put(convertTerminals(values[0]) + "," + convertTerminals(headers[col]), values[col]);
                }

                row++;
            }
            System.out.println(hash);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean parse(PrintWriter pwError, Lexer lex) {
        try {
            //System.out.println("test");

            PrintWriter pwDerivations = new PrintWriter(new File("COMP 442/input&output/" + filename + ".outderivation"));

            s1.push("$");//
            s1.push("START");

            Token token = lex.getNextToken();
            Token previousToken = token;

            String top;
            String[] lookahead = new String[]{};

            //System.out.println(token);

            var line = "START";
            while (!s1.peek().equals("$") && !s1.peek().equals("eof")) {
                //System.out.println(s1);

                while (token.getTokenType() == TokenType.blockComment || token.getTokenType() == TokenType.inlineComment) {
                    previousToken = token;
                    token = lex.getNextToken();
                }
                top = s1.peek();

                if (top.equals("epsilon")) {
                    s1.pop();
                    top = s1.peek();
                }

                if (top.equals("$") || top.equals("eof")) {
                    System.out.println("end of file");
                    break;
                }

                while (top.startsWith("SACT")) {
                    switch (top) {
                        case "SACT0" -> this.makeNode(new Token("epsilon", TokenType.epsilon, token.getPosition()));
                        case "SACT1" -> this.makeNode(previousToken);
                        case "SACT2" -> this.makeNull();
                        case "SACT3" -> this.makeFamily("array Size");
                        case "SACT4" -> this.makeFamily("local var");
                        case "SACT5" -> this.makeFamily("class decl");
                        case "SACT6" -> this.makeFamily("memberVar decl");
                        case "SACT7" -> this.makeFamily("memberFunc decl");
                        case "SACT8" -> this.makeFamily("func def");
                        case "SACT9" -> this.makeFamily("inherit lst");
                        case "SACT10" -> this.makeFamily("func params");
                        case "SACT11" -> this.makeFamily("stat block");
                        case "SACT12"  -> this.makeFamily("scope res");
                        case "SACT13"  -> this.makeFamily("Prog");
                    }
                    s1.pop();
                    top = s1.peek();
                }

                // gets value of the key in the parsing table and stores it in templookahead for the not terminal
                var tempLookahead = hash.get(top + "," + token.getTokenType());


                if (tempLookahead != null) {
                    lookahead = tempLookahead.split("→")[1].trim().split(" ");

                } else {
                    lookahead = new String[]{};
                }

                //System.out.println( lookahead.length > 0);

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
                        hasError = true;
                        return false;

                    }

                } else if (lookahead.length > 0) {

                    var nT = s1.pop(); //pop nonterminal

                    Collections.reverse(Arrays.asList(lookahead));
                    //System.out.println(lookahead.length);
                    for (var i : lookahead) {
                        s1.push(convertTerminals(i));

                    }

                    Collections.reverse(Arrays.asList(lookahead));

                    if (nT == null) {
                        nT = "!";
                    }

                    var tempLine = "";

                    for (var i : lookahead) {
                        tempLine += " " + i;

                    }

                    line = line.replace(nT, tempLine).replace("&epsilon", "");
                    //write to file

                    output += "START => " + line + "\n";
                    pwDerivations.write(output);
                } else {
                    // handle error
                    // System.out.println(token.getTokenType().name());
                    System.out.println("Error:" + top);
                    skipError(token, pwError, lex);
                    hasError = true;
                    return false;

                }

            }

            pwError.close();
            pwDerivations.close();
        } catch (Exception e) {
            System.out.println("here");
        }
        //System.out.println(AST.treeToString());
        try {
            ASTFileWriter.write(this.printTree());
            ASTFileWriter.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public AST makeNull(){
        ASTstack.push(null);
        return null;
    };

    public AST makeNode(Token semanticConcept){
        AST node = new AST(null, null, semanticConcept,  0);
        ASTstack.push(node);
        return node;
    }

    public AST makeFamily(Object semanticConcept){
        ArrayList<AST> childrenNodes = new ArrayList<>();

        // if no childen left to pop
        while(ASTstack.peek() != null){
            // build the tree
            childrenNodes.add(ASTstack.pop());
        }
        ASTstack.pop();

        AST parentNode = new AST(null, childrenNodes, semanticConcept,  0);

        // set the parent node in each of the children nodes of a specific node
        for (var child: parentNode.childrenNodes){
            child.setParentNode(parentNode);
        }
        parentNode.fixTreeDepth();

        // stack requires reverse ordering
        Collections.reverse(childrenNodes);

        // add
        ASTstack.push(parentNode);

        return parentNode;
    }

    public String printTree(){
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

        }

        return "";
    }

    private Token skipError(Token lookahead, PrintWriter pwError, Lexer lex) {
        System.out.println("syntax error at " + lookahead.getPosition() + "\n");
        pwError.write("syntax error at " + lookahead.getPosition() + "\n");
        String top = s1.peek();
        Token token = lookahead;

        if (followSet.get(top).contains(token.getTokenType())) {
            s1.pop();
        } else {
            while (!firstSet.get(top).contains(token.getTokenType())
                    || (!(firstSet.get(top).contains("&epsilon")
                    && followSet.get(top).contains(token.getTokenType()))
            )) {
                token = lex.getNextToken();
            }
        }

        return token;
    }

    private void addFirstFollow() {
        String firstSetFile = "COMP 442/parsing/firstFollow.csv";
        String line = "";

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
                for (int i = 0; i < firstSplit.length; i++) {
                    if (firstSplit[i].contains("∅") || followSplit[i].contains("eof")) {
                        firstVal.add(TokenType.epsilon);
                    } else {
                        String valueToAdd = firstSplit[i].toUpperCase();
                        switch (valueToAdd) {
                            case "RPAR" -> firstVal.add(TokenType.closeBracketRound);

                            case "LPAR" -> firstVal.add(TokenType.openBracketRound);

                            case "RCURBR" -> firstVal.add(TokenType.closedBracketCurly);

                            case "LCURBR" -> firstVal.add(TokenType.openBracketCurly);

                            case "NEQ" -> firstVal.add(TokenType.not);

                            case "RSQBR" -> firstVal.add(TokenType.closeBracketSquare);

                            case "LSQBR" -> firstVal.add(TokenType.openBracketSquare);

                            case "FLOATLIT" -> firstVal.add(TokenType.floatType);

                            case "INTLIT" -> firstVal.add(TokenType.integerType);

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

                            case " " -> firstVal.add(TokenType.epsilon);

                            default -> firstVal.add(TokenType.valueOf(valueToAdd.toUpperCase()));

                        }

                    }
                }
                firstSet.put(key, firstVal);


                ArrayList<TokenType> followVal = new ArrayList<>();
                for (int i = 0; i < followSplit.length; i++) {
                    if (followSplit[i].contains("∅") || followSplit[i].contains("eof")) {
                        followVal.add(TokenType.epsilon);
                    } else {
                        String valueToAdd = followSplit[i].toUpperCase();
                        switch (valueToAdd) {
                            case "RPAR" -> followVal.add(TokenType.closeBracketRound);

                            case "LPAR" -> followVal.add(TokenType.openBracketRound);

                            case "RCURBR" -> followVal.add(TokenType.closedBracketCurly);

                            case "LCURBR" -> followVal.add(TokenType.openBracketCurly);

                            case "NEQ" -> followVal.add(TokenType.not);

                            case "RSQBR" -> followVal.add(TokenType.closeBracketSquare);

                            case "LSQBR" -> followVal.add(TokenType.openBracketSquare);

                            case "FLOATLIT" -> followVal.add(TokenType.floatType);

                            case "INTLIT" -> followVal.add(TokenType.integerType);

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

                            case "MULT" -> followVal.add(TokenType.multiply);

                            case "ATTRIBUTE" -> followVal.add(TokenType.attributeKeyWord);

                            case "CONSTRUCTORKEYWORD" -> followVal.add(TokenType.constructorKeyWord);

                            case "FUNCTION" -> followVal.add(TokenType.functionKeyWord);

                            case "LOCALVAR" -> followVal.add(TokenType.localvarKeyWord);

                            case "CLASS" -> followVal.add(TokenType.classKeyWord);

                            case "PRIVATE" -> followVal.add(TokenType.privateKeyWord);

                            case " " -> followVal.add(TokenType.epsilon);

                            default -> followVal.add(TokenType.valueOf(valueToAdd.toUpperCase()));
                        }
                    }
                    followSet.put(key, followVal);
                }
            }
        } catch (Exception e) {
        }
    }
}
