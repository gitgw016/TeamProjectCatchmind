package catchmind.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import catchmind.game.GameController;
import catchmind.vo.ChatVO;
import catchmind.vo.MemberVO;
import catchmind.vo.PaintVO;

public class MainThread extends Thread{
	
	public MemberController memberController;
	public GameController gameController;
	
	@Override
	public void run() {
		ObjectInputStream ois = null;
		try {
			while(true) {
				if(isInterrupted()) {break;}
				Object obj = null;
				ois = new ObjectInputStream(ClientMain.socket.getInputStream());
				if((obj = ois.readObject()) != null) {
					System.out.println(obj);
					if(obj instanceof MemberVO) {
						memberController.receiveData((MemberVO)obj);
					}else if(obj instanceof PaintVO) {
						gameController.receiveData((PaintVO)obj);
					}else if(obj instanceof ChatVO) {
						gameController.receiveData((ChatVO)obj);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("n");
		}
	}
	
	public void sendData(Object o) {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(ClientMain.socket.getOutputStream());
			oos.writeObject(o);
			oos.flush();
		} catch (IOException e) {
			
		}
	}
}
