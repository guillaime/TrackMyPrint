package org.fontys.trackmyprint.database;

import org.fontys.trackmyprint.database.entities.Employee;
import org.fontys.trackmyprint.database.entities.Order;
import org.fontys.trackmyprint.database.entities.Phase;
import org.fontys.trackmyprint.database.entities.Product;
import org.fontys.trackmyprint.database.entities.ProductPhase;
import org.fontys.trackmyprint.database.entities.User;
import org.fontys.trackmyprint.utils.Throw;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

/**
 * Created by guido on 15-Dec-16.
 */
public final class FirebaseDatabase extends AbstractDatabaseImpl
{
	private final Object lock;
	private boolean initialized;
	private boolean deInitialized;

	public FirebaseDatabase()
	{
		this.lock = new Object();
		this.initialized = false;
		this.deInitialized = false;
	}

	@Override
	public void initialize()
			throws
			IllegalStateException
	{
		synchronized(this.lock)
		{
			Throw.when(IllegalStateException.class, this.initialized, "Database is already initialized");
			Throw.when(IllegalStateException.class, this.deInitialized, "Database is de-initialized");

			// TODO: initialize routine

			this.initialized = true;
		}
	}

	@Override
	public void deInitialize()
	{
		synchronized(this.lock)
		{
			if(!this.initialized || this.deInitialized)
			{
				return;
			}

			// TODO: de-initialize routine

			this.deInitialized = true;
		}
	}

	@Override
	public Employee createEmployee(Phase phase, String name)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return null;
	}

	@Override
	public Employee createEmployee(String name)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return null;
	}

	@Override
	public Order createOrder(User user, Order.OrderStatus orderStatus, Calendar orderDate,
							 Map<String, Product> products)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return null;
	}

	@Override
	public Order createOrder(User user, Order.OrderStatus orderStatus, Calendar orderDate)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return null;
	}

	@Override
	public Order createOrder(User user, Calendar orderDate, Map<String, Product> products)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return null;
	}

	@Override
	public Order createOrder(User user, Calendar orderDate)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return null;
	}

	@Override
	public Phase createPhase(String name)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return null;
	}

	@Override
	public Product createProduct(String name, String image, String description, int amount,
								 Order order, Map<String, ProductPhase> productPhases)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return null;
	}

	@Override
	public Product createProduct(String name, String image, String description, int amount,
								 Order order)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return null;
	}

	@Override
	public ProductPhase createProductPhase(Calendar startDate, Calendar endDate,
										   ProductPhase.ProductPhaseStatus productPhaseStatus,
										   Employee employee, Phase phase)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return null;
	}

	@Override
	public ProductPhase createProductPhase(Employee employee, Phase phase)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return null;
	}

	@Override
	public User createUser(Map<String, Order> orders)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return null;
	}

	@Override
	public User createUser()
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return null;
	}

	@Override
	public void remove(Employee employee)
			throws
			IllegalArgumentException,
			DatabaseException
	{

	}

	@Override
	public void remove(Order order)
			throws
			IllegalArgumentException,
			DatabaseException
	{

	}

	@Override
	public void remove(Phase phase)
			throws
			IllegalArgumentException,
			DatabaseException
	{

	}

	@Override
	public void remove(Product product)
			throws
			IllegalArgumentException,
			DatabaseException
	{

	}

	@Override
	public void remove(ProductPhase productPhase)
			throws
			IllegalArgumentException,
			DatabaseException
	{

	}

	@Override
	public void remove(User user)
			throws
			IllegalArgumentException,
			DatabaseException
	{

	}

	@Override
	public Set<Phase> getPhases()
	{
		return null;
	}

	@Override
	public Set<Employee> getEmployees()
	{
		return null;
	}

	@Override
	public Set<User> getUsers()
	{
		return null;
	}

	@Override
	public Set<Order> getOrders()
	{
		return null;
	}

	@Override
	protected void handleEmployeePhaseChanged(Employee employee)
	{

	}

	@Override
	protected void handleOrderProductAdded(Order order, Product product)
	{

	}

	@Override
	protected void handleOrderProductRemoved(Order order, Product product)
	{

	}

	@Override
	protected void handleProductPhaseAdded(Product product, Phase phase)
	{

	}

	@Override
	protected void handleProductPhaseRemoved(Product product, Phase phase)
	{

	}

	@Override
	protected void handleProductPhaseStartDateChanged(ProductPhase productPhase)
	{

	}

	@Override
	protected void handleProductPhaseEndDateChanged(ProductPhase productPhase)
	{

	}

	@Override
	protected void handleProductPhaseStatusChanged(ProductPhase productPhase)
	{

	}

	@Override
	protected void handleUserOrderAdded(User user, Order order)
	{

	}

	@Override
	protected void handleUserOrderRemoved(User user, Order order)
	{

	}
}
