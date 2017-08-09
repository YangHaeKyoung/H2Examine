package com.people.model;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("people")
public class People {
	private String firstName;
	private String lastName;
	private String ssn;
}