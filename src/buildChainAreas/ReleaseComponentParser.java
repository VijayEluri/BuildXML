package buildChainAreas;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class ReleaseComponentParser extends DefaultHandler{

	private static String defaultBranch = new String("master");
	private static String defaultTag = new String("latest");
	
	List<Component> lstComponents;
	List<String> lstAreas;
	
	private String fileName;
	private String outputFile;
	
	private String tempVal;
	
	//to maintain context
	private Component tempComponent;
	
	public ReleaseComponentParser(String newFile, String newOutput){
		fileName = newFile;
		outputFile =  newOutput;
		lstComponents = new ArrayList<Component>();
		lstAreas = new ArrayList<String>();
	}
	
	public void runExample() {
		parseDocument();
		printData();
	}

	private void parseDocument() {
		
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
		
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			
			//parse the file and also register this class for call backs
			sp.parse(fileName, this);
			
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	/**
	 * Iterate through the list and print
	 * the contents
	 */
	private void printData(){
		
//		
//		Iterator<Component> itr = lstProjects.iterator();
//		while(itr.hasNext()) {
//			System.out.println(itr.next().toString());
//		}
//		
		FileOutputStream outFile; // declare a file output object
        PrintStream p; // declare a print stream object

        try
        {
                // Create a new file output stream
                // connected to "myfile.txt"

        		System.out.println("No of Projects '" + lstComponents.size() + "'.");
        		
                outFile = new FileOutputStream(outputFile);
                // Connect print stream to the output stream
                p = new PrintStream( outFile );

        		Iterator<String> itrAreas = lstAreas.iterator();
        		String strAreas = new String();
        		while(itrAreas.hasNext()) {
        			strAreas = strAreas.concat((String)itrAreas.next()+ ",");
        		}        		
        		p.println("Areas_to_build" + "=" + strAreas);
                Iterator<Component> itr = lstComponents.iterator();
        		Component itrItem;
        		while(itr.hasNext()) {
        			itrItem = (Component)itr.next();
        			System.out.println(itrItem.toString());
        			p.println(itrItem.toFile());
        		}
                p.close();
        }
        catch (Exception e)
        {
                System.err.println ("Error writing to file");
        }
	}
	

	//Event Handlers
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//reset
		tempVal = "";
		if(qName.equalsIgnoreCase("Component")) {
			//create a new instance of employee
			tempComponent = new Component();
			tempComponent.setBranch(defaultBranch);
			tempComponent.setTag(defaultTag);
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(qName.equalsIgnoreCase("Component")) {
			//add it to the list
			lstComponents.add(tempComponent);			
		}else if (qName.equalsIgnoreCase("area")) {
			tempComponent.setArea(tempVal);
			lstAreas.add(tempVal);
		}else if (qName.equalsIgnoreCase("branch")) {
			tempComponent.setBranch(tempVal);
		}else if (qName.equalsIgnoreCase("tag")) {
			tempComponent.setTag(tempVal);
		}else if (qName.equalsIgnoreCase("tooling")) {
			tempComponent.setTooling(tempVal);
		}else if (qName.equalsIgnoreCase("testing")) {
			tempComponent.setTesting(tempVal);
		}
	}
	
	public static void main(String[] args){
		ReleaseComponentParser spe = new ReleaseComponentParser("C:/Users/Mency/Documents/git/BuildXML/src/buildChainAreas/release.xml","C:/Users/Mency/Documents/git/BuildXML/src/buildChainAreas/output.txt");
		spe.runExample();
	}
	
}




