package com.example.tmp.trackmyprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.fontys.trackmyprint.database.Database;
import org.fontys.trackmyprint.database.DatabaseListener;
import org.fontys.trackmyprint.database.entities.Employee;
import org.fontys.trackmyprint.database.entities.Order;
import org.fontys.trackmyprint.database.entities.Phase;
import org.fontys.trackmyprint.database.entities.Product;
import org.fontys.trackmyprint.database.entities.ProductPhase;
import org.fontys.trackmyprint.database.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements DatabaseListener
{
	private Employee currentEmployee;

	private production_proccess_list_adapter adapter;

	private ImageButton btnScan;

	private static MainActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnScan = (ImageButton) findViewById(R.id.btnScan);

		currentEmployee = new Employee("1", "Luuk Hermans");

		try
		{
			Database.getInstance().addDatabaseListener(this);
			Database.initializeInstance();
		}
		catch(Exception ex)
		{
			Database.getInstance().removeDatabaseListener(this);

			ex.printStackTrace();
		}

		TextView employeeName = (TextView) findViewById(R.id.profile_name);
		employeeName.setText(currentEmployee.getName());

		ImageView checkIn = (ImageView) findViewById(R.id.check_in_status);

		// Deze is temporary om naar de userList te gaan.
		ImageView checkIn2 = (ImageView) findViewById(R.id.profile_image);

		instance = this;
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

	public static MainActivity getInstance()
	{
		return instance;
	}

	public void setCurrentPhase(Phase p)
	{
		this.currentEmployee.setPhaseId(p == null ? null : p.getId());
		// update in database


		ImageView status = (ImageView) findViewById(R.id.check_in_status);
		TextView lblScan = (TextView) findViewById(R.id.lblScan);

		if(p.getId() == "100")
		{
			status.setImageResource(R.drawable.checkin_status);
			lblScan.setText("Please check in to a sector");
			btnScan.setImageResource(R.color.colorProfileRectangle);
		}
		else
		{
			status.setImageResource(R.drawable.checkedinbtn);
			lblScan.setText("Scan a product");
			btnScan.setImageResource(R.color.colorScanButton);

			btnScan.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(MainActivity.this, nfcActivity.class);
					startActivity(intent);
				}
			});
		}
		adapter.notifyDataSetChanged();
	}

	public Phase getCurrentPhase()
	{
		return Database.getInstance().getPhases().get(this.currentEmployee.getPhaseId());
	}

	@Override
	public void onEmployeesInitialized(Map<String, Employee> employees)
	{

	}

	@Override
	public void onOrdersInitialized(Map<String, Order> orders)
	{

	}

	@Override
	public void onPhasesInitialized(Map<String, Phase> phases)
	{
		List<Phase> listPhases = new ArrayList<>(phases.size());

		listPhases.addAll(phases.values());

		this.adapter = new production_proccess_list_adapter(this, listPhases);
		ListView productionProcessListView = (ListView) findViewById(R.id.production_proccess);
		productionProcessListView.setAdapter(this.adapter);
	}

	@Override
	public void onProductsInitialized(Map<String, Product> phases)
	{

	}

	@Override
	public void onProductPhasesInitialized(Map<String, ProductPhase> productPhases)
	{

	}

	@Override
	public void onUsersInitialized(Map<String, User> users)
	{

	}

	@Override
	public void onEmployeeAdded(Employee employee)
	{

	}

	@Override
	public void onEmployeeRemoved(Employee employee)
	{

	}

	@Override
	public void onEmployeeChanged(Employee employee)
	{

	}

	@Override
	public void onOrderAdded(Order order)
	{

	}

	@Override
	public void onOrderRemoved(Order order)
	{

	}

	@Override
	public void onOrderChanged(Order order)
	{

	}

	@Override
	public void onPhaseAdded(Phase phase)
	{

	}

	@Override
	public void onPhaseRemoved(Phase phase)
	{

	}

	@Override
	public void onPhaseChanged(Phase phase)
	{

	}

	@Override
	public void onProductAdded(Product product)
	{

	}

	@Override
	public void onProductRemoved(Product product)
	{

	}

	@Override
	public void onProductChanged(Product product)
	{

	}

	@Override
	public void onProductPhaseAdded(ProductPhase productPhase)
	{

	}

	@Override
	public void onProductPhaseRemoved(ProductPhase productPhase)
	{

	}

	@Override
	public void onProductPhaseChanged(ProductPhase productPhase)
	{

	}

	@Override
	public void onUserAdded(User user)
	{

	}

	@Override
	public void onUserRemoved(User user)
	{

	}

	@Override
	public void onUserChanged(User user)
	{

	}
}
