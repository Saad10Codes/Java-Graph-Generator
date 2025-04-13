package com.benlakey.examples.threading;

public class BarPrinter implements Runnable {

	private Thread thread;
	private boolean runPrinter;
	
	public BarPrinter() {
		this.thread = new Thread(this);
	}
	
	public void startPrinter() {
		this.runPrinter = true;
		this.thread.start();
	}
	
	public void stopPrinter() {
		this.runPrinter = false;
	}

	@Override
	public void run() {
		
		while(this.runPrinter) {
			System.out.println("BarPrinter!");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println("BarPrinter could not sleep!");
			}
		}
		
		System.out.println("BarPrinter stopped!");
		
	}
	
}
