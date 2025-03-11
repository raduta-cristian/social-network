package org.example.social_network;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.social_network.controller.LoginController;
import org.example.social_network.domain.*;
import org.example.social_network.domain.dto.ControllerDTO;
import org.example.social_network.domain.validators.CerereValidator;
import org.example.social_network.domain.validators.MessageValidator;
import org.example.social_network.domain.validators.PrietenieValidator;
import org.example.social_network.domain.validators.UtilizatorValidator;
import org.example.social_network.repository.PagingRepository;
import org.example.social_network.repository.Repository;
import org.example.social_network.repository.db.CerereDbRepository;
import org.example.social_network.repository.db.MessageDbRepository;
import org.example.social_network.repository.db.PrietenieDbRepository;
import org.example.social_network.repository.db.UtilizatorDbRepository;
import org.example.social_network.service.NetworkService;

import java.io.IOException;

public class FXApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        NetworkService service = getNetworkService();

        FXMLLoader fxmlLoader = new FXMLLoader(FXApplication.class.getResource("/org/example/social_network/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login Page");
        stage.setScene(scene);

        LoginController controller =  fxmlLoader.getController();
        controller.setData(new ControllerDTO(service));

        stage.getScene().getStylesheets().add("org/example/social_network/css/styles.css");
        stage.show();
    }

    private static NetworkService getNetworkService() {
        // add database credentials
        Repository<Long, Utilizator> uRepo = new UtilizatorDbRepository(new UtilizatorValidator(), "", "", "");
        PagingRepository<Tuple<Long, Long>, Prietenie> pRepo = new PrietenieDbRepository(new PrietenieValidator(), "", "", "");
        Repository<Tuple<Long, Long>, Cerere> cRepo = new CerereDbRepository(new CerereValidator(), "", "", "");
        Repository<Long, Message> mRepo = new MessageDbRepository(new MessageValidator(), "", "", "");
        NetworkService service = new NetworkService(uRepo, pRepo, cRepo, mRepo);
        return service;
    }

    public static void main(String[] args) {
        launch(args);
    }
}