package example.demodic;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeUIController implements Initializable {
    //
    private double x, y;
    private Parent root;
    private Stage stage;
    public static boolean isSignOut = false;

    @FXML
    private ImageView Exit;
    //left Pane
    @FXML
    private AnchorPane pane1;
    @FXML
    private AnchorPane pane2;
    @FXML
    private AnchorPane pane3;
    @FXML
    private AnchorPane pane4;

    //right pane
    @FXML
    private AnchorPane signInPane;
    @FXML
    private AnchorPane signUpPane;
    @FXML
    private AnchorPane signUpSuccessPane;
    @FXML
    private AnchorPane changePasswordPane;
    @FXML
    private AnchorPane changePasswordSuccess;
    @FXML
    private AnchorPane homePane;

    //sign in
    @FXML
    private TextField signInUserName;
    @FXML
    private TextField signInUserPassword;
    @FXML
    private AnchorPane blankUserName;
    @FXML
    private AnchorPane blankUserPassword;
    @FXML
    private AnchorPane wrongNameOrPassword;
    //home Pane
    @FXML
    private Label labelUserName;

    //sign up
    @FXML
    private TextField signUpUserName;
    @FXML
    private TextField signUpUserPassword;
    @FXML
    private AnchorPane blankSingUpUserName;
    @FXML
    private AnchorPane blankSignUpUserPassword;
    @FXML
    private AnchorPane containUserName;

    //change password
    @FXML
    private TextField changeUserName;
    @FXML
    private TextField changeUserPassword;
    @FXML
    private AnchorPane blankChangeUserName;
    @FXML
    private AnchorPane blankChangeUserPassword;
    @FXML
    private AnchorPane userNameDoesNotExit;


    //
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //exit
        Exit.setOnMouseClicked(event -> {
            System.exit(0);
        });
        if (!isSignOut) {
            showSignInPane();
        } else {
            showHomePane();
        }

        //
        blankUserName.setOpacity(0);
        blankUserPassword.setOpacity(0);
        wrongNameOrPassword.setOpacity(0);
        //
        blankSingUpUserName.setOpacity(0);
        blankSignUpUserPassword.setOpacity(0);
        containUserName.setOpacity(0);
        //
        blankChangeUserName.setOpacity(0);
        blankChangeUserPassword.setOpacity(0);
        userNameDoesNotExit.setOpacity(0);
    }

    //
    private void showLeftPane(AnchorPane pane) {
        pane1.setVisible(false);
        pane2.setVisible(false);
        pane3.setVisible(false);
        pane4.setVisible(false);
        //
        pane.setVisible(true);
    }

    private void showRightPane(AnchorPane pane) {
        signInPane.setVisible(false);
        signUpPane.setVisible(false);
        signUpSuccessPane.setVisible(false);
        changePasswordPane.setVisible(false);
        changePasswordSuccess.setVisible(false);
        homePane.setVisible(false);

        pane.setVisible(true);
    }
    //
    //showPane
    public void showSignInPane() {
        showLeftPane(pane1);
        showRightPane(signInPane);
        outSignUpPane();
        outChangePasswordPane();
    }

    public void showSignUpPane() {
        showLeftPane(pane2);
        showRightPane(signUpPane);
        outSignInPane();
        outChangePasswordPane();
    }

    public void showSignUpSuccessPane() {
        showLeftPane(pane2);
        showRightPane(signUpSuccessPane);
    }

    public void showChangePasswordPane() {
        showLeftPane(pane3);
        showRightPane(changePasswordPane);
        outSignInPane();
        outSignUpPane();
    }

    public void showChangePasswordSuccessPane() {
        showLeftPane(pane3);
        showRightPane(changePasswordSuccess);
    }

    public void showHomePane() {
        showLeftPane(pane4);
        showRightPane(homePane);
        outSignInPane();
        outSignUpPane();
        outChangePasswordPane();
    }
    //
    //
    private void outSignInPane() {
        signInUserName.setText(new String());
        signInUserPassword.setText(new String());
    }

    private void outSignUpPane() {
        signUpUserName.setText(new String());
        signUpUserPassword.setText(new String());
    }

    private void outChangePasswordPane() {
        changeUserName.setText(new String());
        changeUserPassword.setText(new String());
    }

    //singInPaneController
    public void signIn(ActionEvent e) throws FileNotFoundException {
        String userName = signInUserName.getText();
        boolean blankNameField = false;
        String userPassword = signInUserPassword.getText();
        boolean blankPasswordField = false;
        if (userName == null || userName.isEmpty()) {
            blankNameField = true;
            FadeTransition userNameFade = new FadeTransition(Duration.seconds(2), blankUserName);
            userNameFade.setFromValue(1);
            userNameFade.setToValue(0);
            userNameFade.play();
        } else {
            blankNameField = false;
        }
        if (userPassword == null || userPassword.isEmpty()) {
            blankPasswordField = true;
            FadeTransition userPasswordFade = new FadeTransition(Duration.seconds(2), blankUserPassword);
            userPasswordFade.setFromValue(1);
            userPasswordFade.setToValue(0);
            userPasswordFade.play();
        } else {
            blankPasswordField = false;
        }
        if (!blankNameField && !blankPasswordField) {
            boolean containUser = UserManagement.contain(userName);
            //
            if (!containUser) {
                FadeTransition wrongNameOrPasswordTransition = new FadeTransition(Duration.seconds(2), wrongNameOrPassword);
                wrongNameOrPasswordTransition.setFromValue(1);
                wrongNameOrPasswordTransition.setToValue(0);
                wrongNameOrPasswordTransition.play();
            } else {
                boolean wrongPassword = UserManagement.getUser(userName).getPassword().equals(userPassword);
                //
                if (!wrongPassword) {
                    FadeTransition wrongNameOrPasswordTransition = new FadeTransition(Duration.seconds(2), wrongNameOrPassword);
                    wrongNameOrPasswordTransition.setFromValue(1);
                    wrongNameOrPasswordTransition.setToValue(0);
                    wrongNameOrPasswordTransition.play();
                } else {
                    UserManagement.setCurrentUser(userName);
                    Dictionary.insertFromFile(UserManagement.getCurrentUser().getFilePath());
                    isSignOut = true;
                    labelUserName.setText(userName);
                    showHomePane();
                }
            }
        }
    }

    //signUpPaneController
    public void signUp(ActionEvent e) throws IOException {
        String userName = signUpUserName.getText();
        boolean blankNameField = false;
        String userPassword = signUpUserPassword.getText();
        boolean blankPasswordField = false;
        if (userName == null || userName.isEmpty()) {
            blankNameField = true;
            FadeTransition userNameFade = new FadeTransition(Duration.seconds(2), blankSingUpUserName);
            userNameFade.setFromValue(1);
            userNameFade.setToValue(0);
            userNameFade.play();
        } else {
            blankNameField = false;
        }
        if (userPassword == null || userPassword.isEmpty()) {
            blankPasswordField = true;
            FadeTransition userPasswordFade = new FadeTransition(Duration.seconds(2), blankSignUpUserPassword);
            userPasswordFade.setFromValue(1);
            userPasswordFade.setToValue(0);
            userPasswordFade.play();
        } else {
            blankPasswordField = false;
        }
        if (!blankNameField && !blankPasswordField) {
            boolean containUser = UserManagement.contain(userName);
            //
            if (containUser) {
                FadeTransition wrongNameOrPasswordTransition = new FadeTransition(Duration.seconds(2), containUserName);
                wrongNameOrPasswordTransition.setFromValue(1);
                wrongNameOrPasswordTransition.setToValue(0);
                wrongNameOrPasswordTransition.play();
            } else {
                UserManagement.addNewUser(userName, userPassword);
                showSignUpSuccessPane();
            }
        }
    }


    //
    public void signOut(ActionEvent e) {
        showSignInPane();
    }

    //change Password pane controller
    public void changePassword(ActionEvent e) throws IOException {
        String userName = changeUserName.getText();
        boolean blankNameField = false;
        String userPassword = changeUserPassword.getText();
        boolean blankPasswordField = false;
        if (userName == null || userName.isEmpty()) {
            blankNameField = true;
            FadeTransition userNameFade = new FadeTransition(Duration.seconds(2), blankChangeUserName);
            userNameFade.setFromValue(1);
            userNameFade.setToValue(0);
            userNameFade.play();
        } else {
            blankNameField = false;
        }
        if (userPassword == null || userPassword.isEmpty()) {
            blankPasswordField = true;
            FadeTransition userPasswordFade = new FadeTransition(Duration.seconds(2), blankChangeUserPassword);
            userPasswordFade.setFromValue(1);
            userPasswordFade.setToValue(0);
            userPasswordFade.play();
        } else {
            blankPasswordField = false;
        }
        if (!blankNameField && !blankPasswordField) {
            boolean containUser = UserManagement.contain(userName);
            //
            if (!containUser) {
                FadeTransition wrongNameOrPasswordTransition = new FadeTransition(Duration.seconds(2), userNameDoesNotExit);
                wrongNameOrPasswordTransition.setFromValue(1);
                wrongNameOrPasswordTransition.setToValue(0);
                wrongNameOrPasswordTransition.play();
            } else {
                UserManagement.changePassword(userName, userPassword);
                UserManagement.exportToFile();
                showChangePasswordSuccessPane();
            }
        }
    }

    //switch scene
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
