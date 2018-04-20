package com.rejoice.blog.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

public class GlobalConfig {
    private static Properties theProps = null;
    public static String readProperty(String propertyName){
        if(theProps == null){
            theProps = loadProperties();
        }

        assert theProps != null;
        return theProps.getProperty(propertyName);
    }

    private static Properties loadProperties(){
        Properties theProps = new Properties();
        try {
        	theProps = PropertiesLoaderUtils.loadAllProperties("config/form.properties");
        } catch (IOException e) {
            return null;
        }
        return theProps;
    }
}
