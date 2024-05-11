import java.util.*;
import java.util.regex.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class LexicalAnalyzer {
    public static class Token {
        String type;
        String value;
        String scope;

        Token() {
            scope = "";
        }
    }

    static Set<String> keywords = new HashSet<>(Arrays.asList(
            "auto", "break", "case", "char", "const", "continue", "default",
            "do", "double", "else", "enum", "extern", "float", "for", "goto",
            "if", "int", "long", "register", "return", "short", "signed",
            "sizeof", "static", "struct", "switch", "typedef", "union",
            "unsigned", "void", "volatile", "while"
    ));

    static List<String> symbolTable = new ArrayList<>();
    static Set<String> symbolSet = new HashSet<>();
    static int scopeLevel = 0;

    static List<Token> tokens = new ArrayList<>();

    static boolean isDelimiter(char chr) {
        return (chr == ' ' || chr == '+' || chr == '-' || chr == '*' ||
                chr == '/' || chr == ',' || chr == ';' || chr == '%' ||
                chr == '>' || chr == '<' || chr == '=' || chr == '(' ||
                chr == ')' || chr == '[' || chr == ']' || chr == '{' ||
                chr == '}' || chr == '!' || chr == '&' || chr == '|' || chr == '^' ||
                chr == '~' || chr == '?' || chr == ':' || chr == '.' || chr == '\n');
    }

    static boolean isOperator(String str) {
        Set<String> operators = new HashSet<>(Arrays.asList(
                "+", "-", "*", "/", ">", "<", "=", "&", "|", "^", "~", "!", "%", "?", ":",
                "==", "!=", "&&", "||", "++", "--", "+=", "-=", "*=", "/=", "%=", "&=", "|=", "^=",
                ">>", "<<", "->", ".", "::", ".*"
        ));
        return operators.contains(str);
    }

    static boolean isOperator(char chr) {
        return isOperator(String.valueOf(chr));
    }

    static boolean isValidIdentifier(String str) {
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

    static boolean isKeyword(String str) {
        return keywords.contains(str);
    }

    static boolean isInteger(String str) {
        Pattern intPattern = Pattern.compile("^[-+]?0$|^[-+]?[1-9][0-9]*$");
        return intPattern.matcher(str).matches();
    }

    static boolean isOctal(String str) {
        Pattern octalPattern = Pattern.compile("^0[0-7]+$");
        return octalPattern.matcher(str).matches();
    }

    static boolean isBinary(String str) {
        Pattern binaryPattern = Pattern.compile("^0b[01]+$");
        return binaryPattern.matcher(str).matches();
    }

    static boolean isHexadecimal(String str) {
        Pattern hexPattern = Pattern.compile("^0x[0-9a-fA-F]+$");
        return hexPattern.matcher(str).matches();
    }

    static List<Token> lexicalAnalyzer(String input) {
        String token = "";
        boolean isString = false;
        boolean isChar = false;
        boolean isFloat = false;

        for (int i = 0; i < input.length(); ++i) {
            char chr = input.charAt(i);

            if (isFloat) {
                if (Character.isDigit(chr) || chr == '.' || (chr == 'e' || chr == 'E')) {
                    token += chr;
                    if ((chr == 'e' || chr == 'E') && (input.charAt(i + 1) == '-' || input.charAt(i + 1) == '+')) {
                        token += input.charAt(i + 1);
                        ++i;
                    }
                    continue;
                } else {
                    Token t = new Token();
                    t.type = "Float";
                    t.value = token;
                    tokens.add(t);
                    token = "";
                    isFloat = false;
                }
            }

            if (chr == '\"') {
                if (!isString) {
                    isString = true;
                } else {
                    Token t = new Token();
                    t.type = "String Literal";
                    t.value = token;
                    tokens.add(t);
                    token = "";
                    isString = false;
                }
            } else if (chr == '\'') {
                if (!isChar) {
                    isChar = true;
                } else {
                    Token t = new Token();
                    t.type = "Character Literal";
                    t.value = token;
                    tokens.add(t);
                    token = "";
                    isChar = false;
                }
            } else if (isString || isChar) {
                token += chr;
            } else if (chr == '#') {
                while (i < input.length() && input.charAt(i) != '\n') {
                    ++i;
                }
            } else if (chr == '/' && i + 1 < input.length() && (input.charAt(i + 1) == '/' || input.charAt(i + 1) == '*')) {
                if (input.charAt(i + 1) == '/') {
                    while (i < input.length() && input.charAt(i) != '\n') {
                        ++i;
                    }
                } else {
                    while (i + 1 < input.length() && !(input.charAt(i) == '*' && input.charAt(i + 1) == '/')) {
                        ++i;
                    }
                    ++i;
                }
            } else if (isDelimiter(chr)) {
                if (!token.isEmpty() && !Pattern.matches("^\\s*$", token)) {
                    Token t = new Token();
                    if (isKeyword(token)) {
                        t.type = "Keyword " + token;
                        t.value = token;
                    } else if (isInteger(token)) {
                        t.type = "Integer";
                        t.value = token;
                    } else if (isFloat) {
                        t.type = "Float";
                        t.value = token;
                    } else if (isValidIdentifier(token)) {
                        t.type = "Identifier " + token;
                        t.value = "index " + symbolTable.size();
                        t.scope += " " + scopeLevel;

                        if (chr == '(') {
                            t.type = "Identifier Function " + token;
                            token += "()";
                        }

                        if (!symbolSet.contains(token)) {
                            symbolTable.add(token);
                            symbolSet.add(token);
                        }
                    } else if (isOperator(token)) {
                        t.type = "Operator " + token;
                        t.value = token;
                    } else if (isHexadecimal(token)) {
                        t.type = "Integer Hexadecimal";
                        t.value = token;
                    } else if (isOctal(token)) {
                        t.type = "Integer Octal";
                        t.value = token;
                    } else if (isBinary(token)) {
                        t.type = "Integer Binary";
                        t.value = token;
                    } else {
                        t.type = "Unidentified";
                        t.value = token;
                    }
                    tokens.add(t);
                    token = "";
                }

                int j = i + 1;
                while (j < input.length() && Character.isWhitespace(input.charAt(j))) {
                    ++j;
                }
                if (j < input.length() && isOperator(String.valueOf(chr) + String.valueOf(input.charAt(j)))) {
                    Token t = new Token();
                    t.type = "Operator " + String.valueOf(chr) + String.valueOf(input.charAt(j));
                    t.value = String.valueOf(chr) + String.valueOf(input.charAt(j));
                    tokens.add(t);
                    i = j;
                } else if (isOperator(chr)) {
                    Token t = new Token();
                    t.type = "Operator " + String.valueOf(chr);
                    t.value = String.valueOf(chr);
                    tokens.add(t);
                } else if (chr != ' ' && chr != '\n') {
                    Token t = new Token();
                    t.type = "Delimiter " + String.valueOf(chr);
                    t.value = String.valueOf(chr);
                    tokens.add(t);
                }
            } else if (Character.isDigit(chr)) {
                token += chr;
                if (i + 1 < input.length() && (input.charAt(i + 1) == '.' || input.charAt(i + 1) == 'e' || input.charAt(i + 1) == 'E'
                        || input.charAt(i + 1) == 'e' && input.charAt(i+2) == '-' || input.charAt(i + 1) == 'E' && input.charAt(i+2) == '-')) {
                    isFloat = true;
                }
            } else {
                token += chr;
            }
            if (chr == '{') {
                ++scopeLevel;
            } else if (chr == '}') {
                --scopeLevel;
            }
        }

        return tokens;
    }

    static void printTokens(List<Token> tokens) {
        for (Token token : tokens) {
            System.out.println("Token: " + token.type + ", Value: " + token.value);
            if (!token.scope.isEmpty()) {
                System.out.println(", Scopes: " + token.scope);
            } else {
                System.out.println();
            }
        }
    }

    static void printSymbolTable(List<Token> tokens) {
        System.out.println("----------------Symbol Table---------------");
        for (int i = 0; i < symbolTable.size(); ++i) {
            System.out.println("Identifier: " + symbolTable.get(i) + ", Index: " + i);
        }
    }

    public static void main(String[] args) {
        String filePath = "Design-of-Compilers-Project/input.c"; // replace with your file path
        StringBuilder content = new StringBuilder();
        try {
            Files.lines(Paths.get(filePath)).forEach(line -> content.append(line).append("\n"));
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath);
            e.printStackTrace();
            return;
        }


        List<Token> tokens = lexicalAnalyzer(content.toString());
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(tokens);
        syntaxAnalyzer.parse();
       

        printTokens(tokens);
        printSymbolTable(tokens);
    }
}