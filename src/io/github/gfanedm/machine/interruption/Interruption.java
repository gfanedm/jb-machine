package io.github.gfanedm.machine.interruption;

import java.util.Scanner;

public class Interruption implements Runnable {

	private final Scanner scanner;
	private boolean running;

	public Interruption() {
		scanner = new Scanner(System.in);
		running = true;
	}

	public void run() {
		while (true) {
			String line = scanner.nextLine();
			if (line.equalsIgnoreCase("stop")) {
				running = false;
			}else if(line.equalsIgnoreCase("continue")){
				running = true;
			}else if(line.equalsIgnoreCase("end")){
				System.exit(0);
			}
		}
	}

	public boolean isRunning() {
		return running;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
}
