package com.shq.oper.util;


public class MemoryViewUtil {

	public static String showMemoryStr() {
		int mb = 1024 * 1024;  
		Runtime run = Runtime.getRuntime();
		long startMem = run.totalMemory()-run.freeMemory();
		StringBuilder sb = new StringBuilder();
		sb.append("-----memory> total:");
		sb.append( run.totalMemory() / mb);
		sb.append("-----free:" );
		sb.append(run.freeMemory()  / mb);
		sb.append("-----used:");
		sb.append( startMem / mb );
		return sb.toString();
	}
}
