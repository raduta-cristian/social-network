package org.example.social_network.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.social_network.domain.Utilizator;
import org.example.social_network.domain.dto.ControllerDTO;
import org.example.social_network.service.NetworkService;
import org.example.social_network.utils.events.Event;
import org.example.social_network.utils.events.PrietenieEntityChangeEvent;
import org.example.social_network.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class SearchController implements Observer<Event>, Controller {
    private NetworkService service;
    private Utilizator activeUser;

    private UserController userController;

    ObservableList<Utilizator> model = FXCollections.observableArrayList();

    @FXML
    TableView<Utilizator> tableView;
    @FXML
    TableColumn<Utilizator,String> tableColumnFirstName;
    @FXML
    TableColumn<Utilizator,String> tableColumnLastName;

    @FXML
    TextField searchUserField;

    public void setData(ControllerDTO controllerDTO) {
        this.service = controllerDTO.getService();
        this.activeUser = controllerDTO.getActiveUser();
        this.userController = controllerDTO.getUserController();
        service.addObserver(this);
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableView.setItems(model);

        searchUserField.textProperty().addListener((observable, oldValue, newValue) -> {
            setModelSearched(newValue);
        });

       tableView.getStyleClass().add("noheader");

    }

    private List<Utilizator> getSearchList(String name){
        return ((ArrayList<Utilizator>) service.getAllUtilizator())
                .stream()
                .filter(utilizator -> {
                    String fullName = utilizator.getFirstName() + " " + utilizator.getLastName();
                    return !utilizator.getId().equals(activeUser.getId()) && fullName.toLowerCase().contains(name.toLowerCase());
                })
                .toList();
    }

    private List<Utilizator> getObservableList(Iterable<Utilizator> iterable){
        return StreamSupport.stream(iterable.spliterator(), false).toList();
    }

    @FXML
    public void handleSendRequest(ActionEvent actionEvent) {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            userController.loadWindow("user-page-view", new ControllerDTO(service, activeUser, tableView.getSelectionModel().getSelectedItem()));
        }
    }

    private void setModelSearched(String name) {
        model.setAll(getObservableList(getSearchList(name)));
    }

    @Override
    public void update(Event event) {
        if(event instanceof PrietenieEntityChangeEvent prietenieEntityChangeEvent) {

            switch(prietenieEntityChangeEvent.getType()){
                case ADD:
                    setModelSearched(searchUserField.getText());
                    break;
                case UPDATE:
                    setModelSearched(searchUserField.getText());
                    break;
                case DELETE:
                    setModelSearched(searchUserField.getText());
                    break;
            }

        }
    }


}
