package org.fontys.trackmyprint.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.fontys.trackmyprint.database.entities.Employee;
import org.fontys.trackmyprint.database.entities.Entity;
import org.fontys.trackmyprint.database.entities.Order;
import org.fontys.trackmyprint.database.entities.Phase;
import org.fontys.trackmyprint.database.entities.Product;
import org.fontys.trackmyprint.database.entities.ProductPhase;
import org.fontys.trackmyprint.database.entities.User;
import org.fontys.trackmyprint.utils.Throw;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by guido on 15-Dec-16.
 */
public final class FirebaseDatabaseImpl extends AbstractDatabaseImpl
{
	private final class EntityInitializer<T extends Entity> implements ValueEventListener
	{
		private final Class<T> tClass;
		private final Map<String, T> dataMap;

		public EntityInitializer(Class<T> tClass, Map<String, T> dataMap)
		{
			this.tClass = tClass;
			this.dataMap = dataMap;
		}

		@Override
		public void onDataChange(DataSnapshot dataSnapshot)
		{
			for(DataSnapshot entry : dataSnapshot.getChildren())
			{
				T entity = entry.getValue(tClass);

				this.dataMap.put(entity.getId(), entity);
			}
		}

		@Override
		public void onCancelled(DatabaseError databaseError)
		{

		}
	}

	private final Object lock;
	private boolean initialized;
	private volatile boolean deInitialized;
	private FirebaseDatabase fBase;
	private DatabaseReference employeeRef;
	private DatabaseReference orderRef;
	private DatabaseReference phaseRef;
	private DatabaseReference productRef;
	private DatabaseReference productPhaseRef;
	private DatabaseReference userRef;
	private final Map<String, Employee> employees;
	private final Map<String, Order> orders;
	private final Map<String, Phase> phases;
	private final Map<String, Product> products;
	private final Map<String, ProductPhase> productPhases;
	private final Map<String, User> users;

	public FirebaseDatabaseImpl()
	{
		this.lock = new Object();
		this.initialized = false;
		this.deInitialized = false;
		this.fBase = null;
		this.employeeRef = null;
		this.orderRef = null;
		this.phaseRef = null;
		this.productRef = null;
		this.productPhaseRef = null;
		this.userRef = null;
		this.employees = new HashMap<>();
		this.orders = new HashMap<>();
		this.phases = new HashMap<>();
		this.products = new HashMap<>();
		this.productPhases = new HashMap<>();
		this.users = new HashMap<>();
	}

	@Override
	public void initialize()
			throws
			IllegalStateException,
			DatabaseException
	{
		synchronized(this.lock)
		{
			Throw.when(IllegalStateException.class, this.initialized, "Database is already initialized");
			Throw.when(IllegalStateException.class, this.deInitialized, "Database is de-initialized");

			try
			{
				this.fBase = FirebaseDatabase.getInstance();
				DatabaseReference rootRef = this.fBase.getReference();

				this.employeeRef = rootRef.child(Employee.class.getSimpleName());
				this.orderRef = rootRef.child(Order.class.getSimpleName());
				this.phaseRef = rootRef.child(Phase.class.getSimpleName());
				this.productRef = rootRef.child(Product.class.getSimpleName());
				this.productPhaseRef = rootRef.child(ProductPhase.class.getSimpleName());
				this.userRef = rootRef.child(User.class.getSimpleName());

				this.employeeRef.addListenerForSingleValueEvent(new EntityInitializer(Employee.class, this.employees));
				this.orderRef.addListenerForSingleValueEvent(new EntityInitializer(Order.class, this.orders));
				this.phaseRef.addListenerForSingleValueEvent(new EntityInitializer(Phase.class, this.phases));
				this.productRef.addListenerForSingleValueEvent(new EntityInitializer(Product.class, this.products));
				this.productPhaseRef.addListenerForSingleValueEvent(new EntityInitializer(ProductPhase.class, this.productPhases));
				this.userRef.addListenerForSingleValueEvent(new EntityInitializer(User.class, this.users));
			}
			catch(Exception ex)
			{
				throw new DatabaseException(ex);
			}

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

			this.deInitialized = true;
		}
	}

