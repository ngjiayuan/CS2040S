public class IntegerExamination {
	
	static class myInteger {
		public int intState;
		
		myInteger(int k) {
			intState = k;
		}

		public String toString(){
			return Integer.toString(intState);
		}
	}

	//public static int i = 7;

	public static void main(String[] args) {
		int i = 7;
		myInteger j = new myInteger(7);
		myInteger k = new myInteger(7);

		addOne(i);
		myIntAddOne(j);
		myOtherIntAddOne(k);

		System.out.println(i);
		System.out.println(j);
		System.out.println(k);
	}

	static public void addOne(int i){
		// the parameter i is changed instead of the i variable in line 16.
		i = i + 1;
	}

	static public void myIntAddOne(myInteger i){
		i.intState = i.intState + 1;
		// Analogy: Teaching tuition
		// You receive the address of the tutee, you go to his house and teach the tutee, tutee become smarter
	}

	static public void myOtherIntAddOne(myInteger i){
		i = new myInteger(i.intState + 1);
		// Analogy: Teaching tuition
		// You recieve the address of the tutee, you replace the address with another smart person's address, tutee does not become smarter
		System.out.println("I am in myOtherIntAddOne. The value of i is " + i);
	}
}