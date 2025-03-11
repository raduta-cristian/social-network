package org.example.social_network.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.example.social_network.domain.Utilizator;
import org.example.social_network.domain.dto.ControllerDTO;
import org.example.social_network.service.NetworkService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class UserController implements Controller {
    private NetworkService service;
    private Utilizator activeUser;

    @FXML
    public ImageView pfpImageView;
    public AnchorPane mainWindow;
    public Label activeUserLabel;

    public void setData(ControllerDTO controllerDTO) {
        this.service = controllerDTO.getService();
        this.activeUser = controllerDTO.getActiveUser();

        activeUserLabel.setText(activeUser.getFirstName() + " " + activeUser.getLastName());
        pfpImageView.getStyleClass().add("bordered");
        try {
            pfpImageView.setImage(new Image(new FileInputStream("src\\main\\resources\\org\\example\\social_network\\pfp.png")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        loadWindow("friends-view", new ControllerDTO(service, activeUser, this));

    }

    public void loadWindow(String windowName, ControllerDTO controllerDTO) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/social_network/" + windowName + ".fxml"));
            Node node = (Node) fxmlLoader.load();
            ((HBox)mainWindow.getChildren().getFirst()).getChildren().setAll(node);
            Controller controller = fxmlLoader.getController();
            controller.setData(controllerDTO);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


    public void handleSearch(ActionEvent event) {
        loadWindow("search-view", new ControllerDTO(service, activeUser, this));
    }

    public void handleHome(ActionEvent event) {
        loadWindow("friends-view", new ControllerDTO(service, activeUser, this));
    }
}
