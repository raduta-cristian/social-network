package org.example.social_network.domain.dto;

import org.example.social_network.controller.UserController;
import org.example.social_network.domain.Utilizator;
import org.example.social_network.service.NetworkService;

public class ControllerDTO {
    NetworkService service;
    Utilizator activeUser;
    Utilizator chattingWithUser;
    UserController userController;

    public ControllerDTO(NetworkService service) {
        this.service = service;
    }

    public ControllerDTO(NetworkService service, Utilizator activeUser) {
        this.service = service;
        this.activeUser = activeUser;
    }

    public ControllerDTO(NetworkService service, Utilizator activeUser, UserController userController) {
        this.service = service;
        this.activeUser = activeUser;
        this.userController = userController;
    }

    public ControllerDTO(NetworkService service, Utilizator activeUser, Utilizator chattingWithUser) {
        this.service = service;
        this.activeUser = activeUser;
        this.chattingWithUser = chattingWithUser;
    }

    public ControllerDTO(NetworkService service, Utilizator activeUser, Utilizator chattingWithUser, UserController userController) {
        this.service = service;
        this.activeUser = activeUser;
        this.chattingWithUser = chattingWithUser;
        this.userController = userController;
    }

    public NetworkService getService() {
        return service;
    }

    public Utilizator getActiveUser() {
        return activeUser;
    }

    public Utilizator getChattingWithUser() {
        return chattingWithUser;
    }

    public UserController getUserController() {
        return userController;
    }
}
