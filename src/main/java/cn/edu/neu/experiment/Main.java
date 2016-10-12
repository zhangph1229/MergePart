package cn.edu.neu.experiment;

import java.util.*;
public class Main {
	public static void main(String[] args) {
//		Scanner sc = new Scanner(System.in);
//		int N = sc.nextInt();
//		String ch = sc.nextLine();
//		int M = sc.nextInt();
//		String[] del = new String[M];
//		for(int i = 0; i < M; i++){
//			del[i] = sc.nextLine();
//		}
//		
//		int res = deleteCharacter(N, ch, M, del);
//		
//		
//		Scanner sc = new Scanner(System.in);
//		int n = sc.nextInt();
//		int x, o = 0, e = 0;
//		for(int i = 0; i < n; i++){
//			x = sc.nextInt();
//			if((x & 1) == 1)o++;
//			else e++;
//		}
//		System.out.println(Math.abs(o - e));
		deleteCharacter();
	}

	private static void deleteCharacter() {
		Scanner sc  =  new Scanner(System.in);
		int n = sc.nextInt();
		String str = sc.next();
		char[] ch = str.toCharArray();
		int m = sc.nextInt();
		char x = 0, y = 0;
		boolean[][] ava = new boolean[27][27];
		for(int i = m; i > 0; i--){
			String tmp = sc.next();
			char[] t = tmp.toCharArray();
			x = t[0];
			y = t[1];
			ava[x - 'a'][y - 'a'] = ava[y - 'a'][x - 'a'] = false;
		}
		int[][] f = new int[n][26];
		int ans = 0;
		for(int i = 1; i < n; i++){
			ch[i] -= 'a';
			for(int j = 0; j < 26; j++){
				f[i][j] = f[i-1][j];
			}
			f[i][ch[i]] = Math.max(f[i][ch[i]], 1);
			for(int k = 0; k < 26; k++){
				if(ava[k][ch[i]]){
					f[i][ch[i]] = Math.max(f[i][ch[i]], f[i - 1][k] + 1);
					ans = Math.max(ans,  f[i][ch[i]]);
				}
			}
		}
		System.out.println(ans);
	}


	
	
	public static List<Integer> deleteNum(int[] num){
		int len = num.length;
		int i = 0, j = 1;
		List<Integer> list = new ArrayList<>();
		while(i < j && j < num.length){
			if((num[i] + num[j]) % 2 != 0){
				len-=2;
				if(i > 0) i--;
				else i = j + 1;
				if(i > j) j += 2;
				else j++;
			}else{
//				if(i == 0)list.add(num[i]);
				list.add(num[j]);
				i++;
				j++;
				
			}
		}
//		System.out.println(len);
		return list;
	}
}
