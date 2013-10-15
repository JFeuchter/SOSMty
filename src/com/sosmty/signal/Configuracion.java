package com.sosmty.signal;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

public class Configuracion extends Activity {
	ListView lv;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuracion);
		
		lv = (ListView) findViewById(R.id.lvconfiguracion);
		// Show the Up button in the action bar.
		String[] menu_perfil = getResources().getStringArray(R.array.menu_configuracion);
		lv.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, menu_perfil));
		
        final ImageView btnHome = (ImageView)findViewById(R.id.BtnHome);
        btnHome.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
	                  Intent intent = new Intent(Configuracion.this,MainActivity.class);
  		              startActivity(intent);
  			    	} 		        	 
  		         });
       final ImageView btnContacto = (ImageView)findViewById(R.id.BtnContacto);
       btnContacto.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
	                  Intent intent = new Intent(Configuracion.this,Contactos.class);
  		              startActivity(intent);
  			    	} 		        	 
  		         });

       final ImageView btnPerfil = (ImageView)findViewById(R.id.BtnPerfil);
       btnPerfil.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
           	 
				dialog_shownote = new Dialog(Configuracion.this);
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
 		                  Intent intent = new Intent(Configuracion.this, Perfil.class);
 				     	 
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

	}

	   
    public void LoadPassword(){
        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loadedpassword = "0000"; //sharedPreferences.getString("showpassword", "empty");
      }	

}
