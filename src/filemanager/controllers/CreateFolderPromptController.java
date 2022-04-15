package filemanager.controllers;

import filemanager.IOManager;
import filemanager.promptdialogs.UserInputPrompt;
import filemanager.utility.Alerts;
import filemanager.utility.FileNameValidator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CreateFolderPromptController {

    @FXML
    public VBox promptWindow;
    @FXML
    public TextField folderNameField;
    @FXML
    public Button createButton;
    @FXML
    public Button cancelButton;


    public void createFolderByButton(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER)
            createFolder();
    }

    public void createFolderByClick(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
            createFolder();
    }

    //close prompt dialog when cancelButton clicked by left mouse button
    public void cancelClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
            UserInputPrompt.getStage().close();
    }

    //close prompt dialog when ESCAPE pressed anywhere
    public void closePromptDialog(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ESCAPE)
            UserInputPrompt.getStage().close();
    }


    private void createFolder() throws IOException {
        String folderName = folderNameField.getText();
        if (!FileNameValidator.validate(folderName)) {
            Alerts.invalidNameAlert();
        } else {
            int creatingResult = IOManager.createNewFolder(folderName);
            if (creatingResult == 1) {
                Alerts.createdAlert(folderName);
                UserInputPrompt.getStage().close();
            } else if (creatingResult == 0) {
                Alerts.sameNameExistsAlert();
            } else if (creatingResult == -1) {
                Alerts.accessDeniedAlert();
                UserInputPrompt.getStage().close();
            }
        }

    }
}

