import java.util.List;

import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.demo.TextInBox;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;
import org.w3c.dom.Text;
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


    private TextInBox root = new TextInBox("Program",140,40);

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
            //system.out.println("Current token index: " + currentTokenIndex);

            //system.out.println("Matched: " + tokens.get(currentTokenIndex).type + " " + tokens.get(currentTokenIndex).value);
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
        tree.addChild(root,new TextInBox(childname,140,40));
    }



    public void advance() {
        if (currentTokenIndex >= tokens.size()-1) {
            //system.out.println("End of tokens"+ currentTokenIndex);
            return;
            
        }
        this.currentTokenIndex++;

        //system.out.println("Advancing to: " + tokens.get(currentTokenIndex).type + " " + tokens.get(currentTokenIndex).value);
    }

    private String getTokenData()
    {
        if (tokens.get(currentTokenIndex).type.matches(IDENTIFIER)) {
            return tokens.get(currentTokenIndex).type.substring(11);
            
        }
        return tokens.get(currentTokenIndex).value;
    }



    public void parse () {
        program();
    }


      private void program() {
        declaration_list();        


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

        //system.out.println("Parsing complete.");

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

        TextInBox decl = new TextInBox("decleration",140,40);
        tree.addChild(decnode,decl);



        if (match(TYPE_SPECIFIER) && !tokens.get(currentTokenIndex).value.equals("struct") && !tokens.get(currentTokenIndex).value.equals("enum") && !tokens.get(currentTokenIndex).value.equals("typedef") ) {
            TextInBox typespecifier = new TextInBox("Type Specifier",140,40);
            tree.addChild(decl,typespecifier);
            tree.addChild(typespecifier,new TextInBox(getTokenData(),140,40));

            advance();
            if(match("Operator \\*")){
                while (match("Operator \\*")) {
                    tree.addChild(decl,new TextInBox(getTokenData(),140,40));
                    advance();
                }
            }
          if (match(IDENTIFIER)) {


                advance();
              if (match("Delimiter \\[")) {
                retract();
                array_declaration(decl);
                    // array declaration       
                } else if (match("Operator \\*")) {

                    while (match("Operator \\*")) {
                        advance();                       
                    }

                    // pointer declaration

                } else if (match("Delimiter \\(")) {
                   //
                  // System.out.println("Function declaration in var dec");
                    retract();
                    fun_declaration(decl);
             
                }else {


                    retract();  
                    var_declaration(decl);
                    

                    //var_declaration();
                  
                }
            }

        } else if (match("Keyword struct") && tokens.get(currentTokenIndex).value.equals("struct")) {



            advance();
            struct_declaration(decl);
        } else if (match("Keyword enum")  && tokens.get(currentTokenIndex).value.equals("enum") ) {
            advance();
            enum_declaration(decl);
        } else if (match("Keyword typedef") && tokens.get(currentTokenIndex).value.equals("typedef")) {
            advance();
            // typedef declaration
        }

        else {
            System.out.println("Invalid Declaration");
            return;
        }
  
    }
    

    private void array_declaration(TextInBox parentNode) {
        TextInBox arraynode = new TextInBox("Array Declaration",140,40);
        tree.addChild(parentNode,arraynode);

        TextInBox Identifier = new TextInBox("Identifier",140,40);
        tree.addChild(arraynode,Identifier);
        tree.addChild(Identifier,new TextInBox(getTokenData(),140,40));


        advance();

        
        if (match("Delimiter \\[")) {
            tree.addChild(arraynode,new TextInBox(getTokenData(),140,40));
            
            advance();

            array_arg(arraynode);

            if (match("Delimiter \\]")) {
                tree.addChild(arraynode,new TextInBox(getTokenData(),140,40));
                advance();
                if (match("Delimiter ;")) {
                    tree.addChild(arraynode,new TextInBox(getTokenData(),140,40));
                    advance();
                    //system.out.println("Array declaration done");
                } else if (match("Operator =")){
                    tree.addChild(arraynode,new TextInBox(getTokenData(),140,40));
                    advance();
                    expression(arraynode);
                    if (match("Delimiter ;")) {
                        tree.addChild(arraynode,new TextInBox(getTokenData(),140,40));
                        advance();
                        //system.out.println("Array declaration done");
                    } else {
                        System.out.println("Syntax Error missing ;");
                        System.exit(1); // Exit with error code 1

                    }
                }                 
                else {
                    System.out.println("Syntax Error missing ;");
                    System.exit(1); // Exit with error code 1

                }
            } else {
                System.out.println("Syntax Error missing ]");
                System.exit(1); // Exit with error code 1
                
            }

            
            
        } else {
            System.out.println("Syntax Error missing [");
        }
    }

    private void array_arg(TextInBox parentNode) {
        if (match(NUM)) {
            TextInBox num = new TextInBox("Number",140,40);
            tree.addChild(parentNode,num);
            tree.addChild(num,new TextInBox(getTokenData(),140,40));
            advance();

        } else if (match("Delimiter \\]") ) {
            
            System.out.println("Syntax Error missing number or identifier");
            System.exit(1); // Exit with error code 1
        }
        else {
            System.out.println("Syntax Error missing number or identifier");
        }
       
    }




    private void struct_declaration(TextInBox parentNode) {
        TextInBox structnode = new TextInBox("Struct Declaration",140,40);
        tree.addChild(parentNode,structnode);

        TextInBox keywordnode = new TextInBox("Keyword",140,40);
        tree.addChild(structnode,keywordnode);
        tree.addChild(keywordnode,new TextInBox(tokens.get(currentTokenIndex-1).value,140,40));


        
            if (match(IDENTIFIER)) {
                TextInBox idtoken = new TextInBox("Identifier",140,40);
                tree.addChild(structnode,idtoken);
                tree.addChild(idtoken,new TextInBox(getTokenData(),140,40));

                advance();
                //system.out.println("Strussdsdct i am her");

                if (match("Delimiter \\{")) {
                    tree.addChild(structnode,new TextInBox(getTokenData(),140,40));
                    //system.out.println("Struct i am her");
                    advance();
                    struct_var_declaration_list(structnode);
                    if (match("Delimiter \\}")) {
                        tree.addChild(structnode,new TextInBox(getTokenData(),140,40));
                        advance();
                        if (match("Delimiter ;")) {
                            tree.addChild(structnode,new TextInBox(getTokenData(),140,40));
                            advance();
                            //system.out.println("Struct declaration");
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
        TextInBox funnode = new TextInBox("Function Declaration",140,40);

        tree.addChild(parentNode,funnode);

      if (match(IDENTIFIER)) {
                TextInBox idtoken = new TextInBox("Identifier",140,40);
                tree.addChild(funnode,idtoken);
                tree.addChild(idtoken,new TextInBox(getTokenData(),140,40));
                advance();
                if (match("Delimiter \\(")) {

                    tree.addChild(funnode,new TextInBox(getTokenData(),140,40));
                    advance();
                   params(parentNode);
                    if (match("Delimiter \\)")) {
                        
                        tree.addChild(funnode,new TextInBox(getTokenData(),140,40));
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
        } else if (match("Delimiter \\)")) {
            return;
        }
        else {
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

            TextInBox typespecifier = new TextInBox("Type Specifier",140,40);
            tree.addChild(parentNode,typespecifier);
            tree.addChild(typespecifier,new TextInBox(getTokenData(),140,40));


            advance();
            if (match(IDENTIFIER)) {
                TextInBox idtoken = new TextInBox("Identifier",140,40);
                tree.addChild(parentNode,idtoken);
                tree.addChild(idtoken,new TextInBox(getTokenData(),140,40));

                advance();
                if (match("Delimiter \\[")) {
                    tree.addChild(parentNode,new TextInBox(getTokenData(),140,40));
                    advance();
                    if (match("Delimiter \\]")) {
                        tree.addChild(parentNode,new TextInBox(getTokenData(),140,40));
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
        TextInBox compoundstmt = new TextInBox("Compound Statement",140,40);
        tree.addChild(parentNode,compoundstmt);
        if (match("Delimiter \\{")) {
            tree.addChild(compoundstmt, new TextInBox(getTokenData(),140,40));
            //system.out.println("Compound statement");
            advance();
            //system.out.println("Compound statement  "+ tokens.get(currentTokenIndex).value);
            // int
            statement_list(compoundstmt);
            if (match("Delimiter \\}")) {
                tree.addChild(compoundstmt, new TextInBox(getTokenData(),140,40));


                advance();
                //system.out.println("Compound statement done");
            } else {
                //system.out.println("Compound statement  "+ tokens.get(currentTokenIndex).value);

                System.err.println("Syntax Error missing }");
            }
        } else {
            System.err.println("Syntax Error missing {");
            System.exit(1); // Exit with error code 1

        }
    }

    private void statement_list(TextInBox parentNode)  {
        if (match(TYPE_SPECIFIER) || match(IDENTIFIER) || match("Keyword if") || match("Keyword while") || match("Keyword for") || match("Keyword do") || match("Keyword return") || match("Delimiter \\{" )) {
            //system.out.println("---------------------------------------------------------------------------Statement list");

            statement(parentNode);
            statement_list(parentNode);
        } else {
            return;
        }
    }

    private void statement(TextInBox parentNode) {
        TextInBox stament = new TextInBox("Statement",140,40);
        tree.addChild(parentNode,stament);
        if (match(TYPE_SPECIFIER)) {
           
            TextInBox typespecifier = new TextInBox("Type Specifier",140,40);
            tree.addChild(stament,typespecifier);
            tree.addChild(typespecifier,new TextInBox(getTokenData(),140,40));


            advance();

            if(match("Operator \\*")){
                while (match("Operator \\*")) {
                    tree.addChild(stament,new TextInBox(getTokenData(),140,40));
                    advance();
                }
            }

            if (match(IDENTIFIER))
            {
                advance();
                if(match("Delimiter \\[")) {
                    retract();
                    array_declaration(stament);
                }
                else
                {
                    retract();
                    var_declaration(stament);
                }



            }



            else {
                System.err.println("Syntax Error missing identifier");
            }
        } else if (match(IDENTIFIER)) {
            expression_stmt(stament);
        } else if (match("Keyword if")) {
            //system.out.println("Selection statement entered");
            selection_stmt(stament);
        } else if (match("Keyword while") || match("Keyword for") || match("Keyword do")) {
            System.err.println("Iteration statement entered");
            iteration_stmt(stament);
        } else if (match("Keyword return")) {
            return_statement(parentNode);
        } else if (match("Delimiter \\{")) {
            compound_stmt(stament);
        } else {
            System.err.println("Syntax Error missing statement");
            System.exit(1); // Exit with error code 1

        }
    }

    private void expression_stmt(TextInBox parentNode) {

        // id(ssd);

       if (match(IDENTIFIER))
       {
        TextInBox expressionstmt = new TextInBox("Expression Stmt",140,40);
        tree.addChild(parentNode,expressionstmt);
        TextInBox idtoken = new TextInBox("Identifier",140,40);
        tree.addChild(expressionstmt,idtoken);
        tree.addChild(idtoken,new TextInBox(getTokenData(),140,40));


        advance();
        if(match("Operator =")){ advance();}
        else {retract();}
        //result = a + b
        expression(expressionstmt);



        if (match("Delimiter ;")) {
            tree.addChild(expressionstmt,new TextInBox(getTokenData(),140,40));
            advance();
        } else {
            System.out.println("Syntax Error missing ;111");
            
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
            TextInBox whilestmt = new TextInBox("While Statement",100,20);
            tree.addChild(parentNode,whilestmt);

            tree.addChild(whilestmt,new TextInBox(getTokenData(),100,20));


            advance();
            if (match("Delimiter \\(")) {
                tree.addChild(whilestmt,new TextInBox(getTokenData(),140,40));
                advance();
                expression(whilestmt);
                if (match("Delimiter \\)")) {
                    tree.addChild(whilestmt,new TextInBox(getTokenData(),140,40));
                    advance();
                    statement(whilestmt);
                } else {
                    System.out.println("Syntax Error missing )");
                }
            } else {
                System.out.println("Syntax Error missing (");
            }
        } else if (match("Keyword for")) {
            TextInBox forstmt = new TextInBox("For statment ",100,20);
            tree.addChild(parentNode,forstmt);

            tree.addChild(forstmt,new TextInBox(getTokenData(),100,20));
    
            advance();
            if (match("Delimiter \\(")) {
                tree.addChild(forstmt,new TextInBox(getTokenData(),140,40));

                advance();

                if (match(TYPE_SPECIFIER)){
                    TextInBox typespecifier = new TextInBox("Type Specifier",140,40);
                    tree.addChild(forstmt,typespecifier);
                    tree.addChild(typespecifier,new TextInBox(getTokenData(),140,40));



                    advance();
                    if (match(IDENTIFIER)){



                        var_declaration(forstmt);

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
                    advance();
                    expression(forstmt);

                    if (match("Delimiter ;")) {
                        tree.addChild(forstmt,new TextInBox(getTokenData(),140,40));
                        advance();
                        expression(forstmt);
                    } else {
                        System.out.println("Syntax Error missing ; 000");
                    }

                    if (match("Delimiter \\)")) {
                        //system.out.println("For loop done");
                        advance();
                        statement(forstmt);
                    } else {
                        System.err.println("Syntax error: Expected ')' after for loop condition" );
                        
                    }

                } else {
                    System.out.println("Syntax Error missing ; 999");
                }
            } else {
                System.out.println("Syntax Error missing (");
            }
        } else if (match("Keyword do")) {
            TextInBox dostmt = new TextInBox("Do Statement",100,20);
            tree.addChild(parentNode,dostmt);

            tree.addChild(dostmt,new TextInBox(getTokenData(),100,20));
            advance();
            statement(dostmt);
            if (match("Keyword while")) {
                TextInBox whilestmt = new TextInBox("Keyword",100,20);
                tree.addChild(dostmt,whilestmt);
                tree.addChild(whilestmt,new TextInBox(getTokenData(),100,20));

                advance();
                if (match("Delimiter \\(")) {
                    tree.addChild(dostmt,new TextInBox(getTokenData(),140,40));
                    advance();
                   expression(dostmt);
                    if (match("Delimiter \\)")) {
                        tree.addChild(dostmt,new TextInBox(getTokenData(),140,40));
                        advance();
                        if (match("Delimiter ;")) {
                            tree.addChild(dostmt,new TextInBox(getTokenData(),140,40));
                            advance();
                        } else {
                            System.out.println("Syntax Error missing ; 6666");
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
            TextInBox returnstmt = new TextInBox("Return Stmt",140,40);
            tree.addChild(parentNode,returnstmt);
            TextInBox keyword = new TextInBox("Keyword",140,40);
            tree.addChild(returnstmt,keyword);
            tree.addChild(keyword,new TextInBox(getTokenData(),140,40));


            advance();


            if (match("Delimiter ;")) {
                tree.addChild(returnstmt,new TextInBox(getTokenData(),140,40));
                advance();
            } else {
                expression(returnstmt);
                if (match("Delimiter ;")) {
                    tree.addChild(returnstmt,new TextInBox(getTokenData(),140,40));
                    advance();
                } else {
                    System.out.println("Syntax Error missing ; 777");
                }
            }
        } else {
            System.out.println("Syntax Error missing return");
        }
    }

    private void struct_var_declaration_list(TextInBox parentNode) {
        if (match(TYPE_SPECIFIER)) {
            declaration(parentNode);
            struct_var_declaration_list(parentNode);
        } else {
            return;
        }
    }


    private void enum_declaration(TextInBox parentNode) {
        TextInBox enumNode = new TextInBox("Enum Declaration",140,40);
        tree.addChild(parentNode,enumNode);

        TextInBox keywordnode = new TextInBox("Keyword",140,40);
        tree.addChild(enumNode,keywordnode);
        tree.addChild(keywordnode,new TextInBox(tokens.get(currentTokenIndex-1).value,140,40));
       
        if (match(IDENTIFIER)) {
            TextInBox idtoken = new TextInBox("Identifier",140,40);
            tree.addChild(enumNode,idtoken);
            tree.addChild(idtoken,new TextInBox(getTokenData(),140,40));

            advance();
            if (match("Delimiter \\{")) {
                tree.addChild(enumNode,new TextInBox(getTokenData(),140,40));
                advance();
                enum_var_list(enumNode);
                if (match("Delimiter \\}")) {
                    tree.addChild(enumNode,new TextInBox(getTokenData(),140,40));
                    advance();
                    if (match("Delimiter ;")) {
                        tree.addChild(enumNode,new TextInBox(getTokenData(),140,40));
                        advance();
                        //system.out.println("Enum declaration Done");
                    } else {
                        System.out.println("Syntax Error missing ; 888");
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

    

    private void enum_var_list(TextInBox parentNode) {
        if (match(IDENTIFIER)) {
            TextInBox idtoken = new TextInBox("Identifier",140,40);
            tree.addChild(parentNode,idtoken);
            tree.addChild(idtoken,new TextInBox(getTokenData(),140,40));
            advance();


            if (match("Delimiter ,")) {
                tree.addChild(parentNode,new TextInBox(getTokenData(),140,40));
                advance();
                enum_var_list(parentNode);
            } else {
                return;
            }
        } else {
            return;
        }
    }
    private void var_declaration(TextInBox parentNode) {
        // Consume the keyword token
        TextInBox vardec = new TextInBox("Variable declaration",140,40);
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
            //system.out.println("Variable declaration with assignment done");
            
           }else {
               System.out.println("Syntax Error missing in assignment  ;");
               System.exit(1);
           }
            
        }else if (match("Delimiter ;")) {
            tree.addChild(vardec, new TextInBox(getTokenData(), 80, 20));

            advance();
            //system.out.println("Variable declaration");
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
        TextInBox expressionnode = new TextInBox("Expression",140,40);
        tree.addChild(parentNode,expressionnode);
        conditional_expression(expressionnode); // Parse the conditional expression
    }

    private void conditional_expression(TextInBox parentNode) {
        TextInBox expressionnode = new TextInBox("Cond Exp",140,40);

        logical_or_expression(parentNode); // Parse the logical OR expression

        if (match("Operator \\? ")) {
            tree.addChild(parentNode,expressionnode);
            tree.addChild(expressionnode,new TextInBox(getTokenData(),140,40));
            advance(); // Consume the '?' token

            expression(expressionnode);

            // Check for the colon ':' token
            if (match("Operator :")) {
                tree.addChild(expressionnode,new TextInBox(getTokenData(),140,40));
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
        TextInBox expressionnode = new TextInBox("Logical OR Exp",140,40);

        logical_and_expression(parentNode);

        while (match("Operator \\|\\|")) {
            tree.addChild(expressionnode,new TextInBox(getTokenData(),140,40));

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
        TextInBox expressionnode = new TextInBox("Logical AND Exp",140,40);


    //    logical_and_expression(parentNode);
        equality_expression(parentNode);


        while (match("Operator &&")) {
            tree.addChild(parentNode,expressionnode);
            tree.addChild(expressionnode,new TextInBox(getTokenData(),140,40));
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
        TextInBox expressionnode = new TextInBox("Equality Exp",140,40);

        relational_expression(parentNode);


        while (match("Operator ==") || match("Operator !=")) {
            tree.addChild(parentNode,expressionnode);
            tree.addChild(expressionnode,new TextInBox(getTokenData(),140,40));
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
        TextInBox expressionnode = new TextInBox("Relational Exp",140,40);
        additive_expression(parentNode);

        while (match("Operator <") || match("Operator >") || match("Operator <=") || match("Operator >=")) {
            tree.addChild(parentNode,expressionnode);
            tree.addChild(expressionnode,new TextInBox(getTokenData(),140,40));
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
        TextInBox expressionnode = new TextInBox("Additive Exp",140,40);
        multiplicative_expression(parentNode);

        while (match("Operator \\+") || match("Operator -")) {
           
        tree.addChild(parentNode,expressionnode);
            tree.addChild(expressionnode,new TextInBox(getTokenData(),140,40));
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
            //system.out.println("Parsed primary expression");
            primary_expression(parentNode); 

        } else if (match("Operator \\+") || match("Operator -") || match("Operator ~") || match("Operator !") || match("Operator \\*") || match("Operator &") || match("Operator sizeof")) {



            tree.addChild(parentNode,new TextInBox(getTokenData(),140,40));

            advance(); // Consume the unary operator

            if(match("Operator \\*")){
                while (match("Operator \\*")) {
                    tree.addChild(parentNode,new TextInBox(getTokenData(),140,40));
                    advance();
                }
            }

            // Check if there is a valid token after the unary operator
            if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")) {
                // Valid token found, continue parsing the expression
                unary_expression(parentNode);
            } else {
                // Error handling: Expected identifier, number, or left parenthesis after unary operator
                System.err.println("Syntax error: Expected identifier, number, or expression after unary operator " + tokens.get(currentTokenIndex).value);
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

            //system.out.println("TESTTT");

            String unaryOp = tokens.get(currentTokenIndex).value;
            tree.addChild(expressionnode,new TextInBox(getTokenData(),140,40));

            advance(); // Consume the unary operator

            // Check if there is a valid token after the unary operator
            if(!isPrevIdentifier && !isNextIdentifier)
            {
                System.err.println("Missing identifier");
                System.exit(0);
                return;

            }
            else if (match(IDENTIFIER)) {
                TextInBox idtoken = new TextInBox("Identifer",140,40);
                tree.addChild(expressionnode,idtoken);

                tree.addChild(idtoken,new TextInBox(getTokenData(),140,40));
                
                advance();

                // Parse the post-increment/decrement expression
                //system.out.println("Parsed post-" + unaryOp + " expression");
            }
            // Arithmtic op
        else if(match(" Operator \\+ ") || match(" Operator - ") || match(" Operator * ") || match(" Operator / ") || match(" Operator % "))
            {



                unary_expression(parentNode);
            }
            else if (match("Delimeter ;"))
            {

                tree.addChild(parentNode,new TextInBox(getTokenData(),140,40));





                return; // NO ITS NEEDED

            }

            else {

                // Error handling: Expected identifier after unary operator

               // System.err.println("Syntax error: Expected identifier after unary operator '" + unaryOp + "'");
                return;
            }
        }else if (match("String Literal")) {
            TextInBox stringnode = new TextInBox("String Literal",140,40);
            tree.addChild(parentNode,stringnode);
            tree.addChild(stringnode,new TextInBox(getTokenData(),140,40));


            advance();
        }else if (match("Character Literal")) {
            TextInBox charnode = new TextInBox("Character Literal",140,40);
            tree.addChild(parentNode,charnode);
            tree.addChild(charnode,new TextInBox(getTokenData(),140,40));

            advance();
            
        }else if (match("Delimiter \\{")){
            tree.addChild(parentNode,new TextInBox(getTokenData(),140,40));

            advance();
            expression(parentNode);


            while (match("Delimiter \\,")) {
                tree.addChild(parentNode,new TextInBox(getTokenData(),140,40));

                advance();
                expression(parentNode);
                
            }
            if (match("Delimiter \\}")){
                tree.addChild(parentNode,new TextInBox(getTokenData(),140,40));

                advance();
            }
            else {
                System.err.println("Syntax error: Expected '}' after unary operator" + tokens.get(currentTokenIndex).value);
                System.exit(1);
            }

           
        
        }

        
        else {
            
            // Error handling: Expected primary expression or unary operator
            System.err.println("Syntax error: Expected primary expression or unary operator" + tokens.get(currentTokenIndex).value );
            System.exit(1);

            return;
            
        }
    }



    private void primary_expression(TextInBox parentNode)
    {

        TextInBox expressionnode = new TextInBox("Primary Exp",100,20);
        tree.addChild(parentNode,expressionnode);
        //system.out.println("Entering primary_expression()");

        if (match(NUM)) {
            tree.addChild(expressionnode,new TextInBox(getTokenData(),100,20));

            //system.out.println("Parsed number: " + tokens.get(currentTokenIndex).value);
            advance(); // Consume the number token
            //system.out.println("Current token index: " + currentTokenIndex);

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
                
        TextInBox arraycall = new TextInBox("Array Call",140,40);
        tree.addChild(expressionnode,arraycall);
        
                array_call(arraycall);


            } else {
                currentTokenIndex = currentIndex; // Reset index

                TextInBox idtoken2 = new TextInBox("Identifier",140,40);
                tree.addChild(expressionnode,idtoken2);
                tree.addChild(idtoken2,new TextInBox(getTokenData(),140,40));
                // It's a regular identifier
                // Handle regular identifier logic here

                //system.out.println("Parsed identifier: " + tokens.get(currentIndex).value);
                advance(); 

                // Check if the next token is a unary operator
                if (match("Operator \\+\\+") || match("Operator --")) {
                    arrayretract = false;
                    // Handle unary expression
                    unary_expression(expressionnode);
                }
            }
        
        
        } else if (match("String Literal")){
            TextInBox stringnode = new TextInBox("String Literal",140,40);
            tree.addChild(expressionnode,stringnode);
            tree.addChild(stringnode,new TextInBox(getTokenData(),140,40));


               advance();
        } else if (match("Character Literal")){
            TextInBox charnode = new TextInBox("Character Literal",140,40);
            tree.addChild(expressionnode,charnode);
            tree.addChild(charnode,new TextInBox(getTokenData(),140,40));


               advance();
        }
        else {

            tree.addChild(expressionnode,new TextInBox("Error",100,20));
            // Error handling: Expected number, identifier, or left parenthesis
            System.err.println("Syntax error: Expected number, identifier, or expression");
            arrayretract = false;
        }
    }




    private void function_call(TextInBox parentNode) {
        TextInBox funcall = new TextInBox("Function Call",140,40);
        tree.addChild(parentNode,funcall);
        if (match(IDENTIFIER)) {
            tree.addChild(funcall,new TextInBox(getTokenData(),140,40));


            advance(); 
        } else {
            System.err.println("Syntax error: Expected identifier for function name in function call");
            return;
        }

        // Check for left parenthesis
        if (match( "Delimiter \\(")) {
            tree.addChild(funcall,new TextInBox(getTokenData(),140,40));

            advance(); 

        } else {
            // Error handling: Expected left parenthesis
            System.err.println("Syntax error: Expected '(' after function name in function call");
            return;
        }

        

        args_opt(funcall);
        

        // Check for right parenthesis
        if (match( "Delimiter \\)")) {

           
            tree.addChild(funcall,new TextInBox(getTokenData(),140,40));
            advance(); // Consume the right parenthesis token

            while (match("Delemiter \\.")) {
                tree.addChild(parentNode,new TextInBox(getTokenData(),140,40));

                advance(); // Consume the dot operator
                // Repeat function call parsing for chaining
                function_call(parentNode);
                retract();
            }

            


        } else {
            // Error handling: Expected right parenthesis
            System.err.println("Syntax error: Expected ')' after function arguments in function call" + tokens.get(currentTokenIndex).value + " " + match("String Literal"));
            System.exit(1);

        }
    }


    private void args_opt(TextInBox parentNode)
    {


        
        if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(") || match("String Literal") || match("Character Literal")) {
            
            //system.out.println("Entering args_opt() kskskskskskskskk");

            args_list(parentNode); // Parse the arguments list
        } else {


            System.out.println("No arguments found in args_opt()");


        }
    }

    private void args_list(TextInBox parentNode) {
        //system.out.println("Entering args_list()");




        expression(parentNode); // Parse the first argument

        // Check for more arguments separated by commas
        if (match("Delimiter ,")) {
            tree.addChild(parentNode,new TextInBox(getTokenData(),140,40));
            advance(); 
            args_list(parentNode); // Parse the next argument
        }
    }
 

    private void array_call(TextInBox parentNode) {

        // Parse the identifier
        if (match(IDENTIFIER))
        {
            TextInBox idtoken = new TextInBox("Identifier",80,20);
            tree.addChild(parentNode,idtoken);
            tree.addChild(idtoken,new TextInBox(getTokenData(),80,20));

            advance(); // Consume the identifier token
        } else {
            // Error handling: Expected an identifier for array call
            System.err.println("Syntax error: Expecting an identifier for array call");
            return;
        }

        // Parse array dimensions
        parse_array_dimensions_in_call(parentNode);


     //   System.out.println("Current array: " + tokens.get(currentTokenIndex).data);

        if(match("Operator ="))
        {
            tree.addChild(parentNode,new TextInBox(getTokenData(),80,20));
            advance();

            if(match("Charachter Literal") || match("String Literal"))
            {
                if(match("Charachter Literal"))
                {
                    TextInBox charc = new TextInBox("Character",80,20);
                    tree.addChild(parentNode,charc);
                    tree.addChild(charc,new TextInBox(getTokenData(),80,20));
                }
                else
                {
                    TextInBox string = new TextInBox("String Literal",80,20);
                    tree.addChild(parentNode,string);
                    tree.addChild(string,new TextInBox(getTokenData(),80,20));
                }

                advance();

            }
            else
            {

                expression(parentNode);

                if(arrayretract)
                {

                    retract();
                    arrayretract = false;
                }



            }

            if(match("Delemiter ;"))
            {
                tree.addChild(parentNode,new TextInBox(getTokenData(),80,20));
                advance();
                return;
            }
            else
            {
                System.err.println("Syntax error: Expecting a semicolon for an array call");
                System.exit(0);
            }
        }
        // Check for semicolon to terminate the array call statement
        else if (!match("Delimeter ;")){
            // Error handling: Expected semicolon to terminate array call
            if(!inParenthesis)
                System.err.println("Syntax error: Expected ';' to terminate array call");
        } else {
            tree.addChild(parentNode,new TextInBox(getTokenData(),80,20));
            advance(); // Consume the semicolon token
        }
    }

    private void parse_array_dimensions_in_call(TextInBox parentNode) {
        // Parse array dimensions (square brackets)
        if (match("Delimiter \\["))
        {
            tree.addChild(parentNode,new TextInBox(getTokenData(),80,20));
            advance(); // Consume the left square bracket

            // Parse the expression inside square brackets
            expression(parentNode); // Assuming you have a method to parse expressions

            // Check for right square bracket
            if (match("Delimiter \\]"))
            {
                tree.addChild(parentNode,new TextInBox(getTokenData(),80,20));
                advance(); // Consume the right square bracket

                if (match("Delimiter \\["))
                {

                    parse_array_dimensions_in_call(parentNode);
                }
            } else {
                // Error handling: Expected right square bracket
                System.err.println("Syntax error: Expected ']' in array dimension for array call");
                return;
            }
        }
    }





}
