package org.fontys.trackmyprint.database;

import org.fontys.trackmyprint.database.entities.Employee;
import org.fontys.trackmyprint.database.entities.Order;
import org.fontys.trackmyprint.database.entities.Phase;
import org.fontys.trackmyprint.database.entities.Product;
import org.fontys.trackmyprint.database.entities.ProductPhase;
import org.fontys.trackmyprint.database.entities.User;

import java.util.Map;

/**
 * Created by guido on 22-Dec-16.
 */

public interface DatabaseListener
{
	void onEmployeesInitialized(Map<String, Employee> employees);

	void onOrdersInitialized(Map<String, Order> orders);

	void onPhasesInitialized(Map<String, Phase> phases);

	void onProductsInitialized(Map<String, Product> phases);

	void onProductPhasesInitialized(Map<String, ProductPhase> productPhases);

	void onUsersInitialized(Map<String, User> users);

	void onEmployeeAdded(Employee employee);

	void onEmployeeRemoved(Employee employee);

	void onEmployeeChanged(Employee employee);

	void onOrderAdded(Order order);

	void onOrderRemoved(Order order);

	void onOrderChanged(Order order);

	void onPhaseAdded(Phase phase);

	void onPhaseRemoved(Phase phase);

	void onPhaseChanged(Phase phase);

	void onProductAdded(Product product);

	void onProductRemoved(Product product);

	void onProductChanged(Product product);

	void onProductPhaseAdded(ProductPhase productPhase);

	void onProductPhaseRemoved(ProductPhase productPhase);

	void onProductPhaseChanged(ProductPhase productPhase);

	void onUserAdded(User user);

	void onUserRemoved(User user);

	void onUserChanged(User user);
}
