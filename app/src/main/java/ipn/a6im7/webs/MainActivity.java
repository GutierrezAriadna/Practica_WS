package ipn.a6im7.webs;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void elegirServicio(View vw)
    {
        Intent servicio;
        switch(vw.getId())
        {
            case R.id.btnCalculadora:
                servicio=new Intent(this,Calculadora.class);
                finish();
                startActivity(servicio);
                break;
            case R.id.btnDivisa:
                servicio=new Intent(this,Divisas.class);
                finish();
                startActivity(servicio);
                break;
            case R.id.btnLogeo:
                servicio=new Intent(this,Logeo.class);
                finish();
                startActivity(servicio);
                break;
        }
    }
    public void llamada(View vw)
    {
        try {
            //new calculadoraWS().execute(num.getText().toString());
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error: 1 "+e.getMessage(),Toast
                    .LENGTH_LONG).show();
        }
    }

    public class calculadoraWS extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String respuesta="";
            try {
                String NAMESPACE = "http://ws/";
                String URL = "http://192.168.1.66:8080/cuadradoWS/cuadrado";
                String METHOD_NAME = "sqrt";
                String SOAP_ACTION = "http://ws/cuadrado/sqrt";// wsam:Action

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("number",strings[0]);
                SoapSerializationEnvelope envelope =
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);


                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapPrimitive result= (SoapPrimitive) envelope.getResponse();
                respuesta = result.toString();

            }
            catch(Exception e){
                Log.d("Err 1","AQUI",e);

            }
            return respuesta;
        }

        protected void onPostExecute(String result)
        {
            try {
                Toast.makeText(getApplicationContext(), "Resultado: " + result, Toast.LENGTH_LONG).show();
            }catch (Exception e)
            {
                Log.d("Err 2","AQUI",e);
            }
        }
    }
}