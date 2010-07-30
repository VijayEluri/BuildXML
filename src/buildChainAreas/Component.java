package buildChainAreas;

public class Component {
	
	private String area;
	private String branch;
	private String tag;
	private String tooling = new String("false");
	private String testing = new String("false");
	
	public Component() {
		this.area = null;
		this.branch = null;
		this.tag = null;
	}

	public Component(String area, String branch, String tag) {
		this.area = area;
		this.branch = branch;
		this.tag = tag;		
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

	public String getTooling() {
		return tooling;
	}

	public void setTooling(String tooling) {
		this.tooling = tooling;
	}

	public String getTesting() {
		return testing;
	}

	public void setTesting(String testing) {
		this.testing = testing;
	}

	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Project Details --\n");
		sb.append("  Area:" + getArea());
		sb.append("\n");
		sb.append("  Branch:" + getBranch());
		sb.append("\n");
		sb.append("  Tag:" + getTag());
		sb.append("\n");
		sb.append("  Tooling:" + getTooling());
		sb.append("\n");
		sb.append("  Testing:" + getTesting());
		sb.append("\n");		
		return sb.toString();
	}
	

	public String toFile() {
		StringBuffer sb = new StringBuffer();
		sb.append(getArea() + "|" + getBranch() + "|" + getTag() + "|" + getTooling() + "|" + getTesting());
		return sb.toString();
	}
}