	@Override
	public Employee createEmployee(Phase phase, String name)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		synchronized(this.lock)
		{
			DatabaseReference newEmployeeRef = this.employeeRef.push();

			Employee newEmployee = new Employee(newEmployeeRef.getKey(), phase, name);

			newEmployeeRef.setValue(newEmployee);

			return newEmployee;
		}
	}

	@Override
	public Employee createEmployee(String name)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return createEmployee(null, name);
	}

	@Override
	public Order createOrder(User user, Order.OrderStatus orderStatus, Calendar orderDate,
							 Map<String, Product> products)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		synchronized(this.lock)
		{
			DatabaseReference newOrderRef = this.orderRef.push();

			Order newOrder = new Order(newOrderRef.getKey(), user, orderStatus, orderDate, products);

			newOrderRef.setValue(newOrder);

			return newOrder;
		}
	}

	@Override
	public Order createOrder(User user, Order.OrderStatus orderStatus, Calendar orderDate)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return createOrder(user, orderStatus, orderDate, null);
	}

	@Override
	public Order createOrder(User user, Calendar orderDate, Map<String, Product> products)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return createOrder(user, Order.OrderStatus.NONE, orderDate, products);
	}

	@Override
	public Order createOrder(User user, Calendar orderDate)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return createOrder(user, Order.OrderStatus.NONE, orderDate, null);
	}

	@Override
	public Phase createPhase(String name)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		synchronized(this.lock)
		{
			DatabaseReference newPhaseRef = this.phaseRef.push();

			Phase newPhase = new Phase(newPhaseRef.getKey(), name);

			newPhaseRef.setValue(newPhase);

			return newPhase;
		}
	}

	@Override
	public Product createProduct(String name, String image, String description, int amount,
								 Order order, Map<String, ProductPhase> productPhases)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		synchronized(this.lock)
		{
			DatabaseReference newProductRef = this.productRef.push();

			Product newProduct = new Product(newProductRef.getKey(), name, image, description,
											 amount, order, productPhases);

			newProductRef.setValue(newProduct);

			return newProduct;
		}
	}

	@Override
	public Product createProduct(String name, String image, String description, int amount,
								 Order order)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return createProduct(name, image, description, amount, order, null);
	}

	@Override
	public ProductPhase createProductPhase(Calendar startDate, Calendar endDate,
										   ProductPhase.ProductPhaseStatus productPhaseStatus,
										   Employee employee, Phase phase)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		synchronized(this.lock)
		{
			DatabaseReference newProductPhaseRef = this.productPhaseRef.push();

			ProductPhase newProductPhase = new ProductPhase(newProductPhaseRef.getKey(), startDate,
															endDate, productPhaseStatus, employee,
															phase);

			newProductPhaseRef.setValue(newProductPhase);

			return newProductPhase;
		}
	}

	@Override
	public ProductPhase createProductPhase(Employee employee, Phase phase)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return createProductPhase(null, null, ProductPhase.ProductPhaseStatus.NONE, employee, phase);
	}

	@Override
	public User createUser(String userId, Map<String, Order> orders)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, userId, "userId");

		synchronized(this.lock)
		{
			Throw.when(IllegalStateException.class, !this.initialized, "Database is not initialized");
			Throw.when(IllegalStateException.class, this.deInitialized, "Database is de-initialized");
			throwIfExists(this.users, userId, "User already exists");

			DatabaseReference newUserRef = this.userRef.child(userId);

			User newUser = new User(userId, orders);

			newUserRef.setValue(newUser);

			return newUser;
		}
	}

	@Override
	public User createUser(String userId)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return createUser(userId, null);
	}

	@Override
	public void remove(Employee employee)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, employee, "employee");

		Employee removedEmployee = this.employees.remove(employee.getId());
		if(removedEmployee != null)
		{
			this.employeeRef.child(employee.getId()).removeValue();
		}
	}

	@Override
	public void remove(Order order)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, order, "order");

		Order removedOrder = this.orders.remove(order.getId());
		if(removedOrder != null)
		{
			this.orderRef.child(order.getId()).removeValue();
		}
	}

	@Override
	public void remove(Phase phase)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, phase, "phase");

		Phase removedPhase = this.phases.remove(phase.getId());
		if(removedPhase != null)
		{
			this.phaseRef.child(phase.getId()).removeValue();
		}
	}

	@Override
	public void remove(Product product)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, product, "product");

		Product removedProduct = this.products.remove(product.getId());
		if(removedProduct != null)
		{
			this.productRef.child(product.getId()).removeValue();
		}
	}

	@Override
	public void remove(ProductPhase productPhase)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, productPhase, "productPhase");

		ProductPhase removedProductPhase = this.productPhases.remove(productPhase.getId());
		if(removedProductPhase != null)
		{
			this.productPhaseRef.child(productPhase.getId()).removeValue();
		}
	}

	@Override
	public void remove(User user)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, user, "user");

		User removedUser = this.users.remove(user.getId());
		if(removedUser != null)
		{
			this.userRef.child(user.getId()).removeValue();
		}
	}

	@Override
	public Map<String, Phase> getPhases()
	{
		return Collections.unmodifiableMap(this.phases);
	}

	@Override
	public Map<String, Employee> getEmployees()
	{
		return Collections.unmodifiableMap(this.employees);
	}

	@Override
	public Map<String, User> getUsers()
	{
		return Collections.unmodifiableMap(this.users);
	}

	@Override
	public Map<String, Order> getOrders()
	{
		return Collections.unmodifiableMap(this.orders);
	}

	@Override
	public Map<String, Product> getProducts()
	{
		return Collections.unmodifiableMap(this.products);
	}

	@Override
	public Map<String, ProductPhase> getProductPhases()
	{
		return Collections.unmodifiableMap(this.productPhases);
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

	private <T extends Entity> void throwIfExists(Map<String, T> dataMap, String key, String message)
			throws
			DatabaseException
	{
		Throw.when(DatabaseException.class, dataMap.containsKey(key), message);
	}
}
