package push_man.waitting_room;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WaittingRoomTestMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{
		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Watting_room.fxml")));
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
