package com.example.ejercicio2_3;


import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ejercicio2_3.configuraciones.SQLiteConexion;
import com.example.ejercicio2_3.configuraciones.Transacciones;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity{
    static final int PETICION_CAM = 100;
    static final int TAKE_PIC_REQUEST = 101;
    Bitmap imgUser;
    SQLiteConexion conexion;
    Bitmap image;
    boolean foto;
    EditText Descripcion;
    Button Salvar,VerFotos;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
        foto=false;
        Descripcion = (EditText) findViewById(R.id.textDescripcion);
        Salvar = (Button) findViewById(R.id.btnSalvar);
        VerFotos = (Button) findViewById(R.id.btnVerFotos);
        imageView = (ImageView) findViewById(R.id.imageViewPhotograh);

        imageView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                permisos();
                return false;
            }
        });

        Salvar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!foto){
                    Toast.makeText(getApplicationContext(), "Tome una foto"
                            ,Toast.LENGTH_LONG).show();
                }else if(Descripcion.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Escriba una descripcion"
                            ,Toast.LENGTH_LONG).show();
                }else{
                    guardarfoto();
                    Descripcion.setText("");
                    foto=false;
                }

            }
        });

        VerFotos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), viewPhotograh.class);
                startActivity(intent);
            }
        });
    }

    private void guardarfoto(){
        SQLiteDatabase db = conexion.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(Transacciones.descripcion, Descripcion.getText().toString());
        String img= encodeImage(imgUser);
        valores.put(Transacciones.imagen, img);
        Long result = db.insert(Transacciones.tablaPhotograh, Transacciones.id, valores);
        Toast.makeText(getApplicationContext(), "Su foto se ha guardado correctamente"
                ,Toast.LENGTH_LONG).show();
        db.close();
    }

    private void permisos(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PETICION_CAM);
        }else{
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PETICION_CAM){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Se necesita los permisos de su camara", Toast.LENGTH_LONG).show();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePictureIntent, TAKE_PIC_REQUEST);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_PIC_REQUEST && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            image = (Bitmap) extras.get("data");
            imgUser = image;
            imageView.setImageBitmap(image);
            foto=true;
        }
    }

    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);

    }
}