package com.benlakey.examples.threading;

public class FooPrinter implements Runnable {

	private boolean runPrinter;
	
	public boolean isRunning() {
		return runPrinter;
	}

	public void stopPrinter() {
		this.runPrinter = false;
	}
	
	@Override
	public void run() {
		
		this.runPrinter = true;
		
		while(runPrinter) {
			
			System.out.println("FooPrinter!");
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println("FooPrinter couldnt sleep!");
			}
			
		}
		
		System.out.println("FooPrinter stopped!");
		
	}

}
