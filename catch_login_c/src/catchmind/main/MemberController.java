package catchmind.main;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import catchmind.vo.ChatVO;
import catchmind.vo.MemberVO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MemberController implements Initializable {
	
	@FXML private AnchorPane apl,apa;
	@FXML private TextField txtID,txtIDa,txtNick;
	@FXML private PasswordField txtPw,txtPwa,txtPwc;
	@FXML private Button btnIn,btnOut,btnAdd,btnJoin,btnCancel,btnCheck;
	
	boolean isJoin;
	
	public static MemberVO user;
	
	public void initialize(URL location, ResourceBundle resources) {
		ClientMain.thread.memberController = this;
		System.out.println("완료");
		
		btnIn.setOnAction(event->{
			String id = txtID.getText().trim();
			String pw = txtPw.getText().trim();
			if(id.equals("") || pw.equals("")) {
				showAlert("아이디 또는 비밀번호를 입력해주세요");
			}else {
				MemberVO member = new MemberVO(id,pw);
				member.setOrder(2);
				ClientMain.thread.sendData(member);
			}
		});
		
		btnAdd.setOnAction(event->{
			Platform.runLater(()->{
				apl.setVisible(false);
				apl.setDisable(true);
				apa.setVisible(true);
				apa.setDisable(false);
			});
		});
		
		btnCancel.setOnAction(event->{
				apl.setVisible(true);
				apl.setDisable(false);
				apa.setVisible(false);
				apa.setDisable(true);
				txtIDa.clear();
				txtPwa.clear();
				txtPwc.clear();
				txtNick.clear();
				btnCheck.setText("중복확인");
				isJoin = false;
		});
		
		txtIDa.textProperty().addListener(event->{
			btnCheck.setText("중복확인");
			isJoin = false;
		});
		
		btnCheck.setOnAction(event->{
			String id = txtIDa.getText().trim();
			if(id.equals("") || id == null) {
				showAlert("아이디를 입력해주세요");
				txtIDa.clear();
				txtIDa.requestFocus();
			}else {
				MemberVO member = new MemberVO(id);
				member.setOrder(0);
				ClientMain.thread.sendData(member);
			}
		});
		
					
		btnOut.setOnAction(e->{
			Platform.exit();
		});
		
		btnJoin.setOnAction(event->{
			String id = txtIDa.getText().trim();
			String pw = txtPwa.getText().trim();
			String repw = txtPwc.getText().trim();
			String nick = txtNick.getText().trim();
			if(!isJoin) {
				showAlert("아이디 중복 확인부터 해주세요");
				return;
			}
			 if(!pw.equals(repw) || pw.equals("") || repw.equals("")) {
				showAlert("비밀번호를 확인해주세요");
				txtPwa.clear();
				txtPwc.clear();
				txtPwa.requestFocus();
			}else {
				if(txtNick.getText().trim().equals("") || txtNick.getText().trim() == null) {
					MemberVO member = new MemberVO("Catchmind",id,pw);
					member.setOrder(1);
					ClientMain.thread.sendData(member);
				}else {
				MemberVO member = new MemberVO(nick,id,pw);
				member.setOrder(1);
				ClientMain.thread.sendData(member);
				}
			}
			
		});

	}

	public void setJoinCheck(boolean isSuccess) {
		if(isSuccess) {
			System.out.println("가입성공");
			apl.setVisible(true);
			apl.setDisable(false);
			apa.setVisible(false);
			apa.setDisable(true);
			Platform.runLater(()->{
				showInfo("가입성공");
			});
		}else {
			Platform.runLater(()->{
				showAlert("회원가입 실패");	
			});
			System.out.println("가입 실패");
		}
	}


	public void setLoginCheck(MemberVO vo) {
		if(vo.isSuccess()) {
			Platform.runLater(()->{
				user = vo;
				successLog(vo.getMemberName()+"님 환영합니다.");
			});
		}else {
			Platform.runLater(()->{
				showAlert("로그인 실패");	
			});
		}
	}


	public void receiveData(MemberVO vo) {
		
		switch(vo.getOrder()) {
			case 0 :
				System.out.println("아이디 중복 체크");
				isJoin = vo.isSuccess();
				Platform.runLater(()->{
					if(!isJoin) {
						showAlert("사용할 수 없는 아이디");
					}else {
						btnCheck.setText("사용가능");
					}
				});
				break;
			case 1 :
				System.out.println("회원가입 요청 처리 결과");
				setJoinCheck(vo.isSuccess());
				break;
			case 2 :
				System.out.println("로그인 요청 처리 결과");
				setLoginCheck(vo);
				break;
		}
		
	}
	
	public void showAlert(String text) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("알림");
		alert.setHeaderText(text);
		alert.show();
	}
	
	public void showInfo(String text) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("성공");
		alert.setHeaderText(text);
		alert.show();
	}
	public void gameStage() throws Exception {
		System.out.println("gameStage 호출");
		FXMLLoader loader = new FXMLLoader(
			getClass().getResource("../game/Canvas.fxml") // 로그인 Main fxml;
		);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setScene(scene);
		stage.show(); // 로그인창 화면 띄우기 
		Stage secondStage = (Stage)btnIn.getScene().getWindow();//스테이지 가져오기
		secondStage.close();//기존창 종료
		Platform.runLater(()->{
			String name = user.getMemberName();
			ChatVO nick = new ChatVO(name,1);
			ClientMain.thread.sendData(nick);
		});
	}
	
	public void successLog(String text) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("성공");
		alert.setHeaderText(text);
		Optional<ButtonType> result = alert.showAndWait();
		//확인 버튼 선택
		if(result.get() == ButtonType.OK) {
			Platform.runLater(()->{
				try {
					gameStage();
				} catch (Exception e) {
					System.out.println("게임창 띄우기 실패");
					e.printStackTrace();
				}
			});
		}
	}

}
