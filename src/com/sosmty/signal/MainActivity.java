package com.sosmty.signal;

import android.net.Uri;
import android.os.Bundle;
//import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends Activity {
	
	 private SQLiteDatabase baseDatos;
	 private static final String nombreBD = "sosmty";
	 private static final String TAG = "bdsosmty"; 
	 private static final String tblPersonal = "personal"; 
	 private static final String tblMedica = "medica"; 
	 private static final String tblMecanica = "mecanica"; 
	 private static final String tblSeguroAuto = "seguroauto"; 

	 	Spinner spMposServ;
		Button btn, btn2;
		Dialog dialog_pass;
		CheckBox cbShowPass, cbShowPass2;
		SharedPreferences sharedPreferences;
		
		EditText pass_current_et, pass_new_et, pass_confirm_et;
		String pass_current, pass_new, pass_confirm;
		String getpass;
		String pass_op;
		String loadedpassword;
		
		Dialog dialog_shownote;
		EditText et_showpass;	
		
	  private static final String creartblPersonal = "create table if not exists "  
		  + " personal (contacto int null, "  
		  + " nombre text null, telefono text null, " 
		  + " direccion text null, municipio integer null, "
	  	  + " celular text null, dia integer null, mes integer null, yr integer null, "
		  + " sexo int null, foto text null);"; 

	  private static final String creartblMedica = "create table if not exists "  
			  + " medica (contacto int null, "  
			  + " titular text null, asegurado text null, " 
			  + " aseguradora text null, poliza text null, "
		  	  + " sangre integer null, dia integer null, mes integer null, yr integer null, "
			  + " alergias text null);"; 

	  private static final String creartblMecanica = "create table if not exists "  
			  + " mecanica (contacto int null, "  
			  + " titular text null, marca text null, " 
			  + " modelo text null, yr text null, "
		  	  + " color text null, foto text null, "
			  + " placas text null, informacion text null);";

	  private static final String creartblSeguroAuto = "create table if not exists "  
			  + " seguroauto (contacto int null, "  
			  + " aseguradora text null, titular text null, " 
			  + " poliza text null);";

	private void abrirBasedatos() 
	  {   
	    try 
	    {   
	      baseDatos = openOrCreateDatabase(nombreBD, 0, null);   
	      baseDatos.execSQL(creartblPersonal);
	      baseDatos.execSQL(creartblMedica);
	      baseDatos.execSQL(creartblMecanica);
	      baseDatos.execSQL(creartblSeguroAuto);
	      if(!ExistsPersonal()) insertarFilaPersonal();
	      if(!ExistsMedica()) insertarFilaMedica();
	      if(!ExistsMecanica()) insertarFilaMecanica();
	      if(!ExistsSeguroAuto()) insertarFilaSeguroAuto();
	      
	    }    
	    catch (Exception e)
	    {   
	      Log.i(TAG, "Error al abrir o crear la base de datos" + e);   
	    }   
	  }  
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		abrirBasedatos();
		
		spMposServ = (Spinner)findViewById(R.id.sp_mpos_servicios);
	    ArrayAdapter adapter = ArrayAdapter.createFromResource(
	            this, R.array.mpos_servicios, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spMposServ.setAdapter(adapter);
	    
	    
        final ImageView btnPolicia = (ImageView)findViewById(R.id.btnPolicia);
        
        btnPolicia.setOnTouchListener(new OnTouchListener(){
        	@Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == (MotionEvent.ACTION_UP)){
                	btnPolicia.setImageResource(R.drawable.policia);
                	Llamar();
                }
                else{
                	btnPolicia.setImageResource(R.drawable.policia_);
                }
                return true;
            }
        });
        
        final ImageView btnAmbulancia = (ImageView)findViewById(R.id.btnAmbulancia);
        
        btnAmbulancia.setOnTouchListener(new OnTouchListener(){
        	@Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == (MotionEvent.ACTION_UP)){
                	btnAmbulancia.setImageResource(R.drawable.ambulancias);
                	Llamar();
                }
                else{
                	btnAmbulancia.setImageResource(R.drawable.ambulancias_);
                }
                return true;
            }
        });

        final ImageView btnBomberos = (ImageView)findViewById(R.id.btnBomberos);
        
        btnBomberos.setOnTouchListener(new OnTouchListener(){
        	@Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == (MotionEvent.ACTION_UP)){
                	btnBomberos.setImageResource(R.drawable.bomberos);
                	Llamar();
                }
                else{
                	btnBomberos.setImageResource(R.drawable.bomberos_);
                }
                return true;
            }
        });
        
        
        final ImageView btnGrua = (ImageView)findViewById(R.id.btnGrua);
        
        btnGrua.setOnTouchListener(new OnTouchListener(){
        	@Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == (MotionEvent.ACTION_UP)){
                	btnGrua.setImageResource(R.drawable.grua);
                	Llamar();
                }
                else{
                	btnGrua.setImageResource(R.drawable.grua_);
                }
                return true;
            }
        });
        
        
        final ImageView btnPerfil = (ImageView)findViewById(R.id.BtnPerfil);

   		
        btnPerfil.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
            	 
 				dialog_shownote = new Dialog(MainActivity.this);
       			dialog_shownote.setContentView(R.layout.perfil_login);


       		
       			dialog_shownote.setTitle("Contraseña");
       			dialog_shownote.setCancelable(true);
    			 
       			et_showpass = (EditText) dialog_shownote.findViewById(R.id.EditText_Password);
       			cbShowPass = (CheckBox) dialog_shownote.findViewById(R.id.cb_password);
       			cbShowPass.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
		        	public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) { 
		        		
		    			if (buttonView.isChecked()) { 
		    				et_showpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		        		}
		        		else
		        		{
		        			et_showpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		        		}
						
					}
				});
	
       			Button btn_ok = (Button) dialog_shownote.findViewById(R.id.btn_okPass);
       			btn_ok.setOnClickListener(new View.OnClickListener() {
  					@Override
  					public void onClick(View v) {
  					
  						LoadPassword();
						getpass = loadedpassword; 
						
  				     	if (et_showpass.getText().toString().equals(getpass))
  				     	{
  		                  Intent intent = new Intent(MainActivity.this, Perfil.class);
  				     	 
  		                  startActivity(intent);
  		                  dialog_shownote.dismiss();
  				     	}
  				     	else
  				     	{
  				     		et_showpass.setError("Contraseña Incorrecta");
  				     	}
  						
  			    	}
           	    	});
  						
  			
  		         Button btn_Cancel = (Button) dialog_shownote.findViewById(R.id.btn_cancelPass);
  		         btn_Cancel.setOnClickListener(new View.OnClickListener() {

  					@Override
  					public void onClick(View v) {
  						dialog_shownote.dismiss();
  					}
  		        	 
  		         });
  		      
    			
    			dialog_shownote.show();        				


             }
        });

        final ImageView btnContacto = (ImageView)findViewById(R.id.BtnContactos);
        btnContacto.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
	                  Intent intent = new Intent(MainActivity.this, Contactos.class);
  		              startActivity(intent);
  			    	} 		        	 
  		         });
  		      
        final ImageView btnConfig = (ImageView)findViewById(R.id.BtnConfiguracion);
        btnConfig.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
	                  Intent intent = new Intent(MainActivity.this, Configuracion.class);
  		              startActivity(intent);
  			    	} 		        	 
  		         });    			
    			

	}
	
	private void Llamar() {
	    try {
	        Intent callIntent = new Intent(Intent.ACTION_CALL);
	        callIntent.setData(Uri.parse("tel:060"));
	        startActivity(callIntent);
	    } catch (ActivityNotFoundException e) {
	        Log.e("Error en llamada", "Error en llamada", e);
	    }
	}
	
	public boolean ExistsPersonal() {
		   Cursor cursor = baseDatos.rawQuery("select 1 from personal where contacto=1", 
		        null);
		   boolean exists = (cursor.getCount() > 0);
		   cursor.close();
		   return exists;
	}
	
	 private boolean insertarFilaPersonal() 
	  {   
	    ContentValues values = new ContentValues();   
	    values.put("contacto", 1 );   
	    return (baseDatos.insert(tblPersonal, null, values) > 0);   
	  }	
	 
		public boolean ExistsMedica() {
			   Cursor cursor = baseDatos.rawQuery("select 1 from medica where contacto=1", 
			        null);
			   boolean exists = (cursor.getCount() > 0);
			   cursor.close();
			   return exists;
		}
		
		 private boolean insertarFilaMedica() 
		  {   
		    ContentValues values = new ContentValues();   
		    values.put("contacto", 1 );   
		    return (baseDatos.insert(tblMedica, null, values) > 0);   
		  }	

		public boolean ExistsMecanica() {
				   Cursor cursor = baseDatos.rawQuery("select 1 from mecanica where contacto=1", 
				        null);
				   boolean exists = (cursor.getCount() > 0);
				   cursor.close();
				   return exists;
			}
			
		private boolean insertarFilaMecanica() 
			  {   
			    ContentValues values = new ContentValues();   
			    values.put("contacto", 1 );   
			    return (baseDatos.insert(tblMecanica, null, values) > 0);   
			  }	

		public boolean ExistsSeguroAuto() {
			   Cursor cursor = baseDatos.rawQuery("select 1 from seguroauto where contacto=1", 
			        null);
			   boolean exists = (cursor.getCount() > 0);
			   cursor.close();
			   return exists;
		}
		
	private boolean insertarFilaSeguroAuto() 
		  {   
		    ContentValues values = new ContentValues();   
		    values.put("contacto", 1 );   
		    return (baseDatos.insert(tblSeguroAuto, null, values) > 0);   
		  }	
	

	   
	    public void LoadPassword(){
	        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	        loadedpassword = "0000"; //sharedPreferences.getString("showpassword", "empty");
	      }	
}
