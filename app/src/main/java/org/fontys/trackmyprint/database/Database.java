package org.fontys.trackmyprint.database;

import android.content.Context;
import android.widget.ImageView;

import org.fontys.trackmyprint.database.entities.Employee;
import org.fontys.trackmyprint.database.entities.Entity;
import org.fontys.trackmyprint.database.entities.Order;
import org.fontys.trackmyprint.database.entities.Phase;
import org.fontys.trackmyprint.database.entities.Product;
import org.fontys.trackmyprint.database.entities.ProductPhase;
import org.fontys.trackmyprint.database.entities.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
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
	public Employee createEmployee(String id, Phase phase, String name, String lastCheckedInDate)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return this.databaseImpl.createEmployee(id, phase, name, lastCheckedInDate);
	}

	@Override
	public Employee createEmployee(String id, String name)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return this.databaseImpl.createEmployee(id, name);
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
								 Order order, String imageBack, String imageFront, int marginBottom,
								 int marginLeft, int marginRight, int marginTop, String paperColor,
								 String paperSize, Map<String, ProductPhase> productPhases)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return this.databaseImpl.createProduct(name, image, description, amount, order, imageBack,
											   imageFront, marginBottom, marginLeft, marginRight,
											   marginTop, paperColor, paperSize, productPhases);
	}

	@Override
	public Product createProduct(String name, String image, String description, int amount,
								 Order order, String imageBack, String imageFront, int marginBottom,
								 int marginLeft, int marginRight, int marginTop, String paperColor,
								 String paperSize)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return this.databaseImpl.createProduct(name, image, description, amount, order, imageBack,
											   imageFront, marginBottom, marginLeft, marginRight,
											   marginTop, paperColor, paperSize);
	}

	@Override
	public ProductPhase createProductPhase(Calendar startDate, Calendar endDate,
										   ProductPhase.ProductPhaseStatus productPhaseStatus,
										   Employee employee, Phase phase)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return this.databaseImpl.createProductPhase(startDate, endDate, productPhaseStatus,
													employee, phase);
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
	public void update(Employee employee)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		this.databaseImpl.update(employee);
	}

	@Override
	public void update(Order order)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		this.databaseImpl.update(order);
	}

	@Override
	public void update(Phase phase)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		this.databaseImpl.update(phase);
	}

	@Override
	public void update(Product product)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		this.databaseImpl.update(product);
	}

	@Override
	public void update(ProductPhase productPhase)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		this.databaseImpl.update(productPhase);
	}

	@Override
	public void update(User user)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		this.databaseImpl.update(user);
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

	@Override
	public void addDatabaseListener(DatabaseListener databaseListener)
	{
		this.databaseImpl.addDatabaseListener(databaseListener);
	}

	@Override
	public void removeDatabaseListener(DatabaseListener databaseListener)
	{
		this.databaseImpl.removeDatabaseListener(databaseListener);
	}

	@Override
	public <T extends Entity> void downloadImage(Class<T> tClass, String id, Context context,
												 ImageView imageView)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		this.databaseImpl.downloadImage(tClass, id, context, imageView);
	}

	@Override
	public SimpleDateFormat getDateFormatter()
	{
		return this.databaseImpl.getDateFormatter();
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
