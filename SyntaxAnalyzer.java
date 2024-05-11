import java.util.List;

import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.demo.TextInBox;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;

/**
 TEST case1 : printf("Hello World");

 TEST case2 : int x = 10;

 Test case3 : int main() {
                int x = 10;
              }



program : declaration_list 

declaration_list → declaration declaration_list | ε


declaration : var_declaration
            | fun_declaration
            | struct_declaration
            | enum_declaration
            | typedef_declaration

var_declaration : type_specifier ID ';'
                | type_specifier ID '[' expression ']' ';'
                | type_specifier pointer ID ';'
                | type_specifier pointer ID '[' expression ']' ';'

pointer : '*' | pointer '*'

type_specifier : int
               | void
               | char
               | float
               | double
               | struct ID
               | enum ID
               | typedef_name

typedef_declaration : typedef type_specifier ID ';'
                    | typedef type_specifier ID '[' expression ']' ';'
                    | typedef type_specifier pointer ID ';'
                    | typedef type_specifier pointer ID '[' expression ']' ';'

fun_declaration : type_specifier ID '(' params ')' compound_stmt

params : param_list
       | void

param_list : param_list ',' param
           | param

param : type_specifier ID
      | type_specifier ID '[' ']'

struct_declaration : struct ID '{' struct_var_declaration_list '}' ';'

struct_var_declaration_list : struct_var_declaration_list var_declaration
                            | var_declaration

enum_declaration : enum ID '{' enum_var_list '}' ';'

enum_var_list : enum_var_list ',' ID
              | ID

compound_stmt : '{'statement_list '}'

local_declarations : local_declarations var_declaration| local_declarations enum_declaration |
                   | empty

statement_list : statement_list statement
               | empty

statement : local_declarations 
          | expression_stmt
          | compound_stmt
          | selection_stmt
          | iteration_stmt
          | return_stmt

expression_stmt : expression ';'
                | ';'

selection_stmt : if '(' expression ')' statement
               | if '(' expression ')' statement else statement

iteration_stmt : while '(' expression ')' statement
               | for '(' expression_stmt expression ';' expression ')' statement
               | do statement while '(' expression ')' ';'

return_stmt : return ';'
            | return expression ';'

expression : assignment_expression

assignment_expression : conditional_expression
                       | unary_expression assignment_operator assignment_expression

assignment_operator : '='

conditional_expression : logical_or_expression
                        | logical_or_expression '?' expression ':' conditional_expression

logical_or_expression : logical_and_expression
                      | logical_or_expression '||' logical_and_expression

logical_and_expression : inclusive_or_expression
                       | logical_and_expression '&&' inclusive_or_expression

inclusive_or_expression : exclusive_or_expression
                         | inclusive_or_expression '|' exclusive_or_expression

exclusive_or_expression : and_expression
                         | exclusive_or_expression '^' and_expression

and_expression : equality_expression
               | and_expression '&' equality_expression

equality_expression : relational_expression
                    | equality_expression '==' relational_expression
                    | equality_expression '!=' relational_expression

relational_expression : shift_expression
                       | relational_expression '<' shift_expression
                       | relational_expression '>' shift_expression
                       | relational_expression '<=' shift_expression
                       | relational_expression '>=' shift_expression

shift_expression : additive_expression
                  | shift_expression '<<' additive_expression
                  | shift_expression '>>' additive_expression

additive_expression : multiplicative_expression
                     | additive_expression '+' multiplicative_expression
                     | additive_expression '-' multiplicative_expression

multiplicative_expression : cast_expression
                           | multiplicative_expression '*' cast_expression
                           | multiplicative_expression '/' cast_expression
                           | multiplicative_expression '%' cast_expression

cast_expression : unary_expression
                | '(' type_name ')' cast_expression

unary_expression : postfix_expression
                 | '++' unary_expression
                 | '--' unary_expression
                 | unary_operator cast_expression
                 | sizeof unary_expression
                 | sizeof '(' type_name ')'

unary_operator : '&'
               | '*'
               | '+'
               | '-'
               | '~'
               | '!'

postfix_expression : primary_expression
                    | postfix_expression '[' expression ']'
                    | postfix_expression '(' argument_expression_list ')'
                    | postfix_expression '.' ID
                    | postfix_expression '->' ID
                    | postfix_expression '++'
                    | postfix_expression '--'
                    | ID '(' argument_expression_list ')'

primary_expression : ID
                    | constant
                    | string
                    | '(' expression ')'

constant : NUM

string : STRING_LITERAL

argument_expression_list : assignment_expression
                         | argument_expression_list ',' assignment_expression


  

 **/
import org.abego.treelayout.demo.TextInBoxNodeExtentProvider;
import org.abego.treelayout.demo.swing.TextInBoxTreePane;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;

