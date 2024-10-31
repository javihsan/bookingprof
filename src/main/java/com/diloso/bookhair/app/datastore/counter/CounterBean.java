package com.diloso.bookhair.app.datastore.counter;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Bean representing a counter stored in datastore
 * @author aaranda
 *
 */
@Entity
public class CounterBean {
	
	@Id
	private String name;
	
	private long value;
	
	/**
	 * Default
	 */
	public CounterBean() {
	}

	/**
	 * @param name
	 * @param value
	 */
	public CounterBean(String name, long value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public long getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(long value) {
		this.value = value;
	}

}
