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
package org.openmrs.module.dataentrystatistics.db.hibernate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Person;
import org.openmrs.User;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.dataentrystatistics.DataEntryStatistic;
import org.openmrs.module.dataentrystatistics.db.DataEntryStatisticDAO;

/**
 * Database methods for the DataEntryStatisticService
 */
public class HibernateDataEntryStatisticDAO implements DataEntryStatisticDAO {
	
	protected Log log = LogFactory.getLog(getClass());
	
	/**
	 * Hibernate session factory
	 */
	private SessionFactory sessionFactory;

	/**
	 * @see DataEntryStatisiticDAO#getDataEntryStatistics(Date, Date, String, String, String)
	 */
	@SuppressWarnings("unchecked")
	public List<DataEntryStatistic> getDataEntryStatistics(Date fromDate, Date toDate, String encounterColumn,
	                                                       String orderColumn, String groupBy) throws DAOException {
		
		// for all encounters, find user, form name, and number of entries
		
		// default userColumn to creator
		if (encounterColumn == null)
			encounterColumn = "creator";
		encounterColumn = encounterColumn.toLowerCase();
		
		List<DataEntryStatistic> ret = new ArrayList<DataEntryStatistic>();

		// data entry stats with extended info
		// check if there's anything else to group by
		if (groupBy == null)
			groupBy = "";
		if (groupBy.length() != 0)
			groupBy = "e." + groupBy + ", ";
		log.debug("GROUP BY IS " + groupBy);
		
		String hql = "select " + groupBy + "e." + encounterColumn + ", e.encounterType"
				+ ", e.form, count(distinct e.encounterId), count(o.obsId) " + "from Obs o right join o.encounter as e ";

		if(encounterColumn.equals("provider")) {
			hql = "select " + groupBy + "p.person"  + ", e.encounterType"
					+ ", e.form, count(distinct e.encounterId), count(o.obsId) " + "from Obs o right join o.encounter as e inner join e.encounterProviders as ep inner join ep.provider as p ";
		}

		if (fromDate != null || toDate != null) {
			String s = "where ";
			if (fromDate != null)
				s += "e.dateCreated >= :fromDate ";
			if (toDate != null) {
				if (fromDate != null)
					s += "and ";
				s += "e.dateCreated <= :toDate ";
			}
			hql += s;
		}
		
		//remove voided obs and encounters.
		if (fromDate != null || toDate != null) {
			hql += " and ";
		}
		else {
			hql += " where ";
		}
		hql += " e.voided = :voided and o.voided = :voided ";
		
		
		hql += "group by ";
		if (groupBy.length() > 0)
			hql += groupBy + " ";
		if(encounterColumn.equals("provider")) {
			hql += "p.person"  + ", e.encounterType, e.form ";
		}else {
			hql += "e." + encounterColumn + ", e.encounterType, e.form ";
		}
		Query q = getCurrentSession().createQuery(hql);
		if (fromDate != null)
			q.setParameter("fromDate", fromDate);
		if (toDate != null)
			q.setParameter("toDate", toDate);
		
		q.setParameter("voided", false);
		
		List<Object[]> l = q.list();
		for (Object[] holder : l) {
			DataEntryStatistic s = new DataEntryStatistic();
			int offset = 0;
			if (groupBy.length() > 0) {
				s.setGroupBy(holder[0]);
				offset = 1;
			}

			Object temp = holder[0 + offset];
			if (temp instanceof User)
				s.setUser(((User) temp).getPerson());
			else
				s.setUser((Person) holder[0 + offset]);
			EncounterType encType = ((EncounterType) holder[1 + offset]);
			Form form = ((Form) holder[2 + offset]);
			s.setEntryType(form != null ? form.getName() : (encType != null ? encType.getName() : "null"));
			int numEncounters = ((Number) holder[3 + offset]).intValue();
			int numObs = ((Number) holder[4 + offset]).intValue();
			s.setNumberOfEntries(numEncounters); // not sure why this comes out as a Long instead of an Integer
			log.debug("NEW Num encounters is " + numEncounters);
			s.setNumberOfObs(numObs);
			log.debug("NEW Num obs is " + numObs);
			ret.add(s);
		}
		
		// default userColumn to creator
		if (orderColumn == null)
			orderColumn = "creator";
		orderColumn = orderColumn.toLowerCase();
		
		// for orders, count how many were created. (should eventually count something with voided/changed)
		hql = "select o." + orderColumn + ", o.orderType.name, count(*) " + "from Order o ";
		if (fromDate != null || toDate != null) {
			String s = "where ";
			if (fromDate != null)
				s += "o.dateCreated >= :fromDate ";
			if (toDate != null) {
				if (fromDate != null)
					s += "and ";
				s += "o.dateCreated <= :toDate ";
			}
			hql += s;
		}
		
		//remove voided orders.
		if (fromDate != null || toDate != null) {
			hql += " and ";
		}
		else {
			hql += " where ";
		}
		hql += " o.voided = :voided ";
		
		hql += "group by o." + orderColumn + ", o.orderType.name ";
		q = getCurrentSession().createQuery(hql);
		if (fromDate != null)
			q.setParameter("fromDate", fromDate);
		if (toDate != null)
			q.setParameter("toDate", toDate);
		
		q.setParameter("voided", false);
		
		l = q.list();
		for (Object[] holder : l) {
			DataEntryStatistic s = new DataEntryStatistic();
			Object temp = holder[0];
			if (temp instanceof User)
				s.setUser(((User) temp).getPerson());
			else
				s.setUser((Person) temp);
			s.setEntryType((String) holder[1]);
			s.setNumberOfEntries(((Number) holder[2]).intValue()); // not sure why this comes out as a Long instead of an Integer
			s.setNumberOfObs(0);
			ret.add(s);
		}
		
		return ret;
	}

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * Gets the current hibernate session while taking care of the hibernate 3 and 4 differences.
	 * 
	 * @return the current hibernate session.
	 */
	private org.hibernate.Session getCurrentSession() {
		try {
			return sessionFactory.getCurrentSession();
		}
		catch (NoSuchMethodError ex) {
			try {
				Method method = sessionFactory.getClass().getMethod("getCurrentSession", null);
				return (org.hibernate.Session)method.invoke(sessionFactory, null);
			}
			catch (Exception e) {
				throw new RuntimeException("Failed to get the current hibernate session", e);
			}
		}
	}
}
