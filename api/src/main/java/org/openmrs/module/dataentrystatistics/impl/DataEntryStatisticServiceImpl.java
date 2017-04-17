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
package org.openmrs.module.dataentrystatistics.impl;

import java.util.Date;
import java.util.List;

import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.dataentrystatistics.DataEntryStatistic;
import org.openmrs.module.dataentrystatistics.DataEntryStatisticService;
import org.openmrs.module.dataentrystatistics.db.DataEntryStatisticDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * Contains all Service methods for managing DataEntryStatistics
 */
@Transactional
public class DataEntryStatisticServiceImpl extends BaseOpenmrsService implements DataEntryStatisticService {
	
	protected DataEntryStatisticDAO dao;
	
	/**
	 * @see DataEntryStatisticService#getDataEntryStatistics(Date,Date,String,String,String)
	 */
	public List<DataEntryStatistic> getDataEntryStatistics(Date fromDate, Date toDate, String encounterUserColumn,
	                                                       String orderUserColumn, String groupBy) throws APIException {
		return dao.getDataEntryStatistics(fromDate, toDate, encounterUserColumn, orderUserColumn, groupBy);
	}

	/**
	 * @return the dao
	 */
	public DataEntryStatisticDAO getDao() {
		return dao;
	}

	/**
	 * @param dao the dao to set
	 */
	public void setDao(DataEntryStatisticDAO dao) {
		this.dao = dao;
	}
}
