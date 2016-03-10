/*
*	Projet DevOps : Suppression des constructeur classe
*	Corentin Hardy, Cyrus Boumedine, Gregory ROBIN
*/




package DevopsTest.mutators;


import java.util.Set;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import java.util.List;
import java.util.Random;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class DelConstructor extends AbstractProcessor<CtClass<?>> {
	private static final double MUTATION_PROBABILITY = 0.05;
	private static final Random random = new Random();
	private static final String file_modif = "../tests_reports/" + MUTATION_PROBABILITY + "-" 
					+ "DelConstructor" + "-" + "modif.txt";


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void process(CtClass candidate) {

		
		if (random.nextFloat()<MUTATION_PROBABILITY) {
			Set<CtConstructor> construct = candidate.getConstructors();
			for (CtConstructor c : construct) {
			candidate.removeConstructor(c);
			}
		}
	}
}	
