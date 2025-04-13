package com.cs.uwindsor.group.project;

import java.io.Serializable;


public class SharedMovie implements Serializable {

	private static final long serialVersionUID = 4502704022224815582L;
	public String name = "None";
	public String list = "1";
	
	public void setname(String name) { this.name = name; }
	public void setlist(String list) { this.list = list; }
	
	public String getname() { return this.name; }
	public String getlist() { return this.list; }
	
}
