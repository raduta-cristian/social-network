package org.example.social_network.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.example.social_network.domain.Utilizator;
import org.example.social_network.domain.dto.ControllerDTO;
import org.example.social_network.service.NetworkService;

import javafx.event.ActionEvent;

import java.io.IOException;

public class LoginController implements Controller {
    private NetworkService service;

    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label warningLabel;


    public void setData(ControllerDTO controllerDTO) {
        this.service = controllerDTO.getService();
    }

    public void handleLoginUser(ActionEvent event) {
        Utilizator selectedUser = service.utilizatorLogIn(emailField.getText(), passwordField.getText());
        if (selectedUser != null) {
            warningLabel.setVisible(false);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/social_network/user-view.fxml"));
                Stage stage = new Stage();
                stage.setTitle("User Page");
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.getScene().getStylesheets().add("org/example/social_network/css/styles.css");
                stage.show();

                UserController controller = fxmlLoader.getController();
                controller.setData(new ControllerDTO(service, selectedUser));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            warningLabel.setVisible(true);
            warningLabel.setText("Invalid Email or Password");
        }
    }

    @FXML
    public void handleSignIn(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/social_network/signin-view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Sign Up");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.getScene().getStylesheets().add("org/example/social_network/css/styles.css");
            stage.show();

            SignInController controller = fxmlLoader.getController();
            controller.setData(new ControllerDTO(service));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}