
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



public class GitSubmissionXMLCreator {

	List<Submission> myData;
	String filePath;
	Document dom;

	private String strCommit;
	private String strAuthor;
	private String strEmail;
	private String strComment;
	private String strFile;
	private String strDate;
	
	
	public GitSubmissionXMLCreator(String filePath) {
		//create a list to hold the data
		this.filePath=filePath;
		myData = new ArrayList<Submission>();
		//initialize private string to help create submission object
		strCommit = new String();
		strAuthor = new String();
		strEmail = new String();
		strComment = new String();
		strFile = new String();
		strDate = new String();
		
		//initialize the list
		loadFile(filePath);
		
		Iterator<Submission> it  = myData.iterator();
		while(it.hasNext()) {
			Submission item = (Submission)it.next();
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
		
		Pattern patternCommit = Pattern.compile("^commit\\s(.*?)$");
		Matcher matcherCommit = patternCommit.matcher(strLine);

		Pattern patternAuthor = Pattern.compile("^Author:\\s(.*?)\\s<(.*?)>");
		Matcher matcherAuthor = patternAuthor.matcher(strLine);
		
		Pattern patternDate = Pattern.compile("^Date:\\s*(.*?)$");
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
			myData.add(new Submission(strCommit, strAuthor, strEmail, strDate, strComment, file_vector));
		} else {
			myData.add(new Submission(strCommit, strAuthor, strEmail, strDate, strComment, null));
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
		Element rootEle = dom.createElement("Submissions");
		dom.appendChild(rootEle);

		Iterator<Submission> it  = myData.iterator();
		while(it.hasNext()) {
			Submission item = (Submission)it.next();
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
	private Element createSubmissionElement(Submission item){
		
		Element submissionEle = dom.createElement("Submission");
		submissionEle.setAttribute("Commit", item.getCommit());

		Element authEle = dom.createElement("Author");
		Text authText = dom.createTextNode(item.getAuthor());
		authEle.appendChild(authText);
		submissionEle.appendChild(authEle);

		Element emailEle = dom.createElement("Email");
		Text emailText = dom.createTextNode(item.getEmail());
		emailEle.appendChild(emailText);
		submissionEle.appendChild(emailEle);

		Element dateEle = dom.createElement("Date");
		Text dateText = dom.createTextNode(item.getDate());
		dateEle.appendChild(dateText);
		submissionEle.appendChild(dateEle);
		
		Element commentEle = dom.createElement("Comment");
		Text commentText = dom.createTextNode(item.getComment());
		commentEle.appendChild(commentText);
		submissionEle.appendChild(commentEle);

		Element filesEle = dom.createElement("Files");
		
		if (item.getFiles() != null) {
			Iterator<String> itr=item.getFiles().iterator();
			while (itr.hasNext())
			{
				Element fileEle=dom.createElement("File");
				Text fileText = dom.createTextNode(itr.next().toString());
				fileEle.appendChild(fileText);
				filesEle.appendChild(fileEle);
			}
			submissionEle.appendChild(filesEle);
		}
		
		return submissionEle;
	}

	/**
	 * This method uses Xerces specific classes
	 * prints the XML document to file.
     */
	private void printToFile(){

		try
		{
			//print
			OutputFormat format = new OutputFormat(dom);
			format.setIndenting(true);

			//to generate output to console use this serializer
			//XMLSerializer serializer = new XMLSerializer(System.out, format);

			//to generate a file output use fileoutputstream instead of system.out
			XMLSerializer serializer = new XMLSerializer(
			new FileOutputStream(new File("submission.xml")), format);

			serializer.serialize(dom);

		} catch(IOException ie) {
		    ie.printStackTrace();
		}
	}

	public void runCreator(){
		System.out.println("Started .. ");
		createDOMTree();
		printToFile();
		System.out.println("Generated file successfully.");
	}

	
	public static void main(String args[]) {
		//create an instance
		for(int i=0; i < args.length; i++){
		    System.out.println( args[i] );
		  }
		//GitSubmissionXMLCreator creator = new GitSubmissionXMLCreator("C:\\Users\\Mency\\Documents\\git\\BuildXML\\src\\commit.log");
		GitSubmissionXMLCreator creator = new GitSubmissionXMLCreator("/home/mencyw/git/BuildXML/commit.log");
		//run the example
		creator.runCreator();
	}
}

