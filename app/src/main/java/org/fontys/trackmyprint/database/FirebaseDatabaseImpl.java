package org.fontys.trackmyprint.database;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.fontys.trackmyprint.database.entities.Employee;
import org.fontys.trackmyprint.database.entities.Entity;
import org.fontys.trackmyprint.database.entities.Order;
import org.fontys.trackmyprint.database.entities.Phase;
import org.fontys.trackmyprint.database.entities.Product;
import org.fontys.trackmyprint.database.entities.ProductPhase;
import org.fontys.trackmyprint.database.entities.User;
import org.fontys.trackmyprint.utils.Throw;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guido on 15-Dec-16.
 */
public final class FirebaseDatabaseImpl implements DatabaseImpl
{
	private static final String IMAGE_EXTENSION = ".png";

	private final class EntityInitializer<T extends Entity> implements ValueEventListener
	{
		private final Class<T> tClass;
		private final Map<String, T> dataMap;
		private final ReentrantLock lock;
		private final Set<DatabaseListener> databaseListeners;
		private final DatabaseInitializedTrigger databaseInitializedTrigger;

		public EntityInitializer(Class<T> tClass, Map<String, T> dataMap, ReentrantLock lock,
								 Set<DatabaseListener> databaseListeners,
								 DatabaseInitializedTrigger databaseInitializedTrigger)
		{
			this.tClass = tClass;
			this.dataMap = dataMap;
			this.lock = lock;
			this.databaseListeners = databaseListeners;
			this.databaseInitializedTrigger = databaseInitializedTrigger;
		}

		@Override
		public void onDataChange(DataSnapshot dataSnapshot)
		{
			this.lock.lock();
			try
			{
				for(DataSnapshot entry : dataSnapshot.getChildren())
				{
					T entity = entry.getValue(tClass);

					this.dataMap.put(entity.getId(), entity);
				}

				for(DatabaseListener databaseListener : this.databaseListeners)
				{
					this.databaseInitializedTrigger.trigger(databaseListener,
															Collections.unmodifiableMap(
																	this.dataMap));
				}
			}
			finally
			{
				this.lock.unlock();
			}
		}

		@Override
		public void onCancelled(DatabaseError databaseError)
		{

		}
	}

	private interface DatabaseInitializedTrigger<T extends Entity>
	{
		void trigger(DatabaseListener databaseListener, Map<String, T> entityMap);
	}

	private final class EmployeeDatabaseInitializedTrigger
			implements DatabaseInitializedTrigger<Employee>
	{
		@Override
		public void trigger(DatabaseListener databaseListener, Map<String, Employee> entityMap)
		{
			databaseListener.onEmployeesInitialized(entityMap);
		}
	}

	private final class OrderDatabaseInitializedTrigger implements DatabaseInitializedTrigger<Order>
	{
		@Override
		public void trigger(DatabaseListener databaseListener, Map<String, Order> entityMap)
		{
			databaseListener.onOrdersInitialized(entityMap);
		}
	}

	private final class PhaseDatabaseInitializedTrigger implements DatabaseInitializedTrigger<Phase>
	{
		@Override
		public void trigger(DatabaseListener databaseListener, Map<String, Phase> entityMap)
		{
			databaseListener.onPhasesInitialized(entityMap);
		}
	}

	private final class ProductDatabaseInitializedTrigger
			implements DatabaseInitializedTrigger<Product>
	{
		@Override
		public void trigger(DatabaseListener databaseListener, Map<String, Product> entityMap)
		{
			databaseListener.onProductsInitialized(entityMap);
		}
	}

	private final class ProductPhaseDatabaseInitializedTrigger
			implements DatabaseInitializedTrigger<ProductPhase>
	{
		@Override
		public void trigger(DatabaseListener databaseListener, Map<String, ProductPhase> entityMap)
		{
			databaseListener.onProductPhasesInitialized(entityMap);
		}
	}

	private final class UserDatabaseInitializedTrigger implements DatabaseInitializedTrigger<User>
	{
		@Override
		public void trigger(DatabaseListener databaseListener, Map<String, User> entityMap)
		{
			databaseListener.onUsersInitialized(entityMap);
		}
	}

	private interface DatabaseListenerTrigger<T extends Entity>
	{
		void triggerAdded(DatabaseListener databaseListener, T entity);

