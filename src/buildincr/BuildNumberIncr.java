package buildincr;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.*;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

public class BuildNumberIncr {

	private String propertyFilePath;
	private String fullVersion;
	private boolean isNightly;

	public BuildNumberIncr(String filePath, String strIsNightly) {
		propertyFilePath = filePath;		
		isNightly = Boolean.parseBoolean(strIsNightly);
	}
	
	public String getPropertyFilePath() {
		return this.propertyFilePath;
	}
	
	public String getFullVersion() {
		return this.fullVersion;
	}
	
	public boolean getIsNightlyBuild() {
		return this.isNightly;
	}
	
	private void loadPropertyFile(String filePath){
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
			      fullVersion = strLine;
		    	}
		    }	
		    
		   
		    }catch (Exception e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage());
		    }
	}
	
	private void incrementVersion(String strVersion, boolean isNightlyBuild) {
		int majorVer;
		int minorVer;
		int nightlyVer;
		int incrVer;
		
		Pattern patternFullVersion = Pattern.compile("(\\d+).(\\d+).(\\d+).(\\d+)");
		Matcher matcherFullVersion = patternFullVersion.matcher(strVersion);
		
		if (matcherFullVersion.find()) {
			majorVer = Integer.parseInt(matcherFullVersion.group(1));			
			minorVer = Integer.parseInt(matcherFullVersion.group(2));
			nightlyVer = Integer.parseInt(matcherFullVersion.group(3));			
			incrVer = Integer.parseInt(matcherFullVersion.group(4));
		} else {
			System.out.println("Build Version does not meet expectation. Abort");
			return;
		}
		
		if (isNightlyBuild) {
			nightlyVer++;
			incrVer = 0;
		} else {
			incrVer++;
		}
		
		this.fullVersion = new String(majorVer + "." + minorVer + "." + nightlyVer + "." + incrVer);

		System.out.print("Old version:" + strVersion + "\nNew Version:" + this.fullVersion + "\nNightly Build:" + this.isNightly );
	}
	

	private void printToPropertyFile(String filePath){

        FileOutputStream out; // declare a file output object
        PrintStream p; // declare a print stream object

        try
        {
                out = new FileOutputStream(filePath);
                // Connect print stream to the output stream
                p = new PrintStream( out );
                p.println (getFullVersion());
                p.close();
        }
        catch (Exception e)
        {
                System.err.println ("Error writing to file");
        }
	}
	
	
	public void runIncrement() {
		loadPropertyFile(getPropertyFilePath());
		incrementVersion(getFullVersion(), getIsNightlyBuild());
		printToPropertyFile(getPropertyFilePath());
		
	}
	

	public static void main(String args[]) {
		//create an instance
		//BuildNumberIncr incrementor = new BuildNumberIncr(args[0],args[1]);
		BuildNumberIncr incrementor = new BuildNumberIncr("C:\\Users\\Mency\\Documents\\version.properties","false");
		//run the example
		incrementor.runIncrement();
	}
	
}
