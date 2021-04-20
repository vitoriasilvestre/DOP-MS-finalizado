package br.ufc.mdcc.caos.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.TYPE)
public @interface DataConfig {
	String endpoint() default "http://10.0.2.2:8080";
	
	StrategyOffload sync() default StrategyOffload.TIME;	
	
	int value() default 10000;
}

