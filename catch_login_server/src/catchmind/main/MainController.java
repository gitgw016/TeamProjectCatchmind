package catchmind.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import catchmind.dao.MemberDAO;
import catchmind.dao.MemberDAOImpl;
import catchmind.vo.ChatVO;
import catchmind.vo.MemberVO;
import catchmind.vo.PaintVO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class MainController implements Initializable {
	
	@FXML private TextArea txtArea;
	@FXML private Button btnStartStop;
	// Client Socket 연결 관리
	ServerSocket server;
	// Client thread 관리
	public static ExecutorService threadPool;
	public static MainController mc;
	
	// 전체 Client 목록
	public static List<Client> clients;
	
	public static MemberDAO memberDAO;
	
	public static List<Client> members;		// 추가

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnStartStop.setOnAction((event)->{
			if(btnStartStop.getText().equals("Stop")) {
				// 서버 중지
				stopServer();
				btnStartStop.setText("Start");
			}else {
				// 서버 실행
				initServer();
				btnStartStop.setText("Stop");
			}
		});
	}
	
	// Socket Server 초기화
	public void initServer() {
		memberDAO = new MemberDAOImpl();
		threadPool = Executors.newFixedThreadPool(30);
		mc = this;
		clients = new ArrayList<>();
		members = new ArrayList<>();	// 추가
		
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					appendText("서버 생성");
					server = new ServerSocket(8001);
					while(true) {
						appendText("Client 연결 대기..");
						// client의 정보를 받아들이게 되면 그 정보를 socket에 저장 후 대기
						Socket client = server.accept();
						// 연결된 주소의 ip값을 받아옴
						String client_ip = client.getInetAddress().getHostAddress();
						appendText(client_ip+" 연결 완료");
						Client c = new Client(client);
						// 임계영역(다른 클라이언트(스레드)는 아래가 완료되기전까지 대기)
						synchronized (clients) {
							clients.add(c);
						}
						appendText(clients.size()+" 생성 완료");
					}
				} catch (Exception e) {
					stopServer();
					System.out.println("서버 종료 : "+e.getMessage());
				}
			}
		};
		threadPool.submit(task);
	}
	
	// Server Stop
	public void stopServer() {
		appendText("서버 종료");
		try {
			if(clients != null) {
				for(Client c : clients) {
					if(c.client != null && !c.client.isClosed()) {
						c.client.close();
					}
				}
			}
			clients.clear();
			
			if(server != null && !server.isClosed()) {
				server.close();
			}
		} catch (IOException e) {}
		finally {
			threadPool.shutdownNow();
		}
	}
	
	// Server Log
	public void appendText(String data) {
		Platform.runLater(()->{
			txtArea.appendText(data +"\n");
		});
	}
	public static void sendAllClient(PaintVO obj) {		// 변경
		List<Client> gameList = MainController.members;
		for(Client c : gameList) {
			c.sendData(obj);
		}
	}
	
	public static void sendAllChat(ChatVO obj) {	// 변경
		List<Client> gameList = MainController.members;
		for(Client c : gameList) {
			c.sendData(obj);
		}
	}

}
