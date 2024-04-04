package com.nijoat.frontend;

import com.nijoat.frontend.controller.LoginController;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class FrontendServer extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        LoginController loginController = new LoginController();
        loginController.showLoginWindow();
    }
    

    public static void main(String[] args) {
        launch();
    }
}