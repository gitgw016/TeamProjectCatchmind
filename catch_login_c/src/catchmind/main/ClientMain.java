package catchmind.main;

import java.io.IOException;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientMain extends Application {
	
	public static Socket socket; //클라이언트가 가진소켓 모든 클래스에서 사용가능(public static) 정의 
	public static MainThread thread;//클라이언트의 메인 스레드 정의
	
	private Stage primaryStage;	// 추가
	
	@Override
	public void start(Stage primaryStage) throws Exception {	// 추가
		this.primaryStage = primaryStage;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml")); // 이름이 Main인 fxml파일 추가, img폴더에 mainscreen 이미지 파일 추가
		Parent root = loader.load();
		Scene scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.centerOnScreen();
		primaryStage.show();
		initClient();//1.실행 initClient()
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	//1.정의 initClient()
	public void initClient() {
		new Thread(()->{
			try {
				socket = new Socket("192.168.0.3",8001);
				System.out.println("연결 성공");
				thread = new MainThread();
				thread.setDaemon(true);
				thread.start();
				Platform.runLater(()->{
					try {
						showMemberStage();//2.실행 showMemberStage()
					} catch (Exception e) {}
				});
			} catch (Exception e) {
				System.out.println("연결 실패");
				Platform.runLater(()->{
					//서버에 연결 실패하면 알림창이 뜨는 메소드가 실행됨
					//3.실행 showAlert(String text)
					primaryStage.close();
					showAlert("연결실패\n다시 실행하라");
				});
			}
		}).start();;
	}
	//2.정의 showMemberStage()
	public void showMemberStage(){
		try {
			System.out.println("showMemberStage 호출");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Root.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
			primaryStage.close();	// 추가
		} catch (IOException e) {
			System.out.println("show?");
			e.getMessage();
		}
		
	}
	//3.정의 showAlert(String text)
	public void showAlert(String text) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("알림");
		alert.setHeaderText(text);
		alert.setContentText("확인 : 재시도 \n취소 : 종료");
		alert.showAndWait(); //창을 띄우고 버튼을 누르기 전까지 대기하는 메소드
		if(alert.getResult() == ButtonType.OK) {
			//OK버튼을 누르면 다시 서버연결 시도
			primaryStage.show();	// 추가
			initClient();
		}else if(alert.getResult() == ButtonType.CANCEL) {
			Platform.exit();//CANCEL 버튼을 누르면 모든창을 닫음
		}
	}
}
