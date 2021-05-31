package netty.client;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import lombok.SneakyThrows;
import netty.FileMessage;
import netty.FileRequest;
import netty.FilesListResponse;

public class Controller implements Initializable {

    private static final String clientRootDir = "clientDir";
    public ListView<String> serverView;
    public ListView<String> clientView;
    private Path clientPath;
    private Network network;

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientPath = Paths.get(clientRootDir);
        showClientFiles();
        network = new Network(
                msg -> {
                    if (msg instanceof FilesListResponse) {
                        updateServerSide((FilesListResponse) msg);
                    }
                    if (msg instanceof FileMessage) {
                        FileMessage fileMessage = (FileMessage) msg;
                        handleFileMessage(fileMessage);
                    }
                }
        );
    }

    @SneakyThrows
    private void handleFileMessage(FileMessage fileMessage) {
        Files.write(
                clientPath.resolve(Paths.get(fileMessage.getName())),
                fileMessage.getBytes(),
                StandardOpenOption.CREATE
        );
        Platform.runLater(this::showClientFiles);
    }

    public void toServer(ActionEvent actionEvent) throws IOException {
        String fileName = clientView.getSelectionModel().getSelectedItem();
        FileMessage fileMessage = new FileMessage(clientPath.resolve(fileName));
        network.sendMessage(fileMessage);
        network.sendMessage(new FilesListRequest());
    }

    public void fromServer(ActionEvent actionEvent) {
        String fileName = serverView.getSelectionModel().getSelectedItem();
        FileRequest fileRequest = new FileRequest(fileName);
        network.sendMessage(fileRequest);
    }

    private void updateServerSide(FilesListResponse response) {
        Platform.runLater(() -> {
            serverView.getItems().clear();
            serverView.getItems().addAll(response.getFiles());
        });
    }

    private void showClientFiles() {
        clientView.getItems().clear();
        try {
            Files.list(clientPath)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .forEach(f -> clientView.getItems().add(f));
        } catch (IOException ignored) {
        }
    }
}
