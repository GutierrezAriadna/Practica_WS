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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class Calculadora extends Activity {

    public String operador="";

    EditText primerNumero;
    EditText segundoNumero;
    Spinner listaOps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);
        primerNumero=(EditText)findViewById(R.id.numA);
        segundoNumero=(EditText)findViewById(R.id.numB);
        listaOps=(Spinner)findViewById(R.id.operaciones);
        initListenerOperaciones();
    }

    //Método que valida los datos e inicia la async task para consumir el servicio
    public void llamarServicioCalculadora(View vw)
    {
        String numA=primerNumero.getText().toString();
        String numB=segundoNumero.getText().toString();
        if(numA.isEmpty() || numB.isEmpty())
            Toast.makeText(getApplicationContext(),"Se necesitan dos números. Asegúrate de escribir en ambos campos",Toast.LENGTH_LONG).show();
        else
        if(operador.equals(""))
            Toast.makeText(getApplicationContext(),"Elige una operación",Toast.LENGTH_LONG).show();
        else
            new wsCalculadora().execute(numA,numB,operador);

    }

    //Método que guarda la operación cada que se hace una selección en el spinner
    public void initListenerOperaciones()
    {
        try {
            listaOps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
                    if(i!=0) {
                        operador = ""+i;

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    operador = "";
                }
            });
        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /*Esta clase es la tarea asíncrona que llama al web service y obtiene el resultado
    de la operación para mostrarlo al usuario
     */
    private class wsCalculadora extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String respuesta="";
            try {
                String NAMESPACE = "http://serviciosWeb/";
                String URL = "http://192.168.1.76:8080/webServices/calc";
                String METHOD_NAME = "resultado";
                String SOAP_ACTION = "http://serviciosWeb/calc/resultado";// wsam:Action

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("primerNum",strings[0]);
                request.addProperty("segundoNum",strings[1]);
                request.addProperty("operacion",strings[2]);
                SoapSerializationEnvelope envelope =
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapPrimitive result= (SoapPrimitive) envelope.getResponse();
                respuesta = result.toString();
            }
            catch(Exception e){
                Log.e("Err 1","AQUI",e);
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(String results) {
            Toast.makeText(getApplicationContext(),"El resultado es: "+results,Toast.LENGTH_LONG).show();
        }
    }
}

