import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Token class to represent tokens in the code
class Token {
    String type; // Type of token (e.g., Keyword, Identifier, Operator)
    String value; // Value of the token (e.g., the actual keyword or identifier)

    Token(String type, String value) {
        this.type = type;
        this.value = value;
    }
}

public class Lexer {

    // Set of keywords in C/C++ programming language
    private static final Set<String> keywords = Set.of(
            "auto", "break", "case", "char", "const", "continue", "default",
            "do", "double", "else", "enum", "extern", "float", "for", "goto",
            "if", "int", "long", "register", "return", "short", "signed",
            "sizeof", "static", "struct", "switch", "typedef", "union",
            "unsigned", "void", "volatile", "while"
    );

    // Global variables for symbol table and scope level
    private static final List<String> symbolTable = new ArrayList<>(); // Stores unique identifiers encountered in the code
    private static final Set<String> symbolSet = new HashSet<>(); // Set for fast lookup of identifiers

    // Vector to store tokens after lexical analysis
    private static final List<Token> tokens = new ArrayList<>();

    // Function to check if a character is a delimiter (e.g., space, comma, semicolon)
    private static boolean isDelimiter(char chr) {
        return (chr == ' ' || chr == '+' || chr == '-' || chr == '*' ||
                chr == '/' || chr == ',' || chr == ';' || chr == '%' ||
                chr == '>' || chr == '<' || chr == '(' ||
                chr == ')' || chr == '[' || chr == ']' || chr == '{' ||
                chr == '}' || chr == '!' || chr == '&' || chr == '|' || chr == '^' ||
                chr == '~' || chr == '?' || chr == ':' || chr == '.' || chr == '\n');
    }

    // Function to check if a string is an operator
    private static boolean isOperator(String operator) {
        String[] operators = {"+", "-", "*", "/", ">", "<", "=", "&", "|", "^", "~", "!", "%", "?", ":",
                "==", "!=", "&&", "||", "++", "--", "+=", "-=", "*=", "/=", "%=", "&=", "|=", "^=",
                ">>", "<<", "->", ".", "::", ".*"};
        for (String op : operators) {
            if (op.equals(operator)) {
                return true;
            }
        }
        return false;
    }

    // Function to check if a character is an operator
    private static boolean isOperator(char chr) {
        return false; // Delimiters, not operators
    }

    // Function to check if a string is a valid identifier
    private static boolean isValidIdentifier(String str) {
        if (str.isEmpty() || Character.isDigit(str.charAt(0)) || isDelimiter(str.charAt(0))) {
            return false;
        }
        for (char chr : str.toCharArray()) {
            if (!Character.isLetterOrDigit(chr) && chr != '_') {
                return false;
            }
        }
        return true;
    }

    // Function to check if a string is a keyword
    private static boolean isKeyword(String str) {
        return keywords.contains(str);
    }

    // Functions to check if a string represents a valid number in various formats
    private static boolean isInteger(String str) {
        Pattern intPattern = Pattern.compile("^[-+]?0$|^[-+]?[1-9][0-9]*$");
        Matcher matcher = intPattern.matcher(str);
        return matcher.matches();
    }

    private static boolean isOctal(String str) {
        Pattern octalPattern = Pattern.compile("^0[0-7]+$");
        Matcher matcher = octalPattern.matcher(str);
        return matcher.matches();
    }

    private static boolean isBinary(String str) {
        Pattern binaryPattern = Pattern.compile("^0b[01]+$");
        Matcher matcher = binaryPattern.matcher(str);
        return matcher.matches();
    }

    private static boolean isHexadecimal(String str) {
        Pattern hexPattern = Pattern.compile("^0x[0-9a-fA-F]+$");
        Matcher matcher = hexPattern.matcher(str);
        return matcher.matches();
    }

