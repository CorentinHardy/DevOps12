import java.io.File;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		LectureXml lec1;
		File rep = new File(args[0]);

		// on verifie que le chemin est absolue
		if (! rep.isAbsolute())
			// trouvons le bon repertoire
			if (! rep.exists()){
				// on essaie dans le repertoire parent
				rep = new File("../" + args[0]);
				if (! rep.exists()){
					System.err.println("Le repertoire donnée en argument est introuvable.");
					return;
				}
			}

		lec1 = new LectureXml(rep);
		lec1.generateReport();

	}

}
