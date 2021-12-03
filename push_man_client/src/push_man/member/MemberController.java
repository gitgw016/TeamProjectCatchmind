package push_man.member;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import push_man.main.ClientMain;
import push_man.vo.MemberVO;

public class MemberController implements Initializable , MemberInterface{

	@FXML private WebView webView;
	@FXML private ImageView imageView;
	@FXML private AnchorPane joinAnchor, loginAnchor;
	@FXML private TextField loginID, joinID, joinName;
	@FXML private PasswordField loginPW, joinPW, joinRePW;
	@FXML private Button btnLogin, btnJoin;
	@FXML private Hyperlink loginLinkBtn, joinLinkBtn;
	@FXML private Label checkID;
	
	// 중복아이디 체크 완료
	boolean isJoin;
	
	// 로그인 완료 시 추가 될 사용자 정보 
	public static MemberVO user;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		ClientMain.thread.memberController = this;
		
		WebEngine wg = webView.getEngine();
		wg.load("https://www.youtube.com/embed/0xBu251omcw");
		wg.getLoadWorker().stateProperty()
		.addListener(new ChangeListener<State>() {
			@Override
			public void changed(
					ObservableValue<? extends State> observable, 
					State oldValue, 
					State newValue) {
				// WebView Load 완료 상태
				if(newValue.equals(State.SUCCEEDED)) {
					imageView.setVisible(false);
				}
			}
		});
		
		setHyperLink();
		setJoinEvent();
		setLoginEvent();
		System.out.println("완료");
		
	}

	@Override
	public void setHyperLink() {
		joinLinkBtn.setOnAction(e->{
			Platform.runLater(()->{
				loginAnchor.setVisible(false);
				loginAnchor.setDisable(true);
				joinAnchor.setVisible(true);
				joinAnchor.setDisable(false);
				joinID.requestFocus();
			});
		});
		
		loginLinkBtn.setOnAction(e->{
			loginAnchor.setVisible(true);
			loginAnchor.setDisable(false);
			joinAnchor.setVisible(false);
			joinAnchor.setDisable(true);
			loginID.requestFocus();
		});
	}

	@Override
	public void initLoginUI() {
		Platform.runLater(()->{
			loginID.clear();
			loginPW.clear();
			loginID.requestFocus();
		});
	}

	@Override
	public void initJoinUI() {
		Platform.runLater(()->{
			joinID.clear();
			joinPW.clear();
			joinName.clear();
			joinRePW.clear();
			joinID.requestFocus();
		});
	}

	@Override
	public void setLoginEvent() {
		loginID.setOnKeyPressed(event->{
			if(event.getCode() == KeyCode.ENTER) {
				loginPW.requestFocus();
			}
		});
		// 로그인 패스워드 작성란에 Enter 키가 눌러지면
		// btnLogin Button action event 실행
		loginPW.setOnKeyPressed(event->{
			if(event.getCode() == KeyCode.ENTER) {
				btnLogin.fire();
			}
		});
		
		btnLogin.setOnAction(event->{
			String id = loginID.getText().trim();
			String pw = loginPW.getText().trim();
			
			if(id.equals("")) {
				loginID.requestFocus();
			}else if(pw.equals("")) {
				loginPW.requestFocus();
			}else {
				MemberVO member = new MemberVO(id,pw);
				member.setOrder(2);
				ClientMain.thread.sendData(member);
			}
		});
		
	}

	@Override
	public void setJoinEvent() {
		joinID.textProperty().addListener((o,b,text)->{
			System.out.println(text);
			if(!text.trim().equals("")) {
				MemberVO member = new MemberVO(text);
				// 아이디 중복 체크
				member.setOrder(0);
				ClientMain.thread.sendData(member);
			}else {
				setJoinIDCheck(false);
			}
		});
		
		btnJoin.setOnAction(event->{
			String id = joinID.getText().trim();
			String pw = joinPW.getText().trim();
			String repw = joinRePW.getText().trim();
			String nick = joinName.getText().trim();
			
			if(id.equals("") || !isJoin) {
				joinID.clear();
				joinID.requestFocus();
			}else if(!pw.equals(repw)) {
				joinPW.clear();
				joinRePW.clear();
				joinPW.requestFocus();
				checkID.setText("비밀번호가 일치하지 않습니다.");
			}else {
				MemberVO member = new MemberVO(nick,id,pw);
				member.setOrder(1);
				ClientMain.thread.sendData(member);
			}
					
		});
	}

	// 아이디 중복체크 UI 변경
	@Override
	public void setJoinIDCheck(boolean isChecked) {
		Platform.runLater(()->{
			String style = isChecked ? "-fx-text-fill:green;" : "-fx-text-fill:red;" ;
			String text = isChecked ? "사용가능합니다." : "사용할 수 없는 아이디입니다." ;
			checkID.setStyle(style);
			checkID.setText(text);
		});
	}

	@Override
	public void setJoinCheck(boolean isSuccess) {
		if(isSuccess) {
			System.out.println("회원가입 성공");
			joinAnchor.setVisible(false);
			joinAnchor.setDisable(true);
			loginAnchor.setVisible(true);
			loginAnchor.setDisable(false);
			initLoginUI();
		}else {
			System.out.println("회원가입 실패");
			initJoinUI();
		}
	}

	@Override
	public void setLoginCheck(MemberVO vo) {
		if(vo.isSuccess()) {
			System.out.println("로그인 성공");
			Platform.runLater(()->{
				// 로그인 완료된 사용자 정보 저장
				user = vo;
				// 무대 이동
				//showGameRoom();
				// 대기실로 이동
				showWaittingRoom();
			});
		}else {
			System.out.println("로그인 실패");
			// textField 초기화
			initLoginUI();
		}
	}

	@Override
	public void receiveData(MemberVO vo) {
		// 0  아이디 중복 체크  1 회원가입 2 로그인
		switch(vo.getOrder()) {
		case 0 :
			System.out.println("아이디 중복 체크");
			isJoin = vo.isSuccess();
			setJoinIDCheck(isJoin);
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

	@Override
	public void showGameRoom() {
		try {
			FXMLLoader loader = new FXMLLoader(
			getClass().getResource("/push_man/game/Game.fxml")
			);
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle(user.getMemberName()+"님 반갑습니다.");
			stage.setResizable(false);
			stage.show();
			Stage memberStage = (Stage)checkID.getScene().getWindow();
			memberStage.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 대기실 오픈
	public void showWaittingRoom() {
		try {
			FXMLLoader loader = new FXMLLoader(
			getClass().getResource("/push_man/waitting_room/Waitting_room.fxml")
			);
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle(user.getMemberName()+"님 반갑습니다.");
			stage.setResizable(false);
			stage.show();
			Stage memberStage = (Stage)checkID.getScene().getWindow();
			memberStage.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}









