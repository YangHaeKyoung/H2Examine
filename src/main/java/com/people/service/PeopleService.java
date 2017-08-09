package com.people.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.people.dao.PeopleDAO;
import com.people.model.People;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PeopleService {

	@Autowired
	private PeopleDAO peopleDAO;

	public void insertPeoples() {
		String uniqTag = String.valueOf(System.nanoTime());

		log.debug("[START] >>>>>>>>" + uniqTag);

		for (int idx = 0; idx < 5; idx++) {
			People pInfo = new People();
			pInfo.setFirstName("firstname" + idx + uniqTag);
			pInfo.setLastName("lastname" + idx + uniqTag);
			pInfo.setSsn(UUID.randomUUID().toString().substring(0, 11));
			log.debug("[INSERT-" + idx + "] >>>>>>>>>>>>>>>>>>" + peopleDAO.insert(pInfo));
		}

		log.debug("[END] >>>>>>>>" + uniqTag);
	}

	public List<People> selectPeoples() {
		return peopleDAO.select(null);
	}
}