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

		//System.out.println(inputStream.hasNext());
		while(inputStream.hasNext() == true){
			if(inputStream.hasNextInt()){
				int x = inputStream.nextInt();
				//System.out.println("Pushing... " + x);
				stack1.push(x);
//				if(stack1.peek() == 7)
//					System.out.println("Last number");
			}

			if(inputStream.hasNext()){
				String symbol = inputStream.next();


				if(symbol.length() != 1 || inputStream.hasNext() == false){
					System.out.println("Not valid symbol or incorrect syntax. Closing program ");
					System.exit(0);
				}
				char y = symbol.charAt(0);

				stack2.push(y);
				if(prec(y) <= prec(stack2.peek())){ //New symbol has greater precedence so we push it onto stack
					//stack2.push(y);
				}

				else{ //Not greater than, therfore we execute what is on stack
					if(stack1.size()>=2){
						int a = stack1.pop();
						int b = stack1.pop();
						int res =stack1.push(op(stack2.pop(),a,b)); 
						System.out.println("Result: " + res);
						
					}
				}
			}

		} //End of stack loop

		while(stack2.isEmpty()==false){
			int a = stack1.pop();
			int b = stack1.pop();
			int res =stack1.push(op(stack2.pop(),a,b)); 
		}
		
		System.out.println("Final result: " + stack1.pop());
		//
		//		stack1.push(15);
		//		stack1.push(3);
		//		stack2.push('/');
		//		int last = stack1.pop();
		//		int first = stack1.pop();
		//		System.out.println(op(stack2.pop(),first,last));

		//				while(stack1.isEmpty() == false){
		//					System.out.println(stack1.pop());
		//				}
		//				
		//				while(stack2.isEmpty() == false){
		//					System.out.println(stack2.pop());
		//				}


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
