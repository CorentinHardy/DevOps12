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
	private int nbMort;
	private int nbMutation;
	private int nbMutationAlive;
	private int nbMutationKilled;
	private String dest="../Report/pages/index.html";
	
	private List<String>  message;
	private List<String> stats;
	
	private String index1="<!DOCTYPE html> \n <html lang=\"en\"> \n<head>\n<meta charset=\"utf-8\">\n<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n    <meta name=\"description\" content=\"\">   \n <meta name=\"author\" content=\"\">\n    <title>Mutation Testing Report</title>\n    <!-- Bootstrap Core CSS -->\n    <link href=\"../bower_components/bootstrap/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n    <!-- MetisMenu CSS -->\n    <link href=\"../bower_components/metisMenu/dist/metisMenu.min.css\" rel=\"stylesheet\">\n   <!-- Timeline CSS -->\n    <link href=\"../dist/css/timeline.css\" rel=\"stylesheet\">\n    <!-- Custom CSS -->\n    <link href=\"../dist/css/sb-admin-2.css\" rel=\"stylesheet\">\n    <!-- Morris Charts CSS -->\n    <link href=\"../bower_components/morrisjs/morris.css\" rel=\"stylesheet\">\n    <!-- Custom Fonts -->\n    <link href=\"../bower_components/font-awesome/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\">\n    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->\n</head>\n<body>\n<div class=\"container\">\n<div class=\"col-lg-1\">\n<a href=\"index.html\"><button class=\"btn btn-primary\">Accueil</button> </a>\n</div>\n<div class=\"col-lg-6 col-lg-offset-2\">\n<h1> Résultat des tests par mutations<h1>\n</div>\n</div>\n<div class=\"container\">\n	<div class=\"col-lg-3 col-lg-offset-O\">";
	
	private String index2="\n<i class=\"fa fa-bar-chart-o fa-fw\"></i> Result		<div class=\"panel-body\">\n		  <div id=\"morris-donut-chart\"></div> \n</div></div><div class=\"col-lg-6 colg-lg-offset-1\"><table class=\"table table-condensed table-bordered\">  ";
                 
	private String index3="<thead><tr> <td><a href=\"index.html\" >Nombre de codes mutants générés</a> </td> <td>"+nbMutation+"</td> </tr> </thead> <tbody> <tr> <td><a href=\"index.html\">Nombre de mutants morts nées</a></td> <td>"+nbMort+"</td> </tr> <tr> <td><a href=\"index.html\" >Nombre de mutants tués</a></td>         <td>"+nbMutationKilled+"</td> </tr><tr>   <td><a href=\"\\index.html\"  target=\"_blank\">Nombre de mutants ayant survécu</a></td> <td>"+nbMutationAlive+"</td>  </tr>   </tbody> </table></div></div>";

	private String index4="<!-- jQuery -->    <script src=\"../bower_components/jquery/dist/jquery.min.js\"></script><script src=\"../bower_components/bootstrap/dist/js/bootstrap.min.js\"></script>    <script src=\"../bower_components/metisMenu/dist/metisMenu.min.js\"></script>  <!-- Morris Charts JavaScript -->    <script src=\"../bower_components/raphael/raphael-min.js\"></script>   <script src=\"../bower_components/morrisjs/morris.min.js\"></script>    <script src=\"../js/morris-data.js\"></script>    <!-- Custom Theme JavaScript -->    <script src=\"../dist/js/sb-admin-2.js\"></script></body></html>";
	
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
		    fw.write (index1+index2+index3+index4);
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
