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
    public String TYPE_SPECIFIER = "Keyword (int|float|char|double|short|long|signed|unsigned|void|String|bool|struct|enum|typedef)";
    // public String KEYWORD = "Keyword *";
    public String IDENTIFIER = "Identifier .*"; 
    public String NUM = "Integer| Float .*";
    

    private int currentTokenIndex;
    private List<LexicalAnalyzer.Token> tokens;

    



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

        if (match(TYPE_SPECIFIER) ) {
            
            declaration();
            declaration_list();

        } else {
            return;
        }
     
    }

    private void declaration() {
        if (match(TYPE_SPECIFIER) && !tokens.get(currentTokenIndex).value.equals("struct") && !tokens.get(currentTokenIndex).value.equals("enum") && !tokens.get(currentTokenIndex).value.equals("typedef") ) {
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
                    System.out.println("Function declaration in var dec");
                    currentTokenIndex = 0;

                    fun_declaration();
             
                }else {

                    var_declaration();

                    //var_declaration();
                  
                }
            }

        } else if (match("Keyword struct") && tokens.get(currentTokenIndex).value.equals("struct")) {
            advance();
            struct_declaration();
        } else if (match("Keyword enum")  && tokens.get(currentTokenIndex).value.equals("enum") ) {
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

    private void fun_declaration() {
        if (match(TYPE_SPECIFIER)) {
            advance();
            if (match(IDENTIFIER)) {
                advance();
                if (match("Delimiter \\(")) {
                    advance();
                    params();
                    if (match("Delimiter \\)")) {
                        advance();
                        compound_stmt();
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

        } else {
            System.out.println("Syntax Error missing type specifier");
            System.exit(1); // Exit with error code 1

        }
    }

    private void params() {
        if (match(TYPE_SPECIFIER)) {
            param_list();
        } else if (match("Keyword void")) {
            advance();
        } else {
            System.out.println("Syntax Error missing type specifier");
        }
    }

    private void param_list() {
        param();
        if (match("Delimiter ,")) {
            advance();
            param_list();
        } else {
            return;
        }
    }

    private void param() {
        if (match(TYPE_SPECIFIER)) {
            advance();
            if (match(IDENTIFIER)) {
                advance();
                if (match("Delimiter \\[")) {
                    advance();
                    if (match("Delimiter \\]")) {
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

    private void compound_stmt() {
        if (match("Delimiter \\{")) {
            System.out.println("Compound statement");
            advance();
            statement_list();
            if (match("Delimiter \\}")) {
                advance();
                System.out.println("Function declaration Done");
            } else {
                System.err.println("Syntax Error missing }");
            }
        } else {
            System.err.println("Syntax Error missing {");
            System.exit(1); // Exit with error code 1

        }
    }

    private void statement_list() {
        if (match(TYPE_SPECIFIER) || match(IDENTIFIER) || match("Keyword if") || match("Keyword while") || match("Keyword for") || match("Keyword do") || match("Keyword return") || match("Delimiter \\{" )) {
            System.out.println("---------------------------------------------------------------------------Statement list");
            statement();
            statement_list();
        } else {
            return;
        }
    }

    private void statement() {
        if (match(TYPE_SPECIFIER)) {
            advance();
            if (match(IDENTIFIER)){
            advance();  
           var_declaration();
            } else {
                System.err.println("Syntax Error missing identifier");
            }
        } else if (match(IDENTIFIER)) {
            expression_stmt();
        } else if (match("Keyword if")) {
            System.out.println("Selection statement entered");
            selection_stmt();
        } else if (match("Keyword while") || match("Keyword for") || match("Keyword do")) {
            System.err.println("Iteration statement entered");
            iteration_stmt();
        } else if (match("Keyword return")) {
            return_stmt();
        } else if (match("Delimiter \\{")) {
            compound_stmt();
        } else {
            System.err.println("Syntax Error missing statement");
            System.exit(1); // Exit with error code 1

        }
    }

    private void expression_stmt() {
        if (match("Delimiter ;")) {
            advance();
        } else {
            expression();
            if (match("Delimiter ;")) {
                advance();
            } else {
                System.err.println("Syntax Error missing ;");
                System.exit(1); // Exit with error code 1

            }
        }
    }

    private void selection_stmt() {
        if (match("Keyword if")) {
            advance();
            if (match("Delimiter \\(")) {
                advance();
                expression();
                if (match("Delimiter \\)")) {
                    advance();
                    statement();
                    if (match("Keyword else")) {
                        advance();
                        statement();
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


    private void iteration_stmt() {
        if (match("Keyword while")) {
            advance();
            if (match("Delimiter \\(")) {
                advance();
                expression();
                if (match("Delimiter \\)")) {
                    advance();
                    statement();
                } else {
                    System.out.println("Syntax Error missing )");
                }
            } else {
                System.out.println("Syntax Error missing (");
            }
        } else if (match("Keyword for")) {
            advance();
            if (match("Delimiter \\(")) {
                advance();

                if (match(TYPE_SPECIFIER)){
                    advance();
                    if (match(IDENTIFIER)){
                        advance();
                        var_declaration();
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
                    expression();

                    if (match("Delimiter ;")) {
                        advance();
                        expression();
                    } else {
                        System.out.println("Syntax Error missing ;");
                    }

                    if (match("Delimiter \\)")) {
                        System.out.println("For loop done");
                        advance();
                        statement();
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
            statement();
            if (match("Keyword while")) {
                advance();
                if (match("Delimiter \\(")) {
                    advance();
                    expression();
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

    private void return_stmt() {
        if (match("Keyword return")) {
            advance();
            if (match("Delimiter ;")) {
                advance();
            } else {
                expression();
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
            declaration();
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
    private void var_declaration() {

        if (match("Operator =")) {
            advance();

            expression();
        
           if ( match("Delimiter ;")) {
            advance();
            System.out.println("Variable declaration with assignment done");
            
           }else {
               System.out.println("Syntax Error missing in assignment  ;");
               System.exit(1);
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
        System.out.println("Entering expression()");
        conditional_expression();
    }

    private void conditional_expression() {
        System.out.println("Entering conditional_expression()");
        logical_or_expression(); 

        if (match("Operator \\? ")) {
            advance(); // Consume the '?' token

            expression();

            // Check for the colon ':' token
            if (match("Operator :")) {
                advance();

                expression();
            } else {
                System.err.println("Syntax error: Expected ':' after the true condition in ternary operator");
                System.exit(1);

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
                System.exit(1);

                return;
            }
        }
    }

    private void logical_and_expression() {
       equality_expression();

        while (match("Operator &&")) {
            advance(); 

            // Check if there is a valid token after the logical AND operator
            if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")) {
                equality_expression();
            } else {
                System.err.println("Syntax error: Expected identifier, number, or expression after logical AND operator");
                System.exit(1);

                return;
            }
        }
    }

    private void equality_expression() {
        relational_expression();

        while (match("Operator ==") || match("Operator !=")) {
            advance(); 

            if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")) {
                relational_expression();
            } else {
                // Error handling: Expected identifier, number, or left parenthesis after comparison operator
                System.err.println("Syntax error: Expected identifier, number, or expression after comparison operator");
                return;
            }
        }
    }


    private void relational_expression() {
        additive_expression();

        while (match("Operator <") || match("Operator >") || match("Operator <=") || match("Operator >=")) {
            advance(); // Consume the relational operator

            // Check if there is a valid token after the relational operator
            if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")) {
                // Valid token found, continue parsing the expression
                additive_expression();
            } else {
                // Error handling: Expected identifier, number, or left parenthesis after relational operator
                System.err.println("Syntax error: Expected identifier, number, or expression after relational operator");
                return;
            }
        }
    }
    private void additive_expression() {
        multiplicative_expression();

        while (match("Operator \\+") || match("Operator -")) {
           
            advance(); // Consume the additive operator

            // Check if there is a valid token after the additive operator
            if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")) {
                // Valid token found, continue parsing the expression
                multiplicative_expression();
            } else {
                // Error handling: Expected identifier, number, or left parenthesis after additive operator
                System.err.println("Syntax error: Expected identifier, number, or expression after additive operator");
                return;
            }
        }
    }

    private void multiplicative_expression() {
        unary_expression();

        while (match("Operator \\*") || match("Operator /") || match("Operator %")){
            advance(); // Consume the multiplicative operator

            if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")){
                // Valid token found, continue parsing the expression
                unary_expression();
            } else {
                // Error handling: Expected identifier, number, or left parenthesis after multiplicative operator
                System.err.println("Syntax error: Expected identifier, number, or expression after multiplicative operator");
                System.exit(1);

                return;
            }
        }
    }

    private void unary_expression() {
        System.out.println("Entering unary_expression()");
        System.out.println("Unary expression debug x: " + tokens.get(currentTokenIndex).value + " " + tokens.get(currentTokenIndex).type );

        if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")) {
            System.out.println("Parsed primary expression");
            primary_expression(); 

        } else if (match("Operator \\+") || match("Operator -") || match("Operator ~") || match("Operator !") || match("Operator \\*") || match("Operator &") || match("Operator sizeof")) {
            advance(); // Consume the unary operator

            // Check if there is a valid token after the unary operator
            if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")) {
                // Valid token found, continue parsing the expression
                unary_expression();
            } else {
                // Error handling: Expected identifier, number, or left parenthesis after unary operator
                System.err.println("Syntax error: Expected identifier, number, or expression after unary operator");
                System.exit(1);

                return;
            }
        }
        else if (match("Operator \\+\\+") || match("Operator --")) 
        {

            int prevIndex = currentTokenIndex - 1;
            int nextIndex = currentTokenIndex + 1;
            boolean isPrevIdentifier = prevIndex >= 0 && tokens.get(prevIndex).type.matches(IDENTIFIER);
            boolean isNextIdentifier = nextIndex < tokens.size() && tokens.get(nextIndex).type.matches(IDENTIFIER);

            System.out.println("TESTTT");

            String unaryOp = tokens.get(currentTokenIndex).value;
            advance(); // Consume the unary operator

            // Check if there is a valid token after the unary operator
            if(!isPrevIdentifier && !isNextIdentifier)
            {
                System.err.println("Missing identifier");
                System.exit(0);
                return;

            }
            else if (match(IDENTIFIER)) {
                
                advance();

                // Parse the post-increment/decrement expression
                System.out.println("Parsed post-" + unaryOp + " expression");
            }
            // Arithmtic op
        else if(match(" Operator \\+ ") || match(" Operator - ") || match(" Operator * ") || match(" Operator / ") || match(" Operator % "))
            {



                unary_expression();
            }
            else if (match("Delimeter ;"))
            {





                return; // NO ITS NEEDED

            }
            else {

                // Error handling: Expected identifier after unary operator

               // System.err.println("Syntax error: Expected identifier after unary operator '" + unaryOp + "'");
                return;
            }
        }
    }




    private void primary_expression()
    {
        System.out.println("Entering primary_expression()");

        if (match(NUM)) {

            System.out.println("Parsed number: " + tokens.get(currentTokenIndex).value);
            advance(); // Consume the number token
            System.out.println("Current token index: " + currentTokenIndex);

            arrayretract = false;
        } else if (match("Delimiter \\(")) {
            advance(); 
            expression(); // Parse the enclosed expression
            arrayretract = false;
            if (match("Delimiter \\)")) {
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
                function_call(); // Parse function call
            }
            else if(match("Delimiter \\["))

            {

               inParenthesis = true; arrayretract = true;
                retract();


            }



            else {
                // It's a regular identifier
                currentTokenIndex = currentIndex; // Reset index
                // Handle regular identifier logic here

                System.out.println("Parsed identifier: " + tokens.get(currentIndex).value);
                advance(); 

                // Check if the next token is a unary operator
                if (match("Operator \\+\\+") || match("Operator --")) {
                    arrayretract = false;
                    // Handle unary expression
                    unary_expression();
                }
            }
        }
    }




    private void function_call() {
        if (match(IDENTIFIER)) {

            advance(); 
        } else {
            System.err.println("Syntax error: Expected identifier for function name in function call");
            return;
        }

        // Check for left parenthesis
        if (match( "Delimiter \\(")) {
            advance(); 

        } else {
            // Error handling: Expected left parenthesis
            System.err.println("Syntax error: Expected '(' after function name in function call");
            return;
        }

        

        args_opt();

        // Check for right parenthesis
        if (match( "Delimiter \\)")) {
            advance(); // Consume the right parenthesis token

            while (match("Delemiter \\.")) {
                advance(); // Consume the dot operator
                // Repeat function call parsing for chaining
                function_call();
                retract();
            }

            if(match("Delimiter ;"))
            {
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

    private void args_opt()
    {


        if (match(IDENTIFIER) || match(NUM) || match("Delimiter \\(")) {
            System.out.println("Entering args_opt()");

            args_list(); // Parse the arguments list
        } else {
            System.out.println("No arguments found in args_opt()");
            


        }
    }

    private void args_list() {
        System.out.println("Entering args_list()");




            expression(); // Parse the first argument

        // Check for more arguments separated by commas
        if (match("Delimiter ,")) {
            advance(); // Consume the comma token
            args_list(); // Parse the next argument
        }
    }

 

}
