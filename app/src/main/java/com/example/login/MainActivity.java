package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    EditText corr,pass;
    Button acces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        corr= (EditText)findViewById(R.id.correo);
        pass=(EditText)findViewById(R.id.contra);

        acces=(Button)findViewById(R.id.acceso);

        acces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Thread tr=new Thread() {
                    @Override
                    public void run() {
                        final String res=enviarPost(corr.getText().toString(), pass.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int r=objJSON(res);
                                if(r>0){
                                    Intent i=new Intent(getApplicationContext(),Home.class);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(getApplicationContext(),"Usuario o Contraseña Incorrecto",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                };
                tr.start();

            }
        });
    }

    public String enviarPost(String cor, String pas){
        String parametros="cor="+cor+"&pas="+pas;
        HttpURLConnection conection=null;
        String respuesta="";
        try{
            URL url=new URL("http:/aqui colocas la ip de tu PC//WebService/valida.php");
            conection=(HttpURLConnection)url.openConnection();
            conection.setRequestMethod("POST");
            conection.setRequestProperty("Content-Length",""+Integer.toString(parametros.getBytes().length));

            conection.setDoOutput(true);
            DataOutputStream wr=new DataOutputStream(conection.getOutputStream());
            wr.writeBytes(parametros);
            wr.close();

            Scanner inStream=new Scanner(conection.getInputStream());

            while (inStream.hasNextLine())
                respuesta+=(inStream.nextLine());


        }catch (Exception e){}
            return respuesta.toString();
        }

        public int objJSON(String rspta){
        int res=0;
        try {
            JSONArray json = new JSONArray(rspta);
            if(json.length()>0)
                res=1;
        }catch(Exception e){}
        return res;
        }
}


