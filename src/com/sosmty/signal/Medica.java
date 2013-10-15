package com.sosmty.signal;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


public class Medica extends Activity {
	Spinner spSangre;
	EditText editTitular, editAsegurado,editAseguradora,editPoliza,editAlergias;
	DatePicker dpVencimiento;
	private Button btnGuardarMedica;
	private SQLiteDatabase baseDatos;
	 private static final String nombreBD = "sosmty";
	 private static final String TAG = "bdsosmty"; 
	 private static final String tblMedica = "medica";  	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medica);
		// Show the Up button in the action bar.
		abrirBasedatos();
		
		spSangre=(Spinner)findViewById(R.id.spsangre);
		dpVencimiento=(DatePicker)findViewById(R.id.vencimiento);
		editTitular=(EditText)findViewById(R.id.edittitular);
		editAsegurado=(EditText)findViewById(R.id.editasegurado);
		editAseguradora=(EditText)findViewById(R.id.editaseguradora);
		editPoliza=(EditText)findViewById(R.id.editpoliza);
		editAlergias=(EditText)findViewById(R.id.editalergias);
		btnGuardarMedica=(Button)findViewById(R.id.btnGuardarMedica);
		
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.tipo_sangre, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSangre.setAdapter(adapter);
        
		Cursor c = baseDatos.rawQuery("select * from medica where contacto=1", 
		        null);
		if(c.moveToFirst())
		{
			if(c.isNull(1)!=true) editTitular.setText(c.getString(1));
			if(c.isNull(2)!=true) editAsegurado.setText(c.getString(2));
			if(c.isNull(3)!=true) editAseguradora.setText(c.getString(3));
			if(c.isNull(4)!=true) editPoliza.setText(c.getString(4));
			if(c.isNull(5)!=true) spSangre.setSelection(c.getInt(5));
			if(c.isNull(6)!=true) 
			{
				dpVencimiento.updateDate(c.getInt(8), c.getInt(7), c.getInt(6));
			}
			else
			{
				dpVencimiento.updateDate(1990, 5, 1);
			}
			
			if(c.isNull(9)!=true) editAlergias.setText(c.getString(9));
		}
	    c.close();		
        
        btnGuardarMedica.setOnClickListener(new View.OnClickListener() 
	    {
  	      public void onClick(View v) 
  	      {
  	    	  
  	        boolean resultado = actualizaMecanica(editTitular.getText().toString(),
  	        		editAsegurado.getText().toString(),editAseguradora.getText().toString(),
  	        		dpVencimiento.getDayOfMonth(),dpVencimiento.getMonth(),dpVencimiento.getYear(),
  	        		spSangre.getSelectedItemPosition(),editAlergias.getText().toString(),
  	        		editPoliza.getText().toString());

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
                      Intent intent = new Intent(Medica.this,MainActivity.class);
    		              startActivity(intent);
    			    	} 		        	 
    		         });
       final ImageView btnContacto = (ImageView)findViewById(R.id.BtnContacto);
       btnContacto.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
                      Intent intent = new Intent(Medica.this,Contactos.class);
    		              startActivity(intent);
    			    	} 		        	 
    		         });
       final ImageView btnPerfil = (ImageView)findViewById(R.id.BtnPerfil);
       btnPerfil.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
                      Intent intent = new Intent(Medica.this,Perfil.class);
    		              startActivity(intent);
    			    	} 		        	 
    		         });  
       final ImageView btnConfig = (ImageView)findViewById(R.id.BtnConfig);
       btnConfig.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
                      Intent intent = new Intent(Medica.this,Configuracion.class);
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
	
	private boolean actualizaMecanica(String titular, String asegurado, String aseguradora,
			int dia, int mes, int yr, int sangre, String alergias, String poliza) 
	  {   
	    ContentValues values = new ContentValues();   
	    values.put("titular",titular );
	    values.put("asegurado",asegurado );
	    values.put("aseguradora",aseguradora );
	    values.put("dia",dia );
	    values.put("mes",mes );	    
	    values.put("yr",yr );
	    values.put("sangre",sangre );
	    values.put("alergias",alergias );
	    values.put("poliza",poliza );
	    return (baseDatos.update(tblMedica, values, null, null) > 0);   
	  }		



}
