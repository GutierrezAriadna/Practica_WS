package ipn.a6im7.webs;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class Divisas extends Activity {

    EditText monto;
    Spinner divisas;
    String tipoCambio="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divisas);
        monto=(EditText)findViewById(R.id.txtCantidad);
        divisas=(Spinner)findViewById(R.id.listaDivisas);
        initListenerDivisas();
    }

    //Se inicia el listener del spinner y se obtiene el id de la divisa cada que se hace una
    //selección
    public void initListenerDivisas()
    {
        try {
            divisas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
                    if(i!=0) {
                        tipoCambio = ""+i;
                        Toast.makeText(adapterView.getContext(), adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
                    }else
                        tipoCambio="";
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    tipoCambio = "";
                }
            });
        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Método que ejecuta el async task para consumir el web service
    public void llamarServicioDivisas(View vw)
    {
        String cantidad=monto.getText().toString();
        if(tipoCambio.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Elige una divisa",Toast.LENGTH_LONG).show();
        }
        else
        if(cantidad.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Necesitas introducir una cantidad a convertir",Toast.LENGTH_LONG).show();
        }
        else
            new wsDivisas().execute(tipoCambio,cantidad);
    }

    /*Esta clase es la que realiza la llamada al web service REST
    y muestra el resultado obtenido al usuario
     */
    private class wsDivisas extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String result="";
            HttpClient cliente=new DefaultHttpClient();
            HttpGet peticion=new HttpGet("http://192.168.1.76:8080/webServices/webresources/div/convertir/moneda="+strings[0]+"&pesos=" +
                    strings[1]+"");
            peticion.setHeader("content-type","text/plain");
            try {
                HttpResponse res=cliente.execute(peticion);
                result= EntityUtils.toString(res.getEntity());
                Log.d("Hey!",""+res.getEntity());
            } catch (IOException e) {
                Log.d("Error1:","AQUI!",e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(),"Resultado: "+s,Toast.LENGTH_LONG).show();
        }
    }
}

