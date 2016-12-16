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

/**
 * Created by guido on 15-Dec-16.
 */
public interface DatabaseImpl
{
	void initialize()
			throws
			IllegalStateException,
			DatabaseException;

	void deInitialize();

	Employee createEmployee(Phase phase, String name)
			throws
			IllegalArgumentException,
			DatabaseException;

	Employee createEmployee(String name)
			throws
			IllegalArgumentException,
			DatabaseException;

	Order createOrder(User user, Order.OrderStatus orderStatus, Calendar orderDate, Map<String, Product> products)
			throws
			IllegalArgumentException,
			DatabaseException;

	Order createOrder(User user, Order.OrderStatus orderStatus, Calendar orderDate)
			throws
			IllegalArgumentException,
			DatabaseException;

	Order createOrder(User user, Calendar orderDate, Map<String, Product> products)
			throws
			IllegalArgumentException,
			DatabaseException;

	Order createOrder(User user, Calendar orderDate)
			throws
			IllegalArgumentException,
			DatabaseException;

	Phase createPhase(String name)
			throws
			IllegalArgumentException,
			DatabaseException;

	Product createProduct(String name, String image, String description, int amount, Order order, Map<String, ProductPhase> productPhases)
			throws
			IllegalArgumentException,
			DatabaseException;

	Product createProduct(String name, String image, String description, int amount, Order order)
			throws
			IllegalArgumentException,
			DatabaseException;

	ProductPhase createProductPhase(Calendar startDate, Calendar endDate, ProductPhase.ProductPhaseStatus productPhaseStatus, Employee employee, Phase phase)
			throws
			IllegalArgumentException,
			DatabaseException;

	ProductPhase createProductPhase(Employee employee, Phase phase)
			throws
			IllegalArgumentException,
			DatabaseException;

	User createUser(String userId, Map<String, Order> orders)
			throws
			IllegalArgumentException,
			DatabaseException;

	User createUser(String userId)
			throws
			IllegalArgumentException,
			DatabaseException;

	void remove(Employee employee)
			throws
			IllegalArgumentException,
			DatabaseException;

	void remove(Order order)
			throws
			IllegalArgumentException,
			DatabaseException;

	void remove(Phase phase)
			throws
			IllegalArgumentException,
			DatabaseException;

	void remove(Product product)
			throws
			IllegalArgumentException,
			DatabaseException;

	void remove(ProductPhase productPhase)
			throws
			IllegalArgumentException,
			DatabaseException;

	void remove(User user)
			throws
			IllegalArgumentException,
			DatabaseException;

	Map<String, Phase> getPhases();

	Map<String, Employee> getEmployees();

	Map<String, User> getUsers();

	Map<String, Order> getOrders();

	Map<String, Product> getProducts();

	Map<String, ProductPhase> getProductPhases();
}
