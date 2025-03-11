package org.example.social_network.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.social_network.domain.*;
import org.example.social_network.domain.dto.ControllerDTO;
import org.example.social_network.domain.dto.MessageDTO;
import org.example.social_network.service.NetworkService;
import org.example.social_network.utils.events.Event;
import org.example.social_network.utils.events.MessageEntityChangeEvent;
import org.example.social_network.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.*;

public class ChatController implements Observer<Event>, Controller {

    private NetworkService service;
    private Utilizator activeUser;
    private Utilizator chattingWithUser;

    ObservableList<MessageDTO> model = FXCollections.observableArrayList();

    @FXML
    public Label chattingToLabel;
    @FXML
    public TextField chatTextField;

    @FXML
    TableView<MessageDTO> tableView;
    @FXML
    TableColumn<MessageDTO,String> tableColumnText;
    @FXML
    TableColumn<MessageDTO,String> tableColumnTimeSent;
    @FXML
    TableColumn<MessageDTO,String> tableColumnReplyingTo;
    @FXML
    TableColumn<MessageDTO,String> tableColumnSentByOther;

    public void setData(ControllerDTO controllerDTO) {
        this.service = controllerDTO.getService();
        this.activeUser = controllerDTO.getActiveUser();
        this.chattingWithUser = controllerDTO.getChattingWithUser();
        service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnText.setCellValueFactory(new PropertyValueFactory<>("text"));
        tableColumnTimeSent.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        tableColumnReplyingTo.setCellValueFactory(new PropertyValueFactory<>("replyingTo"));
        tableColumnSentByOther.setCellValueFactory(new PropertyValueFactory<>("sentByOther"));
        tableView.setItems(model);
    }


    private void initModel() {
        chattingToLabel.setText(chattingWithUser.getFirstName() + " " + chattingWithUser.getLastName());
        populateModel(service.getAllMessage());
    }

    private void populateModel(Iterable<Message> mesaje) {
        List<MessageDTO> m = new ArrayList<>();
        for(Message message : mesaje) {
            if(message.getFromId().equals(activeUser.getId()) &&
                    message.getToIdList().contains(chattingWithUser.getId())) {
                m.add(createDTO(message));
            }
            if(message.getFromId().equals(chattingWithUser.getId()) &&
                    message.getToIdList().contains(activeUser.getId())) {
                m.add(createDTO(message));
            }
        }
        model.setAll(m);
    }

    private MessageDTO createDTO(Message message) {
        String replyText = "";
        if(message.getReplyTo() != null && message.getReplyTo() != 0) {
            Optional<Message> original = service.findMessage(message.getReplyTo());
            if(original.isPresent()) {
                replyText = original.get().getText();

                if(replyText.length() > 12){
                    replyText = replyText.substring(0, 12);
                    replyText = replyText + "...";
                }
            }
        }
        String sentByOther = message.getFromId().equals(chattingWithUser.getId()) ? "->" : "";
        return new MessageDTO(message.getId(), message.getText(), message.getDateTime(), replyText, sentByOther);
    }


    @Override
    public void update(Event event) {

        if(event instanceof MessageEntityChangeEvent messageEntityChangeEvent) {

            switch(messageEntityChangeEvent.getType()){
                case ADD:
                    populateModel(service.getAllMessage());
                    break;
                case UPDATE:
                    populateModel(service.getAllMessage());
                    break;
                case DELETE:
                    populateModel(service.getAllMessage());
                    break;
            }

        }

    }

    @FXML
    public void handleSendMessage(ActionEvent actionEvent) {
        if(chatTextField.getText().isEmpty())
            return;

        service.sendMessage(activeUser.getId(), Arrays.asList(chattingWithUser.getId()), chatTextField.getText(), LocalDateTime.now(), null);
        chatTextField.clear();
    }

    @FXML
    public void handleSendReply(ActionEvent actionEvent) {
        if(chatTextField.getText().isEmpty())
            return;
        if(tableView.getSelectionModel().getSelectedItem() != null){
            service.sendMessage(activeUser.getId(), Arrays.asList(chattingWithUser.getId()), chatTextField.getText(), LocalDateTime.now(), tableView.getSelectionModel().getSelectedItem().getId());
            chatTextField.clear();
            tableView.getSelectionModel().clearSelection();
        }
    }
}
