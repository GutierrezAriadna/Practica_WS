package ipn.a6im7.webs;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class Logeo extends Activity {

    EditText psw;
    EditText user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logeo);

        psw=(EditText)findViewById(R.id.txtPsw);
        user=(EditText)findViewById(R.id.txtUser);
    }

    //Método que valida los datos y ejecuta la tarea asíncrona para consumir el Web Service
    public void llamarServicioLogeo(View vw)
    {
        String usuario=user.getText().toString();
        String password=psw.getText().toString();
        if(!usuario.isEmpty() && !password.isEmpty())
        {
            new wsLogeo().execute(usuario,password);
        }
        else
            Toast.makeText(getApplicationContext(),"No dejes ningún campo vacío",Toast.LENGTH_LONG).show();
    }

    /*Clase que contiene la tarea asíncrona que consume el web Service
    y muestra el resultado del logeo al usuario
     */
    private class wsLogeo extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... strings) {
            String respuesta="";
            try {
                String NAMESPACE = "http://serviciosWeb/";
                String URL = "http://192.168.1.76:8080/webServices/wsLog";
                String METHOD_NAME = "entrar";
                String SOAP_ACTION = "http://serviciosWeb/wsLog/entrar";// wsam:Action

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("usr",strings[0]);
                request.addProperty("pass",strings[1]);
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
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
        }
    }
}

