package com.sosmty.signal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;


public class Perfil extends Activity 
implements OnItemClickListener{
	ListView lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfil);
		lv = (ListView) findViewById(R.id.ListView1);
		// Show the Up button in the action bar.
		String[] menu_perfil = getResources().getStringArray(R.array.menu_perfil);
		lv.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, menu_perfil));
		lv.setOnItemClickListener(this);
		
	    final ImageView btnHome = (ImageView)findViewById(R.id.BtnHome);
	    btnHome.setOnClickListener(new OnClickListener() {
	         @Override
	         public void onClick(View v) {
	                  Intent intent = new Intent(Perfil.this,MainActivity.class);
			              startActivity(intent);
				    	} 		        	 
			         });
	   final ImageView btnContacto = (ImageView)findViewById(R.id.BtnContacto);
	   btnContacto.setOnClickListener(new OnClickListener() {
	         @Override
	         public void onClick(View v) {
	                  Intent intent = new Intent(Perfil.this,Contactos.class);
			              startActivity(intent);
				    	} 		        	 
			         });
	   final ImageView btnConfig = (ImageView)findViewById(R.id.BtnConfig);
	   btnConfig.setOnClickListener(new OnClickListener() {
	         @Override
	         public void onClick(View v) {
	                  Intent intent = new Intent(Perfil.this,Configuracion.class);
			              startActivity(intent);
				    	} 		        	 
			         }); 
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

		switch(position)
		{
		case 0:
			Intent intent = new Intent(Perfil.this, Personal.class);
			startActivity(intent);
			break;
		case 1:
			Intent intent1 = new Intent(Perfil.this, Medica.class);
			startActivity(intent1);
			break;
		case 2:
			Intent intent2 = new Intent(Perfil.this, Mecanica.class);
			startActivity(intent2);
			break;			
		case 3:
			Intent intent3 = new Intent(Perfil.this, SeguroAuto.class);
			startActivity(intent3);
			break;			
		}
		

		
	}



}
