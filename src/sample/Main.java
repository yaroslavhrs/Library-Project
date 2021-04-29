package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/sample/view/start_page.fxml"));
        window.getIcons().add(new Image("sample/assets/icon.png"));
        window.setTitle("База даних бібліотеки");
        window.setScene(new Scene(root, 1000, 700));
        window.setResizable(false);
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
