package org.lairdham;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.lairdham.controllers.PopupController;

import java.io.IOException;

public class Utils {

    public static void showPopup(String popupTitle, String popupBody) throws IOException {
        showPopup(popupTitle, popupBody, null);
    }
    public static void showPopup(String popupTitle, String popupBody, TextAlignment bodyAlignment) throws IOException {

        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.initStyle(StageStyle.UTILITY);

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/popup.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if(t.getCode()== KeyCode.ESCAPE) {
                popupWindow.close();
            }
        });
        popupWindow.setScene(scene);

        PopupController popupController = fxmlLoader.getController();
        popupController.setData(popupTitle, popupBody, bodyAlignment);
        popupWindow.showAndWait();
    }
}
