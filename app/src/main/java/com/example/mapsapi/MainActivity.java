package com.example.mapsapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        String palavras[] = {"Casa", "Casa Viçosa", "Departamento", "Sair"};
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,palavras);
        setListAdapter(adp);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent it = new Intent(this,mapas.class);

        switch (position){
            case 0:
                it.putExtra("local", "Casa");
                startActivity(it);
                break;
            case 1:
                it.putExtra("local", "Vicosa");
                startActivity(it);
                break;
            case 2:
                it.putExtra("local", "Dpto");
                startActivity(it);
                break;
            case 3:
                finish();
                break;
        }
    }
}