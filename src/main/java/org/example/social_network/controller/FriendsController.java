package org.example.social_network.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.example.social_network.domain.dto.ControllerDTO;
import org.example.social_network.domain.dto.PrietenDTO;
import org.example.social_network.domain.Prietenie;
import org.example.social_network.domain.Utilizator;
import org.example.social_network.service.NetworkService;
import org.example.social_network.utils.events.CerereEntityChangeEvent;
import org.example.social_network.utils.events.Event;
import org.example.social_network.utils.events.PrietenieEntityChangeEvent;
import org.example.social_network.utils.observer.Observer;
import org.example.social_network.utils.paging.Page;
import org.example.social_network.utils.paging.Pageable;

import java.util.ArrayList;
import java.util.List;

public class FriendsController implements Observer<Event>, Controller {

    @FXML
    public Label pageLabel;
    @FXML
    public Button previousPageButton;
    @FXML
    public Button nextPageButton;
    private NetworkService service;
    private Utilizator activeUser;
    private UserController userController;

    ObservableList<PrietenDTO> model = FXCollections.observableArrayList();

    @FXML
    TableView<PrietenDTO> tableView;
    @FXML
    TableColumn<PrietenDTO,String> tableColumnFirstName;
    @FXML
    TableColumn<PrietenDTO,String> tableColumnLastName;

    @FXML
    TableColumn<PrietenDTO,String> tableColumnFriendsFrom;

    Stage seeRequestStage = null;
    Stage broadcastStage = null;

    private final int pageSize = 4;
    private int currentPage = 1;

    public void setData(ControllerDTO controllerDTO) {
        this.service = controllerDTO.getService();
        this.activeUser = controllerDTO.getActiveUser();
        this.userController = controllerDTO.getUserController();
        service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableColumnFriendsFrom.setCellValueFactory(new PropertyValueFactory<>("friendsFrom"));
        tableView.setItems(model);
    }


    private void initModel() {
        Page<Prietenie> page = service.getFriendshipsOfPaged(new Pageable(pageSize, currentPage), activeUser.getId());
        int pageCount = (int) Math.ceil((double) page.getTotalNumberOfElements() / pageSize);
        pageLabel.setText("Page " + currentPage + " of " + pageCount);


        previousPageButton.setDisable(false);
        nextPageButton.setDisable(false);

        if(currentPage == 1)
            previousPageButton.setDisable(true);
        if(currentPage >= pageCount)
            nextPageButton.setDisable(true);

        populateModel(page.getElementsOnPage());
    }

    private void populateModel(Iterable<Prietenie> prietenii) {
        List<PrietenDTO> p = new ArrayList<>();
        for(Prietenie prietenie: prietenii)
                p.add(createDTO(prietenie));
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

    public void handleDeleteFriend(ActionEvent actionEvent) {
        PrietenDTO prietenDTO = tableView.getSelectionModel().getSelectedItem();
        if (prietenDTO != null) {
            service.removePrietenie(prietenDTO.getId1(), prietenDTO.getId2());
            tableView.getSelectionModel().clearSelection();
        }
    }

    public Popup createPopup(final String message) {
        final Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        Label label = new Label(message);
        label.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                popup.hide();
            }
        });
        popup.getContent().add(label);
        return popup;
    }

    public void showPopupMessage(final String message) {
        Stage stage = (Stage) tableView.getScene().getWindow();
        final Popup popup = createPopup(message);
        popup.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                popup.setX(stage.getX() + stage.getWidth()/2 - popup.getWidth()/2);
                popup.setY(stage.getY() + stage.getHeight()/2 - popup.getHeight()/2);
            }
        });

        popup.show(stage);
    }

    @Override
    public void update(Event event) {

        if(event instanceof PrietenieEntityChangeEvent prietenieEntityChangeEvent) {

            switch(prietenieEntityChangeEvent.getType()){
                case ADD:
                    initModel();
                    break;
                case UPDATE:
                    initModel();
                    break;
                case DELETE:
                    initModel();
                    break;
            }

        }

        if(event instanceof CerereEntityChangeEvent cerereEntityChangeEvent){
            switch (cerereEntityChangeEvent.getType()){
                case ADD:
                    if(cerereEntityChangeEvent.getData().getId().getRight().equals(activeUser.getId())){
                        Utilizator utilizator = service.findUtilizator(cerereEntityChangeEvent.getData().getId().getLeft()).get();
                        showPopupMessage("New friend request from: " + utilizator.getFirstName() + " " + utilizator.getLastName());
                    }
                    break;
                case UPDATE:
                    break;
                case DELETE:
                    break;
            }
        }

    }

    @FXML
    public void handleSeeRequests(ActionEvent actionEvent) {
        userController.loadWindow("requests-view", new ControllerDTO(service, activeUser));

        /*Parent root;

        if(seeRequestStage != null && seeRequestStage.isShowing())
            return;

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/social_network/requests-view.fxml"));

            if(seeRequestStage != null){
                seeRequestStage.close();
                seeRequestStage = null;
            }

            Stage stage = new Stage();
            stage.setTitle("Requests");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();

            seeRequestStage = stage;

            RequestsController controller =  fxmlLoader.getController();
            controller.setData(new ControllerDTO(service, activeUser));
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }*/
    }

    public void handleChatWithFriend(ActionEvent actionEvent) {

        if(tableView.getSelectionModel().getSelectedItem() == null)
            return;

        Utilizator u = service.findUtilizator(tableView.getSelectionModel().getSelectedItem().getId2()).get();

        userController.loadWindow("chat-view", new ControllerDTO(service, activeUser, u));
        /*Parent root;

        if(tableView.getSelectionModel().getSelectedItem() == null)
            return;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/social_network/chat-view.fxml"));

            Stage stage = new Stage();
            stage.setTitle("Chat");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();


            ChatController controller =  fxmlLoader.getController();

            Utilizator u = service.findUtilizator(tableView.getSelectionModel().getSelectedItem().getId2()).get();

            controller.setData(service, activeUser, u);
            tableView.getSelectionModel().clearSelection();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }*/
    }

    @FXML
    public void handleBroadcast(ActionEvent actionEvent) {
        userController.loadWindow("broadcast-view", new ControllerDTO(service, activeUser));
        /*Parent root;

        if(broadcastStage != null && broadcastStage.isShowing())
            return;

        try {


            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/social_network/broadcast-view.fxml"));

            if(broadcastStage != null){
                broadcastStage.close();
                broadcastStage = null;
            }

            Stage stage = new Stage();
            stage.setTitle("Broadcast");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();

            broadcastStage = stage;

            BroadcastController controller =  fxmlLoader.getController();
            controller.setData(new ControllerDTO(service, activeUser));
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }*/
    }

    @FXML
    public void handlePreviousPage(ActionEvent actionEvent) {
        currentPage--;
        initModel();
    }
    @FXML
    public void handleNextPage(ActionEvent actionEvent) {
        currentPage++;
        initModel();
    }
}
