package com.mintstreet.common.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.mintstreet.common.entity.Domain;
import com.mintstreet.common.util.Filter;

public class GenericDao<K, E extends Domain<?>> extends JpaDaoSupport implements Serializable {
	
	private static final Logger log = LogManager.getLogger(GenericDao.class.getName());
	
	@Autowired
	JpaTransactionManager txm;
	
	@PersistenceContext
	protected EntityManager  entityManager;
	
	private static final long serialVersionUID = 1L;

	protected JpaTemplate getJpaTemplateEx() throws SQLException {
		JpaTemplate template = null;
			template = getJpaTemplate();

		if (template == null) {
			throw new SQLException("failed to acquire JpaTemplate.");
		}

		return template;
	}

	
	public int executeUpdateNative(String query) throws SQLException, NoResultException {
		return executeUpdateNative(query, null);
	}
	public int executeUpdate(String query) throws SQLException {
		return executeUpdate(query, null);
	}
	public int executeUpdate(String query, String paramName, Object paramValue) throws SQLException {
		Query jpaQuery = entityManager.createQuery(query);
		jpaQuery.setParameter(paramName, paramValue);
		return jpaQuery.executeUpdate();
	}

	public int executeUpdate(String query, Map<String, Object> params) throws SQLException {
		Query jpaQuery = entityManager.createQuery(query);
		if (params != null) {
			for (Entry<String, Object> param : params.entrySet()) {
				jpaQuery.setParameter(param.getKey(), param.getValue());
			}

		}
		return jpaQuery.executeUpdate();
	}

	public int executeUpdateNative(String query, List<Object> params) throws SQLException, NoResultException {
		
		TransactionDefinition td = new DefaultTransactionDefinition();
		TransactionStatus ts = txm.getTransaction(td); 
		
		try{
			Query jpaQuery = entityManager.createNativeQuery(query);
			if(params!=null){
				for (int i = 0; i < params.size(); i++) {
					jpaQuery.setParameter(i + 1, params.get(i));
				}
			}
			jpaQuery.executeUpdate();
			entityManager.flush();
			txm.commit(ts);
			return 1;
		} catch(NullPointerException ne ){
			logger.info("Generic Dao LN 86 exception ::", ne);
			txm.rollback(ts);
			return 0;
		} catch(Exception e ){
			logger.info("Generic Dao LN 90 exception ::", e);
			txm.rollback(ts);
			return 0;
		}
		
		
	}