		void triggerRemoved(DatabaseListener databaseListener, T entity);

		void triggerChanged(DatabaseListener databaseListener, T entity);
	}

	private final class EmployeeDatabaseListenerTrigger implements DatabaseListenerTrigger<Employee>
	{
		@Override
		public void triggerAdded(DatabaseListener databaseListener, Employee entity)
		{
			databaseListener.onEmployeeAdded(entity);
		}

		@Override
		public void triggerRemoved(DatabaseListener databaseListener, Employee entity)
		{
			databaseListener.onEmployeeRemoved(entity);
		}

		@Override
		public void triggerChanged(DatabaseListener databaseListener, Employee entity)
		{
			databaseListener.onEmployeeChanged(entity);
		}
	}

	private final class OrderDatabaseListenerTrigger implements DatabaseListenerTrigger<Order>
	{
		@Override
		public void triggerAdded(DatabaseListener databaseListener, Order entity)
		{
			databaseListener.onOrderAdded(entity);
		}

		@Override
		public void triggerRemoved(DatabaseListener databaseListener, Order entity)
		{
			databaseListener.onOrderRemoved(entity);
		}

		@Override
		public void triggerChanged(DatabaseListener databaseListener, Order entity)
		{
			databaseListener.onOrderChanged(entity);
		}
	}

	private final class PhaseDatabaseListenerTrigger implements DatabaseListenerTrigger<Phase>
	{
		@Override
		public void triggerAdded(DatabaseListener databaseListener, Phase entity)
		{
			databaseListener.onPhaseAdded(entity);
		}

		@Override
		public void triggerRemoved(DatabaseListener databaseListener, Phase entity)
		{
			databaseListener.onPhaseRemoved(entity);
		}

		@Override
		public void triggerChanged(DatabaseListener databaseListener, Phase entity)
		{
			databaseListener.onPhaseChanged(entity);
		}
	}

	private final class ProductDatabaseListenerTrigger implements DatabaseListenerTrigger<Product>
	{
		@Override
		public void triggerAdded(DatabaseListener databaseListener, Product entity)
		{
			databaseListener.onProductAdded(entity);
		}

		@Override
		public void triggerRemoved(DatabaseListener databaseListener, Product entity)
		{
			databaseListener.onProductRemoved(entity);
		}

		@Override
		public void triggerChanged(DatabaseListener databaseListener, Product entity)
		{
			databaseListener.onProductChanged(entity);
		}
	}

	private final class ProductPhaseDatabaseListenerTrigger
			implements DatabaseListenerTrigger<ProductPhase>
	{
		@Override
		public void triggerAdded(DatabaseListener databaseListener, ProductPhase entity)
		{
			databaseListener.onProductPhaseAdded(entity);
		}

		@Override
		public void triggerRemoved(DatabaseListener databaseListener, ProductPhase entity)
		{
			databaseListener.onProductPhaseRemoved(entity);
		}

		@Override
		public void triggerChanged(DatabaseListener databaseListener, ProductPhase entity)
		{
			databaseListener.onProductPhaseChanged(entity);
		}
	}

	private final class UserDatabaseListenerTrigger implements DatabaseListenerTrigger<User>
	{
		@Override
		public void triggerAdded(DatabaseListener databaseListener, User entity)
		{
			databaseListener.onUserAdded(entity);
		}

		@Override
		public void triggerRemoved(DatabaseListener databaseListener, User entity)
		{
			databaseListener.onUserRemoved(entity);
		}

		@Override
		public void triggerChanged(DatabaseListener databaseListener, User entity)
		{
			databaseListener.onUserChanged(entity);
		}
	}

	private final ReentrantLock lock;
	private boolean initialized;
	private volatile boolean deInitialized;
	private FirebaseDatabase fBase;
	private FirebaseStorage fStorage;
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
	private final Set<DatabaseListener> databaseListeners;
	private ChildEventListener employeeChildEventListener;
	private ChildEventListener orderChildEventListener;
	private ChildEventListener phaseChildEventListener;
	private ChildEventListener productChildEventListener;
	private ChildEventListener productPhaseChildEventLister;
	private ChildEventListener userChildEventListener;
	private final SimpleDateFormat simpleDateFormat;

