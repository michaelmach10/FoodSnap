package pe.mach.foodsnap;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;


public class Pedido extends ActionBarActivity {

    TextView comida;
    EditText etCantidad, etTelefono, etDireccion;
    Button btnEnviar;
    //String cantidad;
    String[] pedido = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        comida = (TextView) findViewById(R.id.tvNombreComida);
        etCantidad = (EditText) findViewById(R.id.etCantidad);
        etDireccion = (EditText) findViewById(R.id.etDireccion);
        etTelefono = (EditText) findViewById(R.id.etTelefono);
        btnEnviar = (Button) findViewById(R.id.btnEnviarPedido);

        comida.setText(getIntent().getExtras().getString("comida"));

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cantidad = etCantidad.getText().toString();
                //pedido = ["romerito",comida.getText().toString(),etCantidad.getText().toString()];
                pedido[0] = getIntent().getExtras().getString("restaurant");
                pedido[1] = comida.getText().toString();
                pedido[2] = etCantidad.getText().toString();
                pedido[3] = etDireccion.getText().toString();
                pedido[4] = etTelefono.getText().toString();
                try{
                    //new Tarea().execute(cantidad);
                    new Tarea().execute(pedido);
                }catch (Exception e){

                }
                Toast.makeText(getApplicationContext(), "Pedido enviado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class Tarea extends AsyncTask<String, Void, Void> {
        //String cant;
        String[] ped = new String[5];

        @Override
        protected Void doInBackground(String... params) {
            //cant = params[0];
            ped = params;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost envio = new HttpPost("http://192.168.56.1/foodsnap/insertar_pedido.php");
            try{
                List<NameValuePair> pedido = new ArrayList<NameValuePair>();
                pedido.add(new BasicNameValuePair("restaurant", ped[0]));
                pedido.add(new BasicNameValuePair("comida", ped[1]));
                pedido.add(new BasicNameValuePair("cantidad", ped[2]));
                pedido.add(new BasicNameValuePair("direccion", ped[3]));
                pedido.add(new BasicNameValuePair("telefono", ped[4]));
                //pedido.add(new BasicNameValuePair("cantidad", cant));
                envio.setEntity(new UrlEncodedFormEntity(pedido, HTTP.UTF_8));
                HttpResponse response = httpClient.execute(envio);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //Log.d("restaurant", ped[0]);
            //Log.d("comida", ped[1]);
            //Log.d("cantidad", ped[2]);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pedido, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
