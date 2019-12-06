package com.shuogesha.copyFast;


public class ShuogeshaModuleGenerator {
	private static String packName = "com.shuogesha.copyFast";
	private static String fileName = "shuogesha.properties";

	public static void main(String[] args) {
		new ModuleGenerator(packName, fileName).generate();
	}
}
