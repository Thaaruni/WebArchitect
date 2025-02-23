package lk.ijse.dep13.browser.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.Socket;

public class MainSceneController {
    public WebView wbDisplay;
    public TextField txtAddress;
    public AnchorPane root;

    public void initialize() {
        wbDisplay.getEngine().locationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isBlank()) return;
            ((Stage) (root.getScene().getWindow())).setTitle("DEP Browser - " + wbDisplay.getEngine().getTitle());
            txtAddress.setText(wbDisplay.getEngine().getLocation());
        });
        txtAddress.setText("http://google.com");
        loadWebPage(txtAddress.getText());
        txtAddress.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) Platform.runLater(txtAddress::selectAll);
        });
    }

    public void txtAddressOnAction(ActionEvent actionEvent) {
        String url = txtAddress.getText();
        if (url.isBlank()) return;
        loadWebPage(txtAddress.getText());
        txtAddress.selectAll();
    }

    private void loadWebPage(String url) {
        int i = 0;
        String protocol = null;
        String host = null;
        int port = -1;
        String path = "/";

        try {
            // Identify the protocol
            if ((i = url.indexOf("://")) != -1) {
                protocol = url.substring(0, i);
            }

            // Identify the host and port
            int j = url.indexOf("/", protocol == null ? i = 0 : (i = i + 3));
            host = (j != -1 ? url.substring(i, j) : url.substring(i));

            if (protocol == null) protocol = "http";

            // Separate the host and port
            if ((i = host.indexOf(":")) != -1) {
                port = Integer.parseInt(host.substring(i + 1));
                host = host.substring(0, i);
            } else {
                port = switch (protocol) {
                    case "http" -> 80;
                    case "https" -> 443;
                    default -> -1;
                };
            }

            // Identify the path + query string + fragment
            if (j != -1 && j != url.length()) path = url.substring(j);

            if (host.isBlank() || port == -1) throw new RuntimeException("Invalid URL");
            if (!(protocol.equals("http") || protocol.equals("https"))) throw new RuntimeException("Invalid protocol");

            // Establish the connection
            Socket socket = new Socket(host, port);
            String baseUrl = protocol + "://" + host + ":" + port + "/";


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
