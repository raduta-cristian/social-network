package org.example.social_network.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.social_network.domain.dto.ControllerDTO;
import org.example.social_network.domain.dto.PrietenDTO;
import org.example.social_network.domain.Prietenie;
import org.example.social_network.domain.Utilizator;
import org.example.social_network.service.NetworkService;
import org.example.social_network.utils.events.Event;
import org.example.social_network.utils.events.PrietenieEntityChangeEvent;
import org.example.social_network.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BroadcastController implements Observer<Event>, Controller {

    private NetworkService service;
    private Utilizator activeUser;

    ObservableList<PrietenDTO> model = FXCollections.observableArrayList();

    @FXML
    TableView<PrietenDTO> tableView;
    @FXML
    TableColumn<PrietenDTO,String> tableColumnFirstName;
    @FXML
    TableColumn<PrietenDTO,String> tableColumnLastName;


    @FXML
    public TextField messageTextField;

    public void setData(ControllerDTO controllerDTO) {
        this.service = controllerDTO.getService();
        this.activeUser = controllerDTO.getActiveUser();
        service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableView.setItems(model);
    }


    private void initModel() {
        populateModel(service.getAllPrietenie());
    }

    private void populateModel(Iterable<Prietenie> prietenii) {
        List<PrietenDTO> p = new ArrayList<>();
        for(Prietenie prietenie: prietenii) {
            if(prietenie.getId().getLeft().equals(activeUser.getId()) ||
                    prietenie.getId().getRight().equals(activeUser.getId())) {
                p.add(createDTO(prietenie));
            }
        }
        model.setAll(p);
    }

    private PrietenDTO createDTO(Prietenie prietenie){

        Utilizator u;
        if(prietenie.getId().getLeft().equals(activeUser.getId()))
            u = service.findUtilizator(prietenie.getId().getRight()).get();
        else
            u = service.findUtilizator(prietenie.getId().getLeft()).get();

        return new PrietenDTO(activeUser.getId(), u.getId(), u.getFirstName(), u.getLastName(), prietenie.getFriendsFrom());
    }


    @Override
    public void update(Event event) {

        if(event instanceof PrietenieEntityChangeEvent prietenieEntityChangeEvent) {

            switch(prietenieEntityChangeEvent.getType()){
                case ADD:
                    populateModel(service.getAllPrietenie());
                    break;
                case UPDATE:
                    populateModel(service.getAllPrietenie());
                    break;
                case DELETE:
                    populateModel(service.getAllPrietenie());
                    break;
            }

        }

    }

    @FXML
    public void handleBroadcastMessage(ActionEvent actionEvent) {
        if(tableView.getSelectionModel().getSelectedItems().isEmpty() || messageTextField.getText().isEmpty())
            return;
        List<Long> ids = tableView.getSelectionModel().getSelectedItems().stream().map(PrietenDTO::getId2).toList();
        service.sendMessage(activeUser.getId(), ids, messageTextField.getText(), LocalDateTime.now(), null);
    }
}
