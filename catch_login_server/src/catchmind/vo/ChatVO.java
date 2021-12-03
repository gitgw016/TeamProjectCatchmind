package catchmind.vo;

import java.io.Serializable;
import java.util.List;

public class ChatVO implements Serializable{
	
 /**
	 * 
	 */
	private static final long serialVersionUID = -6992485739998460169L;
	
 private String name;
 private String text;
 private int signal;
 
public ChatVO() {}

public ChatVO(String name) {
	this.name = name;
}

public ChatVO(String name, int signal) {
	this.name = name;
	this.signal = signal;
}

public ChatVO(String name, String text, int signal) {
	this.name = name;
	this.text = text;
	this.signal = signal;
}

public ChatVO(String name, String text) {
	this.name = name;
	this.text = text;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getText() {
	return text;
}

public void setText(String text) {
	this.text = text;
}

public int getSignal() {
	return signal;
}

public void setSignal(int signal) {
	this.signal = signal;
}

@Override
public String toString() {
	return "ChatVO [name=" + name + ", text=" + text + ", signal=" + signal + "]";
}

 
}
