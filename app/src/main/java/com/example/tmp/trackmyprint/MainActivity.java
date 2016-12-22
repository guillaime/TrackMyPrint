package com.example.tmp.trackmyprint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.fontys.trackmyprint.database.Database;

public class MainActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			Database.initializeInstance();
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
