package com.example.remote1602lcd;

// context table
public class Context {
	private STR1 str1;
	private STR2 str2;
	
	public class STR {
		private String str;
		
		public STR() {
			str = "";
		}
		
		public void setStr(String it) { str = it; }
		public String getStr()        { return str; }
	}
	
	public class STR1 extends STR {
		public STR1() {
			super.str = "hi respberry pi";  
		}
	}
	
	public class STR2 extends STR {
		public STR2() {
			super.str = "hello world"; 
		}
	}

	public void setStr1(STR1 it) { str1 = it; }
	public void setStr2(STR2 it) { str2 = it; }
	public STR getStr1() { return str1; }
    public STR getStr2() { return str2; }
}