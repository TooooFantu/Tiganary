package example.demodic;

import example.ggApi.EnglishToVietnameseTranslator;
import example.ggApi.TextToSpeech;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

public class SearchUIController implements Initializable {
    //
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

    //tab find
    @FXML
    private TextField textField;
    @FXML
    private WebView webView;
    WebEngine engine;
    @FXML
    private ListView<String> listView;
    private String oldKey = new String();

    private String curWord;

    //tab add
    @FXML
    private WebView addWebView;
    WebEngine addWebViewEngine;
    @FXML
    private TextField wordTextField;
    @FXML
    private ChoiceBox<String> partOfSpeech;
    private ObservableList<String> type = FXCollections.observableArrayList(
            "danh từ",
            "động từ",
            "tính từ",
            "trạng từ",
            "giới từ",
            "đại từ"
    );

    @FXML
    private TextField meaningTextField;
    //ex pane:
    @FXML
    private AnchorPane exPane;
    @FXML
    private TextField exTextField;
    @FXML
    private TextField exMeaningTextField;

    private String wordTarget;
    private String newWord;
    private Stack<String> restoreNewWord = new Stack<>();
    private boolean emptyNewWord = true;

    //notifications:
    @FXML
    private AnchorPane notification;
    @FXML
    private Button submit;

    //
    @FXML
    private Label speaker;

    //tab
    @FXML
    private Tab searchTab;
    @FXML
    private Tab addTab;
    @FXML
    private Tab removeTab;
    @FXML
    private Tab fixTab;
    @FXML
    private Tab ggTab;

    @FXML
    private TabPane tabContainer;

    @FXML
    private AnchorPane searchPane;
    @FXML
    private AnchorPane addPane;
    @FXML
    private AnchorPane removePane;
    @FXML
    private AnchorPane fixPane;
    @FXML
    private AnchorPane ggPane;

    //remove Tab:

    @FXML
    private TextField removeWordTextField;
    @FXML
    private ListView<String> removeListView;
    private String removeOldKey = new String();

    //ggTranslateTab:

    @FXML
    private TextField ggTranslateTextField;
    @FXML
    private WebView ggTranslateWebView;
    WebEngine ggWebViewEngine;

    //fix tab:
    @FXML
    private WebView fixWebView;
    WebEngine fixWebViewEngine;
    //find:
    @FXML
    private TextField fixTextField;
    @FXML
    private ListView<String> fixListView;
    private String fixOldKey = new String();
    //
    @FXML
    private ChoiceBox<String> fixPartOfSpeech;
    @FXML
    private AnchorPane fixSearchWordPane;
    @FXML
    private AnchorPane fixWordDetailsPane;

    @FXML
    private TextField fixMeaningTextField;
    //ex pane:
    @FXML
    private AnchorPane fixExPane;
    @FXML
    private TextField fixExTextField;
    @FXML
    private TextField fixExMeaningTextField;

    private String fixWordTarget;
    private String fixNewWord;
    private Stack<String> fixRestoreNewWord = new Stack<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        engine = webView.getEngine();
        engine.loadContent(Dictionary.getStyle());
        transition();
        //find:
        searching();
        //add:
        partOfSpeech.setItems(type);
        exPane.setVisible(false);
        addWebViewEngine = addWebView.getEngine();
        addWebViewEngine.loadContent(Dictionary.getStyle());

        //remove:
        removeSearching();
        //ggTranslate
        ggWebViewEngine = ggTranslateWebView.getEngine();
        ggWebViewEngine.loadContent(Dictionary.getStyle());
        //fix:
        fixSearching();
        fixExPane.setVisible(false);
        fixPartOfSpeech.setItems(type);
        fixWordDetailsPane.setVisible(false);
        fixWebViewEngine = fixWebView.getEngine();
        fixWebViewEngine.loadContent(Dictionary.getStyle());

        speaker.setVisible(false);

        //
        // notificationTransition();

