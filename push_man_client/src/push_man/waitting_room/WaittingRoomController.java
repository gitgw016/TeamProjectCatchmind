package push_man.waitting_room;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import push_man.game.GameController;
import push_man.game.GameInterface;
import push_man.main.ClientMain;
import push_man.member.MemberController;
import push_man.vo.MemberVO;
import push_man.vo.RankingVO;

public class WaittingRoomController implements Initializable {

	@FXML
	private TableView<MemberVO> userList;
	@FXML
	private TableView<RankingVO> rankingList;
	@FXML
	private ComboBox<Integer> stageBox;
	@FXML
	private TextField inputText;
	@FXML
	private Button btnStart, btnSend;
	@FXML
	TextArea chatArea;
	
	private List<MemberVO> roomList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientMain.thread.waittingRoomController = this;
		initComboBox();
		initRankingTableView(1);
		initUserListTableView();
		Platform.runLater(()->{
			inputText.requestFocus();
		});
		
		btnStart.setOnAction(event->{
			Platform.runLater(()->{
				// 대기실에서 삭제
				ClientMain.thread.sendData(-1);
				showGameRoom();
			});
		});
		
		inputText.setOnKeyPressed(event->{
			if(event.getCode().equals(KeyCode.ENTER)) {
				btnSend.fire();
			}
		});
		
		// 대기실에 있는 사용자 끼리만 채팅
		btnSend.setOnAction(event->{
			ClientMain.thread.sendData(inputText.getText());
			inputText.clear();
			inputText.requestFocus();
		});
	}
	
	// 대기실 사용자 목록
	private void initUserListTableView() {
		TableColumn<MemberVO, String> columnName = new TableColumn<>("닉네임");
		columnName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
		TableColumn<MemberVO, String> columnId = new TableColumn<>("아이디");
		columnId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
		userList.getColumns().add(0,columnName);
		userList.getColumns().add(1,columnId);
		for(TableColumn<MemberVO,?> tc : userList.getColumns()) {
			tc.setStyle("-fx-alignment:center;");
			tc.setPrefWidth(100);
		}
		// 대기실에 사용자 추가 및 목록 요청
		ClientMain.thread.sendData(0);
	}

	public void initComboBox() {
		ObservableList<Integer> list = FXCollections.observableArrayList();
		for (int i = 1; i <= GameInterface.MAX_GAME_LEVEL_NUM; i++) {
			list.add(i);
		}
		stageBox.setItems(list);
		stageBox.getSelectionModel().selectedItemProperty().addListener((o, o1, value) -> {
			// stage 번호 서버로 전송 시 해당 스테이지 랭킹 정보 List로 반환
			// Object Type Integer
			ClientMain.thread.sendData(value);
		});
	}

	// 랭킹 테이블 초기화
	public void initRankingTableView(int stageNum) {
		// public field만 가져옴
		// serialVersionID 제외
		Field[] fields = RankingVO.class.getFields();
		for (int i = 0; i < fields.length; i++) {
			TableColumn<RankingVO, ?> column = new TableColumn<>(fields[i].getName());
			column.setCellValueFactory(new PropertyValueFactory<>(fields[i].getName()));
			column.setResizable(false);
			column.setStyle("-fx-alignment:center");
			column.setPrefWidth(70);
			// 마지막 -- 클리어 시간
			if (i == fields.length - 1) {
				column.setPrefWidth(200);
				// Score
			} else if (i == fields.length - 2) {
				column.setPrefWidth(100);
			}
			rankingList.getColumns().add(column);
			Label label = new Label("아직 등록된 기록이 없습니다.\n지금 도전하세요!");
			label.setStyle("-fx-text-fill:WHITE;-fx-font-size:30;-fx-font-weight:bold;");
			rankingList.setPlaceholder(label);
		}
		
		ClientMain.thread.sendData(stageNum);
	}

	public void receiveData(Object obj) {
		System.out.println("Ranking List 요청");
		System.out.println(obj);
		if (obj instanceof List<?>) {
			List<?> temp = (List<?>) obj;
			System.out.println("temp : " + temp);
			if (temp.isEmpty()) {
				System.out.println("isEmpty");
				rankingList.setItems(FXCollections.observableArrayList());
				return;
			}
			Object o = temp.get(0);
			System.out.println("temp.get(0) " + o);
			if (o instanceof RankingVO) {
				System.out.println("RankingVO");
				List<RankingVO> waitList = (ArrayList<RankingVO>) obj;
				rankingList.setItems(FXCollections.observableArrayList(waitList));
				System.out.println("Ranking List 요청");
				System.out.println(waitList);
			}else if(o instanceof MemberVO) {
				System.out.println("대기실 목록 갱신");
				userList.setItems(FXCollections.observableArrayList((ArrayList<MemberVO>) obj));
			}
		}else if(obj instanceof String) {
			// 대기실 채팅 
			appendText((String)obj);
		}
	}
	
	public void showGameRoom() {
		try {
			FXMLLoader loader = new FXMLLoader(
			getClass().getResource("/push_man/game/Game.fxml")
			);
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			Integer num = stageBox.getSelectionModel().getSelectedItem();
			stage.setTitle(MemberController.user.getMemberName()+"님 반갑습니다.- "+num+" 레벨");
			stage.setResizable(false);
			GameController controller = (GameController)loader.getController();
			controller.setLevel(num);
			stage.show();
			Stage memberStage = (Stage)btnStart.getScene().getWindow();
			memberStage.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void appendText(String text) {
		Platform.runLater(()->{
			chatArea.appendText(text+"\n");
		});
	}
	
	
	
	
}





