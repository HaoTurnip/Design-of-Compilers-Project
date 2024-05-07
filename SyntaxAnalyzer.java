


/**
 TEST case1 : printf("Hello World");

 TEST case2 : int x = 10;

 Test case3 : int main() {
                int x = 10;
              }

  

 **/






// Grammar RULES for the C- language
// program -> declaration_list |initial_statement_list declaration_list

// initial_statement_list -> initial_statement
//                         | initial_statement_list initial_statement

// initial_statement -> function_call_statement
//                     | other_initial_statement

// function_call_statement -> function_call ;
//                          | function_call_statement function_call

// other_initial_statement -> declaration
//                          | struct_declaration

// declaration_list -> declaration
//                   | declaration_list declaration | Ɛ

// declaration -> variable_declaration
//              | function_declaration

// variable_declaration -> type_specifier ID ;
//                       | type_specifier ID [ NUM ] ;

// type_specifier -> int
//                 | float
//                 | char
//                 | double
//                 | void

// function_declaration -> type_specifier ID ( parameters ) compound_statement

// parameters -> parameter_list
//             | void

// parameter_list -> parameter
//                 | parameter_list , parameter

// parameter -> type_specifier ID
//            | type_specifier ID []

// compound_statement -> { statement_list }

// statement_list -> statement
//                  | statement_list statement

// statement ->  if_statement | while_statement | do_while_statement | for_statement | return_statement | switch_statement | compound_statement | declaration | assignment_statement | break_statement | continue_statement | switch_cases

// expression_statement -> expression ;
//                       | ;

// expression -> ID = expression
//             | simple_expression

// simple_expression -> additive_expression
//                    | simple_expression RELOP additive_expression

// additive_expression -> multiplicative_expression
//                       | additive_expression ADDOP multiplicative_expression

// multiplicative_expression -> primary_expression
//                             | multiplicative_expression MULOP primary_expression

// primary_expression -> ID
//                      | NUM
//                      | ( expression )
//                      | function_call

// function_call -> ID ( argument_list )

// argument_list -> expression
//                 | argument_list , expression

// selection_statement -> if ( expression ) statement
//                      | if ( expression ) statement else statement

// iteration_statement -> while ( expression ) statement
//                      | for ( expression_statement ; expression_statement ; expression ) statement

// jump_statement -> return ;
//                 | return expression ;

// class_declaration -> class ID { member_declaration_list }

// member_declaration_list -> member_declaration
//                          | member_declaration_list member_declaration

// member_declaration -> type_specifier ID ;
//                     | type_specifier ID ( parameters ) compound_statement ;

// struct_declaration -> struct ID { struct_member_list }

// struct_member_list -> struct_member
//                      | struct_member_list struct_member

// struct_member -> type_specifier ID ;
//                | type_specifier ID [ NUM ] ;




public class SyntaxAnalyzer {
    private int currentTokenIndex;
    private Token[] tokens;

    public SyntaxAnalyzer(Token[] tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;

    }


    private boolean match(Token expected) {
        if (currentTokenIndex < tokens.length && tokens[currentTokenIndex].equals(expected)) {
            return true;
        }
        return false;
    }


    public void advance() {
        currentTokenIndex++;
    }




    

}
