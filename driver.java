import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;


public class driver {

	public static void main(String[] args) {
		char [] arr = {'-','+','*','/'};

		Stack <Integer> stack1 = new Stack <Integer>();
		Stack <Character> stack2 = new Stack<Character>();

		Scanner inputStream = null;
		try{
			inputStream = new Scanner(new FileInputStream("text.txt"));
		}

		catch(FileNotFoundException e){
			System.out.println("A problem has occured while trying to read in the file.");
			System.exit(0);
		}


		//If have number than push
		while(inputStream.hasNext() == true){
			if(inputStream.hasNextInt()){
				int x = inputStream.nextInt();
				stack1.push(x);

			}//End of reading number

			if(inputStream.hasNext()){
				String symbol = inputStream.next();
				char y = symbol.charAt(0);

				if(stack2.isEmpty())
					stack2.push(y);

				if(prec(y) < prec(stack2.peek())){ //New symbol has greater precedence so we push it onto stack
					stack2.push(y);
				}

				else{
					while(stack1.size() >=2 && stack2.size()>=1 && prec(stack2.peek()) <= prec(y)){
						int b = stack1.pop();
						int a = stack1.pop();
						stack1.push(op(stack2.pop(),a,b));
					}
					if(stack2.isEmpty() ||  prec(stack2.peek())> prec(y)){
						stack2.push(y);
					}
				}

			}

		} //End of stack loop

		int res = 0;
		while(stack2.isEmpty()==false){
			int b = stack1.pop();
			int a = stack1.pop();
			res =stack1.push(op(stack2.pop(),a,b)); 
		}

		System.out.println(res);
//		int x  = 14+2*3/3;
//		System.out.println(x);

	} // End of main






	public static int op(char o,int a, int b){
		if(o == '+')
			return a+b;

		if(o == '-')
			return a-b;

		if(o == '/')
			return a/b;

		if(o == '*')
			return a*b;

		return 0;
	}

	public static int prec (char n){

		if (n == ' ')
			return 9;
		if(n == '+' || n == '-' ){
			return 6;
		}
		if(n == '*' || n == '/')
			return 5;


		return -1;
	}
}
