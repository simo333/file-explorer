package filemanager.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Alerts {
    private static Alert alert;

    public static Alert getAlert(){
        return alert;
    }

    public static void noDirectoryFoundAlert(){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No such directory found");
        alert.setContentText("Please enter a valid path.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK)
            alert.close();
    }

    public static void cannotMoveBackAlert() {
        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("You cannot get back from here!");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK)
            alert.close();
    }

    public static void noDirSelectedAlert(){
        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Choose name that you want to manage.");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.OK)
            alert.close();
    }

    public static void confirmToDeleteAlert(){
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation of deletion");
        alert.setContentText("Press \"OK\" if you want to delete " +
                "or \"Cancel\" to cancel this operation.");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.OK)
            alert.close();
    }

    public static void invalidNameAlert(){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid name!");
        alert.setContentText("The name can have max 100 characters" +
                " and cannot contain these characters: < > / \\ * ? \" : |");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK)
            alert.close();
    }

    public static void createdAlert(String name){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successfully created!");
        alert.setHeaderText("\"" + name + "\" has been created.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK)
            alert.close();
    }

    public static void editedAlert(String oldName, String newName){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successfully changed!");
        alert.setHeaderText("\"" + oldName + "\"" + " has been changed to " +
                "\"" + newName + "\".");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK)
            alert.close();
    }

    public static void sameNameExistsAlert(){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Cannot create it!");
        alert.setContentText("Check if this name does already exists.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK)
            alert.close();
    }

    public static void noTypeChosenAlert(){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText("Choose type first!");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK)
            alert.close();
    }

    //shows alert if AccessDeniedException occurs
    public static void accessDeniedAlert(){
        Alert accessDenied = new Alert(Alert.AlertType.ERROR);
        accessDenied.setTitle("Error");
        accessDenied.setHeaderText("You are not allowed to do this here.");
        accessDenied.setContentText("Please change the directory.");
        accessDenied.showAndWait();
        if (accessDenied.getResult() == ButtonType.OK)
            accessDenied.close();
    }

    public static void successfullyDeletedAlert(String fileName) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successfully deleted!");
        alert.setHeaderText("\"" + fileName + "\"" + " has been deleted.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK)
            alert.close();
    }

    public static void cannotDeleteAlert() {
        Alert accessDenied = new Alert(Alert.AlertType.ERROR);
        accessDenied.setTitle("Error");
        accessDenied.setHeaderText("You cannot delete this.");
        accessDenied.showAndWait();
        if (accessDenied.getResult() == ButtonType.OK)
            accessDenied.close();
    }

    public static void notEmptyDirAlert() {
        Alert accessDenied = new Alert(Alert.AlertType.ERROR);
        accessDenied.setTitle("Error");
        accessDenied.setHeaderText("You cannot delete not empty directory.");
        accessDenied.showAndWait();
        if (accessDenied.getResult() == ButtonType.OK)
            accessDenied.close();
    }
}
