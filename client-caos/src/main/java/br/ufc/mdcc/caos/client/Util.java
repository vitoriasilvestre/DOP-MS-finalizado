package br.ufc.mdcc.caos.client;

import java.io.InputStream;
import java.util.Properties;

public class Util {
	
	static Properties properties; 

    public Util() {
    	properties = new Properties();
        InputStream inputStream = Util.class.getClassLoader().getResourceAsStream("data.properties");            
        
        try{
        	properties.load(inputStream);
        }catch(Exception e){
            e.printStackTrace();
        }        
    }

    public static String getClasse() {
        return properties.getProperty("class");
    }
}
