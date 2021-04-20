package com.example.my;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;

import br.ufc.great.caos.api.Caos;
import br.ufc.great.caos.api.config.CaosConfig;
import br.ufc.great.caos.api.config.Inject;
import br.ufc.great.caos.data.DataOffloading;
import br.ufc.great.caos.data.Sensor;

@CaosConfig(primaryEndpoint = "10.0.2.2")
public class MainActivity extends AppCompatActivity {

    @DataOffloading
    Medical medical;

    @Inject(Calc.class)
    ICalc calc;

    EditText etName;
    EditText etCPF;
    EditText etDate;
    Button btOffloading;
    Button btGet;
    Button btPesquisar;
    CheckBox cbFebre;
    CheckBox cbDCorpo;
    CheckBox cbDCabeca;
    CheckBox cbAnalgesico;
    CheckBox cbAntibiotico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = (EditText) findViewById(R.id.etName);
        etCPF = (EditText) findViewById(R.id.etCPF);
        etDate = (EditText) findViewById(R.id.etDate);
        btOffloading = (Button) findViewById(R.id.btOffloading);
        btGet = (Button) findViewById(R.id.btGet);
        btPesquisar = (Button) findViewById(R.id.btPesquisa);
        cbFebre = (CheckBox) findViewById(R.id.cbFebre);
        cbDCabeca = (CheckBox) findViewById(R.id.cbDCabeca);
        cbDCorpo = (CheckBox) findViewById(R.id.cbDCorpo);
        cbAnalgesico = (CheckBox) findViewById(R.id.cbAnalgesico);
        cbAntibiotico = (CheckBox) findViewById(R.id.cbAntibiotico);

        Caos.getInstance().start(this, this);

        btOffloading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medical = new Medical();
                medical = new Medical();
                medical.name = etName.getText().toString();
                medical.cpf = etCPF.getText().toString();
                medical.date = etDate.getText().toString();

                ArrayList<String> listDoencas = new ArrayList();

                if(cbFebre.isChecked()) {
                    listDoencas.add(cbFebre.getText().toString());
                }
                if(cbDCorpo.isChecked()) {
                    listDoencas.add(cbDCorpo.getText().toString());
                }
                if(cbDCabeca.isChecked()) {
                    listDoencas.add(cbDCabeca.getText().toString());
                }
                medical.disease = listDoencas;

                ArrayList<String> listMedicacao = new ArrayList();

                if(cbAntibiotico.isChecked()) {
                    listMedicacao.add(cbAntibiotico.getText().toString());
                }
                if(cbAnalgesico.isChecked()) {
                    listMedicacao.add(cbAnalgesico.getText().toString());
                }

                medical.medication = listMedicacao;

                //Sensor
                medical.sensorTemperature = new Sensor("smartwatch.temperature", BigDecimal.valueOf(new Random().nextDouble() * (42 - 35) + 35)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue());

                ArrayList<Double> list = new ArrayList<>();
                list.add(BigDecimal.valueOf(new Random().nextDouble() * (3) + 2)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue());
                list.add(BigDecimal.valueOf(new Random().nextDouble() * (10) + 30)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue());

                medical.sensorLocation = new Sensor("smartwatch.location", list);

                medical.sensorHeart = new Sensor("smartwatch.heart", BigDecimal.valueOf(new Random().nextDouble() * (120) + 40)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue());

                Caos.getInstance().makeData();
            }
        });

        btPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        btGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Tuples", "ok");
                Log.i("Tuples", calc.getDatas());
            }
        });
    }
}
