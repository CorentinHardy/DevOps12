import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.text.*; 
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class WriteXml {

	private Map<String,Mutation> mutation=new HashMap<String,Mutation>();

	private int nbmuta;
	private int nbKill;
	private String dest;
	private String titre ="Rapport des tests par mutations";
	private	List<String> accueil;
	private List<String> stats;
	
	public WriteXml(int nbMuta, int nbKill, String dest)
	{
		this.nbmuta=nbMuta;
		this.nbKill=nbKill;
		this.dest=dest;
		this.accueil=new ArrayList<String>();
		DecimalFormat df = new DecimalFormat ( ) ;
		df.setMaximumFractionDigits ( 2 ) ;
		df.setMinimumFractionDigits ( 2 ) ; 
		df.setDecimalSeparatorAlwaysShown ( true ) ; 
		
		accueil.add("Nombre de mutation créés : "+nbMuta+"\n");
		accueil.add("Nombre de mutations tuées :"+nbKill+"\n");
		accueil.add("Nombre de mutations ayant survécu :"+(nbMuta-nbKill)+"\n");
		
		stats=new ArrayList<String>();
		double stats1=(double)nbKill/(double)nbMuta;
		double stats2=((double)nbMuta-(double)nbKill)/(double)nbMuta;
		stats.add("Ratio de mutants ayant été tués :"+ stats1+"\n");
		stats.add("Ratio de mutants ayant survécu :"+stats2+"\n");
		
		
	}
	
	public void createXml()
	{
	
	    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    try {
    
	        final DocumentBuilder builder = factory.newDocumentBuilder();
	        final Document document= builder.newDocument();
	        final Element racine = document.createElement("rapport");
	        document.appendChild(racine);           
	       
	        
	        final Element acc =document.createElement("Accueils");
	        racine.appendChild(acc);
	        Element[] e =new Element[accueil.size()];
	        for (int i=0; i<accueil.size(); i++)
	        {
	        	e[i]=document.createElement("accueil");
	        	acc.appendChild(e[i]);
	        	e[i].appendChild(document.createTextNode(accueil.get(i)+"\n"));
	        }
	        
	        
	        Element[] e2 =new Element[stats.size()];
	        final Element sta =document.createElement("Stats");
	        racine.appendChild(sta);
	        
	        for (int i=0; i<stats.size(); i++)
	        {
	        	e2[i]=document.createElement("stats");
	        	sta.appendChild(e2[i]);
	        	e2[i].appendChild(document.createTextNode(stats.get(i)+"\n"));
	        }
	
	        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        final Transformer transformer = transformerFactory.newTransformer();
	        final DOMSource source = new DOMSource(document);
	        final StreamResult sortie = new StreamResult(new File(dest));
	        //final StreamResult result = new StreamResult(System.out);
  
	        //prologue
	        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");            

	        //formatage
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

	        //sortie
	        transformer.transform(source, sortie);  

	    }
	    catch (final ParserConfigurationException e) {
	        e.printStackTrace();
	    }
	    catch (TransformerConfigurationException e) {
	        e.printStackTrace();
	    }
	    catch (TransformerException e) {
	        e.printStackTrace();
	    }           

	}
	
	
	
}


