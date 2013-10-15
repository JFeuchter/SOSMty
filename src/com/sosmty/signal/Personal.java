package com.sosmty.signal;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.ComponentName;


public class Personal extends Activity {
	EditText editNombre, editTelefono,
	editCelular, editDireccion;
	Spinner spMunicipio,spSexo;
	DatePicker editFecha;

	private Button btnGuardarPersonal;
	private SQLiteDatabase baseDatos;
	 private static final String nombreBD = "sosmty";
	 private static final String TAG = "bdsosmty"; 
	 private static final String tblPersonal = "personal";  

		private Uri mImageCaptureUri;
		private ImageView img;
		
		private static final int PICK_FROM_CAMERA = 1;
		private static final int CROP_FROM_CAMERA = 2;
		private static final int PICK_FROM_FILE = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		// Show the Up button in the action bar.
		abrirBasedatos();
		
		img = (ImageView)findViewById(R.id.pic_personal_foto);
		editNombre=(EditText)findViewById(R.id.txtNombre);
		editTelefono=(EditText)findViewById(R.id.telefono);
		editCelular=(EditText)findViewById(R.id.celular);
		editFecha=(DatePicker)findViewById(R.id.fecha);
		editDireccion=(EditText)findViewById(R.id.direccion);
		spMunicipio=(Spinner)findViewById(R.id.municipio);
		spSexo=(Spinner)findViewById(R.id.sexo);
		btnGuardarPersonal=(Button)findViewById(R.id.btnGuardarPersonal);
		
		File imgFile = new  File("/sdcard/SOSMTY/perfil.jpg");
		if(imgFile.exists()){

		    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		    img.setImageBitmap(myBitmap);
		}
		else
		{
			img.setImageResource(R.drawable.ic_launcher);
		}
		
		
		 
	        ArrayAdapter adapter0 = ArrayAdapter.createFromResource(
	                this, R.array.municipios, android.R.layout.simple_spinner_item);
	        adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spMunicipio.setAdapter(adapter0);
	        
	        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(
	                this, R.array.sexos, android.R.layout.simple_spinner_item);
	        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spSexo.setAdapter(adapter1);	        
	    
		Cursor c = baseDatos.rawQuery("select * from personal where contacto=1", 
		        null);
		if(c.moveToFirst())
		{
			if(c.isNull(1)!=true) editNombre.setText(c.getString(1));
			if(c.isNull(2)!=true) editTelefono.setText(c.getString(2));
			if(c.isNull(3)!=true) editDireccion.setText(c.getString(3));
			if(c.isNull(5)!=true) editCelular.setText(c.getString(5));
			if(c.isNull(4)!=true) spMunicipio.setSelection(c.getInt(4));
			if(c.isNull(6)!=true)
			{
				editFecha.updateDate(c.getInt(8), c.getInt(7), c.getInt(6));
			}
			else
			{
				editFecha.updateDate(1990, 5, 1);
			}
			if(c.isNull(9)!=true) spSexo.setSelection(c.getInt(9));
			//if(c.isNull(10)!=true) img.setSelection(c.getInt(9));
		}
	    c.close();
	   
    
		btnGuardarPersonal.setOnClickListener(new View.OnClickListener() 
	    {
	      public void onClick(View v) 
	      {
  
	        boolean resultado = actualizaPersonal(editNombre.getText().toString(),
	        		editTelefono.getText().toString(),editCelular.getText().toString(),
	        		editFecha.getDayOfMonth(),editFecha.getMonth(),editFecha.getYear(),
	        		editDireccion.getText().toString(),spMunicipio.getSelectedItemPosition(),
	        		spSexo.getSelectedItemPosition());
	        
		    File myDir=new File("/sdcard/SOSMTY");
		    myDir.mkdirs();
		    String fname = "perfil.jpg";
		    File file = new File (myDir, fname);
		    if (file.exists ()) file.delete (); 
		    try {
		    	img.setDrawingCacheEnabled(true);
	            Bitmap finalBitmap = img.getDrawingCache();
		           FileOutputStream out = new FileOutputStream(file);
		           finalBitmap.compress(Bitmap.CompressFormat.JPEG, 95, out);
		           out.flush();
		           out.close();

		    } catch (Exception e) {
		           e.printStackTrace();
		    }
		    
	        if(resultado) 
	          Toast.makeText(getApplicationContext(), 
	        		  "Contacto guardado correctamente", Toast.LENGTH_LONG).show();
	        else 
	          Toast.makeText(getApplicationContext(), 
	        		  "No se ha podido guardar el contacto" ,Toast.LENGTH_LONG).show();             		
	      }
	    });
		
		
        final String [] items			= new String [] {"Desde camara", "Desde galería"};				
		ArrayAdapter<String> adapter	= new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder		= new AlertDialog.Builder(this);
		