    // Function to perform lexical analysis on the input code and generate tokens
    private static List<Token> lexicalAnalyzer(String input) {
        // Initialize variables for token processing and flags for different states
        StringBuilder token = new StringBuilder(); // Current token being processed
        boolean isString = false; // Flag to track if inside a string literal
        boolean isChar = false; // Flag to track if inside a character literal
        boolean isFloat = false; // Flag to track if parsing a float
    
        // Iterate through each character in the input string
        for (int i = 0; i < input.length(); i++) {
            char chr = input.charAt(i); // Current character
    
            // Check if the current character is part of a floating-point number
            if (isFloat) {
                // If it's a digit, period, or 'e'/'E' for scientific notation, add it to the token
                if (Character.isDigit(chr) || chr == '.' || (chr == 'e' || chr == 'E')) {
                    token.append(chr);
                    // Skip the next character if it's a negative sign for scientific notation or positive sign
                    if ((chr == 'e' || chr == 'E') && (input.charAt(i + 1) == '-' || input.charAt(i + 1) == '+')) {
                        token.append(input.charAt(i + 1));
                        i++;
                    }
    
                    continue;
                } else {
                    // If the floating-point token is complete, create a token object and add it to the tokens list
                    tokens.add(new Token("Float", token.toString()));
                    token.setLength(0);
                    isFloat = false;
                }
            }
            // Check if the current character is a double quote for string literals
            if (chr == '\"') {
                // Toggle the string flag to handle opening and closing quotes
                if (!isString) {
                    isString = true;
                } else {
                    // If the string literal is complete, create a token object and add it to the tokens list
                    tokens.add(new Token("String Literal", token.toString()));
                    token.setLength(0);
                    isString = false;
                }
            }
            // Check if the current character is a single quote for character literals
            else if (chr == '\'') {
                // Toggle the character flag to handle opening and closing quotes
                if (!isChar) {
                    isChar = true;
                } else {
                    // If the character literal is complete, create a token object and add it to the tokens list
                    tokens.add(new Token("Character Literal", token.toString()));
                    token.setLength(0);
                    isChar = false;
                }
            }
            // If the current character is part of a string or character literal, add it to the token
            else if (isString || isChar) {
                token.append(chr);
            }
            // Check for comments starting with '/' and handle both single-line and multi-line comments
            else if (chr == '/' && i + 1 < input.length() && (input.charAt(i + 1) == '/' || input.charAt(i + 1) == '*')) {
    
                //skip forward with while loop TILL \n if it's a single line comment or till */ if it's a block comment.
                if (input.charAt(i + 1) == '/') {
                    while (i < input.length() && input.charAt(i) != '\n') {
                        i++;
                    }
                } else {
                    while (i + 1 < input.length() && !(input.charAt(i) == '*' && input.charAt(i + 1) == '/')) {
                        i++;
                    }
                    i++; // Skip the closing '/' of the block comment
                }
            }
            // Check for delimiters, operators, and other special characters
            else if (isDelimiter(chr)) {
                // Process the token if it's not empty or whitespace
                if (token.length() > 0 && !Pattern.matches("^\\s*$", token.toString())) {
                    // Determine the type of token based on its content
                    String tokenStr = token.toString();
                    if (isKeyword(tokenStr)) {
                        tokens.add(new Token("Keyword " + tokenStr, tokenStr));
                    } else if (isInteger(tokenStr)) {
                        tokens.add(new Token("Integer", tokenStr));
                    } else if (isFloat) {
                        tokens.add(new Token("Float", tokenStr));
                    } else if (isValidIdentifier(tokenStr)) {
                        tokens.add(new Token("Identifier " + tokenStr, "index " + symbolTable.size()));
                        if (!symbolSet.contains(tokenStr)) {
                            symbolTable.add(tokenStr);
                            symbolSet.add(tokenStr);
                        }
                    } else {
                        tokens.add(new Token("Unidentified", tokenStr));
                    }
                    token.setLength(0);
                }
                // Skip whitespace characters
                int j = i + 1;
                while (j < input.length() && Character.isWhitespace(input.charAt(j))) {
                    j++;
                }
                // Check for single-character operators and delimiters
                // Handle other delimiters like parentheses, brackets, etc.
                if (chr != ' ' && chr != '\n') {
                    tokens.add(new Token("Delimiter " + chr, String.valueOf(chr)));
                }
            } else if (Character.isDigit(chr)) {
                token.append(chr);
                // Check for '.' or 'e'/'E' to determine if it's a floating-point number
                if (i + 1 < input.length() && (input.charAt(i + 1) == '.' || input.charAt(i + 1) == 'e' || input.charAt(i + 1) == 'E'
                        || input.charAt(i + 1) == 'e' && input.charAt(i + 2) == '-' || input.charAt(i + 1) == 'E' && input.charAt(i + 2) == '-')) {
                    isFloat = true;
                }
            }
            // Check for the '=' operator
            else if (chr == '=') {
                if (token.length() > 0) {
                    // If there's already a token, process it first
                    String tokenStr = token.toString();
                    if (isKeyword(tokenStr)) {
                        tokens.add(new Token("Keyword " + tokenStr, tokenStr));
                    } else if (isInteger(tokenStr)) {
                        tokens.add(new Token("Integer", tokenStr));
                    } else if (isFloat) {
                        tokens.add(new Token("Float", tokenStr));
                    } else if (isValidIdentifier(tokenStr)) {
                        tokens.add(new Token("Identifier " + tokenStr, "index " + symbolTable.size()));
                        if (!symbolSet.contains(tokenStr)) {
                            symbolTable.add(tokenStr);
                            symbolSet.add(tokenStr);
                        }
                    } else {
                        tokens.add(new Token("Unidentified", tokenStr));
                    }
                    token.setLength(0);
                }
                // Add '=' operator token
                tokens.add(new Token("Operator =", String.valueOf(chr)));
            }
            // If none of the above conditions are met, add the character to the token
            else {
                token.append(chr);
            }
        }
        return tokens;
    }

    // Function to print the tokens generated by lexical analysis
    private static void printTokens(List<Token> tokens) {
        for (Token token : tokens) {
            System.out.println("Token: " + token.type + ", Value: " + token.value);
        }
    }

    // Function to print the symbol table containing unique identifiers
    private static void printSymbolTable() {
        System.out.println("----------------Symbol Table---------------");
        for (int i = 0; i < symbolTable.size(); i++) {
            System.out.println("Identifier: " + symbolTable.get(i) + ", Index: " + i);
        }
    }

    // Main function
    public static void main(String[] args) {
        String inputCode = """
                int main() {
                    int x = 10;
                    float y = 3.14;
                    char c = 'a';
                    String str = "hello";
                    return 0;
                }
                """;

        String input2 = """
                int main() {
                    int x = 10;
                    float y = 3.14;
                    char c = 'a';
                    String str = "hello";
                    return 0;
                }
                """;

        List<Token> tokens = lexicalAnalyzer(input2); // Perform lexical analysis

        System.out.println("Tokens");

        printTokens(tokens); // Print tokens generated by lexical analysis
        printSymbolTable(); // Print symbol table
    }
}
