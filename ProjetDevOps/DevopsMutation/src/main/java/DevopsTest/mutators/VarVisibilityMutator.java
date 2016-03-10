/*
*	Projet DevOps : Change les variables globales en protected
*	Corentin Hardy, Cyrus Boumedine, Gregory ROBIN
*/



package DevopsTest.mutators;


import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import java.util.List;
import java.util.Random;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class VarVisibilityMutator extends AbstractProcessor<CtClass<?>> {
	public boolean isToBeProcessed(CtClass<?> candidate) {
		if (!(candidate instanceof CtMethod)) {
		return true;
	}
	return false;
	}
	public void process(CtClass<?> candidate) {
		if (candidate instanceof CtField<?>) {
		return;
	}
	candidate.setVisibility(ModifierKind.PROTECTED);
	}
}
