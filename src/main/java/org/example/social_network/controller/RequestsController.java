package org.example.social_network.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.social_network.domain.Cerere;
import org.example.social_network.domain.dto.CerereDTO;
import org.example.social_network.domain.Utilizator;
import org.example.social_network.domain.dto.ControllerDTO;
import org.example.social_network.service.NetworkService;
import org.example.social_network.utils.events.CerereEntityChangeEvent;
import org.example.social_network.utils.events.Event;
import org.example.social_network.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class RequestsController implements Observer<Event>, Controller{

    private NetworkService service;
    private Utilizator activeUser;

    ObservableList<CerereDTO> modelSent = FXCollections.observableArrayList();
    ObservableList<CerereDTO> modelRecv = FXCollections.observableArrayList();

    @FXML
    TableView<CerereDTO> sentTableView;
    @FXML
    TableColumn<CerereDTO,String> sentTableColumnFirstName;
    @FXML
    TableColumn<CerereDTO,String> sentTableColumnLastName;
    @FXML
    TableColumn<CerereDTO,String> sentTableColumnTimeSent;
    @FXML
    TableColumn<CerereDTO,String> sentTableColumnStatus;

    @FXML
    TableView<CerereDTO> recvTableView;
    @FXML
    TableColumn<CerereDTO,String> recvTableColumnFirstName;
    @FXML
    TableColumn<CerereDTO,String> recvTableColumnLastName;
    @FXML
    TableColumn<CerereDTO,String> recvTableColumnTimeReceived;
    @FXML
    TableColumn<CerereDTO,String> recvTableColumnStatus;


    public void setData(ControllerDTO controllerDTO) {
        this.service = controllerDTO.getService();
        this.activeUser = controllerDTO.getActiveUser();
        service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        sentTableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("toFirstName"));
        sentTableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("toLastName"));
        sentTableColumnTimeSent.setCellValueFactory(new PropertyValueFactory<>("timeSent"));
        sentTableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        sentTableView.setItems(modelSent);

        recvTableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("fromFirstName"));
        recvTableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("fromLastName"));
        recvTableColumnTimeReceived.setCellValueFactory(new PropertyValueFactory<>("timeSent"));
        recvTableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        recvTableView.setItems(modelRecv);
    }

    private List<Utilizator> getObservableList(Iterable<Utilizator> iterable){
        return StreamSupport.stream(iterable.spliterator(), false).toList();
    }

    private void initModel() {
        populateModels(service.getAllCerere());
    }

    private void populateModels(Iterable<Cerere> cereri) {
        List<CerereDTO> sent = new ArrayList<>();
        List<CerereDTO> recv = new ArrayList<>();

        for(Cerere cerere : cereri) {
            if(Objects.equals(cerere.getId().getLeft(), activeUser.getId())) {
                sent.add(createDTO(cerere));
            }
            if(Objects.equals(cerere.getId().getRight(), activeUser.getId())) {
                recv.add(createDTO(cerere));
            }
        }

        modelSent.setAll(sent);
        modelRecv.setAll(recv);
    }

    private CerereDTO createDTO(Cerere cerere){

        Utilizator from = service.findUtilizator(cerere.getId().getLeft()).get();
        Utilizator to = service.findUtilizator(cerere.getId().getRight()).get();

        return new CerereDTO(
                from.getId(),
                from.getFirstName(),
                from.getLastName(),
                to.getId(),
                to.getFirstName(),
                to.getLastName(),
                cerere.getTimeSent(),
                cerere.getStatus());
    }

    @Override
    public void update(Event event) {

        if(event instanceof CerereEntityChangeEvent cerereEntityChangeEvent) {

            System.out.println(cerereEntityChangeEvent.getData().getStatus());

            switch(cerereEntityChangeEvent.getType()){
                case ADD:
                    populateModels(service.getAllCerere());
                    break;
                case UPDATE:
//                    CerereDTO cerereDTO = createDTO(cerereEntityChangeEvent.getData());
//                    List<CerereDTO> toate = new ArrayList<>();
//                    toate.addAll(modelSent);
//                    toate.addAll(modelRecv);
//
//                    for(CerereDTO cDTO : modelSent) {
//                        if(Objects.equals(cDTO.getFromId(), cerereDTO.getFromId()) && Objects.equals(cDTO.getToId(), cerereDTO.getToId())){
//                            cDTO.setFromFirstName(cerereDTO.getFromFirstName());
//                            cDTO.setFromLastName(cerereDTO.getFromLastName());
//                            cDTO.setToFirstName(cerereDTO.getToFirstName());
//                            cDTO.setToLastName(cerereDTO.getToLastName());
//                            cDTO.setTimeSent(cerereDTO.getTimeSent());
//                            cDTO.setStatus(cerereDTO.getStatus());
//                        }
//                    }
//                    for(CerereDTO cDTO : modelRecv) {
//                        if(Objects.equals(cDTO.getFromId(), cerereDTO.getFromId()) && Objects.equals(cDTO.getToId(), cerereDTO.getToId())){
//                            cDTO.setFromFirstName(cerereDTO.getFromFirstName());
//                            cDTO.setFromLastName(cerereDTO.getFromLastName());
//                            cDTO.setToFirstName(cerereDTO.getToFirstName());
//                            cDTO.setToLastName(cerereDTO.getToLastName());
//                            cDTO.setTimeSent(cerereDTO.getTimeSent());
//                            cDTO.setStatus(cerereDTO.getStatus());
//                        }
//                    }
                    populateModels(service.getAllCerere());
                    break;
                case DELETE:
//                    modelRecv.remove(cerereEntityChangeEvent.getData());
//                    modelSent.remove(cerereEntityChangeEvent.getData());
                    populateModels(service.getAllCerere());
                    break;
            }

        }

    }

    @FXML
    public void handleAcceptRequest(ActionEvent actionEvent) {
        CerereDTO selected = recvTableView.getSelectionModel().getSelectedItem();
        if(selected != null) {
            service.acceptCerere(selected.getFromId(), selected.getToId());
            recvTableView.getSelectionModel().clearSelection();
        }
    }

    @FXML
    public void handleRejectRequest(ActionEvent actionEvent) {
        CerereDTO selected = recvTableView.getSelectionModel().getSelectedItem();
        if(selected != null) {
            service.rejectCerere(selected.getFromId(), selected.getToId());
            recvTableView.getSelectionModel().clearSelection();
        }
    }
}
