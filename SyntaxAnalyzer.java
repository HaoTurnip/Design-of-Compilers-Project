import java.util.List;

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

public class SyntaxAnalyzer {
    public String TYPE_SPECIFIER = "Keyword (int|float|char|double|short|long|signed|unsigned|void|String|bool)";
    // public String KEYWORD = "Keyword *";
    public String IDENTIFIER = "Identifier .*"; 
    public String NUM = "Integer .* | Float .*";
    

    private int currentTokenIndex;
    private List<LexicalAnalyzer.Token> tokens;

    public SyntaxAnalyzer(List<LexicalAnalyzer.Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;

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




    public void advance() {
        if (currentTokenIndex >= tokens.size()-1) {
            System.out.println("End of tokens"+ currentTokenIndex);
            return;
            
        }
        this.currentTokenIndex++;

        System.out.println("Advancing to: " + tokens.get(currentTokenIndex).type + " " + tokens.get(currentTokenIndex).value);
    }

    public void parse () {
        program();
    }


      private void program() {
        declaration_list();        
        

    }

    private void declaration_list() {

        if (match(TYPE_SPECIFIER)) {
            
            declaration();
            declaration_list();

        } else {
            return;
        }
     
    }

    private void declaration() {

        // while (match(KEYWORD)) {
        //     advance();       
        // }

        if (match(TYPE_SPECIFIER)) {
            advance();
          if (match(IDENTIFIER)) {
                advance();
              if (match("Delimiter \\[")) {
                    advance();
                    // array declaration       
                } else if (match("Operator \\*")) {

                    while (match("Operator \\*")) {
                        advance();                       
                    }

                    // pointer declaration

                } else if (match("Delimiter \\(")) {
                    advance();
                    // function declaration
                }else {

                    var_declaration();

                    //var_declaration();
                  
                }
            }

        }else {
            System.out.println("Invalid Declaration");
            return;
        }
  
    }

    private void var_declaration() {

        if (match("Operator =")) {
            advance();

            expression();
        
           if ( match("Delimiter ;")) {
            advance();
            System.out.println("Variable declaration");
            
           }else {
               System.out.println("Syntax Error missing in assignment  ;");
           }
            
        }else if (match("Delimiter ;")) {
            advance();
            System.out.println("Variable declaration");
            return;
          
        }else{
            System.out.println("Syntax Error missing but without assignment ;");
        }




    }

    private void expression() {
        conditional_expression();
    }

    private void conditional_expression() {
        logical_or_expression(); 

        // Check if the current token is the ternary operator '?'
        if (match("Operator \\? ")) {
            advance(); // Consume the '?' token

            // Parse the expression for the true condition
            expression();

            // Check for the colon ':' token
            if (match("Operator :")) {
                advance();

                expression();
            } else {
                System.err.println("Syntax error: Expected ':' after the true condition in ternary operator");
            }
        }   
    }

    private void logical_or_expression() {
        logical_and_expression();

        while (match("Operator \\|\\|")) {
            advance(); // Consume the '||' token

            // Check if there is a valid token after the logical OR operator
            if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")) {
                logical_and_expression();
            } else {
                // Error handling: Expected identifier, number, or left parenthesis after logical OR operator
                System.err.println("Syntax error: Expected identifier, number, or expression after logical OR operator");
                return;
            }
        }
    }

