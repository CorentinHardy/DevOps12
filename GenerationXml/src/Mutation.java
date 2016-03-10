import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


public class Mutation {

	private String source;
	private String name;  // selecteur-mutation
	private List<String> test=new ArrayList<String>();
	private List<String> testSucceed=new ArrayList<String>();
	private List<String> testFailed=new ArrayList<String>();
	private List<String> testError=new ArrayList<String>();
	private boolean mortNee=false;
	
	public List<String> getTestSucceed()
	{
		return testSucceed;
	}
	public List<String> getTestFailed()
	{
		return testFailed;
	}
	public List<String> getTestError()
	{
		return testError;
	}
	public int isAlive() // 0 oui, 1 test error, 2 mort née
	{
		if(mortNee)
			return 2;
		if (testFailed.size()==0 && testError.size()==0)
			return 0;
		
		return 1;
					
	}
	
	public Mutation (String name,File rep)
	{
		this.name=name;
		this.source=rep+"/";
	}
	
	public Mutation(String name, Boolean m)
	{
		this.name=name;
			mortNee=m;
	}
	public boolean equals(Mutation m)
	{
		if ( this.name.equals(m.getName()))
		{
			return true;
		}
		
		return false;
	}
	
	public boolean equalsString(String  mName)
	{
		if ( this.name.equals(mName))
		{
			return true;
		}
		
		return false;
	}

	
	public String getName() {
		return this.name;
	}
	
	public void addTest(String nameTest)
	{
		if( ! test.contains(nameTest))
		{
			test.add(nameTest);
			readResultTest(nameTest);
		}
	}
	
	public void readResultTest(String nameTest){
		

		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		int i=test.indexOf(nameTest);
		try{
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document= builder.parse(new File(source+test.get(i)));
			//listMutation.add(listReport.get(i).substring(5,listReport.get(i).length()-4));
			final Element racine = document.getDocumentElement();
			if( ((racine.getAttribute("failures").equals("0"))) && (racine.getAttribute("errors").equals("0")))
			{
				testSucceed.add(nameTest);
			}
			
			
			
			else
			{
				if(!racine.getAttribute("failures").equals("0"))
				{
					testFailed.add(test.get(i));
				}
				else 
				{
					testError.add(test.get(i));
				}
			}
		}catch(final ParserConfigurationException | SAXException | IOException e) {
		    e.printStackTrace();	
		}
		
	}
}

