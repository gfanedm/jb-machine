package io.github.gfanedm.machine;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.github.gfanedm.machine.interruption.Interruption;

public class Main {

	private static final Executor executor = Executors.newFixedThreadPool(2);

	public Interruption interruption;
	public Machine machine;

	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		this.interruption = new Interruption();
		this.machine = new Machine(interruption);

		executor.execute(interruption);
		executor.execute(machine);
	}
}