    private void logical_and_expression() {
       //equality_expression();

        while (match("Operator &&")) {
            advance(); // Consume the '&&' token

            // Check if there is a valid token after the logical AND operator
            if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")) {
                //equality_expression();
            } else {
                System.err.println("Syntax error: Expected identifier, number, or expression after logical AND operator");
                return;
            }
        }
    }

    // private void equality_expression() {
    //     relational_expression();

    //     while (match(LexicalAnalyzer.TokenType.COMPARISON_OP) &&
    //             (tokens.get(currentTokenIndex).data.equals("==") || tokens.get(currentTokenIndex).data.equals("!="))) {
    //         advance(); // Consume the equality operator

    //         // Check if there is a valid token after the comparison operator
    //         if (match(LexicalAnalyzer.TokenType.ID) || match(LexicalAnalyzer.TokenType.NUMBER) || match(LexicalAnalyzer.TokenType.LEFT_PAREN)) {
    //             // Valid token found, continue parsing the expression
    //             relational_expression();
    //         } else {
    //             // Error handling: Expected identifier, number, or left parenthesis after comparison operator
    //             System.err.println("Syntax error: Expected identifier, number, or expression after comparison operator");
    //             return;
    //         }
    //     }
    // }


    // private void relational_expression() {
    //     additive_expression();

    //     while (match(LexicalAnalyzer.TokenType.COMPARISON_OP) &&
    //             (tokens.get(currentTokenIndex).data.equals("<") || tokens.get(currentTokenIndex).data.equals("<=") ||
    //                     tokens.get(currentTokenIndex).data.equals(">") || tokens.get(currentTokenIndex).data.equals(">="))) {
    //         advance(); // Consume the relational operator

    //         // Check if there is a valid token after the relational operator
    //         if (match(LexicalAnalyzer.TokenType.ID) || match(LexicalAnalyzer.TokenType.NUMBER) || match(LexicalAnalyzer.TokenType.LEFT_PAREN)) {
    //             // Valid token found, continue parsing the expression
    //             additive_expression();
    //         } else {
    //             // Error handling: Expected identifier, number, or left parenthesis after relational operator
    //             System.err.println("Syntax error: Expected identifier, number, or expression after relational operator");
    //             return;
    //         }
    //     }
    // }

    // private void additive_expression() {
    //     multiplicative_expression();

    //     while (match(LexicalAnalyzer.TokenType.ARITHMETIC_OP) &&
    //             (tokens.get(currentTokenIndex).data.equals("+") || tokens.get(currentTokenIndex).data.equals("-")))
    //     {
    //         advance(); // Consume the additive operator

    //         // Check if there is a valid token after the additive operator
    //         if (match(LexicalAnalyzer.TokenType.ID) || match(LexicalAnalyzer.TokenType.NUMBER) || match(LexicalAnalyzer.TokenType.LEFT_PAREN)) {
    //             // Valid token found, continue parsing the expression
    //             multiplicative_expression();
    //         } else {
    //             // Error handling: Expected identifier, number, or left parenthesis after additive operator
    //             System.err.println("Syntax error: Expected identifier, number, or expression after additive operator");
    //             return;
    //         }
    //     }
    // }

    // private void multiplicative_expression() {
    //     unary_expression();

    //     while (match(LexicalAnalyzer.TokenType.ARITHMETIC_OP) &&
    //             (tokens.get(currentTokenIndex).data.equals("*") || tokens.get(currentTokenIndex).data.equals("/") ||
    //                     tokens.get(currentTokenIndex).data.equals("%"))) {
    //         advance(); // Consume the multiplicative operator

    //         // Check if there is a valid token after the multiplicative operator
    //         if (match(LexicalAnalyzer.TokenType.ID) || match(LexicalAnalyzer.TokenType.NUMBER) || match(LexicalAnalyzer.TokenType.LEFT_PAREN)) {
    //             // Valid token found, continue parsing the expression
    //             unary_expression();
    //         } else {
    //             // Error handling: Expected identifier, number, or left parenthesis after multiplicative operator
    //             System.err.println("Syntax error: Expected identifier, number, or expression after multiplicative operator");
    //             return;
    //         }
    //     }
    // }

    // private void unary_expression() {
    //     if (match(LexicalAnalyzer.TokenType.ID) || match(LexicalAnalyzer.TokenType.NUMBER) || match(LexicalAnalyzer.TokenType.LEFT_PAREN)) {
    //         primary_expression(); // Parse primary expression

    //     } else if (match(LexicalAnalyzer.TokenType.ARITHMETIC_OP) &&
    //             (tokens.get(currentTokenIndex).data.equals("+") || tokens.get(currentTokenIndex).data.equals("-"))) {
    //         advance(); // Consume the unary operator

    //         // Check if there is a valid token after the unary operator
    //         if (match(LexicalAnalyzer.TokenType.ID) || match(LexicalAnalyzer.TokenType.NUMBER) || match(LexicalAnalyzer.TokenType.LEFT_PAREN)) {
    //             // Valid token found, continue parsing the expression
    //             unary_expression();
    //         } else {
    //             // Error handling: Expected identifier, number, or left parenthesis after unary operator
    //             System.err.println("Syntax error: Expected identifier, number, or expression after unary operator");
    //             return;
    //         }
    //     }
    //     else if (match(LexicalAnalyzer.TokenType.UNARY_OP))
    //     {

    //         int prevIndex = currentTokenIndex - 1;
    //         int nextIndex = currentTokenIndex + 1;
    //         boolean isPrevIdentifier = prevIndex >= 0 && tokens.get(prevIndex).type == LexicalAnalyzer.TokenType.ID;
    //         boolean isNextIdentifier = nextIndex < tokens.size() && tokens.get(nextIndex).type == LexicalAnalyzer.TokenType.ID;

    //         System.out.println("TESTTT");

    //         // Handle unary operators like '++' and '--'
    //         String unaryOp = tokens.get(currentTokenIndex).data;
    //         advance(); // Consume the unary operator

    //         // Check if there is a valid token after the unary operator
    //         if(!isPrevIdentifier && !isNextIdentifier)
    //         {
    //             System.err.println("Missing identifier");
    //             System.exit(0);
    //             return;

    //         }
    //         else if (match(LexicalAnalyzer.TokenType.ID)) {
    //             // Consume the identifier token
    //             advance();

    //             // Parse the post-increment/decrement expression
    //             System.out.println("Parsed post-" + unaryOp + " expression");
    //         }
    //         else if(match(LexicalAnalyzer.TokenType.ARITHMETIC_OP))
    //         {



    //             unary_expression();
    //         }
    //         else if (match(LexicalAnalyzer.TokenType.SEMICOLON))
    //         {





    //             return; // NO ITS NEEDED

    //         }
    //         else {

    //             // Error handling: Expected identifier after unary operator

    //            // System.err.println("Syntax error: Expected identifier after unary operator '" + unaryOp + "'");
    //             return;
    //         }
    //     }
    // }




    // private void primary_expression()
    // {

    //     if (match(LexicalAnalyzer.TokenType.NUMBER)) {

    //         advance(); // Consume the number token
    //         arrayretract = false;
    //     } else if (match(LexicalAnalyzer.TokenType.LEFT_PAREN)) {
    //         advance(); // Consume the left parenthesis token
    //         expression(); // Parse the enclosed expression
    //         arrayretract = false;
    //         if (match(LexicalAnalyzer.TokenType.RIGHT_PAREN)) {
    //             advance(); // Consume the right parenthesis token
    //             arrayretract = false;
    //         } else {
    //             // Error handling: Expected right parenthesis
    //             System.err.println("Syntax error: Missing right parenthesis");
    //             arrayretract = false;
    //             System.exit(0);
    //         }
    //     } else if (match(LexicalAnalyzer.TokenType.ID)) {
    //         // Check if it's a function call or a regular identifier
    //         int currentIndex = currentTokenIndex; // Store current index
    //         advance(); // Consume the identifier token
    //         if (match(LexicalAnalyzer.TokenType.LEFT_PAREN)) {
    //             // It's a function call
    //             currentTokenIndex = currentIndex; // Reset index
    //             arrayretract = false;
    //             function_call(); // Parse function call
    //         }
    //         else if(match(LexicalAnalyzer.TokenType.LEFT_BRACKET))

    //         {

    //            inParenthesis = true; arrayretract = true;
    //             retract();
    //             array_call();


    //         }



    //         else {
    //             // It's a regular identifier
    //             currentTokenIndex = currentIndex; // Reset index
    //             // Handle regular identifier logic here
    //             advance(); // Consume the identifier token

    //             // Check if the next token is a unary operator
    //             if (match(LexicalAnalyzer.TokenType.UNARY_OP)) {
    //                 arrayretract = false;
    //                 // Handle unary expression
    //                 unary_expression();
    //             }
    //         }
    //     }
    // }




    // private void function_call() {
    //     if (match(LexicalAnalyzer.TokenType.ID)) {

    //         advance(); // Consume the identifier token
    //     } else {
    //         // Error handling: Expected identifier for function name
    //         System.err.println("Syntax error: Expected identifier for function name in function call");
    //         return;
    //     }

    //     // Check for left parenthesis
    //     if (match(LexicalAnalyzer.TokenType.LEFT_PAREN)) {
    //         advance(); // Consume the left parenthesis token

    //     } else {
    //         // Error handling: Expected left parenthesis
    //         System.err.println("Syntax error: Expected '(' after function name in function call");
    //         return;
    //     }

    //     // Parse the optional arguments

    //     args_opt();

    //     // Check for right parenthesis
    //     if (match(LexicalAnalyzer.TokenType.RIGHT_PAREN)) {
    //         advance(); // Consume the right parenthesis token

    //         while (match(LexicalAnalyzer.TokenType.DOT)) {
    //             advance(); // Consume the dot operator
    //             // Repeat function call parsing for chaining
    //             function_call();
    //             retract();
    //         }

    //         if(match(LexicalAnalyzer.TokenType.SEMICOLON))
    //         {
    //             advance();
    //         } else
    //         {

    //             System.err.println("Syntax error: Expected ';' after function call");
    //         }


    //     } else {
    //         // Error handling: Expected right parenthesis
    //         System.err.println("Syntax error: Expected ')' after function arguments in function call");
    //     }
    // }

    // private void args_opt()
    // {


    //     if (match(LexicalAnalyzer.TokenType.ID) || match(LexicalAnalyzer.TokenType.NUMBER) || match(LexicalAnalyzer.TokenType.LEFT_PAREN)) {
    //         System.out.println("Entering args_opt()");

    //         args_list(); // Parse the arguments list
    //     } else {
    //         System.out.println("No arguments found in args_opt()");


    //     }
    // }

    // private void args_list() {
    //     System.out.println("Entering args_list()");




    //         expression(); // Parse the first argument

    //     // Check for more arguments separated by commas
    //     if (match(LexicalAnalyzer.TokenType.COMMA)) {
    //         advance(); // Consume the comma token
    //         args_list(); // Parse the next argument
    //     }
    // }

 

}
