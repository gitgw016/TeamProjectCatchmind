package push_man.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import push_man.game.GameController;
import push_man.member.MemberController;
import push_man.vo.MemberVO;
import push_man.vo.ScoreVO;
import push_man.waitting_room.WaittingRoomController;

public class MainThread extends Thread {
	
	public MemberController memberController;
	public GameController gameController;
	public WaittingRoomController waittingRoomController;
	

	// Server에서 발신한 내용을 Receive
	@Override
	public void run() {
		ObjectInputStream ois = null;
		try {
			while(true) {
				if(isInterrupted()) {break;}
				Object obj =null;
				ois = new ObjectInputStream(
						ClientMain.socket.getInputStream()
					);
				if((obj = ois.readObject()) != null) {
					System.out.println(obj);
					if(obj instanceof MemberVO) {
						// 회원관련 요청 처리 결과
						memberController.receiveData((MemberVO)obj);
					}else if(obj instanceof ScoreVO) {
						// 기록관련 요청
						System.out.println("게임 관련 요청 처리 완료");
						gameController.receiveData((ScoreVO)obj);
					}else if(obj instanceof List<?> || obj instanceof String){
						// 대기실 정보 요청 처리 - 대기실 chat
						waittingRoomController.receiveData(obj);
					}
				}
			}
		} catch (Exception e) {
			stopClient();
		}
	}
	
	public void sendData(Object o) {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(
					ClientMain.socket.getOutputStream()
				);
			oos.writeObject(o);
			oos.flush();
		} catch (IOException e) {
			stopClient();
		}
	}
	
	// Client Server와 연결 종료
	public void stopClient() {
		System.out.println("Stop Client 호출");
		this.interrupt();
		if(ClientMain.socket != null && !ClientMain.socket.isClosed()) {
			try {
				ClientMain.socket.close();
			} catch (IOException e) {}
		}
		Platform.runLater(()->{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText(null);
			alert.setContentText("서버와 연결이 끊겼습니다.");
			alert.showAndWait();
			Platform.exit();
		});
	}
	
}





