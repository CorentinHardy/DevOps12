import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class WriteHtml {

	int error;
	int fail;
	private List<String> mutationName;
	private boolean[] mutationResult;
	private int nbMutation;
	private int nbMutationKilled;
	private String dest="../mutation-testing-report.html";
	
	private List<String>  message;
	private List<String> stats;
	
	private String fich="<!doctype html> \n<html lang=\"fr\"> \n<head>\n<meta charset=\"utf-8\">" +
			"\n<title>Tests report</title>" +
			"\n<link rel=\"stylesheet\" href=\"style.css\">" +
			"\n<script src=\"script.js\"></script>"+
			"\n</head>" +
			"\n<body>";
			
	public WriteHtml(List<String> mutaName,boolean[] mutaResult,int nbKilled, int fail, int error)
	{
		this.mutationName=mutaName;
		this.mutationResult=mutaResult;
		this.nbMutation=mutationName.size();
		this.nbMutationKilled=nbKilled;
		this.fail=fail;
		this.error=error;
		
	}
	public void printFile()
	{
		System.out.println(fich);
	}
	public void WriteFile()
	{
		this.calculStats();
		this.writeMessage();
		fich+="</body>\n</head>";
		File f = new File (dest); 
		try
		{
		    FileWriter fw = new FileWriter (f);
		    fw.write (fich);
		    fw.close();
		}
		catch (IOException exception)
		{
		    System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}	
	}

	
	public void add(String text, String balise)
	{
		fich+="<"+balise+">"+text+"</"+balise+">\n";
	}
	public void calculStats()
	{
		DecimalFormat df = new DecimalFormat ( ) ;
		df.setMaximumFractionDigits ( 2 ) ;
		df.setMinimumFractionDigits ( 2 ) ; 
		df.setDecimalSeparatorAlwaysShown ( true ) ; 
		
		stats=new ArrayList<String>();
		double stats1=(double)nbMutationKilled/(double)nbMutation;
		double stats2=((double)mutationName.size()-(double)nbMutationKilled)/(double)nbMutation;
		stats.add("Ratio de mutants ayant été tués : "+ stats1+"\n");
		stats.add("Ratio de mutants ayant survécu : "+stats2+"\n");
		
	}
	public void writeMessage()
	{
		this.add("Résultat des tests par mutations","h1");
		fich+="<div>";
		this.add("Informations générales","h2");
		this.add("Nombre de mutants générés : "+nbMutation,"p");
		this. add("Nombre de mutants tués : "+nbMutationKilled,"p");
		this.add("Nombre de mutants survivants : "+(nbMutation-nbMutationKilled),"p");
		fich+="</div>";
		fich+="<div>";
		this.add("Statistiques", "h2");
		this.add(stats.get(1), "p");
		this.add(stats.get(0), "p");
		fich+="</div>";
		fich+="<div>";
		this.add("Résultats par processeur","h2");
		
		for (int k=0; k<mutationName.size(); k++)
		{
			
			String s;
			s=mutationName.get(k);
			if (mutationResult[k]==true)
			{
				s+= " : La mutation correspondant à ce processeur a été tuée";
			}
			else
			{
				s+= " : La mutation correspondant à ce processeur a survécu";
			}
			this.add(s,"p");
			
		}
		fich+="</div>";
		
		
	}
	
	
}
