package com.example.clientserverconversation;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Client extends Application {

    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;

    @Override
    public void start(Stage primaryStage) {

        TextArea tf = new TextArea();
        tf.setMaxSize(300,300);

        BorderPane mainPane = new BorderPane();
        TextArea ta = new TextArea();
        ta.setMaxSize(300,300);
        ta.setEditable(false);

        mainPane.setBottom(new VBox(new Label("Client"),tf));
        mainPane.setTop(new VBox(new Label("Server"),ta));

        Scene scene = new Scene(mainPane);
        primaryStage.setTitle("Client Page");
        primaryStage.setScene(scene);
        primaryStage.show();

        tf.setOnKeyPressed(event -> {
            try {
                String message = tf.getText().trim();
                if (event.getCode() == KeyCode.ENTER && !message.isEmpty()) {
                    System.out.println("Client: " + message + 'n');
                    toServer.writeUTF(message);
                    toServer.flush();
                    ta.appendText("Client: " + message + '\n');
                    tf.clear();
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }
        });
        try {
            System.out.println("Waiting for a server to accept...");
            Socket socket = new Socket("localhost", 8000);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        new Thread(() -> {
            try {
                while (true) {
                    String message = fromServer.readUTF();
                    System.out.println("Server: " + message + '\n');

                    ta.appendText("Server: " + message + '\n');
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
