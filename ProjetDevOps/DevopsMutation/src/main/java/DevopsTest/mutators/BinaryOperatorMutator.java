package DevopsTest.mutators;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.declaration.CtElement;


/** a trivial mutation operator that transforms all binary operators to minus ("-") */
public class BinaryOperatorMutator extends AbstractProcessor<CtElement> {
	@Override
	public boolean isToBeProcessed(CtElement candidate) {
		return candidate instanceof CtBinaryOperator;
	}
	@Override
	public void process(CtElement candidate) {
		if (!(candidate instanceof CtBinaryOperator)) {
		return;
	}
	CtBinaryOperator op = (CtBinaryOperator)candidate;
	if(op.getKind().equals(BinaryOperatorKind.PLUS))// si c'est un + on met un -
		op.setKind(BinaryOperatorKind.MINUS);
	else if(op.getKind().equals(BinaryOperatorKind.MINUS))// si c'est un - on met un +
		op.setKind(BinaryOperatorKind.PLUS);
	else if(op.getKind().equals(BinaryOperatorKind.DIV))// si c'est un / on met un *
		op.setKind(BinaryOperatorKind.MUL);
	else if(op.getKind().equals(BinaryOperatorKind.MUL)){ // si c'est un * ou % on met un /
		op.setKind(BinaryOperatorKind.DIV);
	}

	}
}
