package DevopsTest.mutators;


import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.UnaryOperatorKind;
import spoon.reflect.declaration.CtElement;
import java.util.List;
import java.util.Random;

public class UnaryOperatorMutator extends AbstractProcessor<CtElement> {
	private static final double MUTATION_PROBABILITY = 0.20;
	private static final Random random = new Random();
	

	@Override
	public boolean isToBeProcessed(CtElement candidate){
		return candidate instanceof CtUnaryOperator;
	}

	public void process(CtElement candidate){
		if(!isToBeProcessed(candidate)){
			return;
		}
		CtUnaryOperator op = (CtUnaryOperator) candidate;

		if (random.nextFloat()<MUTATION_PROBABILITY) {

			if(op.getKind().equals(UnaryOperatorKind.POSTINC))
				op.setKind(UnaryOperatorKind.POSTDEC);
			if(op.getKind().equals(UnaryOperatorKind.PREINC))
				op.setKind(UnaryOperatorKind.PREDEC);
			if(op.getKind().equals(UnaryOperatorKind.NEG))
				op.setKind(UnaryOperatorKind.POS);
		}
	}
}
