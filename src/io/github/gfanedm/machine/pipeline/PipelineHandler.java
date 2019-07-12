package io.github.gfanedm.machine.pipeline;

import java.util.ArrayList;
import java.util.Random;

import io.github.gfanedm.machine.instructions.Instruction;
import io.github.gfanedm.machine.memory.MemoryHandler;
import io.github.gfanedm.machine.methods.HaltMethod;
import io.github.gfanedm.machine.methods.Method;
import io.github.gfanedm.machine.methods.MultiplyMethod;
import io.github.gfanedm.machine.methods.SubtractionMethod;
import io.github.gfanedm.machine.methods.SumMethod;

public class PipelineHandler {

	private final ArrayList<Method> methods;

	public PipelineHandler() {

		this.methods = new ArrayList<>();

		this.methods.add(new HaltMethod(-1));
		this.methods.add(new SumMethod(1));
		this.methods.add(new SubtractionMethod(2));
		this.methods.add(new MultiplyMethod(3));
	}

	public boolean execute(Instruction instruction, MemoryHandler memoryHandler) throws Exception {
		int code = instruction.getOpCode();
 
		if (memoryHandler.getUseList().size() != 3)
			throw new Exception(String.format("The memory is unavailable %s.", memoryHandler.getUseList().size()));

		Method method = getMethod(code);
		if (method == null)
			throw new Exception(String.format("The method with code %s don't exist.", code));

		method.executeFunction(memoryHandler, instruction);

		return true;
	}

	public Method getMethod(int opCode) {
		return methods.stream().filter(method -> method.getOpCode() == opCode).findFirst().orElse(null);
	}

	public int getRandomCode() {
		return methods.get(new Random().nextInt(methods.size())).getOpCode();
	}

}