        //
        changePane();

    }
    private void searching() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                String key = textField.getText();
                oldKey = key;

                List<String> recommendWord = Dictionary.findRecommendWord(key);
                ObservableList<String> listRecommendWord = FXCollections.observableList(recommendWord);
                if (!recommendWord.isEmpty()) {
                    listView.setVisible(true);
                    listView.setItems(listRecommendWord);
                } else {
                    listView.setVisible(false);
                }
            }
        };

        AnimationTimer handleTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (listView.isFocused() || oldKey.equals(textField.getText())) {
                    timer.stop();
                } else {
                    oldKey = textField.getText();
                    timer.start();
                }
            }
        };

        timer.start();
        handleTimer.start();
    }

    public void loadToWebView (ActionEvent e) {
        String wordTarget = listView.getSelectionModel().getSelectedItem();
        if (wordTarget == null) {
            engine.load("null");
            return;
        }
        String html = Dictionary.convertToHtml(wordTarget);
        if (html == null) {
            engine.load("null");
        } else {
            engine.loadContent(html);
            speaker.setVisible(true);
            curWord = wordTarget;
        }
    }

    public void speak() {
        if (curWord == null || curWord.isEmpty()) {
            return;
        }
        TextToSpeech textToSpeech = new TextToSpeech();
        textToSpeech.speak(curWord);
    }

    public void loadNewWord(ActionEvent e) {
        String word = wordTextField.getText();
        if (word.isEmpty() || Dictionary.containWord(word) || word == null) {
            System.out.println("contain");
            newWord = new String();
            loadToAddWebView();
            emptyNewWord = true;
            // reset:
            meaningTextField.setText(new String());
            exTextField.setText(new String());
            exMeaningTextField.setText(new String());
        } else {
            wordTarget = word;
            newWord = Dictionary.convertToTextForm(word, 1) + "\n";
            emptyNewWord = false;
            loadToAddWebView();
        }
    }

    public void loadPartOfSpeech(ActionEvent e) {
        String type = partOfSpeech.getValue();
        if (type == null || type.isEmpty() || emptyNewWord) {
            return;
        } else {
            //
            restoreNewWord.push(newWord);
            //
            newWord += Dictionary.convertToTextForm(type, 2) + "\n";
            loadToAddWebView();
        }
        meaningTextField.setText(new String());
        exTextField.setText(new String());
        exMeaningTextField.setText(new String());
    }

    public void loadMeaning(ActionEvent e) {
        String meaning = meaningTextField.getText();
        if (meaning == null || meaning.isEmpty() || emptyNewWord) {
            return;
        } else {
            //
            restoreNewWord.push(newWord);
            //
            newWord += Dictionary.convertToTextForm(meaning, 3) + "\n";
            loadToAddWebView();
        }
        exTextField.setText(new String());
        exMeaningTextField.setText(new String());
    }

    //load ex:

    public void showExPane(ActionEvent e) {
        if (exPane.isVisible()) {
            exTextField.setText(new String());
            exMeaningTextField.setText(new String());
            exPane.setVisible(false);
        } else {
            exPane.setVisible(true);
        }
    }

    public void loadEx(ActionEvent e) {
        String example = new String();
        String ex = exTextField.getText();
        String exMeaning;
        if (ex.isEmpty() || ex == null || emptyNewWord) {
            return;
        } else {
            exMeaning = exMeaningTextField.getText();
            if (exMeaning == null || exMeaning.isEmpty()) {
                example += ex;
            } else {
                example += ex + "+ " + exMeaning;
            }
            //
            restoreNewWord.push(newWord);
            //
            newWord += Dictionary.convertToTextForm(example, 4) + "\n";
            loadToAddWebView();
        }
        exTextField.setText(new String());
        exMeaningTextField.setText(new String());
    }

    public void loadToAddWebView() {
        if (newWord != null) {
            addWebViewEngine.loadContent(Dictionary.convertToHtmlPreview(newWord));
        }
    }

    public void addNewWordUndo(ActionEvent e) {
        if (restoreNewWord.isEmpty()) {
            newWord = new String();
            wordTarget = new String();
            emptyNewWord = true;
        }
        if (!restoreNewWord.empty()) {
            String restore = restoreNewWord.peek();
            restoreNewWord.pop();
            newWord = restore;
        }
        loadToAddWebView();
    }

    public void addNewWord(ActionEvent e) {
        if (newWord == null || newWord.isEmpty() || wordTarget == null || wordTarget.isEmpty()) {
            System.out.println("fail to add");
            return;
        } else {
            Dictionary.insertNewWord(wordTarget, newWord);
            try {
                Dictionary.exportToFile(UserManagement.getCurrentUser().getFilePath());
            } catch (IOException ex) {
                System.out.println("Fail to export");
                throw new RuntimeException(ex);
            }
            System.out.println("add success");
        }
        //
        wordTarget = new String();
        newWord = new String();
        emptyNewWord = true;
        //
        wordTextField.setText(new String());
        meaningTextField.setText(new String());
        exTextField.setText(new String());
        exMeaningTextField.setText(new String());
        exPane.setVisible(false);
        restoreNewWord.clear();
        removeOldKey = new String();
        emptyNewWord = true;
    }

    //fix search:
    private void fixSearching() {
        AnimationTimer fixTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                String fixKey = fixTextField.getText();
                fixOldKey = fixKey;

                List<String> fixRecommendWord = Dictionary.findRecommendWord(fixKey);
                ObservableList<String> fixListRecommendWord = FXCollections.observableList(fixRecommendWord);
                if (!fixRecommendWord.isEmpty()) {
                    fixListView.setVisible(true);
                    fixListView.setItems(fixListRecommendWord);
                } else {
                    fixListView.setVisible(false);
                }
            }
        };

        AnimationTimer fixHandleTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (fixListView.isFocused() || fixOldKey.equals(fixTextField.getText())) {
                    fixTimer.stop();
                } else {
                    fixOldKey = fixTextField.getText();
                    fixTimer.start();
                }
            }
        };

        fixTimer.start();
        fixHandleTimer.start();
    }

    public void getFixWordTarget (ActionEvent e) {
        String fixWord = fixListView.getSelectionModel().getSelectedItem();
        if (fixWord == null) {
            return;
        }
        if (fixWord.isEmpty() || !Dictionary.containWord(fixWord)) {
            System.out.println("not contain");
            fixNewWord = new String();

            // reset:
            fixMeaningTextField.setText(new String());
            fixExTextField.setText(new String());
            fixExMeaningTextField.setText(new String());
        } else {
            fixWordTarget = fixWord;
            fixNewWord = Dictionary.convertToTextForm(fixWord, 1) + "\n";
            loadToFixWebView();
            fixSearchWordPane.setVisible(false);
            fixWordDetailsPane.setVisible(true);
        }
    }

    public void loadFixPartOfSpeech(ActionEvent e) {
        String fixType = fixPartOfSpeech.getValue();
        if (fixType == null || fixType.isEmpty()) {
            return;
        } else {
            //
            fixRestoreNewWord.push(fixNewWord);
            //
            fixNewWord += Dictionary.convertToTextForm(fixType, 2) + "\n";
            loadToFixWebView();
        }
        fixMeaningTextField.setText(new String());
        fixExTextField.setText(new String());
        fixExMeaningTextField.setText(new String());
    }

    public void loadFixMeaning(ActionEvent e) {
        String fixMeaning = fixMeaningTextField.getText();
        if (fixMeaning == null || fixMeaning.isEmpty()) {
            return;
        } else {
            //
            fixRestoreNewWord.push(fixNewWord);
            //
            fixNewWord += Dictionary.convertToTextForm(fixMeaning, 3) + "\n";
            loadToFixWebView();
        }
        fixExTextField.setText(new String());
        fixExMeaningTextField.setText(new String());
    }

    //load fix ex:

    public void showFixExPane(ActionEvent e) {
        if (fixExPane.isVisible()) {
            fixExTextField.setText(new String());
            fixExMeaningTextField.setText(new String());
            fixExPane.setVisible(false);
        } else {
            fixExPane.setVisible(true);
        }
    }

    public void loadFixEx(ActionEvent e) {
        String fixExample = new String();
        String fixEx = fixExTextField.getText();
        String fixExMeaning;
        if (fixEx.isEmpty() || fixEx == null) {
            return;
        } else {
            fixExMeaning = fixExMeaningTextField.getText();
            if (fixExMeaning == null || fixExMeaning.isEmpty()) {
                fixExample += fixEx;
            } else {
                fixExample += fixEx + "+ " + fixExMeaning;
            }
            //
            fixRestoreNewWord.push(fixNewWord);
            //
            fixNewWord += Dictionary.convertToTextForm(fixExample, 4) + "\n";
            loadToFixWebView();
        }
        fixExTextField.setText(new String());
        fixExMeaningTextField.setText(new String());
    }

    public void fixNewWordUndo(ActionEvent e) {
        if (fixRestoreNewWord.isEmpty()) {
            fixNewWord = new String();
            fixWordTarget = new String();
            fixSearchWordPane.setVisible(true);
            fixWordDetailsPane.setVisible(false);
        }
        if (!fixRestoreNewWord.empty()) {
            String fixRestore = fixRestoreNewWord.peek();
            fixRestoreNewWord.pop();
            fixNewWord = fixRestore;
        }
        loadToFixWebView();
    }

    public void fixNewWord(ActionEvent e) {
        if (fixNewWord == null || fixNewWord.isEmpty() || fixWordTarget == null || fixWordTarget.isEmpty()) {
            System.out.println("fail to add");
            return;
        } else {
            Dictionary.fixWord(fixWordTarget, fixNewWord);
            try {
                Dictionary.exportToFile(UserManagement.getCurrentUser().getFilePath());
            } catch (IOException ex) {
                System.out.println("Fail to export");
                throw new RuntimeException(ex);
            }
            System.out.println("add success");
        }
        //
        fixWordTarget = new String();
        fixNewWord = new String();
        fixSearchWordPane.setVisible(true);
        fixWordDetailsPane.setVisible(false);
        //
        fixListView.setVisible(false);
        fixTextField.setText(new String());
        fixMeaningTextField.setText(new String());
        fixExTextField.setText(new String());
        fixExMeaningTextField.setText(new String());
        fixExPane.setVisible(false);
        fixRestoreNewWord.clear();
        fixOldKey = new String();
    }

    public void loadToFixWebView() {
        if (fixNewWord != null) {
            fixWebViewEngine.loadContent(Dictionary.convertToHtmlPreview(fixNewWord));
        }
    }

    //notification:
    public void notificationTransition() {
        TranslateTransition notificationSlide = new TranslateTransition();
        notificationSlide.setDuration(Duration.seconds(0.3));
        notificationSlide.setNode(notification);
        notification.setTranslateY(50);

        submit.setOnMouseClicked(event -> {
            notificationSlide.stop();
            notificationSlide.setDelay(Duration.seconds(0));
            notification.setTranslateY(50);
            notificationSlide.setToY(0);
            notificationSlide.play();

            notificationSlide.setOnFinished((ActionEvent e) -> {
                notificationSlide.setDelay(Duration.seconds(2));
                notificationSlide.setToY(50);
                notificationSlide.play();
            });
        });
    }
    
    // out tab
    private void outSearchTab() {
        textField.clear();
        engine.loadContent(Dictionary.getStyle());
        listView.setVisible(false);
        speaker.setVisible(false);
    }

    private void outAddTab() {
        wordTextField.setText(new String());
        meaningTextField.setText(new String());
        exTextField.setText(new String());
        exMeaningTextField.setText(new String());
        exPane.setVisible(false);
        newWord = new String();
        wordTarget = new String();
        restoreNewWord.clear();
        emptyNewWord = true;
        loadToAddWebView();
    }

    private void outRemoveTab() {
        removeWordTextField.setText(new String());
        removeOldKey = new String();
        removeListView.setVisible(false);
    }

    private void outGgTab() {
        ggTranslateTextField.setText(new String());

    }

    private void outFixTab() {
        fixListView.setVisible(false);
        fixTextField.setText(new String());
        fixOldKey = new String();
        fixMeaningTextField.setText(new String());
        fixExPane.setVisible(false);
        fixExTextField.setText(new String());
        fixExMeaningTextField.setText(new String());
        fixNewWord = new String();
        fixWordTarget = new String();
        fixRestoreNewWord.clear();
        fixSearchWordPane.setVisible(true);
        fixWordDetailsPane.setVisible(false);
        loadToFixWebView();
    }

    //
    private void changePane() {
        searchPane.setVisible(true);
        addPane.setVisible(false);
        removePane.setVisible(false);
        fixPane.setVisible(false);
        ggPane.setVisible(false);

        //add listener
        tabContainer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab tab, Tab t1) {
                searchTab.setOnSelectionChanged(event -> {
                    if (searchTab.isSelected()) {
                        searchPane.setVisible(true);
                    } else {
                        searchPane.setVisible(false);
                        outSearchTab();
                    }
                });
                addTab.setOnSelectionChanged(event -> {
                    if (addTab.isSelected()) {
                        addPane.setVisible(true);

                        //searchTab:
                        outSearchTab();
                    } else {
                        addPane.setVisible(false);
                        outAddTab();
                    }
                });
                removeTab.setOnSelectionChanged(event -> {
                    if (removeTab.isSelected()) {
                        removePane.setVisible(true);

                        //searchTab:
                        searchPane.setVisible(false);
                        outSearchTab();
                    } else {
                        removePane.setVisible(false);
                        outRemoveTab();
                    }
                });
                fixTab.setOnSelectionChanged(event -> {
                    if (fixTab.isSelected()) {
                        fixPane.setVisible(true);

                        //searchTab:
                        searchPane.setVisible(false);
                        outSearchTab();
                    } else {
                        fixPane.setVisible(false);
                        outFixTab();
                    }
                });
                ggTab.setOnSelectionChanged(event -> {
                    if (ggTab.isSelected()) {
                        ggPane.setVisible(true);

                        //searchTab:
                        searchPane.setVisible(false);
                        outSearchTab();
                    } else {
                        ggPane.setVisible(false);
                        outGgTab();
                    }
                });
            }
        });
    }

    //remove
    private void removeSearching() {
        AnimationTimer removeTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                String removeKey = removeWordTextField.getText();
                removeOldKey = removeKey;

                List<String> removeRecommendWord = Dictionary.findRecommendWord(removeKey);
                ObservableList<String> removeListRecommendWord = FXCollections.observableList(removeRecommendWord);
                if (!removeRecommendWord.isEmpty()) {
                    removeListView.setVisible(true);
                    removeListView.setItems(removeListRecommendWord);
                } else {
                    removeListView.setVisible(false);
                }
            }
        };

        AnimationTimer removeHandleTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (removeListView.isFocused() || removeOldKey.equals(removeWordTextField.getText())) {
                    removeTimer.stop();
                } else {
                    removeOldKey = removeWordTextField.getText();
                    removeTimer.start();
                }
            }
        };

        removeTimer.start();
        removeHandleTimer.start();
    }

    public void removeWord() {
        String removeWordTarget = removeListView.getSelectionModel().getSelectedItem();
        if (removeWordTarget == null || removeWordTarget.isEmpty()) {
            System.out.println("Fail to remove");
            return;
        } else {
            Dictionary.removeWord(removeWordTarget);
            try {
                Dictionary.exportToFile(UserManagement.getCurrentUser().getFilePath());
            } catch (IOException e) {
                System.out.println("Fail to remove");
                return;
            }
        }
        System.out.println("Remove success");
        removeListView.setVisible(false);
        removeOldKey = new String();
        removeWordTextField.setText(new String());
        return;
    }

    //ggTranslate
    public void translate() {
        String sentence = ggTranslateTextField.getText();
        if (sentence == null || sentence.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(Dictionary.getStyle());
        sb.append("<font size = \"4px\"><font color = \"#dd2c00\"><font face = \"Arial\">");
        sb.append(EnglishToVietnameseTranslator.translateText(sentence, "vi"));
        sb.append("</font></font></font>");
        ggWebViewEngine.loadContent(sb.toString());
    }
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

    //switch Scene
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

    public void switchToGameScene(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("GameUI.fxml"));
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
