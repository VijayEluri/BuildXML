
package gitlogxml;

import java.io.*;
import java.util.regex.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import org.apache.xml.serialize.XMLSerializer;
import org.apache.xml.serialize.OutputFormat;



public class GitModificationXMLCreator {

	List<Modification> myData;
	String filePath;
	Document dom;

	private String strCommit;
	private String strAuthor;
	private String strEmail;
	private String strComment;
	private String strFile;
	private String strDate;
	
	
	public GitModificationXMLCreator(String filePath) {
		//create a list to hold the data
		this.filePath=filePath;
		myData = new ArrayList<Modification>();
		//initialize private string to help create submission object
		strCommit = new String();
		strAuthor = new String();
		strEmail = new String();
		strComment = new String();
		strFile = new String();
		strDate = new String();
		
		//initialize the list
		loadFile(filePath);
		
		Iterator<Modification> it  = myData.iterator();
		while(it.hasNext()) {
			Modification item = (Modification)it.next();
			String result=new String(item.toString());
			System.out.print(result);
		}
		//Get a DOM object
		createDocument();
	}

	private void loadFile(String filePath){
			try{
		    // command line parameter
		    FileInputStream fstream = new FileInputStream(filePath);
		    // Get the object of DataInputStream
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    
		    String strLine;
		    //Read File Line By Line
		    while ((strLine = br.readLine()) != null)   {
		      // Print the content on the console
		    	if (strLine.length() > 0) {
			      //System.out.println ("\tNew Read Line:"+strLine);
			      prepareData(strLine);		      
		    	}
		    }	
		    //after parsing file, call loadData one last time for the last instance
		    loadData();
		    //Close the input stream
		    in.close();
		    }catch (Exception e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage());
		    }
	}
	
	
	private void prepareData(String strLine){
		
		Pattern patternCommit = Pattern.compile("^commit\\s(..........).*$");
		Matcher matcherCommit = patternCommit.matcher(strLine);

		Pattern patternAuthor = Pattern.compile("^Author:\\s(.*?)\\s<(.*?)>");
		Matcher matcherAuthor = patternAuthor.matcher(strLine);
		
		Pattern patternDate = Pattern.compile("^Date:\\s*(.*?)\\s-.*$");
		Matcher matcherDate = patternDate.matcher(strLine);

		Pattern patternComment = Pattern.compile("^\\s\\s\\s\\s(.*?)$");
		Matcher matcherComment = patternComment.matcher(strLine);

		Pattern patternExtraNote = Pattern.compile("^(.*:.*?)$");
		Matcher matcherExtraNote = patternExtraNote.matcher(strLine);

		if (matcherCommit.find()) {
			// skip if it is the first element in the group
			if (strComment.isEmpty() != true) {
				loadData();
			}
		    strCommit = matcherCommit.group(1);
			//when the line is commit -- prepare for new entry
			strAuthor="";
			strEmail="";
			strComment="";
			strDate="";			
			strFile="";
		//	System.out.print("Commit found:"+strCommit+"\n");
		} else if (matcherAuthor.find()) {
			strAuthor = matcherAuthor.group(1);			
			strEmail = matcherAuthor.group(2);
		//	System.out.print("Author found:"+strAuthor+"\n");
		//	System.out.print("Email found:"+strEmail+"\n");
		} else if (matcherDate.find()) {
			strDate = matcherDate.group(1);			
		//	System.out.print("Date found:"+strDate+"\n");
		} else if (matcherComment.find()) {
			strComment = matcherComment.group(1);			
		//	System.out.print("Comment found:"+strComment+"\n");
		} else if (matcherExtraNote.find()) {			
		//	System.out.print("ExtraNote found:"+ matcherExtraNote.group(1)+"\n");
		} else  {
			//assuming file
			strFile = strFile.concat(strLine);
			strFile = strFile.concat(";");
		//	System.out.print("File found:"+ strFile+"\n");
		}	
	}
	
	/**
	 * Add a list of books to the list
	 * In a production system you might populate the list from a DB
	 */
	private void loadData(){
		
		String[] file_array;
		Vector<String> file_vector;

		if (strFile.length() > 0) {
			file_array = strFile.split(";");
			file_vector = new Vector<String>(Arrays.asList(file_array));
//public Submission(String commit,String author, String email, String date, String comment, Vector<String> files) 
			myData.add(new Modification(strCommit, strAuthor, strEmail, strDate, strComment, file_vector));
		} else {
			myData.add(new Modification(strCommit, strAuthor, strEmail, strDate, strComment, null));
		}

	}

	/**
	 * Using JAXP in implementation independent manner create a document object
	 * using which we create a xml tree in memory
	 */
	private void createDocument() {

		//get an instance of factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
		//get an instance of builder
		DocumentBuilder db = dbf.newDocumentBuilder();

		//create an instance of DOM
		dom = db.newDocument();

		}catch(ParserConfigurationException pce) {
			//dump it
			System.out.println("Error while trying to instantiate DocumentBuilder " + pce);
			System.exit(1);
		}

	}

	/**
	 * The real workhorse which creates the XML structure
	 */
	private void createDOMTree(){

		//create the root element <Books>
		Element rootEle = dom.createElement("modifications");
		dom.appendChild(rootEle);

		Iterator<Modification> it  = myData.iterator();
		while(it.hasNext()) {
			Modification item = (Modification)it.next();
			//For each Submission object  create <Submission> element and attach it to root
			Element submissionEle = createSubmissionElement(item);
			rootEle.appendChild(submissionEle);
		}
	}

	/**
	 * Helper method which creates a XML element <Book>
	 * @param b The book for which we need to create an xml representation
	 * @return XML element snippet representing a book
	 */
	private Element createSubmissionElement(Modification item){		
		
		Element modificationEle = dom.createElement("modification");
		modificationEle.setAttribute("type", "git");
		
		Element fileEle;
		Element revisionEle;
		Element filenameEle;
		Text revisionText;
		Text fileText;
			
			if (item.getFiles() != null) {
				Iterator<String> itr=item.getFiles().iterator();
				while (itr.hasNext())
				{
					fileEle = dom.createElement("file");
					fileEle.setAttribute("action", "modified");					
						revisionEle = dom.createElement("revision");
						revisionText = dom.createTextNode(item.getCommit());
						revisionEle.appendChild(revisionText);
					fileEle.appendChild(revisionEle);
						filenameEle=dom.createElement("filename");
						fileText = dom.createTextNode(itr.next().toString());
						filenameEle.appendChild(fileText);
					fileEle.appendChild(filenameEle);				
					modificationEle.appendChild(fileEle);
				}
			}

		Element dateEle = dom.createElement("date");
		Text dateText = dom.createTextNode(item.getDate());
		dateEle.appendChild(dateText);
		modificationEle.appendChild(dateEle);
		
		Element userEle = dom.createElement("user");
		Text authText = dom.createTextNode(item.getAuthor());
		userEle.appendChild(authText);
		modificationEle.appendChild(userEle);
		
		Element commentEle = dom.createElement("comment");
		Text commentText = dom.createTextNode(item.getComment());
		commentEle.appendChild(commentText);
		modificationEle.appendChild(commentEle);

		revisionEle = dom.createElement("revision");
		revisionText = dom.createTextNode(item.getCommit());
		revisionEle.appendChild(revisionText);
		modificationEle.appendChild(revisionEle);
		
		Element emailEle = dom.createElement("email");
		Text emailText = dom.createTextNode(item.getEmail());
		emailEle.appendChild(emailText);
		modificationEle.appendChild(emailEle);

		

		
		return modificationEle;
	}

	/**
	 * This method uses Xerces specific classes
	 * prints the XML document to file.
     */
	private void printToFile(String outputXML){

		try
		{
			//print
			OutputFormat format = new OutputFormat(dom);
			format.setIndenting(true);

			//to generate output to console use this serializer
			//XMLSerializer serializer = new XMLSerializer(System.out, format);

			//to generate a file output use fileoutputstream instead of system.out
			XMLSerializer serializer = new XMLSerializer(
			new FileOutputStream(new File(outputXML)), format);

			serializer.serialize(dom);

		} catch(IOException ie) {
		    ie.printStackTrace();
		}
	}

	public void runCreator(String outputXML){
		System.out.println("Started .. ");
		createDOMTree();
		printToFile(outputXML);
		System.out.println("Generated file successfully.");
	}

	
	public static void main(String args[]) {
		//create an instance
		if (args.length < 2) {
			System.out.println("Need argument on input and output. Abort");
			System.exit(-1);
		}
		String userTimezone = System.getProperty("user.timezone");
		System.out.println("user.timezone is"+userTimezone);
		GitModificationXMLCreator creator = new GitModificationXMLCreator(args[0].toString());
		
		creator.runCreator(args[1].toString());
	}
}

