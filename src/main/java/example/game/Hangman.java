package example.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Hangman {
    private String curWord;
    private String wordState;

    private int wrongCount;
    private int maxWrong;

    private List<Character> answerList = new ArrayList<>();

    public Hangman() {
        wrongCount = 0;
        maxWrong = 9;
    }

    public void loadFromFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("games\\words_alpha.txt"));
        List<String> words = new ArrayList<>();

        while (scanner.hasNext()) {
            words.add(scanner.nextLine());
        }

        Random rand = new Random();
        curWord = words.get(rand.nextInt(words.size()));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < curWord.length(); i++) {
            sb.append("_");
        }
        wordState = sb.toString();
    }

    public boolean isWin() {
        return wordState.equals(curWord);
    }

    public boolean isLose() {
        return wrongCount == maxWrong;
    }

    public int getRemainingAnswer() {
        return maxWrong - wrongCount;
    }

    public String getWordState() {
        return wordState;
    }

    public String getCurWord() {
        return curWord;
    }

    public boolean check(Character c) {
        char ch = c;
        if (!answerList.contains(c)) {
            answerList.add(c);
        }
        if (curWord.contains(c.toString())) {
            StringBuilder sb = new StringBuilder(wordState);
            for (int i = 0; i < curWord.length(); i++) {
                if (curWord.charAt(i) == ch) {
                    sb.setCharAt(i, ch);
                }
            }
            wordState = sb.toString();
        } else {
            wrongCount++;
            return false;
        }
        return true;
    }

    public String getAnswerList() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < answerList.size(); i++) {
            sb.append(answerList.get(i)).append(".");
        }
        return sb.toString();
    }
}
