package org.example.social_network.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.example.social_network.domain.Utilizator;
import org.example.social_network.domain.dto.ControllerDTO;
import org.example.social_network.service.NetworkService;

import javafx.event.ActionEvent;

import java.util.Optional;

public class SignInController implements Controller{
    private NetworkService service;

    @FXML
    public TextField fnameField;
    @FXML
    public TextField lnameField;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label warningLabel;


    public void setData(ControllerDTO controllerDTO) {
        this.service = controllerDTO.getService();
    }

    public void handleSignIn(ActionEvent event) {
        Optional<Utilizator> addedUser = service.addUtilizator(fnameField.getText(), lnameField.getText(), emailField.getText(), passwordField.getText());
        if(addedUser.isEmpty()) {
            Stage stage = (Stage) warningLabel.getScene().getWindow();
            stage.close();
        }
        else{
            warningLabel.setVisible(true);
            warningLabel.setText("Invalid Email");
        }
    }
}