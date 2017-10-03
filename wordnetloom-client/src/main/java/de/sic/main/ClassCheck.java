package de.sic.main;

import java.io.Serializable;

/**
 * A helper-class to compare the application names
 */
public class ClassCheck implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String className = null;
	
	public ClassCheck(String className) {
		setClassName(className);
	}
	
	public ClassCheck() {
	}

	public String toString() {
		return className;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
