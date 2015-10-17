import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;


public class driver {

	public static void main(String[] args) {
		char [] arr = {'-','+','*','/'};
		boolean neg = false;

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
				
				if(y == '!'){
					int z = stack1.pop(); //Current integer to be factored
					if(z< 0){
						z = -z;
					stack1.push(-recFact(z));
					}
					
					else 
						stack1.push(recFact(z));
					continue;
				}

				if(stack2.isEmpty())
					stack2.push(y);

				if(prec(y) < prec(stack2.peek())){ //New symbol has greater precedence so we push it onto stack
					stack2.push(y);
				}


				else if(prec(y) >= prec(stack2.peek())){
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

		int res = stack1.peek();
		while(stack2.isEmpty()==false){
			
			while(stack2.peek() == '!'){
				stack1.push(recFact(stack1.pop()));
				stack2.pop();
			}
			int b = stack1.pop();
			int a = stack1.pop();
			
			res = stack1.push(op(stack2.pop(),a,b)); 
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

		if(n == '!')
			return 2;

		return -1;
	}

	public static int recFact(int n){
		if(n<1)
			return 1;
		return n*recFact(n-1);
	}
	
	//Runtime of logn
	public static int pow(int x, int n){
		int y;
		if(n==0)
			return 1;
		
		else if(n%2==1){
			y = pow(x,(n-1)/2);
			return x*y*y;
		}
		
		else{
			y = pow(x,n/2);
			return y*y;
		}
			
	}
}
