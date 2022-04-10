package filemanager.controllers;

import filemanager.IOManager;
import filemanager.promptdialogs.UserInputPrompt;
import filemanager.utility.Alerts;
import filemanager.utility.FileNameValidator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.Path;

public class EditNamePromptController{
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
        if(keyEvent.getCode() == KeyCode.ENTER)
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
        if(keyEvent.getCode() == KeyCode.ESCAPE) {
            IOManager.moveBackPath();
            UserInputPrompt.getStage().close();
        }
    }


//    //renames filename or folder name
//    //TODO DESCRIPTION
//    private void editName() throws IOException {
//        String newName = fileNameField.getText();
//        Path oldName = IOManager.getAddressPath().getFileName();
//        if (FileNameValidator.validate(newName)) {
//            int editingResult = IOManager.editName(newName);
//            if (editingResult == 1) {
//                Alert alertFileCreated = new Alert(Alert.AlertType.INFORMATION);
//                alertFileCreated.setTitle("Successfully changed!");
//                alertFileCreated.setHeaderText("\"" + oldName + "\"" + " has been changed to " +
//                        "\"" + newName + IOManager.getExtension() + "\".");
//                alertFileCreated.showAndWait();
//                if (alertFileCreated.getResult() == ButtonType.OK)
//                    alertFileCreated.close();
//                UserInputPrompt.getStage().close();
//            } else if(editingResult == 0) {
//                    Alert alertFileNotCreated = new Alert(Alert.AlertType.ERROR);
//                    alertFileNotCreated.setTitle("Error");
//                    alertFileNotCreated.setHeaderText("Cannot create such a file!");
//                    alertFileNotCreated.setContentText("Check if a file with this name does already exists.");
//                    alertFileNotCreated.showAndWait();
//                if (alertFileNotCreated.getResult() == ButtonType.OK)
//                    alertFileNotCreated.close();
//            } else if(editingResult == -1){
//                accessDeniedAlert();
//                UserInputPrompt.getStage().close();
//            }
//
//        } else {
//            Alert alertInvalidFileName = new Alert(Alert.AlertType.ERROR);
//            alertInvalidFileName.setTitle("Error");
//            alertInvalidFileName.setHeaderText("Invalid file name!");
//            alertInvalidFileName.setContentText("Your file name can have max 100 characters" +
//                    " and cannot contain these characters: < > / \\ * ? \" : |");
//            alertInvalidFileName.showAndWait();
//            if (alertInvalidFileName.getResult() == ButtonType.OK)
//                alertInvalidFileName.close();
//        }
//    }
    //renames filename or folder name
    //TODO DESCRIPTION
    //TODO CHECK EDITING
    private void editName() throws IOException {
        String newName = fileNameField.getText();
        Path oldName = IOManager.getAddressPath().getFileName();
        if (FileNameValidator.validate(newName)) {
            int editingResult = IOManager.editName(newName);
            if (editingResult == 1) {
                System.out.println("newName: " + newName + IOManager.getExtension());
                Alerts.editedAlert(oldName.toString(), (newName + IOManager.getExtension(oldName)));
                UserInputPrompt.getStage().close();
            } else if(editingResult == 0) {
                Alerts.sameNameExistsAlert();
            } else if(editingResult == -1){
                Alerts.accessDeniedAlert();
                UserInputPrompt.getStage().close();
            }

        } else {
            Alerts.invalidNameAlert();
        }
    }

//    //shows alert if AccessDeniedException occurs
//    private static void accessDeniedAlert(){
//        Alert accessDenied = new Alert(Alert.AlertType.ERROR);
//        accessDenied.setTitle("Error");
//        accessDenied.setHeaderText("You are not allowed to do this here.");
//        accessDenied.setContentText("Please change the directory.");
//        accessDenied.showAndWait();
//        if (accessDenied.getResult() == ButtonType.OK)
//            accessDenied.close();
//    }

    public void onXClicked() throws IOException {
        IOManager.moveBackPath();
    }


}