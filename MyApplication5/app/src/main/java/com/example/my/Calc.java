package com.example.my;

import android.content.Context;
import android.util.Log;

import br.ufc.great.caos.api.Caos;
import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.interfaces.IFilter;
import br.ufc.great.syssu.eval.Expression;
import br.ufc.great.syssu.eval.var.NumberConstant;
import br.ufc.great.syssu.eval.var.NumberSensorVariable;
import br.ufc.great.syssu.eval.var.StringConstant;
import br.ufc.great.syssu.eval.var.StringSensorVariable;
import br.ufc.great.syssu.eval.var.StringVariable;

import static br.ufc.great.syssu.eval.Expression.and;
import static br.ufc.great.syssu.eval.Expression.eq;
import static br.ufc.great.syssu.eval.Expression.gteq;
import static br.ufc.great.syssu.eval.Expression.sensor;

public class Calc implements ICalc {

    @Override
    public String getDatas() {
        Pattern pattern = (Pattern) new Pattern().addField("date", "hoje");
        return Caos.getInstance().filter(pattern, filter).toString();
    }

    @Override
    public String mediaTempAllUsers(String packageName) {
        Pattern pattern = (Pattern) new Pattern().addField("date", "hoje");
        return Caos.getInstance().filter(pattern, filter).toString();
    }

    IFilter filter = new IFilter() {

        @Override
        public Expression remoteFilter() {
            StringVariable field = new StringVariable("name");
            Expression exp1 = eq(field, new StringConstant("vitoria"));

            /*
            StringVariable field = new StringVariable("date");
            Expression exp1 = eq(field, new StringConstant("asd"));

            StringSensorVariable field2 = new StringSensorVariable("type");
            Expression exp2 = eq(field2, new StringConstant("smartwatch.heart"));

            StringSensorVariable field3 = new StringSensorVariable("name2");
            Expression exp3 = eq(field3, new StringConstant("ok1"));

            NumberSensorVariable field4 = new NumberSensorVariable("value");
            Expression exp4 = gteq(field4, new NumberConstant(3));

            NumberSensorVariable field5 = new NumberSensorVariable("timestamp");
            Expression exp5 = gteq(field5, new NumberConstant(3));

            Expression finalExp = sensor(exp2, exp4, exp5);

             */

            Expression finalExp = exp1;

            return finalExp;
        }

        @Override
        public Expression localFilter() {
            StringVariable field = new StringVariable("date");
            Expression exp1 = eq(field, new StringConstant("asd"));

            StringVariable field2 = new StringVariable("cpf");
            Expression exp2 = eq(field2, new StringConstant("123"));

            Expression finalExp = and(exp1, exp2);

            return exp2;
        }
    };
}