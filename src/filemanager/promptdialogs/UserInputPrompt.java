package filemanager.promptdialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class UserInputPrompt {
    private static final Stage STAGE = new Stage(StageStyle.UTILITY);


    //Constructor -> create a new prompt dialog that will enable user to enter new file name
    //Modality.APPLICATION_MODAL -> makes prompt dialog the only clickable area
    public UserInputPrompt(String pathToFxml, String stageTitle) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(pathToFxml)));
        STAGE.setScene(new Scene(parent));
        STAGE.setTitle(stageTitle);
        STAGE.setResizable(false);
        if (STAGE.getModality() != Modality.APPLICATION_MODAL)
            STAGE.initModality(Modality.APPLICATION_MODAL);
    }

    public static Stage getStage() {
        return STAGE;
    }

}


