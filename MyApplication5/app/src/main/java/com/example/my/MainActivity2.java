package com.example.my;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import br.ufc.great.caos.api.Caos;
import br.ufc.great.caos.api.config.CaosConfig;
import br.ufc.great.caos.api.config.Inject;

@CaosConfig(primaryEndpoint = "10.0.2.2")
public class MainActivity2 extends AppCompatActivity {

    Button btPesquisaA;
    RadioGroup group;
    TextView tvResponse;

    @Inject(Read.class)
    IRead read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tvResponse = (TextView) findViewById(R.id.tvvResponse);

        Caos.getInstance().start(this, this);

        group = (RadioGroup) findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                String resposta = button.getText().toString();

                if(resposta.equals("Média aritmética da temperatura do ambiente de todos os usuários")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvResponse.setText(read.mediaTempAllUsers(getApplicationContext().getPackageName()));
                        }
                    });
                } else if(resposta.equals("Média aritmética dos batimentos de todos os usuários")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvResponse.setText(read.mediaBatiUser(getApplicationContext().getPackageName()));
                        }
                    });
                } else if (resposta.equals("Média aritmética dos batimentos dos dez primeiros registros armazenados")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvResponse.setText(read.mediaDezPBatiUser(getApplicationContext().getPackageName()));
                        }
                    });
                }
            }
        });

        btPesquisaA = (Button) findViewById(R.id.btPesquisaA);
        btPesquisaA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                startActivity(intent);
            }
        });
    }
}