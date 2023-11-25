package example.demodic;

import example.game.Hangman;
import example.game.Quiz;
import example.game.QuizManagement;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameUIController implements Initializable {
    private double x, y;
    private Parent root;
    private Stage stage;

    @FXML
    private ImageView Exit;

    //Slider
    @FXML
    private Label Menu;
    @FXML
    private Label MenuClose;
    @FXML
    private AnchorPane slider;
    @FXML
    private AnchorPane backSlider;

    //tab
    @FXML
    private Tab quizTab;
    @FXML
    private Tab hangmanTab;
    @FXML
    private TabPane tabContainer;

    //pane
    @FXML
    private AnchorPane quizPane;
    @FXML
    private AnchorPane hangmanPane;

    @FXML
    private AnchorPane notification;
    @FXML
    private Button confirm;
    //
    @FXML
    private AnchorPane hangmanNotification;
    @FXML
    private Button hangmanConfirm;

    //quizTab:
    @FXML
    private WebView question;
    WebEngine questionEngine;
    @FXML
    private RadioButton a;
    @FXML
    private RadioButton b;
    @FXML
    private RadioButton c;
    @FXML
    private RadioButton d;
    //
    private String answer;
    private String correctAnswer;

    @FXML
    private Label info;
    @FXML
    private Label showCorrect;

    private Quiz preQuiz = new Quiz();

    TranslateTransition notificationSlide = new TranslateTransition();

    TranslateTransition hangmanNotificationSlide = new TranslateTransition();

    //hangman tab:
    @FXML
    private Label wordState;
    @FXML
    private Label remainingAnswer;
    @FXML
    private Label answerList;
    @FXML
    private TextField charTextField;
    @FXML
    private Label hangmanInfo;
    @FXML
    private Label hangmanShowCorrect;
    private Hangman hangman;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transition();
        //quizTab
        questionEngine = question.getEngine();
        questionEngine.loadContent(Dictionary.getStyle());
        loadQuiz();
        //
        changePane();
        notificationTransition();
        //
        try {
            loadHangman();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        hangmanNotificationTransition();

    }
    //loadQuiz
    public void loadQuiz() {
        Quiz quiz = QuizManagement.getRandomQuiz();
        while (quiz.equals(preQuiz)) {
            quiz = QuizManagement.getRandomQuiz();
        }
        preQuiz = quiz;
        String ques = quiz.getQuestion();
        a.setText(quiz.getAnswerA());
        b.setText(quiz.getAnswerB());
        c.setText(quiz.getAnswerC());
        d.setText(quiz.getAnswerD());
        a.setSelected(false);
        b.setSelected(false);
        c.setSelected(false);
        d.setSelected(false);
        correctAnswer = quiz.getCorrectAnswer();
        answer = new String();
        StringBuilder sb = new StringBuilder();
        sb.append(Dictionary.getStyle());
        sb.append("<font size = \"4px\"><font color = \"#dd2c00\"><font face = \"Arial\">");
        sb.append(ques);
        sb.append("</font></font></font>");
        questionEngine.loadContent(sb.toString());
        notificationSlide.stop();
        notification.setTranslateY(80);
        confirm.setDisable(false);
    }

    public void getAnswer(ActionEvent e) {
        if (a.isSelected()) {
            answer = a.getText();
        } else if (b.isSelected()) {
            answer = b.getText();
        } else if (c.isSelected()) {
            answer = c.getText();
        } else if (d.isSelected()) {
            answer = d.getText();
        }
    }

    public void answer() {
        if (answer == null || answer.isEmpty()) {
            return;
        }
        if (answer.equals(correctAnswer)) {
            showCorrect.setText(new String());
            info.setText("CÂU TRẢ LỜI CHÍNH XÁC");
            System.out.println("true");
        } else {
            info.setText("CÂU TRẢ LỜI CHƯA ĐÚNG");
            StringBuilder sb = new StringBuilder();
            sb.append("Đáp án đúng: ").append(correctAnswer);
            showCorrect.setText(sb.toString());
            System.out.println("false");
        }
        confirm.setDisable(true);
    }

    //hangman
    public void loadHangman() throws FileNotFoundException {
        hangman = new Hangman();
        hangman.loadFromFile();
        String tmp = hangman.getWordState();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tmp.length(); i++) {
            sb.append(tmp.charAt(i)).append(" ");
        }
        wordState.setText(sb.toString());
        answerList.setText("Các từ đã đoán: " + hangman.getAnswerList());
        remainingAnswer.setText("Lượt đoán còn lại: " + hangman.getRemainingAnswer());
        charTextField.setText(new String());
        hangmanNotificationSlide.stop();
        hangmanNotification.setTranslateY(80);
        hangmanConfirm.setDisable(false);
        System.out.println(hangman.getCurWord());
    }

    public void submit() {
        String string = charTextField.getText();
        if (string == null || string.isEmpty()) {
            return;
        }
        Character c = string.charAt(0);
        hangman.check(c);
        String tmp = hangman.getWordState();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tmp.length(); i++) {
            sb.append(tmp.charAt(i)).append(" ");
        }
        wordState.setText(sb.toString());
        remainingAnswer.setText("Lượt đoán còn lại: " + hangman.getRemainingAnswer());
        answerList.setText("Các từ đã đoán: " + hangman.getAnswerList());
        charTextField.setText(new String());
        hangmanNotificationTransition();
    }

    //changePane
    private void changePane() {
        quizPane.setVisible(true);
        hangmanPane.setVisible(false);

        //add listener
        tabContainer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab tab, Tab t1) {
                quizTab.setOnSelectionChanged(event -> {
                    if (quizTab.isSelected()) {
                        quizPane.setVisible(true);
                        hangmanPane.setVisible(false);
                        loadQuiz();
                    } else {
                        quizPane.setVisible(false);
                    }
                });
                hangmanTab.setOnSelectionChanged(event -> {
                    if (hangmanTab.isSelected()) {
                        hangmanPane.setVisible(true);
                        quizPane.setVisible(false);
                    } else {
                        hangmanPane.setVisible(false);
                        try {
                            loadHangman();
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
    }
    //
    //
    //transition
    public void transition() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(slider);

        FadeTransition fadeSlider = new FadeTransition();
        fadeSlider.setDuration(Duration.seconds(0.4));
        fadeSlider.setNode(slider);

        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.seconds(0.3));
        fade.setNode(backSlider);

        Exit.setOnMouseClicked(event -> {
            System.exit(0);
        });
        slider.setTranslateX(-300);
        Menu.setOnMouseEntered(event -> {

            slide.stop();
            MenuClose.setVisible(true);
            MenuClose.setDisable(false);
            Menu.setVisible(false);
            Menu.setDisable(true);

            slide.setToX(0);
            slide.play();

            //slider.setTranslateX(-264);

            //fade
            fade.stop();
            fade.setToValue(0.0);
            fade.play();

            fadeSlider.stop();
            fadeSlider.setToValue(1.0);
            fadeSlider.play();


        });

        MenuClose.setOnMouseExited(mouseEvent -> {
            slide.stop();

            Menu.setVisible(true);
            Menu.setDisable(false);
            MenuClose.setVisible(false);
            MenuClose.setDisable(true);


            slide.setToX(-300);
            slide.play();


            slider.setTranslateX(0);

            //
            fade.stop();
            fade.setToValue(1.0);
            fade.play();

            //
            fadeSlider.stop();
            fadeSlider.setToValue(0.0);
            fadeSlider.play();

        });

        slider.setOnMouseExited(mouseEvent -> {
            slide.stop();
            Menu.setVisible(true);
            Menu.setDisable(false);
            MenuClose.setVisible(false);
            MenuClose.setDisable(true);

            slide.setToX(-300);
            slide.play();


            //slider.setTranslateX(0);

            //fade
            fade.stop();
            fade.setToValue(1.0);
            fade.play();


            //
            fadeSlider.stop();
            fadeSlider.setToValue(0.0);
            fadeSlider.play();

        });

        slider.setOnMouseEntered(event -> {
            slide.stop();
            MenuClose.setVisible(true);
            MenuClose.setDisable(false);
            Menu.setVisible(false);
            Menu.setDisable(true);

            slide.setToX(0);
            slide.play();

            //fade
            fade.stop();
            fade.setToValue(0.0);
            fade.play();

            fadeSlider.stop();
            fadeSlider.setToValue(1.0);
            fadeSlider.play();

        });
    }

    public void notificationTransition() {
        notificationSlide.setDuration(Duration.seconds(0.3));
        notificationSlide.setNode(notification);
        notification.setTranslateY(80);

        confirm.setOnMouseClicked(event -> {
            notificationSlide.stop();
            notificationSlide.setDelay(Duration.seconds(0));
            notification.setTranslateY(80);
            notificationSlide.setToY(0);
            notificationSlide.play();
        });
    }

    public void hangmanNotificationTransition() {
        hangmanNotificationSlide.setDuration(Duration.seconds(0.3));
        hangmanNotificationSlide.setNode(hangmanNotification);
        hangmanNotification.setTranslateY(80);

        if (!hangman.isLose() && !hangman.isWin()) {
            return;
        }
        hangmanConfirm.setDisable(true);
        if (hangman.isLose()) {
            hangmanInfo.setText("BẠN ĐÃ THUA");
            hangmanShowCorrect.setText("ĐÁP ÁN: " + hangman.getCurWord());
        }
        if (hangman.isWin()) {
            hangmanInfo.setText("BẠN ĐÃ ĐOÁN ĐÚNG");
            hangmanShowCorrect.setText(new String());
        }

        hangmanNotificationSlide.stop();
        hangmanNotificationSlide.setDelay(Duration.seconds(0));
        hangmanNotification.setTranslateY(80);
        hangmanNotificationSlide.setToY(0);
        hangmanNotificationSlide.play();
    }


    //switch scene
    public void switchToHomeScene(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("HomeUI.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void switchToSearchScene(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SearchUI.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void switchToSettingScene(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SettingUI.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });
        stage.setScene(new Scene(root));
        stage.show();
    }

}
