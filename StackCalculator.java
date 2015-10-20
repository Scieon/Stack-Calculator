import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

/**
 * This class uses stacks to compute arithmetic sums by manually deciding the proper precedence protocol to follow.
 * @author Anhkhoi Vu-Nguyen
 */
public class StackCalculator {

	public static void main(String[] args) {
		Scanner in = null; //The input of he entire text file.
		Scanner inputLine = null; //The input of a single line of the text file.
		Scanner logicLine = null; //The input of one portion of a single line.
		PrintWriter out = null; //Our output stream.
		Boolean flag = false; //This helps setting  up the parentheses.
		Stack <Double> stack1 = new Stack <Double>();
		Stack <Character> stack2 = new Stack<Character>();
		String symbol; //Symbol is the operator or inequality.
		char operator; 
		
		//The following three arrays hold information to compute logical operators.
		String logical[] = new String[1];
		String arr[] = new String[2];
		double result[] = new double[2];

		try{
			out = new PrintWriter("myOutput.txt");
		}
		catch(FileNotFoundException e){
			System.out.println("Error opening the file out.txt");
			System.exit(0);
		}

		try{
			in = new Scanner(new FileInputStream("input.txt"));

		}

		catch(FileNotFoundException e){
			System.out.println("A problem has occured while trying to read in the file.");
			System.exit(0);
		}

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
				if(inputLine.hasNextDouble() && flag == false){
					stack1.push(inputLine.nextDouble());
				}

				//This takes in an operator.
				if(inputLine.hasNext()){

					flag = false; //It will resume taking in a number.
					//System.out.println(stack1.peek());
					symbol = inputLine.next();
					operator = symbol.charAt(0);



					//If we find an inequality sign we must split the line into two parts.
					if(symbol.equals( "==") || symbol.equals( "!=") || symbol.equals( "<=") || symbol.equals( ">=") 
							|| symbol.equals( "<") || symbol.equals( ">")){
						logical[0] = symbol; //Place the inequality inside the array so that we know which one it is.

						//logic line will take in the second half of the line after the inequality sign.
						logicLine = new Scanner(inputLine.nextLine());
						String ln = logicLine.nextLine();
						arr[0] = ln;

						double res = stack1.peek();
						//System.out.println(res);
						if(stack2.isEmpty())
							res = stack1.pop();
						while(!stack2.isEmpty()){
							double b = stack1.pop();
							double a = stack1.pop();
							res = op(stack2.pop(),a,b);
							stack1.push(res);
						}

						//-- This was added for parentheses.
						if(!stack1.isEmpty()) //This is needed after while loop
							res = stack1.pop();

						result[0] = res;
						System.out.print("LHS: " + res);
						out.println("LHS: " + res);
						inputLine = new Scanner(arr[0]);
						continue;
					}




					//If ( we immediately push it onto the stack.
					if(operator == '('){
						stack2.push(operator);	
						continue; //Continue b/c we are expecting a number.
					}

					if(operator == '!'){
						double top = stack1.pop(); //Current integer to be factored

						if(top < 0){
							top = -top; //Make the top positive so we can get the factorial
							stack1.push(-recFact(top));  //Push the negative result onto the stack

						}

						else 
							stack1.push(recFact(top)); //Simply compute factorial and put it onto the stack.

						continue; //Reset the loop
					} //End of factorial condition

					if(operator == ')'){
						while(stack2.peek() != '('){
							double b = stack1.pop();
							double a = stack1.pop();
							stack1.push(op(stack2.pop(),a,b));
						} //Continue until top is (

						//System.out.println(stack2.pop()); //Pop (
						stack2.pop();
						flag = true;
						continue;
					}

					//If stack2 is empty than there is no precedence, push the operator onto the stack.
					if(stack2.isEmpty()){
						stack2.push(operator);

						continue;
					}

					//New symbol has greater precedence so we push it onto stack.
					if(prec(operator) < prec(stack2.peek()) || stack2.peek() == '(' ){ //Notice there is no equal sign.

						stack2.push(operator);
						//System.out.println("pushing " + operator);
						continue;

					}

					//Yes, this can be replaced by else statement.
					//If new symbol has lower precedence than do not push it onto the stack yet.
					if(prec(operator) >= prec(stack2.peek())){

						while(stack1.size() >=2 && stack2.size()>=1 && prec(stack2.peek()) <= prec(operator)){

							double b = stack1.pop();
							double a = stack1.pop();
							stack1.push(op(stack2.pop(),a,b));
							if(!stack2.isEmpty())
								if(stack2.peek() == '(')
									break;
						}

						//At this point the operator trying to enter the stack has the highest precedence. 
						if(stack2.isEmpty() ||  stack2.peek() == '(' ||  prec(stack2.peek())> prec(operator)){
							stack2.push(operator);

						}
					}
				}

			}


			//This is after we read everything from the single line.
			double res = stack1.peek();
			if(stack2.isEmpty())
				res = stack1.pop();
			while(!stack2.isEmpty()){
				double b = stack1.pop();
				double a = stack1.pop();
				res = op(stack2.pop(),a,b);
				stack1.push(res);
			}

			//-- This was added for ()
			if(!stack1.isEmpty()) //This is needed after while loop
				res = stack1.pop();

			//If there is no logical operators simply proceed regularly.
			if(arr[0] == null){
				System.out.println("Result: " + res );// + "\n\n");
				out.println("Result: " + res + "\n");
			}

			//If there is a logical operator we must be careful to return a boolean value.
			if(arr[0] != null){
				System.out.print("\t" + logical[0] + "\tRHS: " + res);
				out.println("RHS: " + res);

				boolean comparison = compare(logical[0],result[0],res);
				System.out.println("\t "+comparison);
				out.println(comparison + "\n");
				arr[0] = null;
			}

		}
		out.println();
		out.println();
		out.close(); //Need to close after both loops are done, when we have finished the text file.

	}

	/**
	 * @param op the logical operator.
	 * @param a the first operand.
	 * @param b the second operand.
	 * @return the result of the logical operator acting on the two operands.
	 */
	public static boolean compare(String op, double a, double b){

		if(op.equals("=="))
			return a == b;

		if(op.equals("!="))
			return a != b;

		if(op.equals("<"))
			return a<b;

		if(op.equals(">"))
			return a>b;

			if(op.equals("<="))
				return a<=b;

			if(op.equals(">="))
				return a>=b;

				return false; 
	}

	/**
	 * @param o the operator.
	 * @param a the first operand.
	 * @param b the second operand.
	 * @return the result of the operand and operator.
	 */
	public static double op(char o,double a, double b){
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

	/**
	 * @param n the operator.
	 * @return the precedence of the operator.
	 */
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

	/**
	 * @param n the number to take the factorial of.
	 * @return the factorial of n.
	 */
	public static double recFact(double n){
		if(n<1)
			return 1;
		return n*recFact(n-1);
	}

	/**
	 * logn run time.
	 * @param x the number to be raised.
	 * @param the number of times to multiply the number to be raised.
	 * @return the power of x i.e., x^n
	 */
	public static double pow(double x, double n){
		double y;
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


