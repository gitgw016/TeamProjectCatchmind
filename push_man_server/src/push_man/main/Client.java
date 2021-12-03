package push_man.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import push_man.vo.MemberVO;
import push_man.vo.ScoreVO;

public class Client {
	
	Socket client;
	// 로그인 완료된 회원 정보
	MemberVO member;

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
						ois = new ObjectInputStream(
							client.getInputStream()
						);
						if((obj = ois.readObject()) != null) {
							System.out.println("요청");
							if(obj instanceof MemberVO) {
								System.out.println("회원관련 요청");
								desposeMember((MemberVO)obj);
							}else if(obj instanceof ScoreVO) {
								System.out.println("게임기록 갱신 요청");
								requestGameInfo((ScoreVO)obj);
							}else if(obj instanceof Integer) {
								System.out.println("대기실 정보 요청 처리");
								System.out.println("스테이지 정보 - 대기실 사용자 목록 요청");
								int num = (Integer)obj;
								switch(num) {
									case 0 :
										// 대기실에 사용자 목록이 존재하지 않으면 추가
										if(!MainController.roomList.contains(Client.this)) {
											MainController.roomList.add(Client.this);
										}
										// 대기실 사용자 목록 갱신
										updateWattingRoomUserList();
										break;
									case -1 : 
										// 대기실에서 목록 삭제 - 게임룸 참여
										removeRoomList();
										updateWattingRoomUserList();
									default :
										// 랭킹 정보 전송
										wattingRoomInfo(num);
										break;
								}
								
							}else if(obj instanceof String) {
								// 대기실 roomList chat broadCast
								String message = (String)obj;
								broadCast(message);
							}
						}
					}
				} catch (Exception e) {
					removeClient();
				}
			}
		});
	}
	
	// 대기실 전체 채팅 
	public void broadCast(String message) {
		String name = member.getMemberName();
		String id = member.getMemberId();
		name = name+="("+id+")";
		for(Client c : MainController.roomList) {
			if(c.equals(this)) {
				c.sendData("나 : "+message);
			}else {
				c.sendData(name+" : "+message);
			}
		}
	}

	// 대기실 정보 요청
	// 대기실에 있는 모든 사용자에게 목록 갱신
	public void updateWattingRoomUserList() {
		List<Client> roomList = MainController.roomList;
		List<MemberVO> memberList = new ArrayList<>();
		for(Client c : roomList) {
			memberList.add(c.member);
		}
		System.out.println(memberList.size());
		for(Client c : roomList) {
			c.sendData(memberList);
		}
		System.out.println("대기실 갱신 완료");
		System.out.println(memberList);
	}
	
	
	// 랭킹 List 정보 
	protected void wattingRoomInfo(Integer obj) {
		System.out.println("wattingRoomInfo stage : " + obj);
		sendData(MainController.scoreDAO.getRankingList(obj));
		System.out.println("send 완료");
	}

	// 게임 기록 저장 요청
	public void requestGameInfo(ScoreVO vo) {
		System.out.println("request game info "+ vo);
		// 기존 기록 존재 여부 확인
		ScoreVO old = MainController.scoreDAO.getScoreVO(vo.getStage(), vo.getMemberNum());
		System.out.println("기존 기록 "+old);
		System.out.println("신규 기록 "+vo);
		if(old == null) {
			// 신규 기록
			MainController.scoreDAO.insertScore(vo);
		}else if(vo.getScore() < old.getScore()){
			// 기록 갱신
			MainController.scoreDAO.updateScore(vo);
		}
		// 신규 삽입 또는 갱신 기록 전송
		vo = MainController.scoreDAO.getScoreVO(vo.getStage(), vo.getMemberNum());
		System.out.println("sendData = "+vo);
		sendData(vo);
	}

	// 회원관련 요청 처리
	public void desposeMember(MemberVO obj) {
		System.out.println("receive MemberVO "+obj);
		switch(obj.getOrder()) {
		case 0 :
			// 아이디 중복 체크
			String memberId = obj.getMemberId();
			boolean isCheck = MainController.memberDAO.checkId(memberId);
			obj.setSuccess(isCheck);
			break;
		case 1 : 
			// 회원가입
			MainController.mc.appendText("회원가입 요청 - "+obj.getMemberName());
			isCheck = MainController.memberDAO.joinMember(obj);
			obj.setSuccess(isCheck);
			break;
		case 2 : 
			// 로그인
			MemberVO vo = MainController.memberDAO.loginMember(obj);
			if(vo.isSuccess()) {
				// 중복 로그인 처리
				// 로그인 성공 시 동일한 아이디로 이미 로그인 중인
				// 사용자가 있으면 기존 사용자의 연결을 끊는다.
				for(Client c : MainController.clients) {
					if(c != this && c.member.equals(vo)) {
						System.out.println("remove : " + c);
						c.removeClient();
						break;
					}
				}
				member = obj = vo;
				System.out.println("LOGIN - " + member);
			}
			break;
		}
		System.out.println("send MemberVO "+obj);
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
		System.out.println("Client 연결 종료 : " + ip);
		
		synchronized (MainController.clients) {
			MainController.clients.remove(this);
		}
		// client 종료 시 
		// 대기실 목록에서도 삭제
		if(MainController.roomList.contains(this)) {
			// 사용자 목록 삭제하고 갱신
			removeRoomList();
			updateWattingRoomUserList();
		}
		
		if(client != null && !client.isClosed()) {
			try {
				client.close();
			} catch (IOException e) {}
		}
	}
	
	// 대기실 목록에서 현재 사용자 삭제
	public void removeRoomList() {
		// 로그인 된 상태 라면
		if(this.member != null) {
			synchronized (MainController.roomList) {
				// 대기실 목록에서 삭제
				MainController.roomList.remove(this);
			}
		}
	}
	
}










