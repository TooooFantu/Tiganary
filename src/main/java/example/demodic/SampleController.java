package example.demodic;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

public class SampleController implements Initializable {

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transition();
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


}
