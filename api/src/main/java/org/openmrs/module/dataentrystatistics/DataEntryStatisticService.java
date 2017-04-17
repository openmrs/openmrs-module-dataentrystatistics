/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.dataentrystatistics;

import java.util.Date;
import java.util.List;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.dataentrystatistics.db.DataEntryStatisticDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * Contains all Service methods for managing DataEntryStatistics
 */
@Transactional
public interface DataEntryStatisticService extends OpenmrsService {
	
	/**
	 * Creates a list of data entry stats from <code>fromDate</code> to <code>toDate</code>
	 * EncounterUserColumn is a column in the encounter table like <code>creator</code>,
	 * <code>provider</code>, etc (defaults to creator) EncounterUserColumn is a column in the
	 * encounter table like <code>creator</code>, <code>orderer</code>, etc (defaults to orderer)
	 * 
	 * @param fromDate
	 * @param toDate
	 * @param encounterUserColumn
	 * @param orderUserColumn
	 * @param groupBy (optional)
	 * @return the list of DataEntryStatistics
	 */
	// Authorization?
	public List<DataEntryStatistic> getDataEntryStatistics(Date fromDate, Date toDate, String encounterUserColumn,
	                                                       String orderUserColumn, String groupBy);
	
	/**
	 * @return the dao
	 */
	public DataEntryStatisticDAO getDao();
}
