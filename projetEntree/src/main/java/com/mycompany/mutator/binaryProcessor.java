package com.mycompany.app;


import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.declaration.CtElement;


public class BinaryOperatorMutator extends AbstractProcessor<CtElement> {
	@Override
	public boolean isToBeProcessed(CtElement candidate) {
		return candidate instanceof CtBinaryOperator;
	}
	public void process(CtElement candidate) {
		if (!(candidate instanceof CtBinaryOperator)) {
		return;
	}
	CtBinaryOperator op = (CtBinaryOperator)candidate;
	op.setKind(BinaryOperatorKind.MINUS);
	}
}
