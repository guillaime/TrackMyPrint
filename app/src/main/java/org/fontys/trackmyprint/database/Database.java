package org.fontys.trackmyprint.database;

import org.fontys.trackmyprint.database.entities.Employee;
import org.fontys.trackmyprint.database.entities.Order;
import org.fontys.trackmyprint.database.entities.Phase;
import org.fontys.trackmyprint.database.entities.Product;
import org.fontys.trackmyprint.database.entities.ProductPhase;
import org.fontys.trackmyprint.database.entities.User;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guido on 15-Dec-16.
 */
public final class Database implements DatabaseImpl
{
	private static final ReentrantLock INSTANCE_LOCK;
	private static Database INSTANCE;

	static
	{
		INSTANCE_LOCK = new ReentrantLock();
		INSTANCE = null;
	}

	private final DatabaseImpl databaseImpl;

	private Database()
	{
		this.databaseImpl = new FirebaseDatabaseImpl();
	}

	@Override
	public void initialize()
			throws
			IllegalStateException,
			DatabaseException
	{
		this.databaseImpl.initialize();
	}

	@Override
	public void deInitialize()
	{
		this.databaseImpl.deInitialize();
	}

	@Override
	public Employee createEmployee(Phase phase, String name)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return this.databaseImpl.createEmployee(phase, name);
	}

	@Override
	public Employee createEmployee(String name)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return this.databaseImpl.createEmployee(name);
	}

	@Override
	public Order createOrder(User user, Order.OrderStatus orderStatus, Calendar orderDate,
							 Map<String, Product> products)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return this.databaseImpl.createOrder(user, orderStatus, orderDate, products);
	}

	@Override
	public Order createOrder(User user, Order.OrderStatus orderStatus, Calendar orderDate)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return this.databaseImpl.createOrder(user, orderStatus, orderDate);
	}

	@Override
	public Order createOrder(User user, Calendar orderDate, Map<String, Product> products)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return this.databaseImpl.createOrder(user, orderDate, products);
	}

	@Override
	public Order createOrder(User user, Calendar orderDate)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return this.databaseImpl.createOrder(user, orderDate);
	}

	@Override
	public Phase createPhase(String name)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return this.databaseImpl.createPhase(name);
	}

	@Override
	public Product createProduct(String name, String image, String description, int amount,
								 Order order, Map<String, ProductPhase> productPhases)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return this.databaseImpl.createProduct(name, image, description, amount, order, productPhases);
	}

	@Override
	public Product createProduct(String name, String image, String description, int amount,
								 Order order)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return this.databaseImpl.createProduct(name, image, description, amount, order);
	}

	@Override
	public ProductPhase createProductPhase(Calendar startDate, Calendar endDate,
										   ProductPhase.ProductPhaseStatus productPhaseStatus,
										   Employee employee, Phase phase)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return this.databaseImpl.createProductPhase(startDate, endDate, productPhaseStatus, employee, phase);
	}

	@Override
	public ProductPhase createProductPhase(Employee employee, Phase phase)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return this.databaseImpl.createProductPhase(employee, phase);
	}

	@Override
	public User createUser(String userId, Map<String, Order> orders)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return this.databaseImpl.createUser(userId, orders);
	}

	@Override
	public User createUser(String userId)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return this.databaseImpl.createUser(userId);
	}

	@Override
	public void remove(Employee employee)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		this.databaseImpl.remove(employee);
	}

	@Override
	public void remove(Order order)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		this.databaseImpl.remove(order);
	}

	@Override
	public void remove(Phase phase)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		this.databaseImpl.remove(phase);
	}

	@Override
	public void remove(Product product)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		this.databaseImpl.remove(product);
	}

	@Override
	public void remove(ProductPhase productPhase)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		this.databaseImpl.remove(productPhase);
	}

	@Override
	public void remove(User user)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		this.databaseImpl.remove(user);
	}

	@Override
	public Map<String, Phase> getPhases()
	{
		return this.databaseImpl.getPhases();
	}

	@Override
	public Map<String, Employee> getEmployees()
	{
		return this.databaseImpl.getEmployees();
	}

	@Override
	public Map<String, User> getUsers()
	{
		return this.databaseImpl.getUsers();
	}

	@Override
	public Map<String, Order> getOrders()
	{
		return this.databaseImpl.getOrders();
	}

	@Override
	public Map<String, Product> getProducts()
	{
		return this.databaseImpl.getProducts();
	}

	@Override
	public Map<String, ProductPhase> getProductPhases()
	{
		return this.databaseImpl.getProductPhases();
	}

	public static void initializeInstance()
			throws
			IllegalStateException,
			DatabaseException
	{
		INSTANCE_LOCK.lock();
		try
		{
			getInstance().initialize();
		}
		finally
		{
			INSTANCE_LOCK.unlock();
		}
	}

	public static void deInitializeInstance()
	{
		INSTANCE_LOCK.lock();
		try
		{
			getInstance().deInitialize();
		}
		finally
		{
			INSTANCE_LOCK.unlock();
		}
	}

	public static Database getInstance()
	{
		INSTANCE_LOCK.lock();
		try
		{
			if(INSTANCE == null)
			{
				INSTANCE = new Database();
			}

			return INSTANCE;
		}
		finally
		{
			INSTANCE_LOCK.unlock();
		}
	}
}
