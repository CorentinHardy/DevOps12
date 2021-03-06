import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LectureXml {
	int fail=0;
	int error=0;
	private File rep;
	private String[] listFile;
	private List<String> listReport=new ArrayList<String>();
	int nbMutakill=0;
	private List<String> listMutation=new ArrayList<String>();
	private boolean[] mutationResult;
	private Map<String,Mutation> mutation=new HashMap<String,Mutation>();

	public LectureXml(File rep)
	{
		this.rep = rep;
	}
	
	public void listerReport()
	{
		listFile=rep.list();
		for (int i=0; i<listFile.length; i++ )
		{
			if (listFile[i].startsWith("TEST"))
			{
				listReport.add(listFile[i]);
			}
			if(listFile[i].startsWith("MORT"))
			{
				String [] bigName=listFile[i].split("-");
				mutation.put(bigName[1]+bigName[2], new Mutation(bigName[1]+"-"+bigName[2],true));
			}
		}
		for (int i=0 ; i<listReport.size() ; i++)
		{
			String [] bigName=listReport.get(i).split("-");
			
			String name=bigName[2]+"-"+bigName[3];
			if ( !mutation.containsKey(name)) 
			{
				mutation.put(name, new Mutation(name,rep));
				mutation.get(name).addTest(listReport.get(i));
				
			}
			else {
				mutation.get(name).addTest(listReport.get(i));
			}
		}
		
		
	}
	public void readAll()
	{
		this.listerReport();
		mutationResult=new boolean[listReport.size()];
		for (int i=0; i<listReport.size(); i++)
		{
			this.read(i);
		}
	}
	
	
	public void read(int i)
	{
		
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try{
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document= builder.parse(new File(rep+"/"+listReport.get(i)));
			listMutation.add(listReport.get(i).substring(5,listReport.get(i).length()-4));
			final Element racine = document.getDocumentElement();
			if( (!(racine.getAttribute("failures").equals("0"))) | !(racine.getAttribute("errors").equals("0")))
			{
				nbMutakill++;
				mutationResult[i]=true;
			
			}
			else
			{
				mutationResult[i]=false;
				if(racine.getAttribute("failures").equals("0"))
				{
					fail++;
				}
				else 
				{
					error++;
				}
			}
		}catch(final ParserConfigurationException | SAXException | IOException e) {
		    e.printStackTrace();	
		}
	}
	
	public void printFile()
	{
		
		for (int i=0; i<listFile.length; i++)
		{
			System.out.println(listFile[i]);
		}
		
	}
	
	public void printReport()
	{
		for (int i=0; i<listReport.size(); i++)
		{
			System.out.println(listReport.get(i));
		}
	}
	
	public void generateReport()
	{
//		WriteXml w=new WriteXml(listReport.size(), nbMutakill ,dest);
//		w.createXml();
		//(List<String> mutaName,boolean[] mutaResult,int nbKilled)
		
		this.readAll();
		WriteHtml w=new WriteHtml(listMutation,mutationResult,nbMutakill,fail,error,mutation);
		
		w.WriteFile();
		w.writeAllMutation();
	
	}
	
	

	
}
