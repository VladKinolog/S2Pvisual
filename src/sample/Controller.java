package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class Controller {
    public volatile static int i = 0;

    @FXML
    private Text text;



    @FXML protected void onClickB1 (ActionEvent event){

        text.setText("епт");

    }



    @FXML protected void onClickB2 (ActionEvent event){

        text.setText(String.valueOf(i));
    }


}
