package com.benlakey.examples.threading;

public class SynchroPrinter {

	public synchronized void syncPrintAlpha() throws InterruptedException {
		
		System.out.println("alpha starting!");
		Thread.sleep(600);
		System.out.println("alpha done!");
		
	}
	
	public synchronized void syncPrintBeta() throws InterruptedException {
		
		System.out.println("beta starting!");
		Thread.sleep(300);
		System.out.println("beta done!");
	}
	
	public void nonSyncPrintAlpha() throws InterruptedException {
		
		System.out.println("alpha starting!");
		Thread.sleep(600);
		System.out.println("alpha done!");
		
	}
	
	public void nonSyncPrintBeta() throws InterruptedException {
		
		System.out.println("beta starting!");
		Thread.sleep(300);
		System.out.println("beta done!");
	}
	
}
