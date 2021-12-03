package push_man.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import push_man.dao.MemberDAO;
import push_man.dao.MemberDAOImpl;
import push_man.dao.ScoreDAOImpl;

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
	// 대기실 사용자 목록
	public static List<Client> roomList;
	
	public static MemberDAO memberDAO;
	public static ScoreDAOImpl scoreDAO;
	
	
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
		// 회원관리 DAO
		memberDAO = new MemberDAOImpl();
		// 기록 관리 DAO
		scoreDAO = new ScoreDAOImpl(); 
		threadPool = Executors.newFixedThreadPool(30);
		mc = this;
		clients = new ArrayList<>();
		roomList = new ArrayList<>();
		
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					appendText("서버 생성");
					server = new ServerSocket(8001);
					while(true) {
						appendText("Client 연결 대기중...");
						Socket client = server.accept();
						String client_ip 
							= client.getInetAddress().getHostAddress();
						appendText(client_ip+" 연결 완료");
						Client c = new Client(client);
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
			txtArea.appendText(data+"\n");
		});
	}

}










