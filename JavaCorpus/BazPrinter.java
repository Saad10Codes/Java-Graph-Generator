package com.benlakey.examples.threading;

public class BazPrinter extends Thread {

	private boolean runPrinter;
	
	public BazPrinter() {
		
	}
	
	public void stopPrinter() {
		this.runPrinter = false;
	}
	
	public void start() {
		this.runPrinter = true;
		super.start();
	}
	
	public void run() {

		while(this.runPrinter) {
			System.out.println("BazPrinter!");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println("BazPrinter could not sleep!");
			}
		}
		
		System.out.println("BazPrinter stopped!");
		
	}
	
}
