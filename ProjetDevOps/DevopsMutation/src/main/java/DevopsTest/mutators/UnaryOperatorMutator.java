package DevopsTest.mutators;


import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.UnaryOperatorKind;
import spoon.reflect.declaration.CtElement;

public class UnaryOperatorMutator extends AbstractProcessor<CtElement> {
@Override
public boolean isToBeProcessed(CtElement candidate){
return candidate instanceof CtUnaryOperator;
}
public void process(CtElement candidate){
if(!isToBeProcessed(candidate)){
return;
}
CtUnaryOperator op = (CtUnaryOperator) candidate;
op.setKind(UnaryOperatorKind.POSTDEC);
}
}