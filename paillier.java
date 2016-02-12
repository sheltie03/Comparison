import java.math.*;
import java.util.ArrayList;
import java.security.SecureRandom;

public class paillier {

	// LCM
	public static BigInteger lcm(BigInteger a, BigInteger b) {
		return  (a.divide(a.gcd(b))).multiply(b);
	}
	
	// Key Generation
	public static ArrayList<BigInteger> PailKeyGen(BigInteger p, BigInteger q) {
		Boolean flag = false;
		BigInteger n = p.multiply(q);
		BigInteger lam = lcm(p.subtract(BigInteger.ONE), q.subtract(BigInteger.ONE));
		BigInteger n2 = n.pow(2);
		BigInteger alpha = BigInteger.ZERO;
		BigInteger beta = BigInteger.ZERO;
		SecureRandom rnd = new SecureRandom();
		ArrayList<BigInteger> tmp = new ArrayList<BigInteger>();
		if (p.multiply(q).gcd(p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))).compareTo(BigInteger.ONE) != 0) {
			return tmp;
		}
		while (flag != true) {
			alpha = new BigInteger(n2.bitLength(), rnd);
			beta = new BigInteger(n2.bitLength(), rnd);
			if (alpha.mod(p).equals(BigInteger.ZERO) | alpha.mod(q).equals(BigInteger.ZERO)) {
				continue;
			} else if (beta.mod(p).equals(BigInteger.ZERO) | beta.mod(q).equals(BigInteger.ZERO)) {
				continue;
			} else {
				flag = true;
			}
		}
		BigInteger g = alpha.multiply(n).add(BigInteger.ONE).multiply(beta.modPow(n, n2)).mod(n2);
		tmp.add(n);
		tmp.add(g);
		tmp.add(lam);
		return tmp;
	}

	
	
	
	// Encoding
	public static BigInteger PailEnc(BigInteger m, BigInteger n, BigInteger g, BigInteger n2) {
		boolean flag = false;
		String str = "2";
		BigInteger b2 = new BigInteger(str);
		SecureRandom rnd = new SecureRandom();
		BigInteger r = BigInteger.ZERO;

		while (flag != true) {
			r = new BigInteger(n2.bitLength(), rnd);
			if (r.mod(b2).compareTo(BigInteger.ZERO) != 0) {
				continue;
			} else {
				flag = true;
			}
		}
		return g.modPow(m, n2).multiply(r.modPow(n, n2));
	}
	
	// L function
	public static BigInteger Lfunc(BigInteger x, BigInteger n) {
		return (x.subtract(BigInteger.ONE)).divide(n);
	}
	
	// Decoding
	public static BigInteger PailDec(BigInteger c, BigInteger lam, BigInteger n, BigInteger g, BigInteger n2) {
		return (Lfunc(c.modPow(lam, n2), n).multiply(Lfunc(g.modPow(lam, n2), n).modInverse(n))).mod(n);
	}

	public static double average(double[] ave) {
		double ans = new Double(0);
		double total = new Double(0);
		for(int i = 0; i < ave.length; i++) {
			total += ave[i];
		}
		ans = total / ave.length;
		return ans;
	}
	
	public static double standard(double[] str) {
		double ans = new Double(0);
		double ave = new Double(0);
		double total = new Double(0);
		for(int i = 0; i < str.length; i++) {
			total += str[i];
		}
		ave = total / str.length;
		total = 0;
		for(int i = 0; i < str.length; i++) {
			total += Math.pow(str[i] - ave, 2);
		}
		ans = Math.sqrt(total / str.length);
		return ans;
	}

	
	public static void main(String[] args){
		String str = "122834028293712296720762660347542146164652093481670201947376324993009557691933003435610085976898758687338316692229094216953583611028600068975461290111594197522486713161058298860890998555271590454234774552183738893956388244959334034615659391123981756945729584040032409298308902656405787758214276110040436127519";
		String str1 = "10613812301896775148829942637102985462412287794185387738467245762759618509602736605195641054166216571968217361061547640892557039740729186347713124789244883";
		BigInteger n1 = new BigInteger(str);
		BigInteger n2 = new BigInteger(str1);
		//System.out.println(lcm(n1,n2));	
		SecureRandom rnd = new SecureRandom();
		//System.out.println(rnd);
		//System.out.println(BigInteger.probablePrime(1024, rnd));
		String str2 = "10613812301896775148829942637102985462412287794185387738467245762759618509602736605195641054166216571968217361061547640892557039740729186347713124789244883";
		String str3 = "11573035663327195853844238843542869333876114938989475499001708305357650732178608301848288199284899914248138795286406545785601968971231714755377248265509893";
		BigInteger p = new BigInteger(str2);
		BigInteger q = new BigInteger(str3);
		//System.out.println(lcm(p.subtract(BigInteger.ONE), q.subtract(BigInteger.ONE)));
		//System.out.println(PailKeyGen(p, q));
		//System.out.println(p.multiply(q).gcd(p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))).compareTo(BigInteger.ONE) != 0);
		/*ArrayList<BigInteger> tmp = new ArrayList<BigInteger>();
		tmp.add(q);
		tmp.add(p);
		System.out.println(tmp);*/
		String plt = "19910813";
		BigInteger m = new BigInteger(plt);
		ArrayList<BigInteger> key = PailKeyGen(p,q);
		BigInteger n = key.get(0);
		BigInteger g = key.get(1);
		BigInteger lam = key.get(2);
		n2 = n.pow(2);

		double[] tmp = new double[100];
		
		for (int i = 0; i < 100; ++i) {
			long t1 = System.nanoTime();
			PailEnc(m, n, g, n2);
			long t2 = System.nanoTime();
			long delta = (t2 - t1) / 1000000;
			tmp[i] = delta;
		}

		System.out.println(average(tmp));
		System.out.println(standard(tmp));

		BigInteger c = PailEnc(m, n, g, n2);
		
		double[] tmp1 = new double[100];

		for (int i = 0; i < 100; ++i) {
			long t1 = System.nanoTime();
			PailDec(c, lam, n, g, n2);
			long t2 = System.nanoTime();
			long delta = (t2 - t1) / 1000000;
			tmp1[i] = delta;
		}

		System.out.println(average(tmp1));
		System.out.println(standard(tmp1));
		

		double[] tmp2 = new double[100];
		
		for (int i = 0; i < 100; ++i) {
			long t1 = System.nanoTime();
		        c = PailEnc(m, n, g, n2);
			PailDec(c, lam, n, g, n2);
			long t2 = System.nanoTime();
			long delta = (t2 - t1) / 1000000;
			tmp2[i] = delta;
		}

		System.out.println(average(tmp2));
		System.out.println(standard(tmp2));
	}
}