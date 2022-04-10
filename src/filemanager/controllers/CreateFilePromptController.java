package filemanager.controllers;

import filemanager.IOManager;
import filemanager.promptdialogs.UserInputPrompt;
import filemanager.utility.Alerts;
import filemanager.utility.FileNameValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;

public class CreateFilePromptController {
    private Map<String, String> listOfTypes = new HashMap<>();
    @FXML
    public ComboBox<String> typeComboBox;
    @FXML
    public Button cancelButton;
    @FXML
    public TextField fileNameField;
    @FXML
    public Button createButton;
    @FXML //whole prompt dialog handler
    public VBox promptWindow;



    public void initialize(){
        //initializing HashMap listOfTypes
        listOfTypes.put("Text file", ".txt");
        listOfTypes.put("Document file of MS Word", ".docx");
        listOfTypes.put("MS Excel Document", ".xlsx");
        listOfTypes.put("PowerPoint presentation", ".pptx");
        listOfTypes.put("Formated Document RTF", ".rtf");
        listOfTypes.put("BMP Picture", ".bmp");

        typeComboBox.setItems(getObservableKeyList());

    }

    public ObservableList<String> getObservableKeyList(){
        return FXCollections.observableList(new ArrayList<>(listOfTypes.keySet()));

    }

    //close prompt dialog when cancelButton clicked by left mouse button
    public void cancelClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
            UserInputPrompt.getStage().close();
    }

    //Go to createFile method when Enter pressed on a fileNameField or createButton
    public void createFileByButton(KeyEvent keyEvent) throws IOException {
        if(keyEvent.getCode() == KeyCode.ENTER)
            createFile();
    }

    //Go to createFile method when createButton clicked by left mouse button
    public void createFileByClick(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
            createFile();
    }

    //get text from fileNameField, then try to create a new file (alert if created and alert when not created)
    //close prompt dialog when a new file is created only
    private void createFile() throws IOException {
        String fileName = fileNameField.getText();
        if(typeComboBox.getValue() == null) {
            Alerts.noTypeChosenAlert();
        } else if(!FileNameValidator.validate(fileName)) {
            Alerts.invalidNameAlert();
        } else {
            fileName = fileName + listOfTypes.get(typeComboBox.getValue());
            int creatingResult =IOManager.createNewFile(fileName);
            if (creatingResult == 1) {
                Alerts.createdAlert(fileName);
                UserInputPrompt.getStage().close();
            } else if(creatingResult == 0){
                Alerts.sameNameExistsAlert();
            } else if(creatingResult == -1){
                Alerts.accessDeniedAlert();
                UserInputPrompt.getStage().close();
            }
        }
    }

    //close prompt dialog when ESCAPE pressed anywhere
    public void closePromptDialog(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ESCAPE)
            UserInputPrompt.getStage().close();
    }
}
