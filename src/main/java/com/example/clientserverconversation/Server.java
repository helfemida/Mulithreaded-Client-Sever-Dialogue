package com.example.clientserverconversation;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Application {

    DataInputStream fromClient = null;
    DataOutputStream toClient = null;

    @Override
    public void start(Stage primaryStage) {
        TextArea taServer = new TextArea();
        taServer.setEditable(false);
        taServer.setMaxSize(300,300);

        TextArea tfServer = new TextArea();
        tfServer.setMaxSize(300,300);

        VBox vboxServer = new VBox(5);
        vboxServer.getChildren().addAll(new Label("Client"), taServer,new Label("Server"), tfServer);

        Scene sceneServer = new Scene(vboxServer);
        primaryStage.setTitle("Server Page");
        primaryStage.setScene(sceneServer);
        primaryStage.show();

        new Thread(() -> {
            try {
                System.out.println("Opening a port... ");
                ServerSocket serverSocket = new ServerSocket(8000);
                Socket socket = serverSocket.accept();

                fromClient = new DataInputStream(socket.getInputStream());
                toClient = new DataOutputStream((socket.getOutputStream()));

                while (true) {
                    String message = fromClient.readUTF();
                    System.out.println("Client: " + message + '\n');
                    Platform.runLater(() -> taServer.appendText("Client: " + message + '\n'));
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }}).start();

        tfServer.setOnKeyPressed(event -> {
            try {
                String message = tfServer.getText().trim();
                if (event.getCode() == KeyCode.ENTER && !message.isEmpty()) {
                    System.out.println("Server: " + message + '\n');

                    toClient.writeUTF(message);
                    toClient.flush();

                    taServer.appendText("Server: " + message + '\n');

                    tfServer.clear();

                }
            } catch(IOException ex){
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}


