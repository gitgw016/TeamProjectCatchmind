package catchmind.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerMain extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Server.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
