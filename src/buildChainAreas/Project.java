package buildChainAreas;

public class Project {
	
	private String name;
	
	private String area;
	
	private String branch;
	
	private String tag;
	
	public Project() {
		this.name = null;
		this.area = null;
		this.branch = null;
		this.tag = null;
	}

	
	public Project(String name, String area, String branch, String tag) {
		this.name = name;
		this.area = area;
		this.branch = branch;
		this.tag = tag;		
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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
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
		sb.append("  Tag:" + getTag());
		sb.append("\n");		
		return sb.toString();
	}
}
