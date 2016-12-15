package org.fontys.trackmyprint.database;

import org.fontys.trackmyprint.database.entities.Employee;
import org.fontys.trackmyprint.database.entities.Entity;
import org.fontys.trackmyprint.database.entities.EntityChanged;
import org.fontys.trackmyprint.database.entities.Order;
import org.fontys.trackmyprint.database.entities.Phase;
import org.fontys.trackmyprint.database.entities.Product;
import org.fontys.trackmyprint.database.entities.ProductPhase;
import org.fontys.trackmyprint.database.entities.User;
import org.fontys.trackmyprint.utils.Throw;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by guido on 15-Dec-16.
 */
public abstract class AbstractDatabaseImpl implements DatabaseImpl, Observer
{
	@Override
	public final void update(Observable o, Object arg)
	{
		Throw.ifNotInstanceOf(RuntimeException.class, arg, "arg", EntityChanged.class);

		EntityChanged entityChanged = (EntityChanged)arg;
		Entity entity = entityChanged.getEntity();

		switch(entity.getEntityType())
		{
			case EMPLOYEE:
				Throw.ifNotInstanceOf(RuntimeException.class, entity, "entity", Employee.class);

				handleEmployeeChange((Employee)entity, getEmployeeChangeType(entityChanged.getChangeType()), entityChanged.getArg());

				break;
			case ORDER:
				Throw.ifNotInstanceOf(RuntimeException.class, entity, "entity", Order.class);

				handleOrderChange((Order)entity, getOrderChangeType(entityChanged.getChangeType()), entityChanged.getArg());

				break;
			case PHASE:
				Throw.ifNotInstanceOf(RuntimeException.class, entity, "entity", Phase.class);

				handlePhaseChange((Phase)entity, getPhaseChangeType(entityChanged.getChangeType()), entityChanged.getArg());

				break;
			case PRODUCT:
				Throw.ifNotInstanceOf(RuntimeException.class, entity, "entity", Product.class);

				handleProductChange((Product)entity, getProductChangeType(entityChanged.getChangeType()), entityChanged.getArg());

				break;
			case PRODUCT_PHASE:
				Throw.ifNotInstanceOf(RuntimeException.class, entity, "entity", ProductPhase.class);

				handleProductPhaseChange((ProductPhase)entity, getProductPhaseChangeType(entityChanged.getChangeType()), entityChanged.getArg());

				break;
			case USER:
				Throw.ifNotInstanceOf(RuntimeException.class, entity, "entity", User.class);

				handleUserChange((User)entity, getUserChangeType(entityChanged.getChangeType()), entityChanged.getArg());

				break;
			default:
				Throw.always(RuntimeException.class, "Invalid entity type");

				break;
		}

	}

	protected abstract void handleEmployeePhaseChanged(Employee employee);

	protected abstract void handleOrderProductAdded(Order order, Product product);

	protected abstract void handleOrderProductRemoved(Order order, Product product);

	protected abstract void handleProductPhaseAdded(Product product, Phase phase);

	protected abstract void handleProductPhaseRemoved(Product product, Phase phase);

	protected abstract void handleProductPhaseStartDateChanged(ProductPhase productPhase);

	protected abstract void handleProductPhaseEndDateChanged(ProductPhase productPhase);

	protected abstract void handleProductPhaseStatusChanged(ProductPhase productPhase);

	protected abstract void handleUserOrderAdded(User user, Order order);

	protected abstract void handleUserOrderRemoved(User user, Order order);

	private Employee.ChangeType getEmployeeChangeType(int id)
			throws
			RuntimeException
	{
		Employee.ChangeType result = Employee.ChangeType.findById(id);

		Throw.when(RuntimeException.class, result == Employee.ChangeType.NONE, "Invalid ChangeType");

		return result;
	}

	private Order.ChangeType getOrderChangeType(int id)
			throws
			RuntimeException
	{
		Order.ChangeType result = Order.ChangeType.findById(id);

		Throw.when(RuntimeException.class, result == Order.ChangeType.NONE, "Invalid ChangeType");

		return result;
	}

