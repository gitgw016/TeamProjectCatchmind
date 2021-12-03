package catchmind.vo;

import java.io.Serializable;

import javafx.scene.paint.Color;

public class PaintVO implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8328799838777171152L;
	
	private int signal;
	private double x; 
	private double y;
	private boolean paintBool;
	private int color;
	private double thickness;
	private String answer;
	
	public PaintVO() {
	}
	public PaintVO(double x, double y) {
		this.x = x;
		this.y = y;
	}
	//signal 제외하고 매개변수로 받는 생성자 
	public PaintVO(double x, double y, boolean paintBool, int color) {
		this.x = x;
		this.y = y;
		this.paintBool = paintBool;
		this.color = color;
	}
	//다 넘겨받는 생성자
	public PaintVO(int signal, double x, double y, boolean paintBool, int color) {
		this.signal = signal;
		this.x = x;
		this.y = y;
		this.paintBool = paintBool;
		this.color = color;
	}
	//-----------게터세터 모음 ------------
	public int getSignal() {
		return signal;
	}
	public void setSignal(int signal) {
		this.signal = signal;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public boolean isPaintBool() {
		return paintBool;
	}
	public void setPaintBool(boolean paintBool) {
		this.paintBool = paintBool;
	}
	public int getColor() {
		
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	
	public double getThickness() {
		return thickness;
	}
	public void setThickness(double thickness) {
		this.thickness = thickness;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	//-----------게터 세터 끝 ----------------
	@Override
	public String toString() {
		return "PaintVO [signal=" + signal + ", x=" + x + ", y=" + y + ", paintBool=" + paintBool + ", color=" + color
				+ ", thickness=" + thickness + ", answer=" + answer + "]";
	}
	
	
	
	
	
	
}