import javax.swing.*;
import java.awt.*;


public class SyntaxAnalyzer {
    public String TYPE_SPECIFIER = "Keyword (int|float|char|double|short|long|signed|unsigned|void|String|bool|struct|enum|typedef)";
    // public String KEYWORD = "Keyword *";
    public String IDENTIFIER = "Identifier .*"; 
    public String NUM = "Integer| Float .*";
    

    private int currentTokenIndex;
    private List<LexicalAnalyzer.Token> tokens;


    private TextInBox root = new TextInBox("Program",80,20);

    DefaultTreeForTreeLayout<TextInBox> tree = new DefaultTreeForTreeLayout<>(root);
    



    boolean inStatement = false;
    boolean inParenthesis = false;
    boolean noarrayassign = false;
    boolean arrayretract = false;

    public SyntaxAnalyzer(List<LexicalAnalyzer.Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;

    }


    
    private void retract() {
        currentTokenIndex--;
    }


    private boolean match(String expected) {
       
     
        if (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).type.matches(expected)) {
            System.out.println("Current token index: " + currentTokenIndex);

            System.out.println("Matched: " + tokens.get(currentTokenIndex).type + " " + tokens.get(currentTokenIndex).value);
            return true;
        } else {
            return false;
            
        }
    }


    private static void showInDialog(JComponent panel) {
        // Create a JScrollPane and add the panel to it
        JScrollPane scrollPane = new JScrollPane(panel);

        // Set empty border around the scroll pane
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a dialog and set its content pane to the scroll pane
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Pack and set dialog properties
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public void addChild(TextInBox parent,String childname)
    {
        tree.addChild(root,new TextInBox(childname,80,20));
    }



    public void advance() {
        if (currentTokenIndex >= tokens.size()-1) {
            System.out.println("End of tokens"+ currentTokenIndex);
            return;
            
        }
        this.currentTokenIndex++;

        System.out.println("Advancing to: " + tokens.get(currentTokenIndex).type + " " + tokens.get(currentTokenIndex).value);
    }

    private String getTokenData()
    {
        return tokens.get(currentTokenIndex).value;
    }



    public void parse () {
        program();
    }


      private void program() {
        declaration_list();        
         tree.addChild(root,new TextInBox("EOF",80,20));


        // Setup the tree layout configuration
        double gapBetweenLevels = 50;
        double gapBetweenNodes = 10;
        DefaultConfiguration<TextInBox> configuration = new DefaultConfiguration<>(gapBetweenLevels, gapBetweenNodes);

        // Create the NodeExtentProvider for TextInBox nodes
        TextInBoxNodeExtentProvider nodeExtentProvider = new TextInBoxNodeExtentProvider();

        // Create the layout
        TreeLayout<TextInBox> treeLayout = new TreeLayout<>(tree, nodeExtentProvider, configuration);

        // Create a panel that draws the nodes and edges
        TextInBoxTreePane panel = new TextInBoxTreePane(treeLayout);

        // Show the panel in a dialog
        showInDialog(panel);

        System.out.println("Parsing complete.");

    }



    private void declaration_list() {

        if (match(TYPE_SPECIFIER) ) {
            
            declaration(root);
            declaration_list();

        } else {
            return;
        }
     
    }

    private void declaration(TextInBox decnode) {

        TextInBox decl = new TextInBox("decleration",80,20);
        tree.addChild(decnode,decl);

        TextInBox keywordnode = new TextInBox("Keyword",80,20);


        if (match(TYPE_SPECIFIER) && !tokens.get(currentTokenIndex).value.equals("struct") && !tokens.get(currentTokenIndex).value.equals("enum") && !tokens.get(currentTokenIndex).value.equals("typedef") ) {
            TextInBox typespecifier = new TextInBox("Type Specifier",80,20);
            tree.addChild(decl,typespecifier);
            tree.addChild(typespecifier,new TextInBox(getTokenData(),80,20));

            advance();
          if (match(IDENTIFIER)) {


                advance();
              if (match("Delimiter \\[")) {

                TextInBox arraydec = new TextInBox("Array declaration",80,20);
                tree.addChild(decl,arraydec);
                tree.addChild(arraydec,new TextInBox(getTokenData(),80,20));
                    advance();
                    // array declaration       
                } else if (match("Operator \\*")) {

                    while (match("Operator \\*")) {
                        advance();                       
                    }

                    // pointer declaration

                } else if (match("Delimiter \\(")) {
                    System.out.println("Function declaration in var dec");
                    retract();
                    fun_declaration(decl);
             
                }else {


                    retract();  
                    var_declaration(decl);
                    

                    //var_declaration();
                  
                }
            }

        } else if (match("Keyword struct") && tokens.get(currentTokenIndex).value.equals("struct")) {
            tree.addChild(decl,keywordnode);
            TextInBox structkeyword = new TextInBox(getTokenData(),80,20);
            tree.addChild(keywordnode,structkeyword);


            advance();
            struct_declaration();
        } else if (match("Keyword enum")  && tokens.get(currentTokenIndex).value.equals("enum") ) {
            tree.addChild(decl,keywordnode);
            TextInBox enumkeyword = new TextInBox(getTokenData(),80,20);
            tree.addChild(keywordnode,enumkeyword); // ENUM KEYWORD

            System.out.println("Enum declaration");
            advance();
            enum_declaration();
        } else if (match("Keyword typedef") && tokens.get(currentTokenIndex).value.equals("typedef")) {
            advance();
            // typedef declaration
        }

        else {
            System.out.println("Invalid Declaration");
            return;
        }
  
    }
    


    private void struct_declaration() {
        
            if (match(IDENTIFIER)) {
                advance();
                System.out.println("Strussdsdct i am her");

                if (match("Delimiter \\{")) {
                    System.out.println("Struct i am her");
                    advance();
                    struct_var_declaration_list();
                    if (match("Delimiter \\}")) {
                        advance();
                        if (match("Delimiter ;")) {
                            advance();
                            System.out.println("Struct declaration");
                        } else {
                            System.out.println("Syntax Error missing ;");
                            System.exit(1); // Exit with error code 1

                        }
                    } else {
                        System.out.println("Syntax Error missing }");
                        System.exit(1); // Exit with error code 1

                    }
                } else {
                    System.out.println("Syntax Error missing {");
                    System.exit(1); // Exit with error code 1

                }
            } else {
                System.out.println("Syntax Error missing identifier");
                System.exit(1); // Exit with error code 1

            }
        
    }

    private void fun_declaration(TextInBox parentNode) {
        TextInBox funnode = new TextInBox("Function Declaration",80,20);

        tree.addChild(parentNode,funnode);

      if (match(IDENTIFIER)) {
                TextInBox idtoken = new TextInBox("Identifier",80,20);
                tree.addChild(funnode,idtoken);
                tree.addChild(idtoken,new TextInBox(getTokenData(),80,20));
                advance();
                if (match("Delimiter \\(")) {

                    tree.addChild(funnode,new TextInBox(getTokenData(),80,20));
                    advance();
                   params(parentNode);
                    if (match("Delimiter \\)")) {
                        
                        tree.addChild(funnode,new TextInBox(getTokenData(),80,20));
                        advance();
                        compound_stmt(funnode);
                    } else {
                        System.out.println("Syntax Error missing )");
                        System.exit(1); // Exit with error code 1

                    }
                } else {
                    System.out.println("Syntax Error missing (");
                    System.exit(1); // Exit with error code 1

                }
            } else {
                System.out.println("Syntax Error missing identifier");
                System.exit(1); // Exit with error code 1

            }

        
    }

    private void params(TextInBox parentNode) {
        if (match(TYPE_SPECIFIER)) {

          
            param_list(parentNode);
        } else if (match("Keyword void")) {
            advance();
        } else {
            System.out.println("Syntax Error missing type specifier");
        }
    }

    private void param_list(TextInBox parentNode) {
        param(parentNode);
        if (match("Delimiter ,")) {
            advance();
            param_list(parentNode);
        } else {
            return;
        }
    }

    private void param(TextInBox parentNode) {
        if (match(TYPE_SPECIFIER)) {

            TextInBox typespecifier = new TextInBox("Type Specifier",80,20);
            tree.addChild(parentNode,typespecifier);
            tree.addChild(typespecifier,new TextInBox(getTokenData(),80,20));


            advance();
            if (match(IDENTIFIER)) {
                TextInBox idtoken = new TextInBox("Identifier",80,20);
                tree.addChild(parentNode,idtoken);
                tree.addChild(idtoken,new TextInBox(getTokenData(),80,20));

                advance();
                if (match("Delimiter \\[")) {
                    tree.addChild(parentNode,new TextInBox(getTokenData(),80,20));
                    advance();
                    if (match("Delimiter \\]")) {
                        tree.addChild(parentNode,new TextInBox(getTokenData(),80,20));
                        advance();
                    } else {
                        System.out.println("Syntax Error missing ]");
                        System.exit(1); // Exit with error code 1

                    }
                }
            } else {
                System.out.println("Syntax Error missing identifier");
                System.exit(1); // Exit with error code 1

            }
        } else {
            System.out.println("Syntax Error missing type specifier");
            System.exit(1); // Exit with error code 1

        }
    }

    private void compound_stmt(TextInBox parentNode) {
        TextInBox compoundstmt = new TextInBox("Compound Statement",80,20);
        tree.addChild(parentNode,compoundstmt);
        if (match("Delimiter \\{")) {
            tree.addChild(compoundstmt, new TextInBox(getTokenData(),80,20));
            System.out.println("Compound statement");
            advance();
          //  statement_list(compoundstmt);
            if (match("Delimiter \\}")) {
                tree.addChild(compoundstmt, new TextInBox(getTokenData(),80,20));


                advance();
                System.out.println("Compound statement done");
            } else {
                System.err.println("Syntax Error missing }");
            }
        } else {
            System.err.println("Syntax Error missing {");
            System.exit(1); // Exit with error code 1

        }
    }

    private void statement_list(TextInBox parentNode)  {
        if (match(TYPE_SPECIFIER) || match(IDENTIFIER) || match("Keyword if") || match("Keyword while") || match("Keyword for") || match("Keyword do") || match("Keyword return") || match("Delimiter \\{" )) {
            System.out.println("---------------------------------------------------------------------------Statement list");
            TextInBox stmtlist = new TextInBox("Statement List",80,20);
            tree.addChild(parentNode,stmtlist);
            statement(stmtlist);
            statement_list(parentNode);
        } else {
            return;
        }
    }

    private void statement(TextInBox parentNode) {
        TextInBox stament = new TextInBox("Statement",80,20);
        tree.addChild(parentNode,stament);
        if (match(TYPE_SPECIFIER)) {
           
            TextInBox typespecifier = new TextInBox("Type Specifier",80,20);
            tree.addChild(stament,typespecifier);
            tree.addChild(typespecifier,new TextInBox(getTokenData(),80,20));

            advance();
            if (match(IDENTIFIER)){
            var_declaration(stament);
            } else {
                System.err.println("Syntax Error missing identifier");
            }
        } else if (match(IDENTIFIER)) {
            expression_stmt(stament);
        } else if (match("Keyword if")) {
            System.out.println("Selection statement entered");
            selection_stmt(stament);
        } else if (match("Keyword while") || match("Keyword for") || match("Keyword do")) {
            System.err.println("Iteration statement entered");
            iteration_stmt(stament);
        } else if (match("Keyword return")) {
            return_statement(parentNode);
        } else if (match("Delimiter \\{")) {
            compound_stmt(parentNode);
        } else {
            System.err.println("Syntax Error missing statement");
            System.exit(1); // Exit with error code 1

        }
    }

    private void expression_stmt(TextInBox parentNode) {


       if (match(IDENTIFIER))
       {
        TextInBox expressionstmt = new TextInBox("Expression Stmt",80,20);
        tree.addChild(parentNode,expressionstmt);
        TextInBox idtoken = new TextInBox("Identifier",80,20);
        tree.addChild(expressionstmt,idtoken);
        tree.addChild(idtoken,new TextInBox(getTokenData(),80,20));

        advance();  

        if (match("Operator =")) {
            advance();
            expression(expressionstmt);
            
        }else if (match("Delimiter \\[")) {
            advance();
            expression(expressionstmt);
            if (match("Delimiter \\]")) {
                advance();
            } else {
                System.out.println("Syntax Error missing ]");
            }
        } else if (match("Delimiter \\(")) {
            advance();
            args_opt(expressionstmt);
            if (match("Delimiter \\)")) {
                advance();
            } else {
                System.out.println("Syntax Error missing )");
                System.exit(1);
            }
        } else if (match("Operator \\+\\+") || match("Operator --")) {
            advance();
        } else if (match("Delimiter \\.")) {
            advance();
            if (match(IDENTIFIER)) {
                advance();
            } else {
                System.out.println("Syntax Error missing identifier");
            }
        } else if (match("Operator ->")) {
            advance();
            if (match(IDENTIFIER)) {
                advance();
            } else {
                System.out.println("Syntax Error missing identifier");
            }
        } else if (match("Operator \\+\\+")) {
            advance();
        } else if (match("Operator --")) {
            advance();
        }  else {
            System.out.println("Syntax Error missing expression");
            System.exit(1);
            
        }

        if (match("Delimiter ;")) {
            advance();
        } else {
            System.out.println("Syntax Error missing ;");
            System.exit(1);
        }
       }
    }

    private void selection_stmt(TextInBox parentNode) {
        if (match("Keyword if")) {
            TextInBox ifstmt = new TextInBox("if Statement",100,20);
            tree.addChild(parentNode,ifstmt);

            tree.addChild(ifstmt,new TextInBox(getTokenData(),100,20));
    
            advance();
            if (match("Delimiter \\(")) {

                tree.addChild(ifstmt,new TextInBox(getTokenData(),100,20));
                advance();
                expression(ifstmt);
                if (match("Delimiter \\)")) {
                    tree.addChild(ifstmt,new TextInBox(getTokenData(),100,20));
                    advance();
                    statement(ifstmt);
                    if (match("Keyword else")) {
                        tree.addChild(ifstmt,new TextInBox(getTokenData(),100,20));
                        advance();
                        statement(ifstmt);
                    }
                } else {
                     System.err.println("Syntax Error missing )");
                }
            } else {
                 System.err.println("Syntax Error missing (");
            }
        } else {
             System.err.println("Syntax Error missing if");
        }
    }


    private void iteration_stmt(TextInBox parentNode) {
        if (match("Keyword while")) {
            advance();
            if (match("Delimiter \\(")) {
                advance();
                //expression();
                if (match("Delimiter \\)")) {
                    advance();
                    //statement();
                } else {
                    System.out.println("Syntax Error missing )");
                }
            } else {
                System.out.println("Syntax Error missing (");
            }
        } else if (match("Keyword for")) {
            TextInBox forstmt = new TextInBox("For Statement",80,20);
            tree.addChild(parentNode,forstmt);
            tree.addChild(forstmt,new TextInBox(getTokenData(),80,20));
            advance();
            if (match("Delimiter \\(")) {
                tree.addChild(forstmt,new TextInBox(getTokenData(),80,20));

                advance();

                if (match(TYPE_SPECIFIER)){

                    tree.addChild(forstmt,new TextInBox(getTokenData(),80,20));

                    advance();
                    if (match(IDENTIFIER)){

                        tree.addChild(forstmt,new TextInBox(getTokenData(),80,20));
                        advance();

                       // var_declaration();
                        retract();
                    } else {
                        System.err.println("Syntax Error missing identifier");
                        System.exit(1); // Exit with error code 1

                        return;

                    }

                }else {
                    System.err.println("Syntax Error missing type specifier");
                    System.exit(1); // Exit with error code 1

                    return;

                }

               
                if (match("Delimiter ;")) {
                    tree.addChild(forstmt,new TextInBox(getTokenData(),80,20));
                    advance();
                    expression(forstmt);

                    if (match("Delimiter ;")) {
                        tree.addChild(forstmt,new TextInBox(getTokenData(),80,20));
                        advance();
                        expression(forstmt);
                    } else {
                        System.out.println("Syntax Error missing ;");
                    }

                    if (match("Delimiter \\)")) {
                        System.out.println("For loop done");
                        advance();
                        statement(forstmt);
                    } else {
                        System.err.println("Syntax error: Expected ')' after for loop condition" );
                        
                    }

                } else {
                    System.out.println("Syntax Error missing ;");
                }
            } else {
                System.out.println("Syntax Error missing (");
            }
        } else if (match("Keyword do")) {
            advance();
            //statement();
            if (match("Keyword while")) {
                advance();
                if (match("Delimiter \\(")) {
                    advance();
                   // expression();
                    if (match("Delimiter \\)")) {
                        advance();
                        if (match("Delimiter ;")) {
                            advance();
                        } else {
                            System.out.println("Syntax Error missing ;");
                        }
                    } else {
                        System.out.println("Syntax Error missing )");
                    }
                } else {
                    System.out.println("Syntax Error missing (");
                }
            } else {
                System.out.println("Syntax Error missing while");
            }
        } else {
            System.out.println("Syntax Error missing iteration statement");
        }
    }

    private void return_statement(TextInBox parentNode) {
        if (match("Keyword return")) {
            TextInBox returnstmt = new TextInBox("Return Stmt",80,20);
            tree.addChild(parentNode,returnstmt);
            TextInBox keyword = new TextInBox("Keyword",80,20);
            tree.addChild(returnstmt,keyword);
            tree.addChild(keyword,new TextInBox(getTokenData(),80,20));


            advance();


            if (match("Delimiter ;")) {
                advance();
            } else {
                expression(parentNode);
                if (match("Delimiter ;")) {
                    advance();
                } else {
                    System.out.println("Syntax Error missing ;");
                }
            }
        } else {
            System.out.println("Syntax Error missing return");
        }
    }

    private void struct_var_declaration_list() {
        if (match(TYPE_SPECIFIER)) {
            declaration(root);
            struct_var_declaration_list();
        } else {
            return;
        }
    }


    private void enum_declaration() {
       
        if (match(IDENTIFIER)) {

            advance();
            if (match("Delimiter \\{")) {
                advance();
                enum_var_list();
                if (match("Delimiter \\}")) {
                    advance();
                    if (match("Delimiter ;")) {
                        advance();
                        System.out.println("Enum declaration Done");
                    } else {
                        System.out.println("Syntax Error missing ;");
                    }
                } else {
                    System.out.println("Syntax Error missing }");
                }
            } else {
                System.out.println("Syntax Error missing {");
            }
        } else {
            System.out.println("Syntax Error missing identifier for the Enum declaration");
        }
        } 

    

    private void enum_var_list() {
        if (match(IDENTIFIER)) {
            advance();


            if (match("Delimiter ,")) {
                advance();
                enum_var_list();
            } else {
                return;
            }
        } else {
            return;
        }
    }
    private void var_declaration(TextInBox parentNode) {
        // Consume the keyword token
        TextInBox vardec = new TextInBox("Variable declaration",80,20);
        tree.addChild(parentNode,vardec);

        if (match(IDENTIFIER)) {
            TextInBox idtoken3 = new TextInBox("Identifier", 80, 20);
            tree.addChild(vardec, idtoken3);
            tree.addChild(idtoken3, new TextInBox(getTokenData(), 80, 20));


            advance();
            
        
        if (match("Operator =")) {
            tree.addChild(vardec, new TextInBox(getTokenData(), 80, 20));

            advance();

            expression(vardec);
        
           if ( match("Delimiter ;")) {
            tree.addChild(vardec, new TextInBox(getTokenData(), 80, 20));

            advance();
            System.out.println("Variable declaration with assignment done");
            
           }else {
               System.out.println("Syntax Error missing in assignment  ;");
               System.exit(1);
           }
            
        }else if (match("Delimiter ;")) {
            tree.addChild(vardec, new TextInBox(getTokenData(), 80, 20));

            advance();
            System.out.println("Variable declaration");
            return;
          
        }else{
            System.out.println("Syntax Error missing but without assignment ;");
        }

    } else {
        System.out.println("Syntax Error missing identifier");
    }



    }

    private void expression(TextInBox parentNode)
    {
        TextInBox expressionnode = new TextInBox("Expression",80,20);
        tree.addChild(parentNode,expressionnode);
        conditional_expression(expressionnode); // Parse the conditional expression
    }

    private void conditional_expression(TextInBox parentNode) {
        TextInBox expressionnode = new TextInBox("Cond Exp",80,20);

        logical_or_expression(parentNode); // Parse the logical OR expression

        if (match("Operator \\? ")) {
            tree.addChild(parentNode,expressionnode);
            tree.addChild(expressionnode,new TextInBox(getTokenData(),80,20));
            advance(); // Consume the '?' token

            expression(expressionnode);

            // Check for the colon ':' token
            if (match("Operator :")) {
                tree.addChild(expressionnode,new TextInBox(getTokenData(),80,20));
                advance(); // Consume the ':' token

                expression(expressionnode);
            } else {
                System.err.println("Syntax error: Expected ':' after the true condition in ternary operator");
                System.exit(1);

            }
        }   
    }

    private void logical_or_expression(TextInBox parentNode)
    {
        TextInBox expressionnode = new TextInBox("Logical OR Exp",80,20);

        logical_and_expression(parentNode);

        while (match("Operator \\|\\|")) {
            tree.addChild(expressionnode,new TextInBox(getTokenData(),80,20));

            advance(); // Consume the '||' token

            // Check if there is a valid token after the logical OR operator
            if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")) {
                logical_and_expression(expressionnode);
            } else {
                // Error handling: Expected identifier, number, or left parenthesis after logical OR operator
                System.err.println("Syntax error: Expected identifier, number, or expression after logical OR operator");
                System.exit(1);

                return;
            }
        }
    }

    private void logical_and_expression(TextInBox parentNode)
    {
        TextInBox expressionnode = new TextInBox("Logical AND Exp",80,20);


    //    logical_and_expression(parentNode);
        equality_expression(parentNode);


        while (match("Operator &&")) {
            tree.addChild(parentNode,expressionnode);
            tree.addChild(expressionnode,new TextInBox(getTokenData(),80,20));
            advance(); // Consume the '&&' token

            // Check if there is a valid token after the logical AND operator
            if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")) {
                equality_expression(expressionnode);
            } else {
                System.err.println("Syntax error: Expected identifier, number, or expression after logical AND operator");
                System.exit(1);

                return;
            }
        }
    }

    private void equality_expression(TextInBox parentNode)
    {
        TextInBox expressionnode = new TextInBox("Equality Exp",80,20);

        relational_expression(parentNode);


        while (match("Operator ==") || match("Operator !=")) {
            tree.addChild(parentNode,expressionnode);
            tree.addChild(expressionnode,new TextInBox(getTokenData(),80,20));
            advance(); // Consume the equality operator

            if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")) {
                relational_expression(expressionnode);
            } else {
                // Error handling: Expected identifier, number, or left parenthesis after comparison operator
                System.err.println("Syntax error: Expected identifier, number, or expression after comparison operator");
                return;
            }
        }
    }


    private void relational_expression(TextInBox parentNode)
    {
        TextInBox expressionnode = new TextInBox("Relational Exp",80,20);
        additive_expression(parentNode);

        while (match("Operator <") || match("Operator >") || match("Operator <=") || match("Operator >=")) {
            tree.addChild(parentNode,expressionnode);
            tree.addChild(expressionnode,new TextInBox(getTokenData(),80,20));
            advance(); // Consume the relational operator
            // Check if there is a valid token after the relational operator
            if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")) {
                // Valid token found, continue parsing the expression
                additive_expression(expressionnode);
            } else {
                // Error handling: Expected identifier, number, or left parenthesis after relational operator
                System.err.println("Syntax error: Expected identifier, number, or expression after relational operator");
                return;
            }
        }
    }

    private void additive_expression(TextInBox parentNode)
    {
        TextInBox expressionnode = new TextInBox("Additive Exp",80,20);
        multiplicative_expression(parentNode);

        while (match("Operator \\+") || match("Operator -")) {
           
        tree.addChild(parentNode,expressionnode);
            tree.addChild(expressionnode,new TextInBox(getTokenData(),80,20));
            advance(); // Consume the additive operator
            // Check if there is a valid token after the additive operator
            if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")) {
                // Valid token found, continue parsing the expression
                multiplicative_expression(expressionnode);
            } else {
                // Error handling: Expected identifier, number, or left parenthesis after additive operator
                System.err.println("Syntax error: Expected identifier, number, or expression after additive operator");
                return;
            }
        }
    }

    
    private void multiplicative_expression(TextInBox parentNode)
    {
        TextInBox expressionnode = new TextInBox("Multiplicative Exp",100,20);
        unary_expression(parentNode);

        while (match("Operator \\*") || match("Operator /") || match("Operator %")){
            tree.addChild(parentNode,expressionnode);
            tree.addChild(expressionnode,new TextInBox(getTokenData(),100,20));
            advance(); // Consume the multiplicative operator

            if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")){
                // Valid token found, continue parsing the expression
                unary_expression(expressionnode);
            } else {
                // Error handling: Expected identifier, number, or left parenthesis after multiplicative operator
                System.err.println("Syntax error: Expected identifier, number, or expression after multiplicative operator");
                System.exit(1);

                return;
            }
        }
    }
    private void unary_expression(TextInBox parentNode)
    {
        TextInBox expressionnode = new TextInBox("Unary Exp",100,20);

        if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")) {
            System.out.println("Parsed primary expression");
            primary_expression(parentNode); 

        } else if (match("Operator \\+") || match("Operator -") || match("Operator ~") || match("Operator !") || match("Operator \\*") || match("Operator &") || match("Operator sizeof")) {
            tree.addChild(parentNode,new TextInBox(getTokenData(),80,20));

            advance(); // Consume the unary operator

            // Check if there is a valid token after the unary operator
            if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")) {
                // Valid token found, continue parsing the expression
                unary_expression(parentNode);
            } else {
                // Error handling: Expected identifier, number, or left parenthesis after unary operator
                System.err.println("Syntax error: Expected identifier, number, or expression after unary operator");
                System.exit(1);

                return;
            }
        }
        else if (match("Operator \\+\\+") || match("Operator --")) 
        {
            tree.addChild(parentNode,expressionnode);


            int prevIndex = currentTokenIndex - 1;
            int nextIndex = currentTokenIndex + 1;
            boolean isPrevIdentifier = prevIndex >= 0 && tokens.get(prevIndex).type.matches(IDENTIFIER);
            boolean isNextIdentifier = nextIndex < tokens.size() && tokens.get(nextIndex).type.matches(IDENTIFIER);

            System.out.println("TESTTT");

            String unaryOp = tokens.get(currentTokenIndex).value;
            tree.addChild(expressionnode,new TextInBox(getTokenData(),80,20));

            advance(); // Consume the unary operator

            // Check if there is a valid token after the unary operator
            if(!isPrevIdentifier && !isNextIdentifier)
            {
                System.err.println("Missing identifier");
                System.exit(0);
                return;

            }
            else if (match(IDENTIFIER)) {
                TextInBox idtoken = new TextInBox("Identifer",80,20);
                tree.addChild(expressionnode,idtoken);

                tree.addChild(idtoken,new TextInBox(getTokenData(),80,20));
                
                advance();

                // Parse the post-increment/decrement expression
                System.out.println("Parsed post-" + unaryOp + " expression");
            }
            // Arithmtic op
        else if(match(" Operator \\+ ") || match(" Operator - ") || match(" Operator * ") || match(" Operator / ") || match(" Operator % "))
            {



                unary_expression(parentNode);
            }
            else if (match("Delimeter ;"))
            {

                tree.addChild(parentNode,new TextInBox(getTokenData(),80,20));





                return; // NO ITS NEEDED

            }
            else {

                // Error handling: Expected identifier after unary operator

               // System.err.println("Syntax error: Expected identifier after unary operator '" + unaryOp + "'");
                return;
            }
        }
    }



    private void primary_expression(TextInBox parentNode)
    {

        TextInBox expressionnode = new TextInBox("Primary Exp",100,20);
        tree.addChild(parentNode,expressionnode);
        System.out.println("Entering primary_expression()");

        if (match(NUM)) {
            tree.addChild(expressionnode,new TextInBox(getTokenData(),100,20));

            System.out.println("Parsed number: " + tokens.get(currentTokenIndex).value);
            advance(); // Consume the number token
            System.out.println("Current token index: " + currentTokenIndex);

            arrayretract = false;
        } else if (match("Delimiter \\(")) {
            tree.addChild(expressionnode,new TextInBox(getTokenData(),100,20));

            advance(); 
            expression(expressionnode); // Parse the enclosed expression
            arrayretract = false;
            if (match("Delimiter \\)")) {
                tree.addChild(expressionnode,new TextInBox(getTokenData(),100,20));

                advance(); // Consume the right parenthesis token
                arrayretract = false;
            } else {
                // Error handling: Expected right parenthesis
                System.err.println("Syntax error: Missing right parenthesis");
                arrayretract = false;
                System.exit(0);
            }
        } else if (match(IDENTIFIER)) {
            // Check if it's a function call or a regular identifier
            int currentIndex = currentTokenIndex; // Store current index
            advance(); 
            if (match("Delimiter \\(")) {
                // It's a function call
                currentTokenIndex = currentIndex; // Reset index
                arrayretract = false;
                function_call(expressionnode); // Parse function call
            }
            else if(match("Delimiter \\["))

            {

               inParenthesis = true; arrayretract = true;
                retract();
                // array_call(expressionnode);


            }



            else {
                TextInBox idtoken2 = new TextInBox("Identifiejjjjjjr",80,20);
                tree.addChild(expressionnode,idtoken2);
                tree.addChild(idtoken2,new TextInBox(tokens.get(currentIndex).value,80,20));
                // It's a regular identifier
                currentTokenIndex = currentIndex; // Reset index
                // Handle regular identifier logic here

                System.out.println("Parsed identifier: " + tokens.get(currentIndex).value);
                advance(); 

                // Check if the next token is a unary operator
                if (match("Operator \\+\\+") || match("Operator --")) {
                    arrayretract = false;
                    // Handle unary expression
                    unary_expression(expressionnode);
                }
            }
        }
    }




    private void function_call(TextInBox parentNode) {
        TextInBox funcall = new TextInBox("Function Call",80,20);
        tree.addChild(parentNode,funcall);
        if (match(IDENTIFIER)) {
            tree.addChild(funcall,new TextInBox(getTokenData(),80,20));


            advance(); 
        } else {
            System.err.println("Syntax error: Expected identifier for function name in function call");
            return;
        }

        // Check for left parenthesis
        if (match( "Delimiter \\(")) {
            tree.addChild(funcall,new TextInBox(getTokenData(),80,20));

            advance(); 

        } else {
            // Error handling: Expected left parenthesis
            System.err.println("Syntax error: Expected '(' after function name in function call");
            return;
        }

        

        args_opt(funcall);

        // Check for right parenthesis
        if (match( "Delimiter \\)")) {

           
            tree.addChild(funcall,new TextInBox(getTokenData(),80,20));
            advance(); // Consume the right parenthesis token

            while (match("Delemiter \\.")) {
                tree.addChild(parentNode,new TextInBox(getTokenData(),80,20));

                advance(); // Consume the dot operator
                // Repeat function call parsing for chaining
                function_call(parentNode);
                retract();
            }

            if(match("Delimiter ;"))
            {
                tree.addChild(funcall,new TextInBox(getTokenData(),80,20));

                advance();
            } else
            {

                System.err.println("Syntax error: Expected ';' after function call");
                System.exit(1);

            }


        } else {
            // Error handling: Expected right parenthesis
            System.err.println("Syntax error: Expected ')' after function arguments in function call");
            System.exit(1);

        }
    }


    private void args_opt(TextInBox parentNode)
    {


        if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")) {
            System.out.println("Entering args_opt()");

            args_list(parentNode); // Parse the arguments list
        } else {
            System.out.println("No arguments found in args_opt()");


        }
    }

    private void args_list(TextInBox parentNode) {
        System.out.println("Entering args_list()");




        expression(parentNode); // Parse the first argument

        // Check for more arguments separated by commas
        if (match("Delimiter ,")) {
            tree.addChild(parentNode,new TextInBox(getTokenData(),80,20));
            advance(); 
            args_list(parentNode); // Parse the next argument
        }
    }
 

}
