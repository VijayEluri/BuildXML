package buildincr;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.regex.*;

public class BuildNumberIncr {

	private String propertyFilePath;
	private String currentVersion;
	private String nextNightlyVersion;
	private String nextIncrVersion;
	
	private String versionSeparator = ".";
	private String sectionSeparator = "_";

	int majorVer;
	int minorVer;
	int currentNightlyVer;
	int currentIncrVer;

	String parameter;
	
	public BuildNumberIncr(String currVersion, String filePath) {
		propertyFilePath = filePath;		
		currentVersion = currVersion;
	}
	
	public String getPropertyFilePath() {
		return this.propertyFilePath;
	}
	
	public String getCurrentVersion() {
		return this.currentVersion;
	}
	
	public String getNextNightlyVersion() {
		return this.nextNightlyVersion;
	}
	
	public String getNextIncrVersion() {
		return this.nextIncrVersion;
	}
	
	
	private void incrementVersion() {
		int nextNightlyNightly;
		int nextIncrIncr;

		Pattern patternCurrentVersion = Pattern.compile("(\\d+).(\\d+)_(\\d+).(\\d+)");
		Matcher matcherCurrentVersion = patternCurrentVersion.matcher(this.currentVersion);
        if (matcherCurrentVersion.find()) {
			majorVer = Integer.parseInt(matcherCurrentVersion.group(1));
			minorVer = Integer.parseInt(matcherCurrentVersion.group(2));
			currentNightlyVer = Integer.parseInt(matcherCurrentVersion.group(3));
			currentIncrVer = Integer.parseInt(matcherCurrentVersion.group(4));
 	    } else {
 	    	System.out.println("Format of Current Version is incorrect.  Abort");
 	    	return;
 	    }
        
        nextNightlyNightly = currentNightlyVer + 1;
        nextIncrIncr = currentIncrVer + 1;
 	    	
        this.nextNightlyVersion = new String (majorVer + versionSeparator + minorVer + sectionSeparator + nextNightlyNightly  + versionSeparator + "0" );
        this.nextIncrVersion = new String (majorVer + versionSeparator + minorVer + sectionSeparator + currentNightlyVer  + versionSeparator + nextIncrIncr );
        
		System.out.println("Current version:" + this.currentVersion);
		System.out.println("Next Nightly Version:" + this.nextNightlyVersion);
		System.out.println("Next Incr Version:" + this.nextIncrVersion );
	}
	
	private void printToPropertyFile(String filePath){
        FileOutputStream out; // declare a file output object
        PrintStream p; // declare a print stream object
        try
        {
                out = new FileOutputStream(filePath);
                // Connect print stream to the output stream
                p = new PrintStream( out );
                p.println ("current.version=" + getCurrentVersion());
                p.println ("next.nightly.version=" + getNextNightlyVersion());
                p.println ("next.incremental.version=" + getNextIncrVersion());
                p.close();
        }
        catch (Exception e)
        {
                System.err.println ("Error writing to file");
        }
	}
	
	
	public void runIncrement() {
	
		incrementVersion();
		
		printToPropertyFile(getPropertyFilePath());
		
	}
	

	public static void main(String args[]) {
		//create an instance
		//BuildNumberIncr incrementor = new BuildNumberIncr("1.0_3.0","/home/mencyw/git/BuildXML/version.properties");
		BuildNumberIncr incrementor = new BuildNumberIncr(args[0],args[1]);
		//run the example
		incrementor.runIncrement();
	}
	
}
