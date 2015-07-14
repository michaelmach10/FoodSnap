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
import android.widget.SearchView;

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


public class MainActivity extends ActionBarActivity {

    ListView lista;
    SearchView searchView;
    String comida, consulta;
    ArrayList<String> restaurantes = new ArrayList<String>();
    ArrayList<String> latitudes = new ArrayList<String>();
    ArrayList<String> longitudes = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = (ListView) findViewById(R.id.lvRestaurantes);
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("Busca tu plato favorito");
        //comida = searchView.getQuery().toString();
        //comida = "pollo";

        /*Tarea tarea = new Tarea();
        tarea.cargarContenido(getApplicationContext());
        tarea.execute(lista);*/

        /*lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it;
                it = new Intent(getApplicationContext(), Comidas.class);
                //Log.d("restaurante", String.valueOf(lista.get));
                //it.putExtra("restaurant",  lista.getSelectedItem().toString());
                startActivity(it);
            }
        });*/

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.d("comida", searchView.getQuery().toString());
                comida = searchView.getQuery().toString();

                Tarea tarea = new Tarea();
                tarea.cargarContenido(getApplicationContext());
                tarea.execute(lista);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    class Tarea extends AsyncTask<ListView, Void, ArrayAdapter<String>> {

        Context contexto;
        ListView list;
        InputStream is;
        ArrayList<String> listaRestaurantes = new ArrayList<String>();
        ArrayList<String> listaLatitud = new ArrayList<String>();
        ArrayList<String> listaLongitud = new ArrayList<String>();


        public void cargarContenido(Context contexto){
            this.contexto = contexto;
        }

        @Override
        protected ArrayAdapter<String> doInBackground(ListView... params) {
            list = params[0];
            String resultado = "fallo";
            String rest, lat, lng;

            //Crear la conexion HTTP
            HttpClient httpClient = new DefaultHttpClient();
            //HttpGet peticion = new HttpGet("http://192.168.56.1/foodsnap/listar.php");
            HttpGet peticion = new HttpGet("http://192.168.56.1/foodsnap/listar_restaurante.php?consulta="+comida+"");
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
                    listaRestaurantes.add(rest);
                    lat = jsonObject.getString("latitud");
                    listaLatitud.add(lat);
                    lng = jsonObject.getString("longitud");
                    listaLongitud.add(lng);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(contexto, android.R.layout.simple_list_item_checked, listaRestaurantes);

            return adaptador;
        }

        @Override
        protected void onPostExecute(ArrayAdapter<String> result){
            list.setAdapter(result);
            list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            restaurantes = listaRestaurantes;
            latitudes = listaLatitud;
            longitudes = listaLongitud;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent it;
        switch (item.getItemId()) {
            case R.id.mMapa:
                it = new Intent(getApplicationContext(), Mapa.class);
                it.putExtra("latitud", latitudes.get(lista.getCheckedItemPosition()).toString());
                it.putExtra("longitud", longitudes.get(lista.getCheckedItemPosition()).toString());
                startActivity(it);
                break;
            case R.id.mPlatos:
                it = new Intent(getApplicationContext(), Comidas.class);
                it.putExtra("restaurante", restaurantes.get(lista.getCheckedItemPosition()).toString());
                startActivity(it);
                break;
            case R.id.mReservar:
                it = new Intent(getApplicationContext(), Reserva.class);
                startActivity(it);
                break;
        }
        return true;
    }
}
