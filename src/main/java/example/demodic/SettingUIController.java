package example.demodic;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingUIController implements Initializable {
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
    private Tab userTab;
    @FXML
    private Tab backUpTab;
    @FXML
    private TabPane tabContainer;

    //pane
    @FXML
    private AnchorPane userPane;
    @FXML
    private AnchorPane backUpPane;

    //userTab:
    @FXML
    private Label userName;
    @FXML
    private AnchorPane removeUserConfirm;

    //back up tab:
    @FXML
    private AnchorPane confirmPane;
    @FXML
    private AnchorPane notification;
    @FXML
    private Button confirm;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transition();
        //userTab
        userName.setText(UserManagement.getCurrentUser().getUserName());
        //backUpTab
        confirmPane.setVisible(false);
        removeUserConfirm.setVisible(false);
        //
        changePane();

        //nofication
        notificationTransition();
    }

    //confirm
    public void refreshData() {
        confirmPane.setVisible(true);
    }

    public void confirmNo() {
        confirmPane.setVisible(false);
    }

    public void confirmYes() {
        Dictionary.backUp();
        confirmPane.setVisible(false);
    }

    //changePane
    private void changePane() {
        userPane.setVisible(true);
        backUpPane.setVisible(false);

        //add listener
        tabContainer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab tab, Tab t1) {
                userTab.setOnSelectionChanged(event -> {
                    if (userTab.isSelected()) {
                        userPane.setVisible(true);
                        backUpPane.setVisible(false);
                    } else {
                        userPane.setVisible(false);
                        removeUserConfirm.setVisible(false);
                    }
                });
                backUpTab.setOnSelectionChanged(event -> {
                    if (backUpTab.isSelected()) {
                        backUpPane.setVisible(true);
                        userPane.setVisible(false);
                    } else {
                        backUpPane.setVisible(false);
                        confirmPane.setVisible(false);
                    }
                });
            }
        });
    }
    //
    public void removeUser() {
        removeUserConfirm.setVisible(true);
    }

    public void removeUserConfirmNo() {
        removeUserConfirm.setVisible(false);
    }

    public void removeUserConfirmYes(ActionEvent event) throws IOException {
        UserManagement.removeUser();
        HomeUIController.isSignOut = false;
        switchToHomeScene(event);
    }
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
    //notification
    public void notificationTransition() {
        TranslateTransition notificationSlide = new TranslateTransition();
        notificationSlide.setDuration(Duration.seconds(0.3));
        notificationSlide.setNode(notification);
        notification.setTranslateY(80);

        confirm.setOnMouseClicked(event -> {
            notificationSlide.stop();
            notificationSlide.setDelay(Duration.seconds(0));
            notification.setTranslateY(80);
            notificationSlide.setToY(0);
            notificationSlide.play();

            notificationSlide.setOnFinished((ActionEvent e) -> {
                notificationSlide.setDelay(Duration.seconds(2));
                notificationSlide.setToY(80);
                notificationSlide.play();
            });
        });
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