	public FirebaseDatabaseImpl()
	{
		this.lock = new ReentrantLock();
		this.initialized = false;
		this.deInitialized = false;
		this.fBase = null;
		this.fStorage = null;
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
		this.databaseListeners = new HashSet<>();
		this.employeeChildEventListener = null;
		this.orderChildEventListener = null;
		this.phaseChildEventListener = null;
		this.productChildEventListener = null;
		this.productPhaseChildEventLister = null;
		this.userChildEventListener = null;
		this.simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
	}

	@Override
	public void initialize()
			throws
			IllegalStateException,
			DatabaseException
	{
		this.lock.lock();
		try
		{
			Throw.when(IllegalStateException.class, this.initialized,
					   "Database is already initialized");
			Throw.when(IllegalStateException.class, this.deInitialized,
					   "Database is de-initialized");

			try
			{
				this.fBase = FirebaseDatabase.getInstance();
				this.fStorage = FirebaseStorage.getInstance();
				DatabaseReference rootRef = this.fBase.getReference();

				this.employeeRef = rootRef.child(Employee.class.getSimpleName());
				this.orderRef = rootRef.child(Order.class.getSimpleName());
				this.phaseRef = rootRef.child(Phase.class.getSimpleName());
				this.productRef = rootRef.child(Product.class.getSimpleName());
				this.productPhaseRef = rootRef.child(ProductPhase.class.getSimpleName());
				this.userRef = rootRef.child(User.class.getSimpleName());

				this.employeeRef.addListenerForSingleValueEvent(
						new EntityInitializer(Employee.class, this.employees, this.lock,
											  this.databaseListeners,
											  new EmployeeDatabaseInitializedTrigger()));
				this.orderRef.addListenerForSingleValueEvent(
						new EntityInitializer(Order.class, this.orders, this.lock,
											  this.databaseListeners,
											  new OrderDatabaseInitializedTrigger()));
				this.phaseRef.addListenerForSingleValueEvent(
						new EntityInitializer(Phase.class, this.phases, this.lock,
											  this.databaseListeners,
											  new PhaseDatabaseInitializedTrigger()));
				this.productRef.addListenerForSingleValueEvent(
						new EntityInitializer(Product.class, this.products, this.lock,
											  this.databaseListeners,
											  new ProductDatabaseInitializedTrigger()));
				this.productPhaseRef.addListenerForSingleValueEvent(
						new EntityInitializer(ProductPhase.class, this.productPhases, this.lock,
											  this.databaseListeners,
											  new ProductPhaseDatabaseInitializedTrigger()));
				this.userRef.addListenerForSingleValueEvent(
						new EntityInitializer(User.class, this.users, this.lock,
											  this.databaseListeners,
											  new UserDatabaseInitializedTrigger()));

				this.employeeChildEventListener = addEntityChildEventListener(Employee.class,
																			  this.employeeRef,
																			  this.employees,
																			  new EmployeeDatabaseListenerTrigger());
				try
				{
					this.orderChildEventListener = addEntityChildEventListener(Order.class,
																			   this.orderRef,
																			   this.orders,
																			   new OrderDatabaseListenerTrigger());
					try
					{
						this.phaseChildEventListener = addEntityChildEventListener(Phase.class,
																				   this.phaseRef,
																				   this.phases,
																				   new PhaseDatabaseListenerTrigger());
						try
						{
							this.productChildEventListener = addEntityChildEventListener(
									Product.class, this.productRef, this.products,
									new ProductDatabaseListenerTrigger());
							try
							{
								this.productPhaseChildEventLister = addEntityChildEventListener(
										ProductPhase.class, this.productPhaseRef,
										this.productPhases,
										new ProductPhaseDatabaseListenerTrigger());
								try
								{
									this.userChildEventListener = addEntityChildEventListener(
											User.class, this.userRef, this.users,
											new UserDatabaseListenerTrigger());
								}
								catch(Exception ex)
								{
									this.productPhaseRef.removeEventListener(
											this.productPhaseChildEventLister);

									throw ex;
								}
							}
							catch(Exception ex)
							{
								this.productRef.removeEventListener(this.productChildEventListener);

								throw ex;
							}
						}
						catch(Exception ex)
						{
							this.phaseRef.removeEventListener(this.phaseChildEventListener);

							throw ex;
						}
					}
					catch(Exception ex)
					{
						this.orderRef.removeEventListener(this.orderChildEventListener);

						throw ex;
					}
				}
				catch(Exception ex)
				{
					this.employeeRef.removeEventListener(this.employeeChildEventListener);

					throw ex;
				}
			}
			catch(Exception ex)
			{
				throw new DatabaseException(ex);
			}

			this.initialized = true;
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public void deInitialize()
	{
		this.lock.lock();
		try
		{
			if(!this.initialized || this.deInitialized)
			{
				return;
			}

			this.employeeRef.removeEventListener(this.employeeChildEventListener);
			this.orderRef.removeEventListener(this.orderChildEventListener);
			this.phaseRef.removeEventListener(this.phaseChildEventListener);
			this.productRef.removeEventListener(this.productChildEventListener);
			this.productPhaseRef.removeEventListener(this.productPhaseChildEventLister);
			this.userRef.removeEventListener(this.userChildEventListener);

			this.deInitialized = true;
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public Employee createEmployee(Phase phase, String name)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, phase, "phase");

		this.lock.lock();
		try
		{
			DatabaseReference newEmployeeRef = this.employeeRef.push();

			Employee newEmployee = new Employee(newEmployeeRef.getKey(), phase.getId(), name);

			newEmployeeRef.setValue(newEmployee);

			this.employees.put(newEmployee.getId(), newEmployee);

			return newEmployee;
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
		finally
		{
			this.lock.unlock();
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
		Throw.ifNull(IllegalArgumentException.class, user, "user");

		this.lock.lock();
		try
		{
			DatabaseReference newOrderRef = this.orderRef.push();

			List<String> productIds;
			if(products == null)
			{
				productIds = null;
			}
			else
			{
				productIds = new ArrayList<>(products.size());

				for(String productId : products.keySet())
				{
					productIds.add(productId);
				}
			}

			Order newOrder = new Order(newOrderRef.getKey(), user.getId(), orderStatus,
									   orderDate == null ? null : this.simpleDateFormat.format(
											   orderDate.getTime()), productIds);

			newOrderRef.setValue(newOrder);

			this.orders.put(newOrder.getId(), newOrder);

			return newOrder;
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
		finally
		{
			this.lock.unlock();
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
		this.lock.lock();
		try
		{
			DatabaseReference newPhaseRef = this.phaseRef.push();

			Phase newPhase = new Phase(newPhaseRef.getKey(), name);

			newPhaseRef.setValue(newPhase);

			this.phases.put(newPhase.getId(), newPhase);

			return newPhase;
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public Product createProduct(String name, String image, String description, int amount,
								 Order order, Map<String, ProductPhase> productPhases)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, order, "order");

		this.lock.lock();
		try
		{
			DatabaseReference newProductRef = this.productRef.push();

			List<String> productPhaseIds;
			if(productPhases == null)
			{
				productPhaseIds = null;
			}
			else
			{
				productPhaseIds = new ArrayList<>();

				for(String productPhaseId : productPhases.keySet())
				{
					productPhaseIds.add(productPhaseId);
				}
			}

			Product newProduct = new Product(newProductRef.getKey(), name, image, description,
											 amount, order.getId(), productPhaseIds);

			newProductRef.setValue(newProduct);

			this.products.put(newProduct.getId(), newProduct);

			return newProduct;
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
		finally
		{
			this.lock.unlock();
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
		Throw.ifNull(IllegalArgumentException.class, employee, "employee");
		Throw.ifNull(IllegalArgumentException.class, phase, "phase");

		this.lock.lock();
		try
		{
			DatabaseReference newProductPhaseRef = this.productPhaseRef.push();

			ProductPhase newProductPhase = new ProductPhase(newProductPhaseRef.getKey(),
															startDate ==
															null ? null : this.simpleDateFormat.format(
																	startDate.getTime()), endDate ==
																						  null ? null : this.simpleDateFormat.format(
					endDate.getTime()), productPhaseStatus, employee.getId(), phase.getId());

			newProductPhaseRef.setValue(newProductPhase);

			this.productPhases.put(newProductPhase.getId(), newProductPhase);

			return newProductPhase;
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public ProductPhase createProductPhase(Employee employee, Phase phase)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		return createProductPhase(null, null, ProductPhase.ProductPhaseStatus.NONE, employee,
								  phase);
	}

	@Override
	public User createUser(String userId, Map<String, Order> orders)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, userId, "userId");

		this.lock.lock();
		try
		{
			Throw.when(IllegalStateException.class, !this.initialized,
					   "Database is not initialized");
			Throw.when(IllegalStateException.class, this.deInitialized,
					   "Database is de-initialized");
			throwIfExists(this.users, userId, "User already exists");

			DatabaseReference newUserRef = this.userRef.child(userId);

			List<String> orderIds;
			if(orders == null)
			{
				orderIds = null;
			}
			else
			{
				orderIds = new ArrayList<>();

				for(String orderId : orders.keySet())
				{
					orderIds.add(orderId);
				}
			}

			User newUser = new User(userId, orderIds);

			newUserRef.setValue(newUser);

			this.users.put(newUser.getId(), newUser);

			return newUser;
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
		finally
		{
			this.lock.unlock();
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

		this.lock.lock();
		try
		{
			Employee removedEmployee = this.employees.remove(employee.getId());
			if(removedEmployee != null)
			{
				this.employeeRef.child(employee.getId()).removeValue();
			}
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public void remove(Order order)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, order, "order");

		this.lock.lock();
		try
		{
			Order removedOrder = this.orders.remove(order.getId());
			if(removedOrder != null)
			{
				this.orderRef.child(order.getId()).removeValue();
			}
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public void remove(Phase phase)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, phase, "phase");

		this.lock.lock();
		try
		{
			Phase removedPhase = this.phases.remove(phase.getId());
			if(removedPhase != null)
			{
				this.phaseRef.child(phase.getId()).removeValue();
			}
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public void remove(Product product)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, product, "product");

		this.lock.lock();
		try
		{
			Product removedProduct = this.products.remove(product.getId());
			if(removedProduct != null)
			{
				this.productRef.child(product.getId()).removeValue();
			}
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public void remove(ProductPhase productPhase)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, productPhase, "productPhase");

		this.lock.lock();
		try
		{
			ProductPhase removedProductPhase = this.productPhases.remove(productPhase.getId());
			if(removedProductPhase != null)
			{
				this.productPhaseRef.child(productPhase.getId()).removeValue();
			}
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public void remove(User user)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, user, "user");

		this.lock.lock();
		try
		{
			User removedUser = this.users.remove(user.getId());
			if(removedUser != null)
			{
				this.userRef.child(user.getId()).removeValue();
			}
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public void update(Employee employee)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, employee, "employee");

		this.lock.lock();
		try
		{
			employee.getLock().lock();
			try
			{
				throwIfNotExists(this.employees, employee.getId(), "Employee does not exist");

				DatabaseReference updateRef = this.employeeRef.child(employee.getId());

				updateRef.setValue(employee);
			}
			finally
			{
				employee.getLock().unlock();
			}
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public void update(Order order)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, order, "order");

		this.lock.lock();
		try
		{
			order.getLock().lock();
			try
			{
				throwIfNotExists(this.orders, order.getId(), "Order does not exist");

				DatabaseReference updateRef = this.orderRef.child(order.getId());

				updateRef.setValue(order);
			}
			finally
			{
				order.getLock().unlock();
			}
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public void update(Phase phase)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, phase, "phase");

		this.lock.lock();
		try
		{
			phase.getLock().lock();
			try
			{
				throwIfNotExists(this.phases, phase.getId(), "Phase does not exist");

				DatabaseReference updateRef = this.phaseRef.child(phase.getId());

				updateRef.setValue(phase);
			}
			finally
			{
				phase.getLock().unlock();
			}
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public void update(Product product)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, product, "product");

		this.lock.lock();
		try
		{
			product.getLock().lock();
			try
			{
				throwIfNotExists(this.products, product.getId(), "Product does not exist");

				DatabaseReference updateRef = this.productRef.child(product.getId());

				updateRef.setValue(product);
			}
			finally
			{
				product.getLock().unlock();
			}
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public void update(ProductPhase productPhase)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, productPhase, "productPhase");

		this.lock.lock();
		try
		{
			productPhase.getLock().lock();
			try
			{
				throwIfNotExists(this.productPhases, productPhase.getId(),
								 "ProductPhase does not exist");

				DatabaseReference updateRef = this.productPhaseRef.child(productPhase.getId());

				updateRef.setValue(productPhase);
			}
			finally
			{
				productPhase.getLock().unlock();
			}
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public void update(User user)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, user, "user");

		this.lock.lock();
		try
		{
			user.getLock().lock();
			try
			{
				throwIfNotExists(this.users, user.getId(), "User does not exist");

				DatabaseReference updateRef = this.userRef.child(user.getId());

				updateRef.setValue(user);
			}
			finally
			{
				user.getLock().unlock();
			}
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
		finally
		{
			this.lock.unlock();
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
	public void addDatabaseListener(DatabaseListener databaseListener)
	{
		Throw.ifNull(IllegalArgumentException.class, databaseListener, "databaseListener");

		this.lock.lock();
		try
		{
			this.databaseListeners.add(databaseListener);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public void removeDatabaseListener(DatabaseListener databaseListener)
	{
		Throw.ifNull(IllegalArgumentException.class, databaseListener, "databaseListener");

		this.lock.lock();
		try
		{
			this.databaseListeners.remove(databaseListener);
		}
		finally
		{
			this.lock.unlock();
		}
	}

	@Override
	public <T extends Entity> void downloadImage(Class<T> tClass, String id, Context context,
												 ImageView imageView)
			throws
			IllegalArgumentException,
			DatabaseException
	{
		Throw.ifNull(IllegalArgumentException.class, tClass, "tClass");
		Throw.ifNull(IllegalArgumentException.class, id, "id");
		Throw.ifNull(IllegalArgumentException.class, context, "context");
		Throw.ifNull(IllegalArgumentException.class, imageView, "imageView");

		try
		{
			StorageReference rootRef = this.fStorage.getReference();
			StorageReference entityRef = rootRef.child(tClass.getSimpleName());

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(id).append(IMAGE_EXTENSION);

			StorageReference imageRef = entityRef.child(stringBuilder.toString());

			Glide.with(context).using(new FirebaseImageLoader()).load(imageRef).into(imageView);
		}
		catch(Exception ex)
		{
			throw new DatabaseException(ex);
		}
	}

	private <T extends Entity> void throwIfExists(Map<String, T> dataMap, String key,
												  String message)
			throws
			DatabaseException
	{
		Throw.when(DatabaseException.class, dataMap.containsKey(key), message);
	}

	private <T extends Entity> void throwIfNotExists(Map<String, T> dataMap, String key,
													 String message)
			throws
			DatabaseException
	{
		Throw.when(DatabaseException.class, !dataMap.containsKey(key), message);
	}

	private <T extends Entity> ChildEventListener addEntityChildEventListener(final Class<T> tClass,
																			  DatabaseReference databaseReference,
																			  final Map<String, T> entityMap,
																			  final DatabaseListenerTrigger<T> databaseListenerTrigger)
	{
		ChildEventListener result = new ChildEventListener()
		{
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName)
			{
				lock.lock();
				try
				{
					T newT = dataSnapshot.getValue(tClass);

					if(!entityMap.containsKey(newT.getId()))
					{
						entityMap.put(newT.getId(), newT);
					}

					for(DatabaseListener databaseListener : databaseListeners)
					{
						databaseListenerTrigger.triggerAdded(databaseListener, newT);
					}
				}
				finally
				{
					lock.unlock();
				}
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName)
			{
				lock.lock();
				try
				{
					T changedT = dataSnapshot.getValue(tClass);

					for(DatabaseListener databaseListener : databaseListeners)
					{
						databaseListenerTrigger.triggerChanged(databaseListener, changedT);
					}
				}
				finally
				{
					lock.unlock();
				}
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot)
			{
				lock.lock();
				try
				{
					T removedT = dataSnapshot.getValue(tClass);

					entityMap.remove(removedT.getId());

					for(DatabaseListener databaseListener : databaseListeners)
					{
						databaseListenerTrigger.triggerRemoved(databaseListener, removedT);
					}
				}
				finally
				{
					lock.unlock();
				}
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName)
			{

			}

			@Override
			public void onCancelled(DatabaseError databaseError)
			{

			}
		};

		databaseReference.addChildEventListener(result);

		return result;
	}
}
