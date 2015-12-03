package br.univel.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import br.univel.model.Cliente;

/**
 *  DAO for Cliente
 */
@Stateless
public class ClienteDao {
	@PersistenceContext(unitName = "jaas-server-webapp-persistence-unit")
	private EntityManager em;

	public void create(Cliente entity) {
		em.persist(entity);
	}

	public void deleteById(Long id) {
		Cliente entity = em.find(Cliente.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public Cliente findById(Long id) {
		return em.find(Cliente.class, id);
	}

	public Cliente update(Cliente entity) {
		return em.merge(entity);
	}

	public List<Cliente> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Cliente> findAllQuery = em
				.createQuery("SELECT DISTINCT c FROM Cliente c ORDER BY c.id",
						Cliente.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}
}
