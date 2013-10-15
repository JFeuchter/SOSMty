package com.sosmty.signal;

import java.io.File;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class SeguroAuto extends Activity {

	EditText editAseguradora, editTitular,editPoliza;
	TextView txtTitular,txtMarca,txtModelo,txtYr,txtColor,txtPlacas;
	private Button btnGuardarSeguroAuto;
	private SQLiteDatabase baseDatos;
	 private static final String nombreBD = "sosmty";
	 private static final String TAG = "bdsosmty"; 
	 private static final String tblSeguroAuto = "seguroauto";  
	 
	 private String selectedImagePath;
	 private ImageView img;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seguro_auto);

		abrirBasedatos();
		
		editAseguradora=(EditText)findViewById(R.id.edit_seg_car_aseguradora);
		editTitular=(EditText)findViewById(R.id.edit_seg_car_titlar);
		editPoliza=(EditText)findViewById(R.id.edit_seg_car_poliza);
		txtTitular=(TextView)findViewById(R.id.txt_mecanica_titular_);
		txtMarca=(TextView)findViewById(R.id.txt_mecanica_marca_);
		txtModelo=(TextView)findViewById(R.id.txt_mecanica_modelo_);
		txtYr=(TextView)findViewById(R.id.txt_mecanica_yr_);
		txtColor=(TextView)findViewById(R.id.txt_mecanica_color_);
		txtPlacas=(TextView)findViewById(R.id.txt_mecanica_placas_);
		img = (ImageView)findViewById(R.id.pic_mecanica_foto);
		btnGuardarSeguroAuto=(Button)findViewById(R.id.btnGuardarSeguroAuto);		

		Cursor c = baseDatos.rawQuery("select * from seguroauto where contacto=1", 
		        null);
		if(c.moveToFirst())
		{
			if(c.isNull(1)!=true) editAseguradora.setText(c.getString(1));
			if(c.isNull(2)!=true) editTitular.setText(c.getString(2));
			if(c.isNull(3)!=true) editPoliza.setText(c.getString(3));
		}
	    c.close();			
		
		c = baseDatos.rawQuery("select * from mecanica where contacto=1", 
		        null);
		if(c.moveToFirst())
		{
			if(c.isNull(1)!=true) txtTitular.setText(c.getString(1));
			if(c.isNull(2)!=true) txtMarca.setText(c.getString(2));
			if(c.isNull(3)!=true) txtModelo.setText(c.getString(3));
			if(c.isNull(4)!=true) txtYr.setText(c.getString(4));
			if(c.isNull(5)!=true) txtColor.setText(c.getString(5));
			if(c.isNull(7)!=true) txtPlacas.setText(c.getString(7));
		}
	    c.close();		
	    
		File imgFile = new  File("/sdcard/SOSMTY/auto.jpg");
		if(imgFile.exists()){

		    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		    img.setImageBitmap(myBitmap);
		}
		else
		{
			img.setImageResource(R.drawable.ic_launcher);
		}
		
	    btnGuardarSeguroAuto.setOnClickListener(new View.OnClickListener() 
	    {
  	      public void onClick(View v) 
  	      {
  	    	  
  	        boolean resultado = actualizaSeguroAuto(editAseguradora.getText().toString(),
  	        		editTitular.getText().toString(),editPoliza.getText().toString());

  	        if(resultado) 
  	          Toast.makeText(getApplicationContext(), 
  	        		  "Datos guardados correctamente", Toast.LENGTH_LONG).show();
  	        else 
  	          Toast.makeText(getApplicationContext(), 
  	        		  "No se han podido guardar los datos" ,Toast.LENGTH_LONG).show();             		
  	      }
  	    }); 
	      
	    final ImageView btnHome = (ImageView)findViewById(R.id.BtnHome);
	    btnHome.setOnClickListener(new OnClickListener() {
	         @Override
	         public void onClick(View v) {
	                  Intent intent = new Intent(SeguroAuto.this,MainActivity.class);
			              startActivity(intent);
				    	} 		        	 
			         });
	   final ImageView btnContacto = (ImageView)findViewById(R.id.BtnContacto);
	   btnContacto.setOnClickListener(new OnClickListener() {
	         @Override
	         public void onClick(View v) {
	                  Intent intent = new Intent(SeguroAuto.this,Contactos.class);
			              startActivity(intent);
				    	} 		        	 
			         });
	   final ImageView btnPerfil = (ImageView)findViewById(R.id.BtnPerfil);
	   btnPerfil.setOnClickListener(new OnClickListener() {
	         @Override
	         public void onClick(View v) {
	                  Intent intent = new Intent(SeguroAuto.this,Perfil.class);
			              startActivity(intent);
				    	} 		        	 
			         });  
	   final ImageView btnConfig = (ImageView)findViewById(R.id.BtnConfig);
	   btnConfig.setOnClickListener(new OnClickListener() {
	         @Override
	         public void onClick(View v) {
	                  Intent intent = new Intent(SeguroAuto.this,Configuracion.class);
			              startActivity(intent);
				    	} 		        	 
			         }); 	    
	}
	
	private void abrirBasedatos() 
	  {   
	    try 
	    {   
	      baseDatos = openOrCreateDatabase(nombreBD, 0, null);   
	    }    
	    catch (Exception e)
	    {   
	      Log.i(TAG, "Error al abrir o crear la base de datos" + e);   
	    }   
	  } 
	
	private boolean actualizaSeguroAuto(String aseguradora, String titular, String poliza) 
	  {   
	    ContentValues values = new ContentValues();   
	    values.put("aseguradora",aseguradora );
	    values.put("titular",titular );
	    values.put("poliza",poliza );
	    return (baseDatos.update(tblSeguroAuto, values, null, null) > 0);   
	  }	
	

}
