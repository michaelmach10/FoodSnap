package pe.mach.foodsnap;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Comidas extends ActionBarActivity {

    ListView lista;
    ArrayList<String> comidas = new ArrayList<String>();
    String restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comidas);

        lista = (ListView) findViewById(R.id.lvComidas);

        Tarea tarea = new Tarea();
        tarea.cargarContenido(getApplicationContext());
        tarea.execute(lista);
    }

    class Tarea extends AsyncTask<ListView, Void, ArrayAdapter<String>> {

        Context contexto;
        ListView list;
        InputStream is;
        ArrayList<String> listaComidas = new ArrayList<String>();
        String restaurante = getIntent().getExtras().getString("restaurante");
        //String restaurante = "romerito";

        public void cargarContenido(Context contexto){
            this.contexto = contexto;
        }

        @Override
        protected ArrayAdapter<String> doInBackground(ListView... params) {
            list = params[0];
            String resultado = "fallo";
            return obtenerDatos();
            //String array[] = return obtenerDatos();
        }

        @Override
        protected void onPostExecute(ArrayAdapter<String> result){
            list.setAdapter(result);
            list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            comidas = listaComidas;
            restaurant = restaurante;
            //String comida = listaComidas.get(lista.getCheckedItemPosition());
            //System.out.print(comida);
        }

        public ArrayAdapter<String> obtenerDatos(){

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet peticion = new HttpGet("http://192.168.56.1/foodsnap/listar_comida.php?restaurante='"+restaurante+"'");
            //HttpGet peticion = new HttpGet("http://192.168.56.1/foodsnap/listar_comida.php?restaurante='romerito'");
            String resultado = "Fallo";
            String rest;
            try{
                HttpResponse response = httpClient.execute(peticion);
                HttpEntity contenido = response.getEntity();
                is = contenido.getContent();
            } catch (ClientProtocolException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

            BufferedReader lector = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String linea = null;
            try{
                while ((linea = lector.readLine())!=null){
                    sb.append(linea);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            try{
                is.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            resultado = sb.toString();

            try{
                JSONArray jsonArray = new JSONArray(resultado);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    rest = jsonObject.getString("nombre");
                    listaComidas.add(rest);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(contexto, android.R.layout.simple_list_item_checked, listaComidas);
            //ComidasAdapter adaptador = new ComidasAdapter(contexto, android.R.layout.simple_list_item_checked, listaComidas);

            return adaptador;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comidas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent it;
        switch (item.getItemId()) {
            case R.id.mReserva:
                it = new Intent(getApplicationContext(), Reserva.class);
                startActivity(it);
                break;
            case R.id.mPedido:
                it = new Intent(getApplicationContext(), Pedido.class);
                //it.putExtra("comida", String.valueOf(lista.getCheckedItemPosition()));
                it.putExtra("comida", comidas.get(lista.getCheckedItemPosition()).toString());
                it.putExtra("restaurant", restaurant);
                startActivity(it);
                break;
            case R.id.mIr:
                it = new Intent(getApplicationContext(), Mapa.class);
                startActivity(it);
                break;
        }
        return true;
    }
}
