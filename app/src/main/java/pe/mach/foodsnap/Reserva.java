package pe.mach.foodsnap;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TimePicker;


public class Reserva extends ActionBarActivity {

    NumberPicker numero;
    DatePicker dia;
    TimePicker hora;
    Button btnReserva;
    String fecha, horario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);

        numero = (NumberPicker) findViewById(R.id.numberPicker);
        dia = (DatePicker) findViewById(R.id.datePicker);
        hora = (TimePicker) findViewById(R.id.timePicker);
        btnReserva = (Button) findViewById(R.id.btnReserva);

        numero.setMinValue(1);
        numero.setMinValue(10);

        btnReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("numero", String.valueOf(numero.getValue()));
                Log.d("dia", String.valueOf(dia.getYear())+"-"+String.valueOf(dia.getMonth())+"-"+String.valueOf(dia.getDayOfMonth()));
                Log.d("hora", String.valueOf(hora.getCurrentHour())+":"+String.valueOf(hora.getCurrentMinute())+":00");
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reserva, menu);
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
