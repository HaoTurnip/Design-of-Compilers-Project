import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
        Color background = new Color(0, 0, 0);
        Color textForeground = new Color(0, 255, 0);
        Color buttonBackground = new Color(0, 128, 0);
        Color buttonForeground = Color.WHITE;
        Color scrollBarColor = new Color(0, 255, 0);

        inputTextArea = new JTextArea(15, 50);
        inputTextArea.setBackground(background);
        inputTextArea.setForeground(textForeground);
        inputTextArea.setFont(new Font("Minecraft", Font.PLAIN, 16));
        inputTextArea.setCaretColor(Color.GREEN);

        outputTextArea = new JTextArea(15, 50);
        outputTextArea.setEditable(false);
        outputTextArea.setBackground(background);
        outputTextArea.setForeground(textForeground);
        outputTextArea.setFont(new Font("Minecraft", Font.PLAIN, 16));

        analyzeButton = new JButton("Tokenize this");
        createParseTree = new JButton("Parse this");
        analyzeButton.setSize(30, 30);
        createParseTree.setSize(30, 30);

        // Change button colors and font
        analyzeButton.setBackground(buttonBackground);
        analyzeButton.setForeground(buttonForeground);
        analyzeButton.setFont(new Font("Minecraft", Font.BOLD, 18)); // Adjust font size

        createParseTree.setBackground(buttonBackground);
        createParseTree.setForeground(buttonForeground);
        createParseTree.setFont(new Font("Minecraft", Font.BOLD, 18)); // Adjust font size

        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
        inputScrollPane.getVerticalScrollBar().setBackground(scrollBarColor);
        inputScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, Integer.MAX_VALUE));
        inputScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
        outputScrollPane.getVerticalScrollBar().setBackground(scrollBarColor);
        outputScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, Integer.MAX_VALUE));
        outputScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

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
        panel.setLayout(new BorderLayout());
        panel.setBackground(background);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(outputScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBackground(background);
        buttonPanel.add(analyzeButton);
        buttonPanel.add(createParseTree);

        panel.add(inputPanel, BorderLayout.WEST);
        panel.add(outputPanel, BorderLayout.EAST);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);

        redirectConsoleOutput();

        // Initialize file
        file = new File("Design-of-Compilers-Project/input.c"); // Relative path

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

        // Add the transparent glass pane with scanline effect
        ScanlineGlassPane glassPane = new ScanlineGlassPane();
        setGlassPane(glassPane);
        glassPane.setVisible(true); // Make the glass pane visible
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

        List<String> symbolTable = LexicalAnalyzer.symbolTable;

        // Print the tokens to the outputTextArea
        for (LexicalAnalyzer.Token token : tokens) {
            outputTextArea.append("Token: " + token.type + ", Value: " + token.value + "\n");
            if (!token.scope.isEmpty()) {
                outputTextArea.append(", Scopes: " + token.scope + "\n");
            } else {
                outputTextArea.append("\n");
            }
        }

        outputTextArea.append("----------------Symbol Table---------------\n");
        for (int i = 0; i < symbolTable.size(); ++i) {
            outputTextArea.append("Identifier: " + symbolTable.get(i) + ", Index: " + i + "\n");
        }

        //print the symbol table
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
        SwingUtilities.invokeLater(Main::new);
    }

    // Glass pane class for CRT scanline effect
    private static class ScanlineGlassPane extends JComponent {
        private static final Color SCANLINE_COLOR = new Color(0, 128, 0, 75); // Dark green with transparency
        private static final int SCANLINE_HEIGHT = 2; // Height of each scanline
        private static final int SCANLINE_GAP = 3; // Gap between scanlines
        private static final int SCANLINE_SPEED = 1; // Speed of scanline movement
        private int scanlinePosition = 0; // Current position of the scanline

        public ScanlineGlassPane() {
            setOpaque(false); // Make the glass pane transparent
            startScanlineAnimation();
        }

        private void startScanlineAnimation() {
            Timer timer = new Timer(20, e -> {
                scanlinePosition += SCANLINE_SPEED;
                if (scanlinePosition >= SCANLINE_HEIGHT + SCANLINE_GAP) {
                    scanlinePosition = 0;
                }
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(SCANLINE_COLOR);
            int y = scanlinePosition;
            while (y < getHeight()) {
                g2d.fillRect(0, y, getWidth(), SCANLINE_HEIGHT);
                y += SCANLINE_HEIGHT + SCANLINE_GAP;
            }
            g2d.dispose();
        }
    }
}
