package gitlogxml;

import java.util.*;

public class Submission {
	
	private String commit;
	
	private String author;
	
	private String email;
	
	private String date;
	
	private String comment;
	
	private Vector<String> files;
	
	public Submission(String commit,String author, String email, String date, String comment, Vector<String> files) {
		this.commit = commit;
		this.author = author;
		this.email = email;
		this.date = date;
		this.comment = comment;
		this.files = files;
	}

	public String getCommit() {
		return commit;
	}

	public void setCommit(String commit) {
		this.comment = commit;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Vector<String> getFiles() {
		return files;
	}

	public void setFiles(Vector<String> files) {
		this.files=files;
	}
	
	public String printFiles(){
		
		if (this.files != null) {
			Iterator<String> itr=this.files.iterator();

			String filestring=new String();
			filestring="\n";
			
			while (itr.hasNext())
			{
				String elementString = new String();
				elementString=itr.next().toString();
				filestring=filestring.concat(elementString);
				filestring=filestring.concat("\n");
			}
			return filestring;
		} else {
			return new String("\n");
		}
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Submission Details --\n");
		sb.append("  Commit:" + getCommit());
		sb.append("\n");
		sb.append("  Author:" + getAuthor());
		sb.append("\n (" );
		sb.append(getEmail());
		sb.append(")\n");
		sb.append("  Date:" + getDate());
		sb.append("\n");
		sb.append("  Comment:" + getComment());
		sb.append("\n");
		sb.append("  Files:" + printFiles());
		sb.append("\n");
		
		return sb.toString();
	}
}
