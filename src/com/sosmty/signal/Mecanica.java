package com.sosmty.signal;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Mecanica extends Activity {

	EditText editTitular, editMarca,editModelo,editYr,editColor,editPlacas,editInformacion;
	private Button btnGuardarMecanica;
	private SQLiteDatabase baseDatos;
	 private static final String nombreBD = "sosmty";
	 private static final String TAG = "bdsosmty"; 
	 private static final String tblMecanica = "mecanica";  	
	 private static final int SELECT_PICTURE = 1;
	    
		private Uri mImageCaptureUri;
		private ImageView img;
		
		private static final int PICK_FROM_CAMERA = 1;
		private static final int CROP_FROM_CAMERA = 2;
		private static final int PICK_FROM_FILE = 3;
	 	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mecanica);
		abrirBasedatos();
		img = (ImageView)findViewById(R.id.pic_mecanica_foto);
		editTitular=(EditText)findViewById(R.id.edit_mecanica_titular);
		editMarca=(EditText)findViewById(R.id.edit_mecanica_marca);
		editModelo=(EditText)findViewById(R.id.edit_mecanica_modelo);
		editYr=(EditText)findViewById(R.id.edit_mecanica_yr);
		editColor=(EditText)findViewById(R.id.edit_mecanica_color);
		editPlacas=(EditText)findViewById(R.id.edit_mecanica_placas);
		editInformacion=(EditText)findViewById(R.id.edit_mecanica_informacion);
		btnGuardarMecanica=(Button)findViewById(R.id.btnGuardarMecanica);
		
		Cursor c = baseDatos.rawQuery("select * from mecanica where contacto=1", 
		        null);
		if(c.moveToFirst())
		{
			if(c.isNull(1)!=true) editTitular.setText(c.getString(1));
			if(c.isNull(2)!=true) editMarca.setText(c.getString(2));
			if(c.isNull(3)!=true) editModelo.setText(c.getString(3));
			if(c.isNull(4)!=true) editYr.setText(c.getString(4));
			if(c.isNull(5)!=true) editColor.setText(c.getString(5));
			if(c.isNull(7)!=true) editPlacas.setText(c.getString(7));
			if(c.isNull(8)!=true) editInformacion.setText(c.getString(8));
			
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
	    
	    img.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Seleccionar imagen"), SELECT_PICTURE);
            }
        });
	    
        btnGuardarMecanica.setOnClickListener(new View.OnClickListener() 
	    {
  	      public void onClick(View v) 
  	      {
  	    	  
  	        boolean resultado = actualizaMecanica(editTitular.getText().toString(),
  	        		editMarca.getText().toString(),editModelo.getText().toString(),
  	        		editYr.getText().toString(),editColor.getText().toString(),
  	        		editPlacas.getText().toString(),editInformacion.getText().toString());
  	        
		    File myDir=new File("/sdcard/SOSMTY");
		    myDir.mkdirs();
		    String fname = "auto.jpg";
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
  	        		  "Datos guardados correctamente", Toast.LENGTH_LONG).show();
  	        else 
  	          Toast.makeText(getApplicationContext(), 
  	        		  "No se han podido guardar los datos" ,Toast.LENGTH_LONG).show();             		
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
                  Intent intent = new Intent(Mecanica.this,MainActivity.class);
		              startActivity(intent);
			    	} 		        	 
		         });
   final ImageView btnContacto = (ImageView)findViewById(R.id.BtnContacto);
   btnContacto.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
                  Intent intent = new Intent(Mecanica.this,Contactos.class);
		              startActivity(intent);
			    	} 		        	 
		         });
   final ImageView btnPerfil = (ImageView)findViewById(R.id.BtnPerfil);
   btnPerfil.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
                  Intent intent = new Intent(Mecanica.this,Perfil.class);
		              startActivity(intent);
			    	} 		        	 
		         });  
   final ImageView btnConfig = (ImageView)findViewById(R.id.BtnConfig);
   btnConfig.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
                  Intent intent = new Intent(Mecanica.this,Configuracion.class);
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
	
	private boolean actualizaMecanica(String titular, String marca, String modelo,
			String yr, String color, String placas, String informacion) 
	  {   
	    ContentValues values = new ContentValues();   
	    values.put("titular",titular );
	    values.put("marca",marca );
	    values.put("modelo",modelo );
	    values.put("yr",yr );
	    values.put("color",color );
	    values.put("placas",placas );
	    values.put("informacion",informacion );
	    return (baseDatos.update(tblMecanica, values, null, null) > 0);   
	  }	


}
