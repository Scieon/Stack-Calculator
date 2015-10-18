import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;


public class reader {

	public static void main(String[] args) {
		Scanner in = null;
		Scanner inputLine = null;
		PrintWriter out = null;
		Boolean flag = false;
		Stack <Integer> stack1 = new Stack <Integer>();
		Stack <Character> stack2 = new Stack<Character>();
		String symbol;
		char operand;


		try{
			out = new PrintWriter("myOutput.txt");
		}
		catch(FileNotFoundException e){
			System.out.println("Error opening the file out.txt");
			System.exit(0);
		}

		try{
			in = new Scanner(new FileInputStream("text.txt"));

		}

		catch(FileNotFoundException e){
			System.out.println("A problem has occured while trying to read in the file.");
			System.exit(0);
		}


		//While the entire text file that has lines remaining.
		while(in.hasNextLine()){
			//Put the next line into another scanner object
			String line = in.nextLine(); 
			//Print out that 'single' line onto an output.
			out.println(line);

			//inputLine is the that single line that we retrieved from the entire file.
			inputLine = new Scanner(line);
			flag = false;

			while(inputLine.hasNext()){

				//This takes a number and pushes it onto our stack
				if(inputLine.hasNextInt() && flag == false){
					stack1.push(inputLine.nextInt());
					
					//System.out.println(stack1.peek());
				}

				//This takes in an operand.
				if(inputLine.hasNext()){
					flag = false; //It will resume taking in an integer.
					//System.out.println(stack1.peek());
					symbol = inputLine.next();
					operand = symbol.charAt(0);

					//If ( we immediately push it onto the stack.
					if(operand == '('){
						stack2.push(operand);	
						continue; //Continue b/c we are expecting a number.
					}
				
					if(operand == '!'){
						int top = stack1.pop(); //Current integer to be factored

						if(top < 0){
							top = -top; //Make the top positive so we can get the factorial
							stack1.push(-recFact(top));  //Push the negative result onto the stack

						}

						else 
							stack1.push(recFact(top)); //Simply compute factorial and put it onto the stack.

						continue; //Reset the loop
					} //End of factorial condition
					
					if(operand == ')'){
						while(stack2.peek() != '('){
							int b = stack1.pop();
							int a = stack1.pop();
							stack1.push(op(stack2.pop(),a,b));
							//System.out.println(stack1.peek());
						} //Continue until top is (
						
						//System.out.println(stack2.pop()); //Pop (
						stack2.pop();
						flag = true;
						continue;
					}

					//If stack2 is empty than there is no precedence, push the operand onto the stack.
					if(stack2.isEmpty()){
						stack2.push(operand);
						
						continue;
					}

					//New symbol has greater precedence so we push it onto stack.
					if(prec(operand) < prec(stack2.peek()) || stack2.peek() == '(' ){ //Notice there is no equal sign.

						stack2.push(operand);
						//System.out.println("pushing " + operand);
						continue;

					}

					//Yes, this can be replaced by else statement.
					//If new symbol has lower precedence than do not push it onto the stack yet.
					if(prec(operand) >= prec(stack2.peek())){
						
						while(stack1.size() >=2 && stack2.size()>=1 && prec(stack2.peek()) <= prec(operand)){
							
							int b = stack1.pop();
							int a = stack1.pop();
							stack1.push(op(stack2.pop(),a,b));
							if(!stack2.isEmpty())
							if(stack2.peek() == '(')
								break;
						}
					
						//At this point the operand trying to enter the stack has the highest precedence. (not parentheses related)
						if(stack2.isEmpty() ||  stack2.peek() == '(' ||  prec(stack2.peek())> prec(operand)){
							stack2.push(operand);
							
						}

					}
				}

			}

			//This is after we read everything from the single line.
			int res = stack1.peek();
			//System.out.println(res);
			if(stack2.isEmpty())
				res = stack1.pop();
			while(!stack2.isEmpty()){
				int b = stack1.pop();
				int a = stack1.pop();
				//System.out.println("a: " + a + " b: " + b);
				res = op(stack2.pop(),a,b);
				stack1.push(res);
			}
			
			//-- This was added for ()
			if(!stack1.isEmpty()) //This is needed after while loop
				res = stack1.pop();
			
			
			System.out.println("Result: " + res );// + "\n\n");
			out.println("Result: " + res + "\n");
		}

		out.close(); //Need to close after both loops are done, when we have finished the text file.


	}

	public static int op(char o,int a, int b){
		if(o == '+')
			return a+b;

		if(o == '-')
			return a-b;

		if(o == '/')
			return a/b;

		if(o == '*')
			return a*b;

		if(o == '^')
			return pow(a,b);

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
		if(n == '^')
			return 4;

		if(n == '!')
			return 2;

		if(n == '(')
			return 1;

		return 99;
	}

	public static int recFact(int n){
		if(n<1)
			return 1;
		return n*recFact(n-1);
	}

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
