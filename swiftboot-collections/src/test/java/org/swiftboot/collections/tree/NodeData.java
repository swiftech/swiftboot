package org.swiftboot.collections.tree;

/**
 * 
 * @author swiftech
 * @since 
 */
public class NodeData {

	private long id;
	private long pid;
	private String name;

	public NodeData(long id, long pid, String name) {
		this.id = id;
		this.pid = pid;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "NodeData{" + "id=" + id + ", pid=" + pid + ", name=" + name + '}';
	}
	
}
