
public class Test {
	public static void main(String [] args){
		
		long startTime = System.nanoTime(); 
		System.out.println(factorial(25));
		long endTime = System.nanoTime(); 
		long duration = (endTime - startTime); 
		System.out.println("Nano seconds: " + duration);
		startTime = System.nanoTime(); 
		System.out.println(recFact(35));
		endTime = System.nanoTime(); 
		duration = endTime - startTime;
		System.out.println("Nano seconds: " + duration);
	}
	
	
	public static int factorial(int n){
	
		int sum = 1; //Base case
		while(n>1){
			sum = sum * n*(n-1); 
			n-=2;
		}
		return sum;
	}
	
	//Possible stack overflow, but shorter runtime.
	public static int recFact(int n){
		
		if(n<1)
			return 1;
		return n*recFact(n-1);
		
		
	}
}


