package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage stage;
    public Stage addItemStage = new Stage();
    public Stage deleteItemStage = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        adminMainPage();
    }

    public void adminMainPage() throws IOException {
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("admin_main_page.fxml"));
        Parent root = loader.load();
        AdminMainPage controller = loader.getController();
        controller.setMain(this);
        //   Scene scene1 = new Scene(root,366,590);
        stage.setScene(new Scene(root,800,650));
        stage.setTitle("Admin Page");
        stage.show();
    }

    public void accountPage() throws IOException {
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("accounts.fxml"));
        Parent root = loader.load();
        Accounts controller2 = loader.getController();
        controller2.setMain(this);
        // controller2.ConnectServer();
        //   Scene scene1 = new Scene(root,366,590);
        stage.setScene(new Scene(root,800,650));
        stage.setTitle("AdminWindow Page");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
