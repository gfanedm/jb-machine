package io.github.gfanedm.machine.pipeline;

import java.util.ArrayList;
import java.util.Random;

import io.github.gfanedm.machine.instruction.Instruction;
import io.github.gfanedm.machine.memory.MemoryHandler;
import io.github.gfanedm.machine.methods.DistanceMethod;
import io.github.gfanedm.machine.methods.FaneMethod;
import io.github.gfanedm.machine.methods.PowMethod;
import io.github.gfanedm.machine.methods.HaltMethod;
import io.github.gfanedm.machine.methods.Method;
import io.github.gfanedm.machine.methods.MultiplyMethod;
import io.github.gfanedm.machine.methods.SqrtMethod;
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
		this.methods.add(new FaneMethod(4));
		this.methods.add(new SqrtMethod(5));
		this.methods.add(new DistanceMethod(6));
		this.methods.add(new PowMethod(7));
	}

	public boolean execute(MemoryHandler memoryHandler, Instruction instruction) throws Exception {
		int code = instruction.get(0);
		
		if(memoryHandler == null)
			throw new Exception(String.format("The memory is unavailable." , code));
		 
		Method method = getMethod(code);
		if(method == null)
			throw new Exception(String.format("The method with code %s don't exist." , code));
		
		method.executeFunction(memoryHandler, instruction);
		return true;
	}
	
	public Method getMethod(int opCode) {
		return methods.stream().filter(method -> method.getOpCode() == opCode).findFirst().orElse(null);
	}
	
	public int getRandomCode() {
		return methods.get(new Random().nextInt(methods.size())).getOpCode();
	}
	
	public ArrayList<Method> getMethods() {
		return methods;
	}

}