	private Phase.ChangeType getPhaseChangeType(int id)
			throws
			RuntimeException
	{
		Phase.ChangeType result = Phase.ChangeType.findById(id);

		Throw.when(RuntimeException.class, result == Phase.ChangeType.NONE, "Invalid ChangeType");

		return result;
	}

	private Product.ChangeType getProductChangeType(int id)
			throws
			RuntimeException
	{
		Product.ChangeType result = Product.ChangeType.findById(id);

		Throw.when(RuntimeException.class, result == Product.ChangeType.NONE, "Invalid ChangeType");

		return result;
	}

	private User.ChangeType getUserChangeType(int id)
			throws
			RuntimeException
	{
		User.ChangeType result = User.ChangeType.findById(id);

		Throw.when(RuntimeException.class, result == User.ChangeType.NONE, "Invalid ChangeType");

		return result;
	}

	private ProductPhase.ChangeType getProductPhaseChangeType(int id)
			throws
			RuntimeException
	{
		ProductPhase.ChangeType result = ProductPhase.ChangeType.findById(id);

		Throw.when(RuntimeException.class, result == ProductPhase.ChangeType.NONE, "Invalid ChangeType");

		return result;
	}

	private void handleEmployeeChange(Employee employee, Employee.ChangeType changeType, Object arg)
	{
		switch(changeType)
		{
			case PHASE_CHANGED:
				handleEmployeePhaseChanged(employee);

				break;
			default:
				Throw.always(RuntimeException.class, "Invalid change type");

				break;
		}
	}

	private void handleOrderChange(Order order, Order.ChangeType changeType, Object arg)
	{
		switch(changeType)
		{
			case PRODUCT_ADDED:
				Throw.ifNotInstanceOf(RuntimeException.class, arg, "arg", Product.class);

				handleOrderProductAdded(order, (Product)arg);

				break;
			case PRODUCT_REMOVED:
				Throw.ifNotInstanceOf(RuntimeException.class, arg, "arg", Product.class);

				handleOrderProductRemoved(order, (Product)arg);

				break;
			case ORDER_STATUS_CHANGED:
				break;
			default:
				Throw.always(RuntimeException.class, "Invalid change type");

				break;
		}
	}

	private void handlePhaseChange(Phase phase, Phase.ChangeType changeType, Object arg)
	{
		switch(changeType)
		{
			default:
				Throw.always(RuntimeException.class, "Invalid change type");

				break;
		}
	}

	private void handleProductChange(Product product, Product.ChangeType changeType, Object arg)
	{
		switch(changeType)
		{
			case PRODUCT_PHASE_ADDED:
				Throw.ifNotInstanceOf(RuntimeException.class, arg, "arg", Phase.class);

				handleProductPhaseAdded(product, (Phase)arg);

				break;
			case PRODUCT_PHASE_REMOVED:
				Throw.ifNotInstanceOf(RuntimeException.class, arg, "arg", Phase.class);

				handleProductPhaseRemoved(product, (Phase)arg);

				break;
			default:
				Throw.always(RuntimeException.class, "Invalid change type");

				break;
		}
	}

	private void handleProductPhaseChange(ProductPhase productPhase, ProductPhase.ChangeType changeType, Object arg)
	{
		switch(changeType)
		{
			case START_DATE_CHANGED:
				handleProductPhaseStartDateChanged(productPhase);

				break;
			case END_DATE_CHANGED:
				handleProductPhaseEndDateChanged(productPhase);

				break;
			case PRODUCT_PHASE_STATUS_CHANGED:
				handleProductPhaseStatusChanged(productPhase);

				break;
			default:
				Throw.always(RuntimeException.class, "Invalid change type");

				break;
		}
	}

	private void handleUserChange(User user, User.ChangeType changeType, Object arg)
	{
		switch(changeType)
		{
			case ORDER_ADDED:
				Throw.ifNotInstanceOf(RuntimeException.class, arg, "arg", Order.class);

				handleUserOrderAdded(user, (Order)arg);

				break;
			case ORDER_REMOVED:
				Throw.ifNotInstanceOf(RuntimeException.class, arg, "arg", Order.class);

				handleUserOrderRemoved(user, (Order)arg);

				break;
			default:
				Throw.always(RuntimeException.class, "Invalid change type");

				break;
		}
	}
}
