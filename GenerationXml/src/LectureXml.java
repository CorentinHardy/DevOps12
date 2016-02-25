import java.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LectureXml {
	
	String racineMaven;
	String repReport="/target/surefire-reports";
	File rep;
	String[] listFile;
	List<String> listReport=new ArrayList<String>();
	int nbMutakill=0;
	
	private String dest="../mutation-testing-report.xml";
	public LectureXml(String racineMaven)
	{
		this.racineMaven="../"+racineMaven;
		this.rep=new File(this.racineMaven+repReport+"");
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
		}
		
		
	}
	public void readAll()
	{
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
			final Element racine = document.getDocumentElement();
			if( (!(racine.getAttribute("failures").equals("0"))) | !(racine.getAttribute("errors").equals("0")))
			{
				nbMutakill++;
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
		WriteXml w=new WriteXml(listReport.size(), nbMutakill ,dest);
		w.createXml();
	}
	
}