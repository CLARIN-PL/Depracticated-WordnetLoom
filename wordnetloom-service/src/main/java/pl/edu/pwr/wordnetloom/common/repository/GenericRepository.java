package pl.edu.pwr.wordnetloom.common.repository;

import pl.edu.pwr.wordnetloom.common.model.PaginatedData;
import pl.edu.pwr.wordnetloom.common.model.filter.PaginationData;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public abstract class GenericRepository<T> {

	protected abstract Class<T> getPersistentClass();

	protected abstract EntityManager getEntityManager();

	public T add(final T entity) {
		getEntityManager().persist(entity);
		return entity;
	}

	public T findById(final Long id) {
		if (id == null) {
			return null;
		}
		return getEntityManager().find(getPersistentClass(), id);
	}

	public void update(final T entity) {
		getEntityManager().merge(entity);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(final String orderField) {
		return getEntityManager().createQuery(
				"Select e From " + getPersistentClass().getSimpleName() + " e Order by e." + orderField)
				.getResultList();
	}

	public boolean alreadyExists(final String propertyName, final String propertyValue, final Long id) {
		final StringBuilder jpql = new StringBuilder();
		jpql.append("Select 1 From " + getPersistentClass().getSimpleName() + " e where e." + propertyName
				+ " = :propertyValue");
		if (id != null) {
			jpql.append(" and e.id != :id");
		}

		final Query query = getEntityManager().createQuery(jpql.toString());
		query.setParameter("propertyValue", propertyValue);
		if (id != null) {
			query.setParameter("id", id);
		}

		return query.setMaxResults(1).getResultList().size() > 0;
	}

	public boolean existsById(final Long id) {
		return getEntityManager()
				.createQuery("Select 1 From " + getPersistentClass().getSimpleName() + " e where e.id = :id")
				.setParameter("id", id)
				.setMaxResults(1)
				.getResultList().size() > 0;
	}

	@SuppressWarnings("unchecked")
	protected PaginatedData<T> findByParameters(final String clause, final PaginationData paginationData,
												final Map<String, Object> queryParameters, final String defaultSortFieldWithDirection) {
		final String clauseSort = "Order by e." + getSortField(paginationData, defaultSortFieldWithDirection);
		final Query queryEntities = getEntityManager().createQuery(
				"Select e From " + getPersistentClass().getSimpleName()
						+ " e " + clause + " " + clauseSort);
		applyQueryParametersOnQuery(queryParameters, queryEntities);
		applyPaginationOnQuery(paginationData, queryEntities);

		final List<T> entities = queryEntities.getResultList();

		return new PaginatedData<T>(countWithFilter(clause, queryParameters), entities);
	}

	private int countWithFilter(final String clause, final Map<String, Object> queryParameters) {
		final Query queryCount = getEntityManager().createQuery(
				"Select count(e) From " + getPersistentClass().getSimpleName() + " e " + clause);
		applyQueryParametersOnQuery(queryParameters, queryCount);
		return ((Long) queryCount.getSingleResult()).intValue();
	}

	private void applyPaginationOnQuery(final PaginationData paginationData, final Query query) {
		if (paginationData != null) {
			query.setFirstResult(paginationData.getFirstResult());
			query.setMaxResults(paginationData.getMaxResults());
		}
	}

	private String getSortField(final PaginationData paginationData, final String defaultSortField) {
		if (paginationData == null || paginationData.getOrderField() == null) {
			return defaultSortField;
		}
		return paginationData.getOrderField() + " " + getSortDirection(paginationData);
	}

	private String getSortDirection(final PaginationData paginationData) {
		return paginationData.isAscending() ? "ASC" : "DESC";
	}

	private void applyQueryParametersOnQuery(final Map<String, Object> queryParameters, final Query query) {
		for (final Entry<String, Object> entryMap : queryParameters.entrySet()) {
			query.setParameter(entryMap.getKey(), entryMap.getValue());
		}
	}
}