
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		LectureXml lec1=new LectureXml(args[0]);
		lec1.listerReport();
		lec1.readAll();
		lec1.generateReport();

	}

}