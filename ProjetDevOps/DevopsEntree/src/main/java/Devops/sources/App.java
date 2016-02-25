package Devops.sources;


public class App 
{
    public static void main( String[] args )
    {
    	retourneVal();
	unaireVal();
	
    }

        static int retourneVal(){
        int i=1;
		i= i + 1;
		return i;
        }
	static int unaireVal(){
	int i=1;
	i++;
	
		return i;	
	}
}
