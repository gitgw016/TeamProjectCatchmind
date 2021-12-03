package push_man.main;

public interface MainInterface {
	
	// client 초기화 - Server와 연결
	void initClient();
	// 연결 완료 시  - 회원관련 무대를 보여줌
	void showMemberStage() throws Exception;
	// 연결 실패 시 경고 알림
	void showAlert(String text);
	
}






