package com.benlakey.examples.threading;

public class BuzPrinter {

	private boolean runPrinter;
	
	public void start() {
		
		this.runPrinter = true;
		
		Thread thread = new Thread(new Runnable() {
			public void run() {
				printingCore();
			}
		});
		thread.start();
		
	}
	
	public void stopPrinter() {
		this.runPrinter = false;
	}
	
	private void printingCore() {
		
		while(this.runPrinter) {
			
			while(this.runPrinter) {
				System.out.println("BuzPrinter!");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					System.out.println("BuzPrinter could not sleep!");
				}
			}
			
			System.out.println("BuzPrinter stopped!");
			
		}
		
	}
	
}
