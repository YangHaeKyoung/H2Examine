package com.people.model;

import org.apache.ibatis.type.Alias;

@Alias("people")
public class People {
	private String firstName;
	private String lastName;
	private String ssn;

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getSsn() {
		return ssn;
	}
}