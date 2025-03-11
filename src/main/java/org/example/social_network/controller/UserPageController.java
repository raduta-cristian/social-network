package org.example.social_network.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.social_network.domain.Utilizator;
import org.example.social_network.domain.dto.ControllerDTO;
import org.example.social_network.service.NetworkService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class UserPageController implements Controller {
    private NetworkService service;
    private Utilizator activeUser;
    private Utilizator pageUser;

    public ImageView profilePicture;
    public Label nameLabel;
    public Label friendCountLabel;
    public Button requestButton;

    public void setData(ControllerDTO controllerDTO) {
        this.service = controllerDTO.getService();
        this.activeUser = controllerDTO.getActiveUser();
        this.pageUser = controllerDTO.getChattingWithUser();

        nameLabel.setText(pageUser.getFirstName() + " " + pageUser.getLastName());
        friendCountLabel.setText("Friends: " + service.getFriendCount(pageUser.getId()));

        if(service.getFriendFromUser(activeUser.getId(), pageUser.getId()) != null) {
            requestButton.setDisable(true);
        }
        try {
            profilePicture.setImage(new Image(new FileInputStream("src\\main\\resources\\org\\example\\social_network\\pfp.png")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleSendRequest(ActionEvent event) {
        service.sendCerere(activeUser.getId(), pageUser.getId());
    }
}
