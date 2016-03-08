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
	//public void process(CtElement candidate) {
		public process(CtElement element){
			if(element instanceof CtClass){
				CtClass maClass = (CtClass)candidate;
				Random r = new Random();
				//20% des classes
				if(r > 0.2){
					return;
				}
				List<CtElement> elementsToBeMutated = maClasse.getElements(new Filter<CtElement>(){
					@Override
					public boolean matches(CtElement arg0){
						return this.isToBeProcessed(arg0);
					}
				});
				for(CtElement e : elementsToBeMutated ){
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
		}
		
		/*
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
	}*/

	
}
