package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        Thread th = new Thread(new Runnable() {

            @Override
            public void run() {

                while (true) {
                    Controller.i++;
                    System.out.println(Controller.i);
                    //Controller.text.setText (String.valueOf(Controller.i));
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        th.setDaemon(true);
        th.start();


    }


    public static void main(String[] args) {
        launch(args);

    }
}
