
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.declaration.CtElement;
import spoon.support.reflect.code.CtBinaryOperatorImpl;
import java.util.List;
import java.util.Random;


public class BinaryOperatorMutator extends AbstractProcessor<CtElement> {
	 private static final double MUTATION_PROBABILITY = 0.20;
	private static final Random random = new Random();

	@Override
	public boolean isToBeProcessed(CtElement candidate) {
		return candidate instanceof CtBinaryOperatorImpl;
	}
	public void process(CtElement candidate) {
		if (!(candidate instanceof CtBinaryOperatorImpl)) {
		return;
	}
	
	CtBinaryOperatorImpl op = (CtBinaryOperatorImpl) candidate;
	
	if (random.nextFloat()<MUTATION_PROBABILITY) {


		if(op.getKind().equals(BinaryOperatorKind.PLUS))
			op.setKind(BinaryOperatorKind.MINUS);
		else if(op.getKind().equals(BinaryOperatorKind.MINUS))
			op.setKind(BinaryOperatorKind.PLUS);
		else if(op.getKind().equals(BinaryOperatorKind.DIV))
			op.setKind(BinaryOperatorKind.MUL);
		else if(op.getKind().equals(BinaryOperatorKind.MUL))
			op.setKind(BinaryOperatorKind.DIV);
		}
	}
}