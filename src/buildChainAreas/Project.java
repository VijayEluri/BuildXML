package buildChainAreas;

public class Project {
	
	private String name;
	
	private String area;
	
	private String branch;
	
	private String timeStamp;
	
	public Project() {
		this.name = null;
		this.area = null;
		this.branch = null;
		this.timeStamp = null;
	}

	
	public Project(String name, String area, String branch, String timeStamp) {
		this.name = name;
		this.area = area;
		this.branch = branch;
		this.timeStamp = timeStamp;		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Project Details --\n");
		sb.append("  Name:" + getName());
		sb.append("\n");
		sb.append("  Area:" + getArea());
		sb.append("\n");
		sb.append("  Branch:" + getBranch());
		sb.append("\n");
		sb.append("  Time Stamp:" + getTimeStamp());
		sb.append("\n");		
		return sb.toString();
	}
}
