package cn.edu.bjut.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/*
 * A cleaning method of DOI names of cited references in Web of Science.  
 * 
 * References: 
 * [1] Shuo Xu, Liyuan Hao, Xin An, Dongsheng Zhai, and Hongshen Pang, 2019. Types of 
 * DOI Errors of Cited References in Web of Science with a Cleaning Method. Scientometrics, 
 * Vol. 120, No. 3, pp. 1427-1437. DOI: https://doi.org/10.1007/s11192-019-03162-4 
 * 
 * Author: XU, Shuo (pzczxs@gmail.com)
 * */

public class StringUtils {
	public static String join(String delimer, Set<?> set) {
		StringBuilder sb = new StringBuilder(); 
		
		List<?> list = new ArrayList<>(set); 
		for (int i = 0; i < list.size() - 1; i++) {
			sb.append(list.get(i) + delimer); 
		}
		sb.append(list.get(list.size() - 1)); 
		
		return sb.toString(); 
	}
	
	public static boolean isMatch(String str) {
		char[] charArray = str.toCharArray(); 
		Stack<Character> stack = new Stack<Character>(); 
		for (char c: charArray) {
			if (c == '(' || c == '[' || c == '{') {
				stack.push(c); 
			}
			
			if (c == ')' || c == ']' || c == '}') {
				if (stack.isEmpty()) {
					return false; 
				}
				
				Character cc = stack.pop(); 
				
				switch(c) {
				case ')': 
					if (cc != '(') {
						return false; 
					}
					break; 
				case ']': 
					if (cc != '[') {
						return false; 
					}
					break; 
				case '}': 
					if (cc != '{') {
						return false; 
					}
					break; 
				}
			}
		}
		
		return stack.isEmpty()? true: false; 
	}
	
	public static double similarity(String binaryString1, String binaryString2) {
		int commonPrefix = 0; 
		
		for (int i = 0; i < Math.min(binaryString1.length(), binaryString2.length()); i++) {
			if (binaryString1.charAt(i) == binaryString2.charAt(i)) {
				commonPrefix++; 
			} else {
				break; 
			}
		}
		
		return (2.0 * commonPrefix / (binaryString1.length() + binaryString2.length())); 
	}
}
