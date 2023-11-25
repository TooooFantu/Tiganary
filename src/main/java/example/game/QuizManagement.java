package example.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class QuizManagement {
    private static List<Quiz> quizList = new ArrayList<>();

    public static void loadFromFile() throws FileNotFoundException {
        File file = new File("games\\quiz.txt");
        Scanner sc = new Scanner(file);
        boolean check = false;
        int cnt = 0;
        String question = new String();
        String a = new String();
        String b = new String();
        String c = new String();
        String d = new String();
        String corr = new String();
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.startsWith("@")) {
                if (check) {
                    cnt = 0;
                    Quiz quiz = new Quiz();
                    quiz.setQuestion(question);
                    quiz.setAnswerA(a);
                    quiz.setAnswerB(b);
                    quiz.setAnswerC(c);
                    quiz.setAnswerD(d);
                    quiz.setCorrectAnswer(corr);
                    quizList.add(quiz);
                    question = line.substring(1);
                } else {
                    question = line.substring(1);
                    check = true;
                }
            } else if (line.startsWith("= ")) {
                if (cnt == 0) {
                    a = line.substring(2);
                    cnt++;
                } else if (cnt == 1) {
                    b = line.substring(2);
                    cnt++;
                } else if (cnt == 2) {
                    c = line.substring(2);
                    cnt++;
                } else if (cnt == 3) {
                    d = line.substring(2);
                    cnt++;
                }
            } else if (line.startsWith("+ ")) {
                corr = line.substring(2);
            }
        }
    }

    public static Quiz getRandomQuiz() {
        Random random = new Random();
        int ran = random.nextInt() % quizList.size();
        while (ran < 0 || ran > quizList.size()) {
            ran = random.nextInt() % quizList.size();
        }
        return quizList.get(ran);
    }
}
