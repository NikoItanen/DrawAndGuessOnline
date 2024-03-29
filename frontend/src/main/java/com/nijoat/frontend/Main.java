package com.nijoat.frontend;

import com.nijoat.frontend.controller.LoginController;
import com.nijoat.frontend.controller.MessageController;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        LoginController loginController = new LoginController();
        loginController.showLoginWindow();

        MessageController messageController = new MessageController();
        messageController.openSecondWindow();
    }
    

    public static void main(String[] args) {
        launch();
    }
}