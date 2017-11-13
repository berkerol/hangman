package hangman;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;

public class Hangman {

    private static final ArrayList<String> ALL_WORDS = new ArrayList<>();
    private static final String EOL = System.lineSeparator();
    private static final ImageIcon[] ICONS = {new ImageIcon("images/0.jpg"), new ImageIcon("images/1.jpg"), new ImageIcon("images/2.jpg"),
        new ImageIcon("images/3.jpg"), new ImageIcon("images/4.jpg"), new ImageIcon("images/5.jpg"), new ImageIcon("images/6.jpg"),
        new ImageIcon("images/7.jpg"), new ImageIcon("images/8.jpg"), new ImageIcon("images/9.jpg")};
    private static final JLabel FIGURE_LABEL = new JLabel(ICONS[0]);
    private static final Random RANDOM = new Random();
    private static final int[] RESULTS = new int[6];
    private static final JLabel SCORE_LABEL = new JLabel("", SwingConstants.CENTER);
    private static final JTextArea TEXT_AREA = new JTextArea(17, 30);
    private static final String TITLE = "Hangman";
    private static final JLabel WORD_LABEL = new JLabel("", SwingConstants.CENTER);
    private static ArrayList<String> allNames;
    private static boolean deleteNames;
    private static boolean deleteWords;
    private static ArrayList<Character> encrypted;
    private static boolean firstTime = true;
    private static boolean letterGuessType;
    private static int score;
    private static boolean stickControl;
    private static int stickLimit;
    private static int sticks;
    private static ArrayList<Character> unusedLetters;
    private static String word;
    private static boolean wordGuessType;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TEXT_AREA.setEditable(false);
                TEXT_AREA.setLineWrap(true);
                TEXT_AREA.setWrapStyleWord(true);
                ((DefaultCaret) (TEXT_AREA.getCaret())).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
                JButton giveUpButton = new JButton("Give Up");
                giveUpButton.setMnemonic(KeyEvent.VK_G);
                giveUpButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        exit();
                    }
                });
                JButton wordButton = new JButton("Guess Word");
                wordButton.setMnemonic(KeyEvent.VK_W);
                wordButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (wordGuessType) {
                            Object o = JOptionPane.showInputDialog(null, "Guess the word!", TITLE, JOptionPane.QUESTION_MESSAGE, null, allNames.toArray(), allNames.get(0));
                            if (o != null && checkWord((String) o)) {
                                exit();
                            }
                        }
                        else {
                            String s = JOptionPane.showInputDialog(null, "Guess the word!", TITLE, JOptionPane.QUESTION_MESSAGE);
                            if (s != null && checkWord(s)) {
                                exit();
                            }
                        }
                    }
                });
                JButton letterButton = new JButton("Guess Letter");
                letterButton.setMnemonic(KeyEvent.VK_L);
                letterButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (letterGuessType) {
                            Object o = JOptionPane.showInputDialog(null, "Guess a letter!", TITLE, JOptionPane.QUESTION_MESSAGE, null, unusedLetters.toArray(), unusedLetters.get(0));
                            if (o != null) {
                                checkLetter((char) o);
                            }
                        }
                        else {
                            String s = JOptionPane.showInputDialog(null, "Unused letters: " + print(unusedLetters) + EOL + "Guess a letter!", TITLE, JOptionPane.QUESTION_MESSAGE);
                            if (s != null) {
                                char letter = s.toLowerCase(Locale.ENGLISH).charAt(0);
                                if (s.length() != 1 || letter < 97 || letter > 122) {
                                    TEXT_AREA.append("Not a letter." + EOL);
                                }
                                else if (!unusedLetters.contains(letter)) {
                                    TEXT_AREA.append("This letter was used before." + EOL);
                                }
                                else {
                                    checkLetter(letter);
                                }
                            }
                        }
                    }
                });
                JButton randomButton = new JButton("Random Letter");
                randomButton.setMnemonic(KeyEvent.VK_R);
                randomButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        char letter;
                        do {
                            letter = (char) (RANDOM.nextInt(26) + 97);
                        }
                        while (!unusedLetters.contains(letter));
                        checkLetter(letter);
                    }
                });
                JPanel textPanel = new JPanel(new BorderLayout()), infoPanel = new JPanel(new BorderLayout()), buttonPanel = new JPanel();
                textPanel.add(WORD_LABEL, BorderLayout.PAGE_START);
                textPanel.add(new JScrollPane(TEXT_AREA), BorderLayout.CENTER);
                infoPanel.add(SCORE_LABEL, BorderLayout.PAGE_START);
                infoPanel.add(FIGURE_LABEL, BorderLayout.CENTER);
                buttonPanel.add(giveUpButton);
                buttonPanel.add(wordButton);
                buttonPanel.add(letterButton);
                buttonPanel.add(randomButton);
                JFrame frame = new JFrame(TITLE);
                frame.add(textPanel, BorderLayout.LINE_START);
                frame.add(infoPanel, BorderLayout.LINE_END);
                frame.add(buttonPanel, BorderLayout.PAGE_END);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                start();
            }
        });
    }

    private static void checkLetter(char letter) {
        unusedLetters.remove((Character) (letter));
        boolean letterExists = false;
        for (int i = 0; i < word.length(); i++) {
            if (("" + word.charAt(i)).equalsIgnoreCase("" + letter)) {
                encrypted.set(i, word.charAt(i));
                letterExists = true;
            }
        }
        if (letterExists) {
            TEXT_AREA.append(letter + " exists." + EOL);
            SCORE_LABEL.setText("SCORE: " + --score);
            WORD_LABEL.setText(print(encrypted));
        }
        else {
            TEXT_AREA.append(letter + " does not exist." + EOL);
            if (sticks < stickLimit) {
                FIGURE_LABEL.setIcon(ICONS[++sticks]);
            }
        }
        controls();
    }

    private static boolean checkWord(String guess) {
        if (guess.equalsIgnoreCase(word)) {
            TEXT_AREA.append("Correct.");
            RESULTS[1] += score;
            RESULTS[4]++;
            if (sticks < stickLimit) {
                RESULTS[2] += score;
                RESULTS[5]++;
            }
            return true;
        }
        else {
            TEXT_AREA.append(guess + " is incorrect." + EOL);
            if (sticks < stickLimit) {
                FIGURE_LABEL.setIcon(ICONS[++sticks]);
            }
            controls();
            return false;
        }
    }

    private static void controls() {
        if (print(encrypted).replaceAll("\\s+", "").equalsIgnoreCase(word)) {
            TEXT_AREA.append("Word is completed.");
            exit();
            return;
        }
        if (stickControl && sticks == stickLimit) {
            stickControl = false;
            int exit = JOptionPane.showConfirmDialog(null, "Figure is completed. Do you want to continue anyway?", TITLE, JOptionPane.YES_NO_OPTION);
            if ((exit == 1) || (exit == -1)) {
                exit();
            }
        }
    }

    private static void exit() {
        RESULTS[0] += score;
        RESULTS[3]++;
        WORD_LABEL.setText(word);
        if (deleteWords) {
            ALL_WORDS.remove(word);
            if (deleteNames) {
                allNames.remove(word);
            }
        }
        if (ALL_WORDS.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No more words to guess.", TITLE, JOptionPane.ERROR_MESSAGE);
            statistics();
            return;
        }
        int exit = JOptionPane.showConfirmDialog(null, "Do you want to continue?", TITLE, JOptionPane.YES_NO_OPTION);
        if ((exit == 1) || (exit == -1)) {
            statistics();
        }
        else {
            start();
        }
    }

    private static String print(ArrayList<Character> characters) {
        StringBuilder sb = new StringBuilder().append(characters.get(0));
        for (int i = 1; i < characters.size(); i++) {
            sb.append(" ").append(characters.get(i));
        }
        return sb.toString();
    }

    private static void start() {
        Scanner input = null;
        try {
            input = new Scanner(new File("HangmanConfiguration.ini"), "UTF-8");
        }
        catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex, TITLE, JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        letterGuessType = input.nextInt() != 0;
        input.nextLine();
        wordGuessType = input.nextInt() != 0;
        input.nextLine();
        deleteWords = input.nextInt() != 0;
        input.nextLine();
        deleteNames = input.nextInt() != 0;
        input.nextLine();
        stickLimit = input.nextInt();
        stickControl = true;
        if (firstTime) {
            firstTime = false;
            input.nextLine();
            Scanner db = null;
            try {
                db = new Scanner(new File(input.nextLine()), "UTF-8");
            }
            catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, ex, TITLE, JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            while (db.hasNextLine()) {
                ALL_WORDS.add(db.nextLine());
            }
            allNames = new ArrayList<>(ALL_WORDS);
            db.close();
        }
        input.close();
        word = ALL_WORDS.get(RANDOM.nextInt(ALL_WORDS.size()));
        encrypted = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == ' ') {
                encrypted.add('_');
            }
            else {
                encrypted.add('*');
            }
        }
        WORD_LABEL.setText(print(encrypted));
        unusedLetters = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            unusedLetters.add((char) (i + 97));
        }
        sticks = 0;
        FIGURE_LABEL.setIcon(ICONS[sticks]);
        score = word.length();
        SCORE_LABEL.setText("SCORE: " + score);
        TEXT_AREA.setText("");
    }

    private static void statistics() {
        String s = "Total games: " + RESULTS[3];
        if (RESULTS[4] != 0) {
            s += EOL + "Total correct games: " + RESULTS[4];
        }
        if (RESULTS[5] != 0) {
            s += EOL + "Total correct games within the limit: " + RESULTS[5];
        }
        s += EOL + "Total score: " + RESULTS[0];
        if (RESULTS[1] != 0) {
            s += EOL + "Total correct score: " + RESULTS[1];
        }
        if (RESULTS[2] != 0) {
            s += EOL + "Total correct score within the limit: " + RESULTS[2];
        }
        s += String.format("%nAverage score: %.2f", 1.0 * RESULTS[0] / RESULTS[3]);
        if (RESULTS[4] != 0) {
            s += String.format("%nAverage correct score: %.2f", 1.0 * RESULTS[1] / RESULTS[4]);
        }
        if (RESULTS[5] != 0) {
            s += String.format("%nAverage correct score within the limit: %.2f", 1.0 * RESULTS[2] / RESULTS[5]);
        }
        TEXT_AREA.setText(s);
    }
}