		builder.setTitle("Seleccionar imagen");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int item ) { //pick from camera
				if (item == 0) {
					Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					
					mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
									   "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

					intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

					try {
						intent.putExtra("return-data", true);
						
						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
				} else { //pick from file
					Intent intent = new Intent();
					
	                intent.setType("image/*");
	                intent.setAction(Intent.ACTION_GET_CONTENT);
	                
	                startActivityForResult(Intent.createChooser(intent, "Completar accion usando"), PICK_FROM_FILE);
				}
			}
		} );
		
		final AlertDialog dialog = builder.create();
		
	    img.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
            	dialog.show();
            }
        });

	    final ImageView btnHome = (ImageView)findViewById(R.id.BtnHome);
	    btnHome.setOnClickListener(new OnClickListener() {
	         @Override
	         public void onClick(View v) {
	                  Intent intent = new Intent(Personal.this,MainActivity.class);
			              startActivity(intent);
				    	} 		        	 
			         });
	   final ImageView btnContacto = (ImageView)findViewById(R.id.BtnContacto);
	   btnContacto.setOnClickListener(new OnClickListener() {
	         @Override
	         public void onClick(View v) {
	                  Intent intent = new Intent(Personal.this,Contactos.class);
			              startActivity(intent);
				    	} 		        	 
			         });
	   final ImageView btnPerfil = (ImageView)findViewById(R.id.BtnPerfil);
	   btnPerfil.setOnClickListener(new OnClickListener() {
	         @Override
	         public void onClick(View v) {
	                  Intent intent = new Intent(Personal.this,Perfil.class);
			              startActivity(intent);
				    	} 		        	 
			         });  
	   final ImageView btnConfig = (ImageView)findViewById(R.id.BtnConfig);
	   btnConfig.setOnClickListener(new OnClickListener() {
	         @Override
	         public void onClick(View v) {
	                  Intent intent = new Intent(Personal.this,Configuracion.class);
			              startActivity(intent);
				    	} 		        	 
			         }); 
	}

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode != RESULT_OK) return;
	   
	    switch (requestCode) {
		    case PICK_FROM_CAMERA:
		    	doCrop();
		    	
		    	break;
		    	
		    case PICK_FROM_FILE: 
		    	mImageCaptureUri = data.getData();
		    	
		    	doCrop();
	    
		    	break;	    	
	    
		    case CROP_FROM_CAMERA:	    	
		        Bundle extras = data.getExtras();
	
		        if (extras != null) {	        	
		            Bitmap photo = extras.getParcelable("data");
		            
		            img.setImageBitmap(photo);
		        }
	
		        File f = new File(mImageCaptureUri.getPath());            
		        
		        if (f.exists()) f.delete();
	
		        break;

	    }
	}
    
    private void doCrop() {
		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
    	
    	Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        
        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );
        
        int size = list.size();
        
        if (size == 0) {	        
        	Toast.makeText(this, "No se encuentra la aplicacion", Toast.LENGTH_SHORT).show();
        	
            return;
        } else {
        	intent.setData(mImageCaptureUri);
            
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            
        	if (size == 1) {
        		Intent i 		= new Intent(intent);
	        	ResolveInfo res	= list.get(0);
	        	
	        	i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
	        	
	        	startActivityForResult(i, CROP_FROM_CAMERA);
        	} else {
		        for (ResolveInfo res : list) {
		        	final CropOption co = new CropOption();
		        	
		        	co.title 	= getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
		        	co.icon		= getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
		        	co.appIntent= new Intent(intent);
		        	
		        	co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
		        	
		            cropOptions.add(co);
		        }
	        
		        CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);
		        
		        AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        builder.setTitle("Elegir aplicacion");
		        builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
		            public void onClick( DialogInterface dialog, int item ) {
		                startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
		            }
		        });
	        
		        builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
		            @Override
		            public void onCancel( DialogInterface dialog ) {
		               
		                if (mImageCaptureUri != null ) {
		                    getContentResolver().delete(mImageCaptureUri, null, null );
		                    mImageCaptureUri = null;
		                }
		            }
		        } );
		        
		        AlertDialog alert = builder.create();
		        
		        alert.show();
        	}
        }
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
	
	
	private boolean actualizaPersonal(String nombre, String telefono, String celular,
			int dia, int mes, int yr, String direccion, int municipio, int sexo) 
	  {   
	    ContentValues values = new ContentValues();   
	    values.put("nombre",nombre );
	    values.put("telefono",telefono );
	    values.put("celular",celular );
	    values.put("dia",dia );
	    values.put("mes",mes );	    
	    values.put("yr",yr );
	    values.put("municipio",municipio );
	    values.put("sexo",sexo );
	    values.put("direccion",direccion );
	    return (baseDatos.update(tblPersonal, values, null, null) > 0);
	    
	    
	  }	
	

}
