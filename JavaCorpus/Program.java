package com.benlakey.examples.threading;

public class Program {

	public static void main(String[] args) throws InterruptedException {
		
		runFooPrinter();
		runBarPrinter();
		runBazPrinter();
		runBuzPrinter();
		runBozPrinter();
		runSynchroPrinter1();
		runSynchroPrinter2();
	}

	private static void runSynchroPrinter1() {
		
		final SynchroPrinter synchroPrinter = new SynchroPrinter();
		
		Thread alphasThread = new Thread(new Runnable() {
			public void run() {
				for(int i = 0; i < 10; i++) {
					try {
						synchroPrinter.syncPrintAlpha();
						Thread.sleep(50);
					} catch (InterruptedException e) {
						System.out.println("printAlphas interrupted!");
					}
				}
			}
		});
		
		Thread betasThread = new Thread(new Runnable() {
			public void run() {
				for(int i = 0; i < 10; i++) {
					try {
						synchroPrinter.syncPrintBeta();
						Thread.sleep(50);
					} catch (InterruptedException e) {
						System.out.println("printAlphas interrupted!");
					}
				}
			}
		});
		
		alphasThread.start();
		betasThread.start();
		
	}

	private static void runSynchroPrinter2() {
		
		final SynchroPrinter synchroPrinter = new SynchroPrinter();
		
		final Object lockObj = new Object();
		
		Thread alphasThread = new Thread(new Runnable() {
			public void run() {
				for(int i = 0; i < 10; i++) {
					try {
						synchronized(lockObj) {
							synchroPrinter.nonSyncPrintAlpha();
						}
						Thread.sleep(50);
					} catch (InterruptedException e) {
						System.out.println("printAlphas interrupted!");
					}
				}
			}
		});
		
		Thread betasThread = new Thread(new Runnable() {
			public void run() {
				for(int i = 0; i < 10; i++) {
					try {
						synchronized(lockObj) {
							synchroPrinter.nonSyncPrintBeta();
						}
						Thread.sleep(50);
					} catch (InterruptedException e) {
						System.out.println("printAlphas interrupted!");
					}
				}
			}
		});
		
		alphasThread.start();
		betasThread.start();
		
	}
	
	private static void runFooPrinter() throws InterruptedException {
		
		FooPrinter fooPrinter = new FooPrinter();
		Thread thread = new Thread(fooPrinter);
		thread.start();
		
		RunSomeMainThreadStuff();
		
		System.out.println("Main thread has ended. Stopping fooPrinter.");
		fooPrinter.stopPrinter();
		
	}
	
	private static void runBarPrinter() throws InterruptedException {
		
		BarPrinter barPrinter = new BarPrinter();
		barPrinter.startPrinter();
		
		RunSomeMainThreadStuff();
		
		System.out.println("Main thread has ended. Stopping barPrinter.");
		barPrinter.stopPrinter();
		
	}
	
	private static void runBazPrinter() throws InterruptedException {
		
		BazPrinter bazPrinter = new BazPrinter();
		bazPrinter.start();
		
		RunSomeMainThreadStuff();
		
		System.out.println("Main thread has ended. Stopping bazPrinter.");
		bazPrinter.stopPrinter();

	}
	
	private static void runBuzPrinter() throws InterruptedException {
		
		BuzPrinter buzPrinter = new BuzPrinter();
		buzPrinter.start();
		
		RunSomeMainThreadStuff();
		
		System.out.println("Main thread has ended. Stopping buzPrinter.");
		buzPrinter.stopPrinter();
		
	}
	
	private static void runBozPrinter() throws InterruptedException {
		
		Thread thread = new Thread(new Runnable() {
			public void run() {
				for(int second = 0; second < 5; second++) {
					
					System.out.println("BozPrinter thread, at second " + (second + 1) + "!");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						System.out.println("BozPrinter could not sleep!");
					}
					
				}
			}
		});
		
		thread.start();
		
		System.out.println("I'm the main thread. I'm going to hold off on continuing until BozPrinter is done.");
		thread.join();
		System.out.println("I'm the main thread. BozPrinter is done, so I'll go ahead and continue.");
		
		RunSomeMainThreadStuff();
		
	}
	
	private static void RunSomeMainThreadStuff() throws InterruptedException {

		for(int second = 0; second < 5; second++) {
			
			System.out.println("Main thread, at second " + (second + 1) + "!");
			Thread.sleep(1000);
			
		}
		
	}
}
