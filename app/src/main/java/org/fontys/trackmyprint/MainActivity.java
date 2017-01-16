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
import org.fontys.trackmyprint.database.DatabaseException;
import org.fontys.trackmyprint.database.DatabaseListener;
import org.fontys.trackmyprint.database.entities.Employee;
import org.fontys.trackmyprint.database.entities.Order;
import org.fontys.trackmyprint.database.entities.Phase;
import org.fontys.trackmyprint.database.entities.Product;
import org.fontys.trackmyprint.database.entities.ProductPhase;
import org.fontys.trackmyprint.database.entities.User;
import org.fontys.trackmyprint.utils.Phone;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements DatabaseListener
{
	private Employee currentEmployee;
	private Product currentProduct;

	private List<Product> listProducts;

	private production_proccess_list_adapter adapter;

	private ProductPhaseListAdapter adapter;
	private ImageButton btnScan;
	private static MainActivity instance;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		btnScan = (ImageButton) findViewById(R.id.btnScan);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.getIndeterminateDrawable().setColorFilter(Color.rgb(235, 127, 0), PorterDuff.Mode.MULTIPLY);

		listProducts = new ArrayList<>();
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
			Database.getInstance().removeDatabaseListener(this);

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

		try
		{
			Database.getInstance().update(this.currentEmployee);
		}
		catch(DatabaseException e)
		{
			e.printStackTrace();
		}

		updateGUIPhase(p);
	}

	private void updateGUIPhase(Phase p){

		ImageView status = (ImageView) findViewById(R.id.check_in_status);
		TextView lblScan = (TextView) findViewById(R.id.lblScan);

		if(p == null)
		{
			status.setImageResource(R.drawable.checkin_status);
			lblScan.setText("Please check in to a sector");
			btnScan.setImageResource(R.color.colorProfileRectangle);
		}
		else
		{
			status.setImageResource(R.drawable.checkedin_btn);
			lblScan.setText("Scan a product");
			btnScan.setImageResource(R.color.colorScanButton);

			btnScan.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if(getCurrentPhase()!= null){
						Intent intent = new Intent(MainActivity.this, NFCActivity.class);
						startActivity(intent);
					}
				}
			});
		}
		adapter.notifyDataSetChanged();
	}

	public void setCurrentProduct(String productID) {
		System.out.println(productID);
		System.out.println(listProducts.size());
		currentProduct = Database.getInstance().getProducts().get(productID);

		currentProduct.addProductPhaseId(this.currentEmployee.getPhaseId());
		try
		{
			Database.getInstance().update(this.currentProduct);
		}
		catch(DatabaseException e)
		{
			e.printStackTrace();
		}
	}

	public Product getCurrentProduct() {
		return Database.getInstance().getProducts().get(this.currentProduct.getId());
	}

	public Phase getCurrentPhase()
	{
		return Database.getInstance().getPhases().get(this.currentEmployee.getPhaseId());
	}

	@Override
	public void onEmployeesInitialized(Map<String, Employee> employees)
	{
		this.currentEmployee = employees.get(Phone.getIMEI(getApplicationContext()));

		TextView employeeName = (TextView) findViewById(R.id.profile_name);
		employeeName.setText(this.currentEmployee.getName());

		ImageView employeeImage = (ImageView) findViewById(R.id.profile_image);

		try
		{
			Database.getInstance().downloadImage(Employee.class, this.currentEmployee.getId(),
												 getApplicationContext(), employeeImage);
		}
		catch(DatabaseException ex)
		{
			ex.printStackTrace();
		}

		updateGUIPhase(getCurrentPhase());
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

		this.adapter = new ProductPhaseListAdapter(this, listPhases);

		progressBar.setVisibility(View.GONE);

		ListView productionProcessListView = (ListView) findViewById(R.id.production_proccess);
		productionProcessListView.setAdapter(this.adapter);
	}

	@Override
	public void onProductsInitialized(Map<String, Product> products)
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
		listProducts.add(product);
	}

	@Override
	public void onProductRemoved(Product product)
	{
		for (Product p : listProducts) {
			if (p.getId() == product.getId()) {
				listProducts.remove(p);
			}
		}
	}

	@Override
	public void onProductChanged(Product product)
	{
		for (Product p : listProducts) {
			if (p.getId() == product.getId()) {
				listProducts.remove(p);
				listProducts.add(product);
			}
		}
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
