package DevopsTest.mutators;


import spoon.processing.AbstractProcessor;
import spoon.reflect.*;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.UnaryOperatorKind;
import spoon.reflect.declaration.CtElement;
import java.util.List;
import java.util.Random;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UnaryOperatorMutator extends AbstractProcessor<CtElement> {
	private static final double MUTATION_PROBABILITY = 0.1;
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
			String Pos=op.getPosition().toString();			
			if(op.getKind().equals(UnaryOperatorKind.POSTINC))
				op.setKind(UnaryOperatorKind.POSTDEC);
			if(op.getKind().equals(UnaryOperatorKind.PREINC))
				op.setKind(UnaryOperatorKind.PREDEC);
			if(op.getKind().equals(UnaryOperatorKind.NEG))
				op.setKind(UnaryOperatorKind.POS);
			try {
				PrintWriter modif = new PrintWriter(new FileWriter("../tests_reports/modif.txt", true));
				modif.println(Pos);
					modif.close();
			} 

			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
