package DevopsTest.mutators;


import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.declaration.CtElement;
import spoon.support.reflect.code.CtBinaryOperatorImpl;
import java.util.List;
import java.util.Random;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BinaryOperatorMutator extends AbstractProcessor<CtElement> {
	private static final double MUTATION_PROBABILITY = 0.1;
	private static final Random random = new Random();
	private static final String file_modif = "../tests_reports/" + MUTATION_PROBABILITY + "-" 
					+ "BinaryOperatorMutator" + "-" + "modif.txt";

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
			String Pos=op.getPosition().toString();
			if(op.getKind().equals(BinaryOperatorKind.PLUS))
				op.setKind(BinaryOperatorKind.MINUS);
			else if(op.getKind().equals(BinaryOperatorKind.MINUS))
				op.setKind(BinaryOperatorKind.PLUS);
			else if(op.getKind().equals(BinaryOperatorKind.DIV))
				op.setKind(BinaryOperatorKind.MUL);
			else if(op.getKind().equals(BinaryOperatorKind.MUL))
				op.setKind(BinaryOperatorKind.DIV);
			try {
				PrintWriter modif = new PrintWriter(new FileWriter(new File(file_modif).getAbsolutePath(), true));
				modif.println(Pos);
				modif.close();
			} 

			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
