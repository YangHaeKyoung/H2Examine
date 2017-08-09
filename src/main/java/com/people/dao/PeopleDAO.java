package com.people.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.people.model.People;

@Repository
public interface PeopleDAO {

	int insert(People data);

	List<People> select(People pInfo);

}
