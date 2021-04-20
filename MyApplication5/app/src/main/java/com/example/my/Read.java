package com.example.my;

import android.util.Log;

import java.util.List;

import br.ufc.great.caos.api.Caos;
import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.interfaces.IFilter;
import br.ufc.great.syssu.eval.Expression;
import br.ufc.great.syssu.eval.var.NumberConstant;
import br.ufc.great.syssu.eval.var.NumberSensorVariable;
import br.ufc.great.syssu.eval.var.StringConstant;
import br.ufc.great.syssu.eval.var.StringSensorVariable;

import static br.ufc.great.syssu.eval.Expression.eq;
import static br.ufc.great.syssu.eval.Expression.gt;
import static br.ufc.great.syssu.eval.Expression.sensor;

public class Read implements IRead {
    @Override
    public String mediaTempAllUsers(String packageName) {
        //Offloading de dados
        Pattern pattern = (Pattern) new Pattern().addField("id_app", packageName);

        IFilter filter = new IFilter() {
            @Override
            public Expression localFilter() {
                return null;
            }

            @Override
            public Expression remoteFilter() {
                StringSensorVariable field = new StringSensorVariable("type");
                Expression exp1 = eq(field, new StringConstant("smartwatch.temperature"));

                return sensor(exp1);
            }
        };

        List<Tuple> list = Caos.getInstance().filter(pattern, filter);

        //Processamento de dados
        double result = 0.0;

        for(int i = 0; i < list.size(); i++) {
            Tuple tuple = list.get(i);

            for(int j = 0; j < tuple.size(); j++) {
                if(tuple.getField(j).getName().equals("sensor")) {
                    Tuple sensor = (Tuple) tuple.getField(j).getValue();

                    //Verificando se o sensor é de temperatura
                    if(sensor.getField("type").getValue().toString().equals("smartwatch.temperature")) {
                        //Pegando os valores do sensor e aculumando em uma variavel (Result)
                        Log.i("Sensor1", sensor.getField("value").getValue().toString());
                        result += Double.parseDouble(sensor.getField("value").getValue().toString());
                    }
                }
            }
        }

        //Calculando a média
        return ""+result/list.size();
    }

    @Override
    public String mediaBatiUser(String packageName) {
        //Offloading de dados
        Pattern pattern = (Pattern) new Pattern().addField("id_app", packageName);

        IFilter filter = new IFilter() {
            @Override
            public Expression localFilter() {
                return null;
            }

            @Override
            public Expression remoteFilter() {
                StringSensorVariable field = new StringSensorVariable("type");
                Expression exp1 = eq(field, new StringConstant("smartwatch.heart"));

                return sensor(exp1);
            }
        };

        List<Tuple> list = Caos.getInstance().filter(pattern, filter);

        //Processamento de dados
        double result = 0.0;

        for(int i = 0; i < list.size(); i++) {
            Tuple tuple = list.get(i);

            for(int j = 0; j < tuple.size(); j++) {
                if(tuple.getField(j).getName().equals("sensor")) {
                    Tuple sensor = (Tuple) tuple.getField(j).getValue();

                    //Verificando se o sensor do coração
                    if(sensor.getField("type").getValue().toString().equals("smartwatch.heart")) {
                        //Pegando os valores do sensor e aculumando em uma variavel (Result)
                        Log.i("Sensor1", sensor.getField("value").getValue().toString());
                        result += Double.parseDouble(sensor.getField("value").getValue().toString());
                    }
                }
            }
        }

        //Calculando a média
        return ""+result/list.size();
    }


    @Override
    public String mediaDezPBatiUser(String packageName) {
        //Offloading de dados
        Pattern pattern = (Pattern) new Pattern().addField("id_app", packageName);

        IFilter filter = new IFilter() {
            @Override
            public Expression localFilter() {
                return null;
            }

            @Override
            public Expression remoteFilter() {
                StringSensorVariable field = new StringSensorVariable("type");
                Expression exp1 = eq(field, new StringConstant("smartwatch.heart"));

                return sensor(exp1);
            }
        };

        List<Tuple> list = Caos.getInstance().filter(pattern, filter);

        //Processamento de dados
        double result = 0.0;

        for(int i = 0; i < list.size() && i < 10 ; i++) {
            Tuple tuple = list.get(i);

            for(int j = 0; j < tuple.size(); j++) {
                if(tuple.getField(j).getName().equals("sensor")) {
                    Tuple sensor = (Tuple) tuple.getField(j).getValue();

                    //Verificando se o sensor do coração
                    if(sensor.getField("type").getValue().toString().equals("smartwatch.heart")) {
                        //Pegando os valores do sensor e aculumando em uma variavel (Result)
                        Log.i("Sensor1", sensor.getField("value").getValue().toString());
                        result += Double.parseDouble(sensor.getField("value").getValue().toString());
                    }
                }
            }
        }

        //Calculando a média
        return ""+result/list.size();
    }



    /*
    IFilter filter = new IFilter() {
            @Override
            public Expression localFilter() {
                return null;
            }

            @Override
            public Expression remoteFilter() {
                StringSensorVariable field = new StringSensorVariable("type");
                Expression exp1 = eq(field, new StringConstant("smartwatch.temperature"));

                NumberSensorVariable field2 = new NumberSensorVariable("value");
                Expression exp2 = gt(field2, new NumberConstant(40));

                return sensor(exp1, exp2);
            }
        };
     */
}
