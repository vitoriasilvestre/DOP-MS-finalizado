package br.ufc.great.syssu.eval;

import static br.ufc.great.syssu.eval.Expression.*;

import br.ufc.great.syssu.eval.var.NumberConstant;
import br.ufc.great.syssu.eval.var.NumberVariable;
import br.ufc.great.syssu.eval.var.StringConstant;
import br.ufc.great.syssu.eval.var.StringVariable;

public class Main {
	
	public static void main(String[] args) {
		StringVariable field = new StringVariable("date");
		Expression auxExp = eq(field, new StringConstant("asd"));		
		
		StringVariable field5 = new StringVariable("cpf");
		Expression auxExp5 = eq(field5, new StringConstant("qwe"));
		
		NumberVariable field2 = new NumberVariable("map.value");
		Expression auxExp2 = gt(field2, new NumberConstant(40));
		
		StringVariable field3 = new StringVariable("map.type");
		Expression auxExp3 = eq(field3, new StringConstant("smartwatch.heart"));
		
		NumberVariable field4 = new NumberVariable("map.timestamp");
		Expression auxExp4 = gt(field4, new NumberConstant(40));
						
		//Expression finalExp = and(auxExp, sensor(auxExp2, auxExp3, auxExp4));
		Expression finalExp = and(auxExp, auxExp5, sensor(auxExp2, auxExp3, auxExp4));
				
		System.out.println(clearCaracters(and(auxExp, auxExp2)));		
	}

	private static String clearExpression(Expression exp) {
		return exp.toString().replace("\"", "");
	}
	
	private static String clearCaracters(Expression exp) {
		String result = exp.toString().replace("\"", "");
		
		int firstIndex = 0;
		
		while(firstIndex != -1){
			firstIndex = result.indexOf("{ 'map.sensors.myArrayList': { $elemMatch : ", firstIndex);
		    		    
		    if(firstIndex != -1){
		    	int lastIndex1 = result.indexOf("} }", firstIndex) + 2;
		    	result = result.substring(0, lastIndex1) + ", " + result.substring(lastIndex1 + 6);
		    	lastIndex1 = result.indexOf("} }", lastIndex1) + 2;		    	
		    	result = result.substring(0, lastIndex1) + ", " + result.substring(lastIndex1 + 6);
		    	firstIndex += "{ 'map.sensors.myArrayList': { $elemMatch : ".length();		    	
		    }
		}
		
		return result;
	}
}
