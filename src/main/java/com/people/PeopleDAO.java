package com.people;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

@Repository("peopleDao")
public class PeopleDAO {
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	public List<PeopleInfo> getPeopleWithLoginInfo() {
		return this.namedJdbcTemplate.getJdbcOperations().query(
			"select * from people_with_logins",
			new RowMapper<PeopleInfo>() {
				public PeopleInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					PeopleInfo info = new PeopleInfo();
					info.setFirstName(rs.getString("first_name"));
					info.setLastName(rs.getString("last_name"));
					info.setSsn(rs.getString("ssn"));
					return info;
				}
			});
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	private static Map<String, Object> createBatchUpsertValuesMap(PeopleInfo info) {
		Map<String, Object> valMap = Maps.newHashMap();
		valMap.put("first", info.getFirstName());
		valMap.put("last", info.getLastName());
		valMap.put("ssn", info.getSsn());
		return valMap;
	}

	public void batchUpsert(final List<PeopleInfo> pInfo) {
		doUpsert(pplListToSqlValuesMapArray(pInfo));
	}

	public void batchUpsertInTx(final List<PeopleInfo> pInfo) {
		doUpsertInTx(pplListToSqlValuesMapArray(pInfo));
	}

	//---
	// private utility methods
	//---
	private static Map<String, ?>[] pplListToSqlValuesMapArray(List<PeopleInfo> peopleInfos) {
		Map<String, ?>[] valuesMaps = new Map[peopleInfos.size()];
		for (int i = 0; i < peopleInfos.size(); i++) {
			valuesMaps[i] = createBatchUpsertValuesMap(peopleInfos.get(i));
		}
		return valuesMaps;
	}

	private void doUpsert(Map<String, ?>[] valuesMap) {
		int[] ids = this.namedJdbcTemplate.batchUpdate(
			"INSERT INTO people(first_name, last_name, ssn) VALUES (:first, :last, :ssn)",
			valuesMap);
	}

	@Transactional
	void doUpsertInTx(Map<String, ?>[] valuesMap) {
		int[] ids = this.namedJdbcTemplate.batchUpdate(
			"INSERT INTO people(first_name, last_name, ssn) VALUES (:first, :last, :ssn)",
			valuesMap);
	}
}