	public E findById(K id, Class<E> klass) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
		return entityManager.find(klass, id); 
	}

	@SuppressWarnings("unchecked")
	public List<E> findByNamedQuery(String name) throws NoResultException {
		Query query = entityManager.createNamedQuery(name);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<E> findByNamedQuery(String name, int first, int pageSize) throws SQLException, NoResultException {
		Query query = entityManager.createNamedQuery(name);
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<E> findByNamedQuery(String name, Map<String, Object> params) throws NoResultException {
		Query query = entityManager.createNamedQuery(name);
		if(params!=null){
			for (Entry<String, Object> param : params.entrySet()) {
				query.setParameter(param.getKey(), param.getValue());
			}
		}

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<E> findByNamedQuery(String name, Map<String, Object> params, int first, int pageSize) throws NoResultException, SQLException {
		Query query = entityManager.createNamedQuery(name);
		if(params!=null){
			for (Entry<String, Object> param : params.entrySet()) {
				query.setParameter(param.getKey(), param.getValue());
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<E> findByNamedQuery(String name, String param, Object value) throws SQLException, NoResultException {
		Query query = entityManager.createNamedQuery(name);
		query.setParameter(param, value);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<E> findByNamedQuery(String name, String param, Object value, int first, int pageSize) throws SQLException, NoResultException {
		Query query = entityManager.createNamedQuery(name);
		query.setParameter(param, value);

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return query.getResultList();
	}

	@SuppressWarnings("rawtypes")
	public List findByNamedQueryRaw(String name) throws SQLException, NoResultException {
		Query query = entityManager.createNamedQuery(name);
		return query.getResultList();
	}

	@SuppressWarnings("rawtypes")
	public List findByNamedQueryRaw(String name, int first, int pageSize) throws SQLException, NoResultException {
		Query query = entityManager.createNamedQuery(name);

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return query.getResultList();
	}

	@SuppressWarnings("rawtypes")
	public List findByNamedQueryRaw(String name, Map<String, Object> params) throws SQLException, NoResultException {
		Query query = entityManager.createNamedQuery(name);
		if(params!=null){
			for (Entry<String, Object> param : params.entrySet()) {
				query.setParameter(param.getKey(), param.getValue());
			}
		}

		return query.getResultList();
	}

	@SuppressWarnings("rawtypes")
	public List findByNamedQueryRaw(String name, Map<String, Object> params, int first, int pageSize) throws SQLException, NoResultException {
		Query query = entityManager.createNamedQuery(name);
		if(params!=null){
			for (Entry<String, Object> param : params.entrySet()) {
				query.setParameter(param.getKey(), param.getValue());
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return query.getResultList();
	}

	@SuppressWarnings("rawtypes")
	public List findByNamedQueryRaw(String name, String param, Object value) throws SQLException, NoResultException {
		Query query = entityManager.createNamedQuery(name);
		query.setParameter(param, value);
		return query.getResultList();
	}

	@SuppressWarnings("rawtypes")
	public List findByNamedQueryRaw(String name, String param, Object value, int first, int pageSize) throws SQLException, NoResultException {
		Query query = entityManager.createNamedQuery(name);
		query.setParameter(param, value);

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return query.getResultList();
	}

	public List<? extends Object> findByNativeQuery(String qry) throws NoResultException {
		return findByNativeQuery(qry, null);
	}

	@SuppressWarnings("unchecked")
	public List<? extends Object> findByNativeQuery(String qry, List<Object> params) {
		try {
			Query query = entityManager.createNativeQuery(qry);
			if (params != null && params.size() > 0) {
				for (int i = 0; i < params.size(); i++) {
					query.setParameter(i + 1, params.get(i));
				}
			}
			return query.getResultList();
		} catch (NullPointerException ne) {
			logger.info("GenericDao.java LNo : 235 : Exception Caught", ne);
		} catch (Exception e) {
				logger.info("GenericDao.java LNo : 219 : Exception Caught",e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<E> findBySortAndFilter(int first, int pageSize, String sortField, boolean sortOrder, List<Filter> filters,
			int rowCount[], String className, String baseQuery, Map<String, Object> additionalParams, String countQuery) throws NoResultException, SQLException {

		StringBuilder query = new StringBuilder();
		boolean where = false;
		boolean whereTemp = false;

		int baseQueryFromIndex = baseQuery.toLowerCase().indexOf("from");
		if (countQuery != null) {
			query.append(countQuery);
			where = countQuery.toLowerCase().indexOf("where") > -1;
		} else {
			query.append("Select count(distinct ");
			query.append(className);
			query.append(" ) ");
			query.append(baseQuery.substring(baseQueryFromIndex));
			where = baseQuery.toLowerCase().indexOf("where") > -1;
		}

		whereTemp = where;

		for (Filter filter : filters) {
			if (filter.getValue() == null || filter.getValue() == "") {
				continue;
			}

			if (!where) {
				query.append(" where ");
				where = true;
			} else {
				query.append(" and ");
			}
			query.append(className);
			query.append(".");
			query.append(filter.getName());

			switch (filter.getType()) {
			case Contains:
			case StartsWith:
				query.append(" like :");
				break;
			case Equals:
				query.append(" = :");
				break;
			case LessThan:
				query.append(" < :");
				break;
			case GreaterThan:
				query.append(" > :");
				break;
			case LessThanEqualTo:
				query.append(" <= :");
				break;
			case GreaterThanEqualto:
				query.append(" >= :");
				break;
			}

			query.append(filter.getName().replace('.', '_'));
		}

		Query jpaQuery = entityManager.createQuery(query.toString());
		for (Filter filter : filters) {
			if (filter.getValue() == null || filter.getValue() == "") {
				continue;
			}

			String name = filter.getName().replace('.', '_');
			switch (filter.getType()) {
			case Contains:
				jpaQuery.setParameter(name, "%" + filter.getValue() + "%");
				break;
			case StartsWith:
				jpaQuery.setParameter(name, filter.getValue() + "%");
				break;
			case Equals:
				jpaQuery.setParameter(name, filter.getValue());
				break;
			case LessThan:
				jpaQuery.setParameter(name, filter.getValue());
				break;
			case GreaterThan:
				jpaQuery.setParameter(name, filter.getValue());
				break;
			case LessThanEqualTo:
				jpaQuery.setParameter(name, filter.getValue());
				break;
			case GreaterThanEqualto:
				jpaQuery.setParameter(name, filter.getValue());
				break;
			}
		}

		if (additionalParams != null && additionalParams.size() > 0) {
			for (Entry<String, Object> param : additionalParams.entrySet()) {
				jpaQuery.setParameter(param.getKey(), param.getValue());
			}
		}
		rowCount[0] = ((Long) jpaQuery.getSingleResult()).intValue();

		if (rowCount[0] == 0) {
			return new ArrayList<E>();
		}

		where = whereTemp;

		query.setLength(0);
		query.append(baseQuery);

		for (Filter filter : filters) {
			if (filter.getValue() == null || filter.getValue() == "") {
				continue;
			}

			if (!where) {
				query.append(" where ");
				where = true;
			} else {
				query.append(" and ");
			}
			query.append(className);
			query.append(".");
			query.append(filter.getName());

			switch (filter.getType()) {
			case Contains:
			case StartsWith:
				query.append(" like :");
				break;
			case Equals:
				query.append(" = :");
				break;
			case LessThan:
				query.append(" < :");
				break;
			case GreaterThan:
				query.append(" > :");
				break;
			case LessThanEqualTo:
				query.append(" <= :");
				break;
			case GreaterThanEqualto:
				query.append(" >= :");
				break;
			}

			query.append(filter.getName().replace('.', '_'));
		}

		if (sortField != null) {
			query.append(" order by ").append(className).append(".");
			query.append(sortField);
			query.append(sortOrder ? " asc " : " desc ");
		}

		jpaQuery = entityManager.createQuery(query.toString());
		for (Filter filter : filters) {
			if (filter.getValue() == null || filter.getValue() == "") {
				continue;
			}

			String name = filter.getName().replace('.', '_');

			switch (filter.getType()) {
			case Contains:
				jpaQuery.setParameter(name, "%" + filter.getValue() + "%");
				break;
			case StartsWith:
				jpaQuery.setParameter(name, filter.getValue() + "%");
				break;
			case Equals:
				jpaQuery.setParameter(name, filter.getValue());
				break;
			case LessThan:
				jpaQuery.setParameter(name, filter.getValue());
				break;
			case GreaterThan:
				jpaQuery.setParameter(name, filter.getValue());
				break;
			case LessThanEqualTo:
				jpaQuery.setParameter(name, filter.getValue());
				break;
			case GreaterThanEqualto:
				jpaQuery.setParameter(name, filter.getValue());
				break;
			}
		}

		if (additionalParams != null && additionalParams.size() > 0) {
			for (Entry<String, Object> param : additionalParams.entrySet()) {
				jpaQuery.setParameter(param.getKey(), param.getValue());
			}
		}

		jpaQuery.setFirstResult(first);
		jpaQuery.setMaxResults(pageSize);

		return jpaQuery.getResultList();
	}

	public void flush() throws TransactionRequiredException, PersistenceException {
		entityManager.flush();
	}

	public E merge(E entity) throws IllegalArgumentException, TransactionRequiredException{
		return entityManager.merge(entity);
	}

	public void persist(E entity) throws IllegalArgumentException, TransactionRequiredException {
		entityManager.persist(entity);
	}

	public void refresh(E entity) throws IllegalArgumentException, TransactionRequiredException, EntityNotFoundException {
		entityManager.refresh(entity);
	}

	public void remove(E entity) throws IllegalArgumentException, TransactionRequiredException {
		entityManager.remove(entity);
	}

	public Object getSingleResult(String name) throws SQLException, NoResultException {
		Query query = entityManager.createNamedQuery(name);
		return query.getSingleResult();
	}

	public Object getSingleResult(String name, String param, Object value) throws NoResultException, SQLException {
		Query query = entityManager.createNamedQuery(name);
		query.setParameter(param, value);
		return query.getSingleResult();
	}

	public Object getSingleResult(String name, Map<String, Object> params) throws NoResultException, SQLException {
		Query query = entityManager.createNamedQuery(name);
		if(params!=null){
			for (Entry<String, Object> param : params.entrySet()) {
				query.setParameter(param.getKey(), param.getValue());
			}
		}

		return query.getSingleResult();
	}

	public Object findSingleResult(String name) throws SQLException, NoResultException {
		Query jpaQuery = entityManager.createNamedQuery(name);
		return ((Object) jpaQuery.getSingleResult());
	}

	public Object findSingleResult(String name, String param, Object value) throws SQLException, NoResultException {
		Query query = entityManager.createNamedQuery(name);
		query.setParameter(param, value);
		return ((Object) query.getSingleResult());
	}
	public E save(Integer id, E obj){
		try {
			
			log.info("id "+id);
			log.info("obj "+obj);
			
			TransactionDefinition td = new DefaultTransactionDefinition();
			TransactionStatus ts = txm.getTransaction(td); 
			
			try{
				if(id != null && id > 0 ){
					entityManager.merge(obj);
				}else{
					entityManager.persist(obj);
				}
				
				entityManager.flush();
				txm.commit(ts);
			} catch (NullPointerException ne) {
				log.info("exception", ne);
				ne.printStackTrace();
				log.info("exception",ne.getMessage());
				logger.info("GenericDao.java LNo : 511 : Exception Caught", ne);
				txm.rollback(ts);
				return null;
			} catch(Exception e ){
				log.info("exception", e);
				e.printStackTrace();
				log.info("exception", e.getMessage());
				logger.info("Generic Dao LN 513 exception ::", e);
				txm.rollback(ts);
				return null;
			}
			
			return obj;
		} catch (NullPointerException ne) {
			logger.info("Generic Dao LN 522 exception ::", ne);
			return null;
		} catch (Exception e) {
			logger.info("Generic Dao LN 492 exception ::", e);
			return null;
		}
	}
	
	//read clob data separately to avoid privacy violation error [14, 2] after DB TCPS implementation
	public String getClobData (String table, String clobColumn, String idColumn, Integer idVal) {
		
		String clobData = null;
		try {
			Query query = entityManager.createNativeQuery(
					"select DBMS_LOB.SUBSTR(" + clobColumn + ", 4000, 1) as part1, "
					+ " DBMS_LOB.SUBSTR(" + clobColumn + ", 4000, 4001)  as part2, "
					+ " DBMS_LOB.SUBSTR(" + clobColumn + ", 4000, 8001)  as part3 "
					+ " from " + table
					+ " where " + idColumn + " = :id"
					);
			
			query.setParameter("id", idVal);
			
			Object[] row = (Object[]) query.getSingleResult();
			
			String part1 = (String) row[0];
			String part2 = (String) row[1];
			String part3 = (String) row[2];
			
			clobData = 
					(part1 != null ? part1 : "") + 
					(part2 != null ? part2 : "") + 
					(part3 != null ? part3 : ""); 
			
		} catch (NullPointerException e) {
			log.info("NullPointerException occured while fetching clob data: " , e);
		} catch (Exception e) {
			log.info("Exception occured while fetching clob data: ",e);
		}
		return clobData;
	}
	
}
