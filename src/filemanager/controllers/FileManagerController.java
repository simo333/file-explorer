package filemanager.controllers;

import filemanager.IOManager;
import filemanager.promptdialogs.UserInputPrompt;
import filemanager.utility.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileManagerController {

    @FXML
    private BorderPane fileManagerWindow;
    @FXML
    private TextField pathAddress = new TextField();
    @FXML
    private ListView<Path> pathsFoundList = new ListView<>();
    @FXML
    private Button goButton;
    @FXML
    private Button backButton;
    @FXML
    private Button createFileButton;
    @FXML
    private Button createFolderButton;
    @FXML
    private Button editNameButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button findButton;
    @FXML
    private TextField findTextField;

    //Paths to FXML resources
    private final String createFilePromptPath = "/filemanager/resources/create-file-prompt.fxml";
    private final String editNamePromptPath = "/filemanager/resources/edit-name-prompt.fxml";
    private final String createFolderPromptPath = "/filemanager/resources/create-folder-prompt.fxml";
    //long var for mouse clicking time count (to verify double click in listItemSelected() method
    private long lastTimeClicked = 0;

//  ExecutorService service = Executors.newFixedThreadPool(5);

    //Fill ListView with folders and files in C:\
    public void initialize() throws IOException {
        refreshPathsFoundList();
//        service.submit(new WatchServiceTask(this));

//        watchServiceManager = new WatchServiceManager(this, Thread.currentThread());
//        Thread thread = new Thread(watchServiceManager);
//        thread.setDaemon(true);
//        Platform.runLater(watchServiceManager);
    }


    public ListView<Path> getPathsFoundList() {
        return pathsFoundList;
    }

    public void refreshPathsFoundList() throws IOException {
        pathsFoundList.setItems(IOManager.getObservablePathList());
    }

    //Go to a new address (or open the file) when Go button is clicked by left mouse button
    public void goButtonClicked(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
            setAddress();

    }

    //Go to a new address (or open the file) when Go button is pressed by Enter
    public void goButtonKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER)
            setAddress();
    }

    //Go to a new address (or open the file) when address field is pressed by enter
    public void addressFieldKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER)
            setAddress();
    }

    //Move to the parent address when back button is clicked by left mouse button
    public void moveBackButton(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
            backAddress();
    }

    //Move to the parent address when escape button is pressed anywhere
    public void backspacePressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.BACK_SPACE)
            backAddress();

    }

    //Setting a new address (or opening selected file) if exists
    private void setAddress() throws IOException {
        if (!IOManager.setPathOrOpen(pathAddress.getText())) {
            Alerts.noDirectoryFoundAlert();
            pathAddress.setText(IOManager.getAddressPath().toString());

        } else if (Files.isDirectory(IOManager.getAddressPath())) {
            pathsFoundList.setItems(IOManager.getObservablePathList());
        }
    }

    //Cutting back address if possible
    private void backAddress() throws IOException {
        if (!(IOManager.moveBackPath())) {
            Alerts.cannotMoveBackAlert();
        }
        pathAddress.setText(IOManager.getAddressPath().toString());
        pathsFoundList.setItems(IOManager.getObservablePathList());
    }

    //Processing selecting items in ListView
    //Single click -> set selected item in address field
    //Double click (under 255ms) -> move to the selected directory of open the file
    public void listItemSelected(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            long diffClicksTime = 0;
            long currentTime = System.currentTimeMillis();
            if (!(pathsFoundList.getSelectionModel().getSelectedItem() == null)) {
                pathAddress.setText(IOManager.getAddressPath().toString() + "\\");
                pathAddress.setText(pathAddress.getText() + pathsFoundList.getSelectionModel().getSelectedItem());
                if (lastTimeClicked != 0 && currentTime != 0) {
                    diffClicksTime = currentTime - lastTimeClicked;
                    if (diffClicksTime <= 255)
                        setAddress();
                }
                lastTimeClicked = currentTime;
            }
        }
    }

    //Keyboard shortcuts when working with ListView
    //ESC -> clear selection
    //ENTER -> move to the selected directory of open the file
    //UP_ARROW and DOWN_ARROW -> set selected item in ListView to the address field
    public void listViewKeyBoardFunctions(KeyEvent keyEvent) throws IOException {
        //Clear address field while ESCAPE pressed
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            pathsFoundList.getSelectionModel().clearSelection();
            pathAddress.setText(IOManager.getAddressPath().toString());
        }
        //Go to a new address when ENTER pressed
        if (keyEvent.getCode() == KeyCode.ENTER)
            setAddress();
        //Set selected address in address filed when UP_ARROW or DOWN ARROW pressed
        if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.UP) {
            pathAddress.setText(IOManager.getAddressPath().toString() + "\\");
            pathAddress.setText(pathAddress.getText() + pathsFoundList.getSelectionModel().getSelectedItem());
        }
    }

    //Creates a new files and refreshes ListView
    public void createFile(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            new UserInputPrompt(createFilePromptPath, "Create a file");
            UserInputPrompt.getStage().showAndWait();
            pathsFoundList.setItems(IOManager.getObservablePathList());
        }
    }

    /*Changes name and refreshes ListView
    Check if any path is selected, if no then call alert
    if yes, then if left mouse button is clicked on Edit name button then set addressPath in IOManager
    open a new window promp with editing form
    at the end set pathAddress field and pathsFound ListView*/
    public void editName(MouseEvent mouseEvent) throws IOException {
        if (!pathsFoundList.getSelectionModel().isEmpty()) {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                IOManager.setPath(pathAddress.getText());
                new UserInputPrompt(editNamePromptPath, "Edit name");
                UserInputPrompt.getStage().showAndWait();
                pathAddress.setText(IOManager.getAddressPath().toString());
                pathsFoundList.setItems(IOManager.getObservablePathList());
            }
        } else {
            Alerts.noDirSelectedAlert();
        }
        pathsFoundList.getSelectionModel().clearSelection();
    }

    //Creates a new folder and refreshes ListView
    public void createFolder(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            IOManager.setPath(pathAddress.getText());
            new UserInputPrompt(createFolderPromptPath, "Create a folder");
            UserInputPrompt.getStage().showAndWait();
            pathAddress.setText(IOManager.getAddressPath().toString());
            pathsFoundList.setItems(IOManager.getObservablePathList());
        }
    }

    /* checks if any path is selected, if no then calls alert
    if yes then try to delete selected path and work with return states
    returned states: 1 - correctly deleted | 0 - not deleted | -1 - access denied
    at the end clear selection, set pathAddress field and pathsFound ListView */
    public void deletePath(MouseEvent mouseEvent) throws IOException {
        if (!pathsFoundList.getSelectionModel().isEmpty()) {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                Alerts.confirmToDeleteAlert();
                if (Alerts.getAlert().getResult() == ButtonType.OK) {
                    Path deletedPath = Paths.get(pathAddress.getText());
                    int deletingResult = IOManager.deletePath(deletedPath.toString());
                    if (deletingResult == 1) {
                        Alerts.successfullyDeletedAlert(deletedPath.getFileName().toString());
                    } else if (deletingResult == 0) {
                        Alerts.cannotDeleteAlert();
                    } else if (deletingResult == -1) {
                        Alerts.accessDeniedAlert();
                    } else if (deletingResult == -2) {
                        Alerts.notEmptyDirAlert();
                    }
                }
                pathsFoundList.getSelectionModel().clearSelection();
                pathsFoundList.setItems(IOManager.getObservablePathList());
                pathAddress.setText(IOManager.getAddressPath().toString());
            }
        } else Alerts.noDirSelectedAlert();
    }

    public void findPressedOrTyped(KeyEvent keyEvent) throws IOException {
        pathsFoundList.setItems(IOManager.find(findTextField.getText()));
    }

    public void findClicked(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            pathsFoundList.setItems(IOManager.find(findTextField.getText()));
        }

    }
}
