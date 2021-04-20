package br.ufc.great.syssu.eval;

import static br.ufc.great.syssu.eval.Expression.*;
import static br.ufc.great.syssu.eval.Expression.gt;
import static br.ufc.great.syssu.eval.Expression.sensor;

import java.util.ArrayList;
import java.util.List;

import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.interfaces.IFilter;
import br.ufc.great.syssu.eval.var.NumberConstant;
import br.ufc.great.syssu.eval.var.NumberSensorVariable;
import br.ufc.great.syssu.eval.var.StringConstant;
import br.ufc.great.syssu.eval.var.StringSensorVariable;
import br.ufc.great.syssu.eval.var.StringVariable;
import br.ufc.great.syssu.main.SysSUManager;

public class Main2 {

	public static void main(String[] args) {
		SysSUManager syssu = new SysSUManager("ok", true);

		List<Double> list = new ArrayList();

		list.add(2.0);
		list.add(3.0);

		Tuple tuple = (Tuple) new Tuple().addField("name", "ok").addField("list", list);
		Tuple tuple3 = (Tuple) new Tuple().addField("name", "ok").addField("ok", 1);
		Tuple tuple4 = (Tuple) new Tuple().addField("name", "ok2").addField("ok", 2);
		Tuple tuple6 = (Tuple) new Tuple().addField("timestamp", 2).addField("name2", "ok1").addField("ok2", "asd");
		Tuple tuple2 = (Tuple) new Tuple().addField("name", "ok").addField("ok", 2).addField("cok", 3)
				.addField("sensor", tuple3);
		Tuple tuple5 = (Tuple) new Tuple().addField("name", "ok").addField("ok", 3).addField("sensor", tuple4);
		Tuple tuple7 = (Tuple) new Tuple().addField("name", "ok").addField("ok2", "asd").addField("sensor", tuple6);

		syssu.put(tuple);
		syssu.put(tuple3);
		syssu.put(tuple4);
		syssu.put(tuple2);
		syssu.put(tuple5);
		syssu.put(tuple6);
		syssu.put(tuple7);

		// List<Tuple> list2 = syssu.read((Pattern) new Pattern().addField("?", "?"),
		// null);
		// List<Double> list3 = (List<Double>) list2.get(0).getField("list").getValue();

		System.out.println(syssu.read((Pattern) new Pattern().addField("cpf", "qwe"), filter));
		// System.out.println(syssu.read((Pattern) new Pattern().addField("name",
		// "ok").addField("ok", "2").addField("asd", 2), null));
	}

	static IFilter filter = new IFilter() {

		@Override
		public Expression remoteFilter() {
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
			
			Expression finalExp = and(exp1, sensor(exp2));

			return finalExp;
		}

		@Override
		public Expression localFilter() {
			StringVariable field = new StringVariable("ok2");
			Expression exp1 = eq(field, new StringConstant("asd"));
			
			StringSensorVariable field2 = new StringSensorVariable("ok2");
			Expression exp2 = eq(field2, new StringConstant("asd"));
			
			StringSensorVariable field3 = new StringSensorVariable("name2");
			Expression exp3 = eq(field3, new StringConstant("ok1"));
			
			NumberSensorVariable field4 = new NumberSensorVariable("name3");
			Expression exp4 = gt(field4, new NumberConstant(1));
			
			return and(exp1, sensor(exp2, exp3, exp1));			
		}
	};

	public static String clearCaracters(Expression exp) {
		String result = exp.toString().replace("\"", "");

		int firstIndex = 0;

		while (firstIndex != -1) {
			firstIndex = result.indexOf("{ 'map.sensors.myArrayList': { $elemMatch : ", firstIndex);

			try {
				if (firstIndex != -1) {
					int lastIndex1 = result.indexOf("} }", firstIndex) + 2;
					result = result.substring(0, lastIndex1) + ", " + result.substring(lastIndex1 + 6);
					lastIndex1 = result.indexOf("} }", lastIndex1) + 2;
					result = result.substring(0, lastIndex1) + ", " + result.substring(lastIndex1 + 6);
					firstIndex += "{ 'map.sensors.myArrayList': { $elemMatch : ".length();
				}
			} catch (StringIndexOutOfBoundsException e) {
				break;
			}
		}

		return result;
	}
}
