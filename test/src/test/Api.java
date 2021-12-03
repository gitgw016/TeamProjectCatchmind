package test;

public class Api {

	public static void main(String[] args) {
		String str = "123\n 123\n 123\n 123";
		String result = str.replaceFirst("123\n", "");
		System.out.println(result);
		
	}

}
