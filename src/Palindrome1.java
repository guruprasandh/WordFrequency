public class Palindrome1 {
	
	public String shortestPalindrome2(String p) {
		if (p == null || p.length() <= 1)
			return p;
	 
		String res = null;
		int len = p.length();
		int mid = len / 2;
		if(checkPalindrome(p, len, mid))
			return p;
		else{
			int i= len-1;
			for(; i>=1; i--)
				if(p.charAt(i)!=p.charAt(i-1))
					break;
			StringBuilder sb = new StringBuilder(p.substring(0,i));
			sb.reverse();
			return p.concat(sb.toString());
		}
	}
	
	public boolean checkPalindrome(String p, int len, int mid){
		String s1 = "";
		String s2 = "";
		if(len%2==0){
			s1 = p.substring(0,mid);
			s2 = p.substring(mid,len);
		}else{
			s1 = p.substring(0,mid);
			s2 = p.substring(mid+1,len);
		}
		StringBuilder sb = new StringBuilder(s2);
		sb.reverse();
		if( s1.equals(sb.toString()))
			return true;
		return false;
	}
	
	public static void main(String[] args){
		String input = "abacdaba";
		Palindrome1 pal = new Palindrome1();
		System.out.println(pal.shortestPalindrome2(input));
	}
}
