package org.salespointframework.core.order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

/**
 * This class provides functionality to manage orders (<code>OrderEntry</code>)
 * in the system.
 * 
 * @author Thomas Dedek
 * @author hannesweisbach
 * @author Paul Henke
 * 
 */

public class PersistentOrderManager implements OrderManager {
	private EntityManagerFactory emf;

	/**
	 * Create a new <code>OrderManager</code>. For persistence, an
	 * <code>EntityManager</code> is created internally as required. The
	 * <code>Database.INSTANCE</code> has to be initialized first.
	 * 
	 */
	public PersistentOrderManager() {
		this.emf = Database.INSTANCE.getEntityManagerFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.order.IOrderManager#addOrder(org.
	 * salespointframework.core.order.OrderEntry)
	 */
	@Override
	public void addOrder(OrderEntry order) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(order);
		em.getTransaction().commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.order.IOrderManager#findOrder(org.
	 * salespointframework.core.order.OrderIdentifier)
	 */
	@Override
	public OrderEntry findOrder(OrderIdentifier orderIdentifier) {
		Objects.requireNonNull(orderIdentifier, "orderIdentifier");

		EntityManager em = emf.createEntityManager();

		TypedQuery<OrderEntry> q = em
				.createQuery(
						"SELECT o FROM OrderEntry o WHERE o.orderIdentifier == :orderIdentifier",
						OrderEntry.class);
		q.setParameter("orderIdentifier", orderIdentifier);

		return q.getSingleResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.salespointframework.core.order.IOrderManager#findOrders(org.joda.
	 * time.DateTime, org.joda.time.DateTime)
	 */
	@Override
	public Iterable<OrderEntry> findOrders(DateTime from, DateTime to) {
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");

		EntityManager em = emf.createEntityManager();

		TypedQuery<OrderEntry> q = em
				.createQuery(
						"SELECT o FROM OrderEntry o WHERE o.timeStamp BETWEEN :from and :to",
						OrderEntry.class);
		q.setParameter("from", from.toDate(), TemporalType.TIMESTAMP);
		q.setParameter("to", to.toDate(), TemporalType.TIMESTAMP);

		return SalespointIterable.from(q.getResultList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.order.IOrderManager#findOrders(org.
	 * salespointframework.core.order.OrderStatus)
	 */
	@Override
	public Iterable<OrderEntry> findOrders(OrderStatus status) {
		Objects.requireNonNull(status, "status");

		EntityManager em = emf.createEntityManager();

		TypedQuery<OrderEntry> q = em.createQuery(
				"SELECT o FROM OrderEntry o WHERE o.status == :status",
				OrderEntry.class);
		q.setParameter("status", status);

		return SalespointIterable.from(q.getResultList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.order.IOrderManager#remove(org.
	 * salespointframework.core.order.OrderIdentifier)
	 */
	@Override
	public OrderEntry remove(OrderIdentifier orderIdentifier) {
		Objects.requireNonNull(orderIdentifier, "orderIdentifier");

		EntityManager em = emf.createEntityManager();

		OrderEntry orderEntry = em.find(OrderEntry.class, orderIdentifier);

		em.getTransaction().begin();
		em.remove(orderEntry);
		em.getTransaction().commit();
		return orderEntry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.order.IOrderManager#update(org.
	 * salespointframework.core.order.OrderEntry)
	 */
	@Override
	public void update(OrderEntry orderEntry) {
		Objects.requireNonNull(orderEntry, "orderEntry");

		EntityManager em = emf.createEntityManager();

		OrderEntry oe = em.find(OrderEntry.class,
				orderEntry.getOrderIdentifier());

		if (oe == null)
			return;
		else {
			em.getTransaction().begin();
			em.merge(orderEntry);
			em.getTransaction().commit();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.salespointframework.core.order.IOrderManager#hasOpenOrders(org.salespointframework.core.users.UserIdentifier)
	 */
	@Override
	public boolean hasOpenOrders(UserIdentifier userIdentifier) {
		
		Objects.requireNonNull(userIdentifier, "userIdentifier");

		EntityManager em = emf.createEntityManager();

		TypedQuery<OrderEntry> q = em
				.createQuery(
						"SELECT o FROM OrderEntry o WHERE o.userIdentifier == :userIdentifier",
						OrderEntry.class);
		q.setParameter("userIdentifier", userIdentifier);

		for(OrderEntry oe : q.getResultList()) {
			if(oe.getOrderStatus() == OrderStatus.PROCESSING ||
					oe.getOrderStatus() == OrderStatus.OPEN ||
					oe.getOrderStatus() == OrderStatus.INITIALIZED) {
				return true;
			}		
		}
		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.salespointframework.core.order.IOrderManager#getOrders(org.salespointframework.core.users.UserIdentifier)
	 */
	@Override
	public Iterable<OrderEntry> getOrders(UserIdentifier userIdentifier) {
		
		Objects.requireNonNull(userIdentifier, "userIdentifier");

		EntityManager em = emf.createEntityManager();

		TypedQuery<OrderEntry> q = em
				.createQuery(
						"SELECT o FROM OrderEntry o WHERE o.userIdentifier == :userIdentifier",
						OrderEntry.class);
		q.setParameter("userIdentifier", userIdentifier);
		
		return SalespointIterable.from(q.getResultList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.salespointframework.core.order.IOrderManager#getOrders(org.salespointframework.core.users.UserIdentifier, org.joda.
	 * time.DateTime, org.joda.time.DateTime)
	 */
	@Override
	public Iterable<OrderEntry> getOrders(UserIdentifier userIdentifier,
			DateTime from, DateTime to) {
		
		Objects.requireNonNull(userIdentifier, "userIdentifier");
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");

		EntityManager em = emf.createEntityManager();

		TypedQuery<OrderEntry> q = em
				.createQuery(
						"SELECT o FROM OrderEntry o WHERE o.userIdentifier == :userIdentifier and o.timeStamp BETWEEN :from and :to",
						OrderEntry.class);
		q.setParameter("userIdentifier", userIdentifier);
		q.setParameter("from", from.toDate(), TemporalType.TIMESTAMP);
		q.setParameter("to", to.toDate(), TemporalType.TIMESTAMP);
		
		return SalespointIterable.from(q.getResultList());
	}

}
