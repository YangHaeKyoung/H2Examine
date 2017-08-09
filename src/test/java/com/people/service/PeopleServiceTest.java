package com.people.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.people.model.People;
import com.people.service.PeopleService;

@ContextConfiguration(locations = {"classpath:test-db-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class PeopleServiceTest {

	@Autowired
	private PeopleService pService;

	@Test
	public void testPeopleService() {
		pService.insertPeoples();
		List<People> pList = pService.selectPeoples();
		assertEquals(5, pList.size());
	}
}