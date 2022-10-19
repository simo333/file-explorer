package filemanager.controllers;

import filemanager.IOManager;
import filemanager.promptdialogs.UserInputPrompt;
import filemanager.utility.Alerts;
import filemanager.utility.FileNameValidator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.Path;

public class EditNamePromptController {
    @FXML
    public Label oldNameText;
    @FXML
    public TextField fileNameField;
    @FXML
    public Label extensionLabel;
    @FXML
    public Button editButton;
    @FXML
    public Button cancelButton;
    @FXML //whole prompt dialog handler
    public VBox promptWindow;
    @FXML
    public ComboBox typeComboBox;


    //TODO add / check description to all methods
    //show name that is editing in oldNameText label
    public void initialize() {
        oldNameText.setText(IOManager.getAddressPath().getFileName().toString());
        fileNameField.setText(IOManager.getFileNameOnly());
        extensionLabel.setText(IOManager.getExtension());
        UserInputPrompt.getStage().setOnCloseRequest(windowEvent -> {
            try {
                onXClicked();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    //calls method editName() when enter is pressed
    public void editNameByButton(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER)
            editName();
    }

    //calls method editName() when mouse clicked
    public void editNameByClick(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
            editName();
    }


    //close prompt dialog when cancelButton clicked by left mouse button
    public void cancelClicked(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            IOManager.moveBackPath();
            UserInputPrompt.getStage().close();
        }
    }

    //close prompt dialog when ESCAPE pressed anywhere
    public void closePromptDialog(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            IOManager.moveBackPath();
            UserInputPrompt.getStage().close();
        }
    }

    //renames filename or folder name
    //TODO DESCRIPTION
    //TODO CHECK EDITING
    private void editName() throws IOException {
        String newName = fileNameField.getText();
        Path oldName = IOManager.getAddressPath().getFileName();
        if (FileNameValidator.validate(newName)) {
            int editingResult = IOManager.editName(newName);
            if (editingResult == 1) {
                Alerts.editedAlert(oldName.toString(), (newName + IOManager.getExtension(oldName)));
                UserInputPrompt.getStage().close();
            } else if (editingResult == 0) {
                Alerts.sameNameExistsAlert();
            } else if (editingResult == -1) {
                Alerts.accessDeniedAlert();
                UserInputPrompt.getStage().close();
            }

        } else {
            Alerts.invalidNameAlert();
        }
    }

    public void onXClicked() throws IOException {
        IOManager.moveBackPath();
    }


}