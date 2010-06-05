package buildChainAreas;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class loadProjectList extends DefaultHandler {
	
	List<Project> chainProjectList;
	
	private Project tmpProject;
	
	private String tmpVal;
	

	
	public void runLoad() {
		parseXML();
		printData();
	}
	
	
	public loadProjectList() {
		chainProjectList = new ArrayList<Project>();
	}
	
	private void parseXML() {
	
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
		
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			
			//parse the file and also register this class for call backs
			sp.parse("/home/mencyw/git/BuildSystem/chain-def/indicee-master.xml", this);
			
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
				
		Iterator<Project> it = chainProjectList.iterator();
		while(it.hasNext()) {
			System.out.println(it.next().toString());
		}
	}
	
	private void gitPull(){
				
		Iterator<Project> it = chainProjectList.iterator();
		while(it.hasNext()) {
			try {
				// cd ${root.dir}
				// if [ -d $area ] cd $area; git pull origin
				// else git clone git@github.com:indicee/$area.git
				// cd $area; git checkout $branch
				//String command="git pull".concat();
				String command="";
				Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

	
	//Event Handlers
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {		
		//reset
		tmpVal = "";
		if(qName.equalsIgnoreCase("project")) {
			tmpProject = new Project();
			tmpProject.setName(attributes.getValue("name"));
		}
	}
	

	public void characters(char[] ch, int start, int length) throws SAXException {
		tmpVal = new String(ch,start,length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(qName.equalsIgnoreCase("project")) {
			//add it to the list
			chainProjectList.add(tmpProject);
		
		}else if (qName.equalsIgnoreCase("area")) {
			tmpProject.setArea(tmpVal);
		}else if (qName.equalsIgnoreCase("branch")) {
			tmpProject.setBranch(tmpVal);		
		}else if (qName.equalsIgnoreCase("timestamp")) {
			tmpProject.setTimeStamp(tmpVal);
		}
	}

	public static void main(String[] args){
		loadProjectList ex = new loadProjectList();
		ex.runLoad();
	}

}
