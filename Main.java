import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class Main extends JFrame {

    ArrayList<LexicalAnalyzer.Token> tokens;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton analyzeButton;
    private JButton createParseTree;
    private File file;

    public Main() {
        setTitle("Syntax Analyzer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Change background and font colors
        Color background = new Color(230, 230, 230);
        Color textForeground = new Color(50, 50, 50);

        inputTextArea = new JTextArea(15, 50);
        inputTextArea.setBackground(background);
        inputTextArea.setForeground(textForeground);
        inputTextArea.setFont(new Font("Arial", Font.PLAIN, 14));

        outputTextArea = new JTextArea(15, 50);
        outputTextArea.setEditable(false);
        outputTextArea.setBackground(background);
        outputTextArea.setForeground(textForeground);
        outputTextArea.setFont(new Font("Arial", Font.PLAIN, 14));

        analyzeButton = new JButton("Analyze" );
        createParseTree = new JButton("Create Parse Tree");
        analyzeButton.setSize(30, 30);
        createParseTree.setSize(30, 30);


        // Change button colors and font
        analyzeButton.setBackground(new Color(100, 150, 220));
        analyzeButton.setForeground(Color.WHITE);
        analyzeButton.setFont(new Font("Arial", Font.BOLD, 12)); // Adjust font size

        createParseTree.setBackground(new Color(100, 150, 220));
        createParseTree.setForeground(Color.WHITE);
        createParseTree.setFont(new Font("Arial", Font.BOLD, 12)); // Adjust font size

        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);

        analyzeButton.addActionListener(e -> {
            analyzeInput();
        });

        createParseTree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createParseTree();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(background);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Input"), BorderLayout.NORTH);
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(new JLabel("Output"), BorderLayout.NORTH);
        outputPanel.add(outputScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 1, 0, 0));
        buttonPanel.add(analyzeButton);
        buttonPanel.add(createParseTree);

        panel.add(inputPanel);
        panel.add(outputPanel);
        panel.add(buttonPanel);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);

        redirectConsoleOutput();

        // Initialize file
        file = new File("/Users/kareem/Documents/Design-of-Compilers-Project/input.c");

        // Load content of file into inputTextArea
        loadFileContent();

        // Listen for changes in inputTextArea
        inputTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                saveChanges();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                saveChanges();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                saveChanges();
            }
        });
    }

    private void redirectConsoleOutput() {
        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                outputTextArea.append(String.valueOf((char) b));
            }
        };

        PrintStream printStream = new PrintStream(outputStream, true);
        System.setOut(printStream);
    }

    private void analyzeInput() {
        if (!outputTextArea.getText().isEmpty()) {
            outputTextArea.setText("");
        }

        // Call the lexer method to tokenize the content of inputTextArea
        tokens = new ArrayList<>(LexicalAnalyzer.lexicalAnalyzer(inputTextArea.getText()));

        // Print the tokens to the outputTextArea
        for (LexicalAnalyzer.Token token : tokens) {
            outputTextArea.append("Token: " + token.type + ", Value: " + token.value + "\n");
            if (!token.scope.isEmpty()) {
                outputTextArea.append(", Scopes: " + token.scope + "\n");
            } else {
                outputTextArea.append("\n");
            }
        }
    }

    private void createParseTree() {
        // Placeholder method for creating parse tree
        (new SyntaxAnalyzer(tokens)).parse();
    }

    private void loadFileContent() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder fileContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            inputTextArea.setText(fileContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveChanges() {
        // Save content of inputTextArea to file
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.write(inputTextArea.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
