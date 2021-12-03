package push_man.main;

import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientMain extends Application implements MainInterface{

	public static Socket socket;
	public static MainThread thread;
	
	private Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		this.primaryStage = primaryStage;
		FXMLLoader loader = new FXMLLoader(
			getClass().getResource("main.fxml")
		);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		// scene 배경색을 투명으로 지정
		scene.setFill(Color.TRANSPARENT);
		primaryStage.setScene(scene);
		// 무대 색상을 투명하게 지정
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		// 무대를 모니터 중앙으로 지정
		primaryStage.centerOnScreen();
		primaryStage.show();
		initClient();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void initClient() {
		new Thread(()->{
			
			try {
				socket = new Socket("192.168.1.113",8001);
				System.out.println("연결 성공");
				thread = new MainThread();
				thread.setDaemon(true);
				thread.start();
				
				Platform.runLater(()->{
					try {
						showMemberStage();
					} catch (Exception e) {}
				});
			} catch (Exception e) {
				System.out.println("연결 실패");
				Platform.runLater(()->{
					primaryStage.close();
					showAlert("연결실패\n서버와연결할 수 없습니다.\n다시 실행해주세요.");
				});
			}
		}).start();
	}

	@Override
	public void showMemberStage() throws Exception {
		System.out.println("showMemberStage 호출");
		FXMLLoader loader = new FXMLLoader(
			getClass().getResource("../member/Member.fxml")
		);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setScene(scene);
		stage.show();
		primaryStage.close();
	}

	@Override
	public void showAlert(String text) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("알림");
		alert.setHeaderText(text);
		alert.setContentText("확인 : 재시도 \n취소:종료");
		alert.showAndWait();
		if(alert.getResult() == ButtonType.OK) {
			primaryStage.show();
			initClient();
		}else if(alert.getResult() == ButtonType.CANCEL) {
			Platform.exit();
		}
	}
}












