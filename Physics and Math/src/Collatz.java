import java.io.*;
import java.math.*;

public class Collatz {

	public static BigInteger fact(BigInteger num){
		//0! = 1
		if (num.compareTo(BigInteger.valueOf(0)) == 0)
			return BigInteger.valueOf(1);
		
		//Calculate factorials greater than 0
		BigInteger result = new BigInteger("1");
		for(BigInteger count = new BigInteger("1"); count.compareTo(num) <= 0; count = count.add(BigInteger.ONE)){
			//System.out.println(result);
			
			result = result.multiply(count);
		}
		return result;
	}
	
	public static BigInteger pow(BigInteger base, int n){
		//n^0 = 1
		if (n == 0)
			return BigInteger.valueOf(1);
		
		//
		BigInteger result = new BigInteger("1");
		for(int i = 1; i <= n; i++){
			result = result.multiply(base);
		}
		return result;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("test");
		// TODO Auto-generated method stub
		//BigInteger thing = new BigInteger("10");
		//double thing = 6*Math.pow(2, 6;//6*Math.pow(2, 60);//Integer.MAX_VALUE;//6*Math.pow(2, 60);
		PrintWriter pw = new PrintWriter(new File("BigNum.txt"));
		
		BigDecimal sum = BigDecimal.ZERO;
		
		////System.out.println(fact(thing));
		
		int total = 100;
		BigDecimal numerator = new BigDecimal("0");
		BigDecimal denominator = new BigDecimal("0");
		BigDecimal term = new BigDecimal("0");
		
//		for(int n = 0; n <= total; n++){
//			BigInteger fact = fact(BigInteger.valueOf(n));
//			numerator = new BigDecimal(pow(BigInteger.valueOf(4),n).multiply(fact).multiply(fact));
//			//numerator.setScale(10,BigDecimal.ROUND_HALF_UP);
//			denominator = new BigDecimal(fact(BigInteger.valueOf(2*n)));
//			term = numerator.divide(denominator, 10, BigDecimal.ROUND_HALF_UP);
//			sum = sum.add(term);
//		}
		
		int n = 1000;
		BigInteger fact = fact(BigInteger.valueOf(n));
		numerator = new BigDecimal(pow(BigInteger.valueOf(4),n).multiply(fact).multiply(fact));
		denominator = new BigDecimal(fact(BigInteger.valueOf(2*n)));
		term = numerator.divide(denominator, 10, BigDecimal.ROUND_HALF_UP);
		
		pw.println(numerator);
		pw.println(denominator);
		pw.println(sum);
		System.out.println(numerator.toString().length());
		System.out.println(denominator.toString().length());
		System.out.println(term);
		System.out.println(sum);
		
		
		
		
//		double count = 1;
//		while (thing.compareTo(BigInteger.valueOf(1)) > 0){
//			System.out.println(thing.toString());
//			pw.println(thing.toString());
//			if (thing.mod(BigInteger.valueOf(2)).compareTo(BigInteger.valueOf(0)) == 0)
//				thing = thing.divide(BigInteger.valueOf(2));
//			else{
//				thing = thing.multiply(BigInteger.valueOf(3));
//				thing = thing.add(BigInteger.valueOf(1));
//			}
//			count++;
//		}
//		System.out.println("Number of steps " + count);
		pw.close();
	}

}
