import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class WriteHtml {

	private Map<String,Mutation> mutation;
	int error;
	int fail;
	private List<String> mutationName;
	private boolean[] mutationResult;
	private int nbMort;
	private int nbMutation;
	private int nbMutationAlive;
	private int nbMutationKilled;
	private int nbMortNee;
	private String dest="../Report/pages/index.html";
	private String destAllMutation="../Report/pages/allMutation.html";
	private String destJs="../Report/js/morris-data.js";
	private String destSucceed="../Report/pages/aliveMutation.html";
	private String destFailed="../Report/pages/killedMutation.html";
	private String destMort="../Report/pages/mortNéeMutation.html";
	
	private List<String>  message;
	private List<String> stats;
	
	private String index1="<!DOCTYPE html> \n <html lang=\"en\"> \n<head>\n<meta charset=\"utf-8\">\n<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n    <meta name=\"description\" content=\"\">   \n <meta name=\"author\" content=\"\">\n    <title>Mutation Testing Report</title>\n    <!-- Bootstrap Core CSS -->\n    <link href=\"../bower_components/bootstrap/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n    <!-- MetisMenu CSS -->\n    <link href=\"../bower_components/metisMenu/dist/metisMenu.min.css\" rel=\"stylesheet\">\n   <!-- Timeline CSS -->\n    <link href=\"../dist/css/timeline.css\" rel=\"stylesheet\">\n    <!-- Custom CSS -->\n    <link href=\"../dist/css/sb-admin-2.css\" rel=\"stylesheet\">\n    <!-- Morris Charts CSS -->\n    <link href=\"../bower_components/morrisjs/morris.css\" rel=\"stylesheet\">\n    <!-- Custom Fonts -->\n    <link href=\"../bower_components/font-awesome/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\">\n    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->\n</head>\n<body>\n<div class=\"container\">\n<div class=\"col-lg-1\">\n<a href=\"index.html\"><button class=\"btn btn-primary\">Accueil</button> </a>\n</div>\n<div class=\"col-lg-6 col-lg-offset-2\">\n<h1> Résultat des tests par mutations<h1>\n</div>\n</div>\n<div class=\"container\">\n	<div class=\"col-lg-3 col-lg-offset-O\">";
	
	private String index2="\n<i class=\"fa fa-bar-chart-o fa-fw\"></i> Result		<div class=\"panel-body\">\n		  <div id=\"morris-donut-chart\"></div> \n</div></div><div class=\"col-lg-6 colg-lg-offset-1\"><table class=\"table table-condensed table-bordered\">  ";
                 
	private String index3;

	private String index4;
	private String fich="<!doctype html> \n<html lang=\"fr\"> \n<head>\n<meta charset=\"utf-8\">" +
			"\n<title>Tests report</title>" +
			"\n<link rel=\"stylesheet\" href=\"style.css\">" +
			"\n<script src=\"script.js\"></script>"+
			"\n</head>" +
			"\n<body>";
			
	
	
	public WriteHtml(List<String> mutaName,boolean[] mutaResult,int nbKilled, int fail, int error,Map<String,Mutation> mutation)
	{

		this.mutationName=mutaName;
		this.mutationResult=mutaResult;
		this.nbMutation=mutation.size();
		this.nbMutationKilled=0;
		this.mutation=mutation;
		
		for (String mapKey : mutation.keySet()) {
			// utilise ici hashMap.get(mapKey) pour accéder aux valeurs
		
			if (mutation.get(mapKey).isAlive()==1)
			{
				nbMutationKilled++;
			}
			if (mutation.get(mapKey).isAlive()==2)
			{
				nbMortNee++;
			}
			if (mutation.get(mapKey).isAlive()==0)
			{
				nbMutationAlive++;
			}
		}
		
		this.fail=fail;
		this.error=error;
		index3="<thead><tr> <td><a href=\"allMutation.html\" >Nombre de codes mutants générés</a> </td> <td>"+nbMutation+"</td> </tr> </thead> <tbody> <tr> <td><a href=\"mortNéeMutation.html\">Nombre de mutants morts nés</a></td> <td>"+nbMortNee+"</td> </tr> <tr> <td><a href=\"killedMutation.html\" >Nombre de mutants tués</a></td>         <td>"+nbMutationKilled+"</td> </tr><tr>   <td><a href=\"aliveMutation.html\" >Nombre de mutants ayant survécus</a></td> <td>"+nbMutationAlive+"</td>  </tr>   </tbody> </table></div></div>";
		index4="<!-- jQuery -->    <script src=\"../bower_components/jquery/dist/jquery.min.js\"></script><script src=\"../bower_components/bootstrap/dist/js/bootstrap.min.js\"></script>    <script src=\"../bower_components/metisMenu/dist/metisMenu.min.js\"></script>  <!-- Morris Charts JavaScript -->    <script src=\"../bower_components/raphael/raphael-min.js\"></script>   <script src=\"../bower_components/morrisjs/morris.min.js\"></script>    <script src=\"../js/morris-data.js\"></script>    <!-- Custom Theme JavaScript -->    <script src=\"../dist/js/sb-admin-2.js\"></script></body></html>";

		
		
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
		this.updateJs();
		this.writeAllMutation();
		this.writeSucceedMutation();
		this.writeMortNe();
		this.writeKilledMutation();
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
	public String addT(String dest, String text, String balise)
	{
		return dest+="<"+balise+">"+text+"</"+balise+">\n";
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
	
	
	public void updateJs()
	{
		File f = new File (destJs); 
		try
		{
			
			String js="$(function()\n {$(function() { });\n    Morris.Donut({  \n      element: 'morris-donut-chart',\n       data: [{    \n        label: \"Mutants morts nés\",\n         value: "+nbMortNee+"        }, {            label: \"Mutants tués\",            value: "+nbMutationKilled+"        }, {            label: \"Mutants ayant survécus\",   value: "+nbMutationAlive+"        }],        resize: true    }); \n ;});";
		    FileWriter fw = new FileWriter (f);
		    fw.write (js);
		    fw.close();
		}
		catch (IOException exception)
		{
		    System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}	
	}
	
	public void writeAllMutation()
	{
		String result="";
		result+="<!DOCTYPE html> \n <html lang=\"en\"> \n<head>\n<meta charset=\"utf-8\">\n<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n    <meta name=\"description\" content=\"\">   \n <meta name=\"author\" content=\"\">\n    <title>Mutation Testing Report</title>\n    <!-- Bootstrap Core CSS -->\n    <link href=\"../bower_components/bootstrap/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n    <!-- MetisMenu CSS -->\n    <link href=\"../bower_components/metisMenu/dist/metisMenu.min.css\" rel=\"stylesheet\">\n   <!-- Timeline CSS -->\n    <link href=\"../dist/css/timeline.css\" rel=\"stylesheet\">\n    <!-- Custom CSS -->\n    <link href=\"../dist/css/sb-admin-2.css\" rel=\"stylesheet\">\n    <!-- Morris Charts CSS -->\n    <link href=\"../bower_components/morrisjs/morris.css\" rel=\"stylesheet\">\n    <!-- Custom Fonts -->\n    <link href=\"../bower_components/font-awesome/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\">\n    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->\n</head>\n<body>\n<div class=\"container\">\n<div class=\"col-lg-1\">\n<a href=\"index.html\"><button class=\"btn btn-primary\">Accueil</button> </a>\n</div>\n<div class=\"col-lg-6 col-lg-offset-2\">\n<h1> Affichage de toutes les mutations</h1>\n</div>\n</div>";
		File f = new File (destAllMutation);
		result+="<div class=\"col-lg-6 colg-lg-offset-3\">";
		result+="<table class=\"table table-condensed table-bordered\"> ";
		result+="<tdbody> ";
		

		result+="<div class=\"col-lg-6 colg-lg-offset-3\">";
		result+="<table class=\"table table-condensed table-bordered\"> ";
		result+="<tdbody> ";
		
		for (String mapKey : mutation.keySet()) {
			
			
		
			if (mutation.get(mapKey).isAlive()==0)
			{
				result+=" <tr> <td>"+mutation.get(mapKey).getName().substring(0, mutation.get(mapKey).getName().length()-4)+"</td> <td> A survécu </td> </tr> ";
			}
			
			if (mutation.get(mapKey).isAlive()==1)
			{
				result+=" <tr> <td>"+mutation.get(mapKey).getName().substring(0, mutation.get(mapKey).getName().length()-4)+"</td> <td> A été tué par les tests </td> </tr> ";
			}
			if (mutation.get(mapKey).isAlive()==2)
			{
				result+=" <tr> <td>"+mutation.get(mapKey).getName().substring(0, mutation.get(mapKey).getName().length()-4)+"</td> <td> A été tué à la compilation </td> </tr> ";
			}
			
		}
		result+="</tdbody>";
		result+="</table";
		result+="</div>";
		result+="</body>";
		
	
		try
		{
			FileWriter fw=new FileWriter(f);
			fw.write(result);
			fw.close();
		}
		catch (IOException exception)
		{
		    System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}	
		
	}
	
	public void writeMortNe()
	{
		String result="";
		result+="<!DOCTYPE html> \n <html lang=\"en\"> \n<head>\n<meta charset=\"utf-8\">\n<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n    <meta name=\"description\" content=\"\">   \n <meta name=\"author\" content=\"\">\n    <title>Mutation Testing Report</title>\n    <!-- Bootstrap Core CSS -->\n    <link href=\"../bower_components/bootstrap/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n    <!-- MetisMenu CSS -->\n    <link href=\"../bower_components/metisMenu/dist/metisMenu.min.css\" rel=\"stylesheet\">\n   <!-- Timeline CSS -->\n    <link href=\"../dist/css/timeline.css\" rel=\"stylesheet\">\n    <!-- Custom CSS -->\n    <link href=\"../dist/css/sb-admin-2.css\" rel=\"stylesheet\">\n    <!-- Morris Charts CSS -->\n    <link href=\"../bower_components/morrisjs/morris.css\" rel=\"stylesheet\">\n    <!-- Custom Fonts -->\n    <link href=\"../bower_components/font-awesome/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\">\n    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->\n</head>\n<body>\n<div class=\"container\">\n<div class=\"col-lg-1\">\n<a href=\"index.html\"><button class=\"btn btn-primary\">Accueil</button> </a>\n</div>\n<div class=\"col-lg-8 col-lg-offset-2\">\n<h1> Mutations ayant été tuées à la compilation</h1>\n</div>\n</div>";
		File f = new File (destMort);
		result+="<div class=\"col-lg-12 colg-lg-offset-3\">";
		result+="<table class=\"table table-condensed table-bordered\"> ";
		result+="<tdbody> ";
		

		result+="<div class=\"col-lg-6 colg-lg-offset-3\">";
		result+="<table class=\"table table-condensed table-bordered\"> ";
		result+="<tdbody> ";
		
		for (String mapKey : mutation.keySet()) {
			
			
		
			
			if (mutation.get(mapKey).isAlive()==2)
			{
				result+=" <tr> <td>"+mutation.get(mapKey).getName().substring(0, mutation.get(mapKey).getName().length()-4)+"</td> <td> A été tué à la compilation </td> </tr> ";
			}
			
		}
		result+="</tdbody>";
		result+="</table";
		result+="</div>";
		result+="</body>";
		
	
		try
		{
			FileWriter fw=new FileWriter(f);
			fw.write(result);
			fw.close();
		}
		catch (IOException exception)
		{
		    System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}	
		
	}
	
	
	
	public void writeKilledMutation()
	{
		String result="";
		result+="<!DOCTYPE html> \n <html lang=\"en\"> \n<head>\n<meta charset=\"utf-8\">\n<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n    <meta name=\"description\" content=\"\">   \n <meta name=\"author\" content=\"\">\n    <title>Mutation Testing Report</title>\n    <!-- Bootstrap Core CSS -->\n    <link href=\"../bower_components/bootstrap/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n    <!-- MetisMenu CSS -->\n    <link href=\"../bower_components/metisMenu/dist/metisMenu.min.css\" rel=\"stylesheet\">\n   <!-- Timeline CSS -->\n    <link href=\"../dist/css/timeline.css\" rel=\"stylesheet\">\n    <!-- Custom CSS -->\n    <link href=\"../dist/css/sb-admin-2.css\" rel=\"stylesheet\">\n    <!-- Morris Charts CSS -->\n    <link href=\"../bower_components/morrisjs/morris.css\" rel=\"stylesheet\">\n    <!-- Custom Fonts -->\n    <link href=\"../bower_components/font-awesome/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\">\n    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->\n</head>\n<body>\n<div class=\"container\">\n<div class=\"col-lg-1\">\n<a href=\"index.html\"><button class=\"btn btn-primary\">Accueil</button> </a>\n</div>\n<div class=\"col-lg-8 col-lg-offset-2\">\n<h1> Mutations ayant été tuées par les tests</h1>\n</div>\n</div>";
		File f = new File (destFailed);
		
		int c=0;
		for (String mapKey : mutation.keySet()) {
			// utilise ici hashMap.get(mapKey) pour accéder aux valeurs
			result+="<div class=\"col-lg-6 colg-lg-offset-3\">";
			result+="<table class=\"table table-condensed table-bordered\"> ";
			
			if (mutation.get(mapKey).isAlive()==1)
			{
				c++;
				result+=" <thead>"+mutation.get(mapKey).getName().substring(0, mutation.get(mapKey).getName().length()-4)+"</thead> ";
				for (int i=0;i<mutation.get(mapKey).getTestFailed().size(); i++)
				{
					String [] e=mutation.get(mapKey).getTestFailed().get(i).split("-");
					result+="<tr> <td>"+e[1]+"</td> <td> Le mutant est passé à travers ce test </td> </tr>";
					
				}
				for (int i=0;i<mutation.get(mapKey).getTestError().size(); i++)
				{
					String [] e=mutation.get(mapKey).getTestError().get(i).split("-");
					result+="<tr> <td>"+e[1]+"</td> <td> Le mutant a été tué par ce test (failed) </td> </tr>";
					
				}
				for (int i=0;i<mutation.get(mapKey).getTestSucceed().size(); i++)
				{
					String [] e=mutation.get(mapKey).getTestSucceed().get(i).split("-");
					result+="<tr> <td>"+e[1]+"</td> <td> Le mutant a été tué par ce test (erreur) </td> </tr>";
					
				}
			}
			result+="</tdbody>";
			result+="</table";
			result+="</div>";
			
		}
		if(c==0)
		{
			result+="<h1>Aucune mutation n'a été tuée par les tests</h1>";
		}
		result+="</body>";
		try
		{
			FileWriter fw=new FileWriter(f);
			fw.write(result);
			fw.close();
		}
		catch (IOException exception)
		{
		    System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}	

	}
	
	public void writeSucceedMutation()
	{
		String result="";
		result+="<!DOCTYPE html> \n <html lang=\"en\"> \n<head>\n<meta charset=\"utf-8\">\n<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n    <meta name=\"description\" content=\"\">   \n <meta name=\"author\" content=\"\">\n    <title>Mutation Testing Report</title>\n    <!-- Bootstrap Core CSS -->\n    <link href=\"../bower_components/bootstrap/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n    <!-- MetisMenu CSS -->\n    <link href=\"../bower_components/metisMenu/dist/metisMenu.min.css\" rel=\"stylesheet\">\n   <!-- Timeline CSS -->\n    <link href=\"../dist/css/timeline.css\" rel=\"stylesheet\">\n    <!-- Custom CSS -->\n    <link href=\"../dist/css/sb-admin-2.css\" rel=\"stylesheet\">\n    <!-- Morris Charts CSS -->\n    <link href=\"../bower_components/morrisjs/morris.css\" rel=\"stylesheet\">\n    <!-- Custom Fonts -->\n    <link href=\"../bower_components/font-awesome/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\">\n    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->\n</head>\n<body>\n<div class=\"container\">\n<div class=\"col-lg-1\">\n<a href=\"index.html\"><button class=\"btn btn-primary\">Accueil</button> </a>\n</div>\n<div class=\"col-lg-8 col-lg-offset-0\">\n<h1> Affichage des mutations ayant survécues</h1>\n</div>\n</div>";
		File f = new File (destSucceed);
		
		int c=0;
		for (String mapKey : mutation.keySet()) {
			result+="<div class=\"col-lg-6 colg-lg-offset-3\">";
			result+="<table class=\"table table-condensed table-bordered\"> ";
			
			if (mutation.get(mapKey).isAlive()==0)
			{
				c++;
				
				result+=" <tr> <td>"+mutation.get(mapKey).getName().substring(0, mutation.get(mapKey).getName().length()-4)+"</td> <td> <a href=\"../../tests_reports/"+mutation.get(mapKey).getName().substring(0, mutation.get(mapKey).getName().length()-4)+"-modif.txt\" >Voir les différences avec le code original</a>  </td> </tr> ";
			}
			result+="</tdbody>";
			result+="</table";
			result+="</div>";
			
		}
		if(c==0)
		{
			result+="<h1>Aucune mutation n'a survécue</h1>";
		}
		result+="</body>";
		try
		{
			FileWriter fw=new FileWriter(f);
			fw.write(result);
			fw.close();
		}
		catch (IOException exception)
		{
		    System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}	

	}
}
