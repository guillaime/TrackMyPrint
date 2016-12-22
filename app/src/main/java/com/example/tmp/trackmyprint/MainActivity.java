package com.example.tmp.trackmyprint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.fontys.trackmyprint.database.Database;
import org.fontys.trackmyprint.database.entities.Employee;
import org.fontys.trackmyprint.database.entities.Order;
import org.fontys.trackmyprint.database.entities.Phase;
import org.fontys.trackmyprint.database.entities.Product;
import org.fontys.trackmyprint.database.entities.ProductPhase;
import org.fontys.trackmyprint.database.entities.User;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			Database.initializeInstance();

			User user = Database.getInstance().createUser("2342343");
			Phase phase = Database.getInstance().createPhase("A phase");
			Employee employee = Database.getInstance().createEmployee(phase, "An employee");
			ProductPhase productPhase = Database.getInstance().createProductPhase(employee, phase);
			Order order = Database.getInstance().createOrder(user, Order.OrderStatus.NONE, Calendar.getInstance());
			Product product = Database.getInstance().createProduct("A product", "Some image", "Product Desc", 100, order);
			product.addProductPhaseId(productPhase.getId());
			order.addProductId(product.getId());

			Database.getInstance().update(product);
			Database.getInstance().update(order);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		try
		{
			Database.deInitializeInstance();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
