package catchmind.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import catchmind.vo.ChatVO;
import catchmind.vo.MemberVO;
import catchmind.vo.PaintVO;
import javafx.application.Platform;

public class Client {
	
	 Socket client;
	// 로그인 완료된 회원 정보
	MemberVO member;
	PaintVO paint;

	public Client(Socket client) {
		this.client = client;
		serverClientReceive();
	}
	
	// client에서 출력되는 정보를 읽어들임
	public void serverClientReceive() {
		MainController.threadPool.submit(new Runnable() {
			@Override
			public void run() {
				ObjectInputStream ois = null;
				try {
				Object obj;
				while(true) {
						ois = new ObjectInputStream(client.getInputStream());
						if((obj = ois.readObject()) != null) {
							System.out.println("요청");
							if(obj instanceof MemberVO) {
								System.out.println("회원관련 요청");
								desposeMember((MemberVO)obj);
							}
							else if(obj instanceof PaintVO) {
								System.out.println("그림관련 요청");
								desposePaint((PaintVO)obj);
							}else if(obj instanceof ChatVO) {
								System.out.println("채팅관련 요청");
								desposeChat((ChatVO)obj);
							}
						}
					} 
					}catch (Exception e) {
						removeClient();
				}
			}
		});
	}
	public void desposeChat(ChatVO obj) {
		
		switch(obj.getSignal()) {
		case 1 :
			String user = obj.getName();
			MainController.userlist = MainController.userlist + user+("\n");
			ChatVO list = new ChatVO(MainController.userlist);
			list.setSignal(1);
			Platform.runLater(()->{
				MainController.sendAllChat(list);
				System.out.println("ChatVO 1 : "+obj);
			});
			break;
		case 2 :
			String name = obj.getName();
			String text = obj.getText();
			ChatVO chat = new ChatVO(name,text);
			chat.setSignal(2);
			MainController.sendAllChat(chat);
			System.out.println("ChatVO 2: "+obj);
			break;
//		case 3 :
//			String outName = obj.getName();
//			MainController.userlist = MainController.userlist.replaceFirst(outName+("\n"), "");
//			ChatVO chatList = new ChatVO(MainController.userlist,3);
//			ChatVO outter = new ChatVO(outName,4);
//			MainController.sendAllChat(chatList);
//			MainController.sendAllChat(outter);
		}
		
	}
	
	protected void desposePaint(PaintVO obj) {
		System.out.println("receive PaintVO" +obj);
		MainController.sendAllClient(obj);
	}

	// 회원관련 요청 처리
	public void desposeMember(MemberVO obj) {
		System.out.println("receive MemberVO" +obj);
		switch(obj.getOrder()) {
			case 0 :
				// 아이디 중복 체크
				String memberId = obj.getMemberId();
				boolean isCheck = MainController.memberDAO.checkId(memberId);
				obj.setSuccess(isCheck);
				break;
			case 1 :
				// 회원가입
				MainController.mc.appendText("회원가입 요청 : "+obj.getMemberName());
				isCheck = MainController.memberDAO.joinMember(obj);
				obj.setSuccess(isCheck);
				break;
			case 2 :
				// 로그인
				MemberVO vo = MainController.memberDAO.loginMember(obj);
				if(vo.isSuccess()) {
					// 회원 정보 등록
					member = obj = vo;
					System.out.println("Login : "+member);
				}
				break;
		}
		System.out.println("send memberVO : "+obj);
		sendData(obj);
	}
	// 연결되어 있는 client에 정보를 출력
	public synchronized void sendData(Object data) {
		ObjectOutputStream oos = null;
		try {
			OutputStream os = client.getOutputStream();
			oos = new ObjectOutputStream(os);
			oos.writeObject(data);
			oos.flush();
		} catch (IOException e) {
			removeClient();
		}
	}
	
	// client 연결 종료
	public void removeClient() {
		String ip = client.getInetAddress().getHostAddress();
		MainController.mc.appendText(new Date()+ip+"연결 종료");
		System.out.println("Client 연결 종료 : "+ip);
		
		synchronized (MainController.clients) {
			MainController.clients.remove(this);
			
		}
		
			if(this.member != null) {
				synchronized (MainController.userlist) {
					// 대기실 목록에서 삭제
					String s = this.member.getMemberName();
					MainController.userlist = MainController.userlist.replaceFirst(s+("\n"), "");
					ChatVO chatList = new ChatVO(MainController.userlist,3);
					ChatVO outter = new ChatVO(s,4);
					MainController.sendAllChat(chatList);
					MainController.sendAllChat(outter);
			}
		}
		
		if(client != null && !client.isClosed()) {
			try {
				client.close();
			} catch (IOException e) {}
		}
	}
	
}
