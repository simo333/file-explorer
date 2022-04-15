package filemanager.promptdialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class UserInputPrompt {
    private static Stage stage = new Stage(StageStyle.UTILITY);
    private static Parent parent;


    //Constructor -> create a new prompt dialog that will enable user to type a new file name
    //Modality.APPLICATION_MODAL -> makes prompt dialog being the only clickable window
    public UserInputPrompt(String pathToFxml, String stageTitle) throws IOException {
        parent = FXMLLoader.load(getClass().getResource(pathToFxml));
        stage.setScene(new Scene(parent));
        stage.setTitle(stageTitle);
        stage.setResizable(false);
        if (!(stage.getModality() == Modality.APPLICATION_MODAL))
            stage.initModality(Modality.APPLICATION_MODAL);
    }

    public static Stage getStage() {
        return stage;
    }

}


