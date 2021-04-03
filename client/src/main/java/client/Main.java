package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static client.UIutils.updateUI;
import static javafx.fxml.FXMLLoader.load;


public class Main
        extends Application
{

    private Stage loginStage;
    private Stage mainStage;

    private static final NetConnection conn = new NetConnection();

    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final String TITLE = "Cloud Storage Client";
    private static final String MAIN_FXML_PATH = "/main.fxml";
    private static final String LOGIN_FXML_PATH = "/login.fxml";
    static final String STORAGE_DIR = "client/client_storage";


    public static NetConnection getNetConnection()
    {
        return conn;
    }


    @Override
    public void start(Stage primaryStage)
            throws Exception
    {
        mainStage = primaryStage;
        loginStage = new Stage();
        loginStage.setTitle(TITLE + " - Autorization");

        conn.open();

        showLoginStage(() -> updateUI(this::showMainStage));
    }


    @Override
    public void stop()
            throws Exception
    {
        super.stop();

        conn.close();
    }


    private void showLoginStage(Runnable callback)
            throws IOException
    {
        URL res = getClass().getResource(LOGIN_FXML_PATH);
        FXMLLoader loader = new FXMLLoader(res);

        Parent root = loader.load();
        RegController ctrl = loader.getController();
        ctrl.setCallback(callback);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        loginStage.setScene(scene);

        loginStage.showAndWait();
    }


    private void showMainStage()
    {
        loginStage.getScene().getWindow().hide();
        URL res = getClass().getResource(MAIN_FXML_PATH);

        try
        {
            Parent root = load(res);
            Scene scene = new Scene(root, WIDTH, HEIGHT);
            mainStage.setScene(scene);
            mainStage.setTitle(TITLE);
            mainStage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public static void main(String... args)
    {
        launch(args);
    }

}
