package com.transfer.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created by xuhandong on 17-9-25.
 */
public class Config {
    private String localHost;
    private Integer localPort;
    private String remoteHost;
    private Integer remotePort;
    private String repository;

    public static Config resolve(){
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
        Config config = new Config();
        Field[] fields = Config.class.getDeclaredFields();
        for(int i = 0 ; i < fields.length ; i ++ ){
            Field field = fields[i];
            String fieldName = field.getName();
            if(Modifier.isStatic(field.getModifiers())){
                continue;
            }
            String property = null;
            try {
                property = resourceBundle.getString(translateField2Config(fieldName));
            }catch (MissingResourceException e){

            }
            //TODO 添加环境变量或运行变量的读取，覆盖config.properties
            if(null == property || property.isEmpty()){
                continue;
            }
            Object value = property;
            if(field.getType() != String.class){
                value = castValue(field.getType(),property);
            }
            try{
                field.setAccessible(true);
                field.set(config,value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return config;
    }

    private static <T> T castValue(Class<T> fieldType, String value) {
        if (fieldType == Integer.class) {
            return (T)Integer.valueOf(value);
        } else if (fieldType == Long.class) {
            return (T)Long.valueOf(value);
        } else if (fieldType == Float.class) {
            return (T)Float.valueOf(value);
        } else if (fieldType == Double.class) {
            return (T)Double.valueOf(value);
        } else if (fieldType == BigDecimal.class) {
            return (T)BigDecimal.valueOf(Double.valueOf(value));
        } else if (fieldType == BigInteger.class) {
            return (T)BigInteger.valueOf(Double.valueOf(value).longValue());
        } else if (fieldType == Date.class) {
            try {
                return (T)new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    private static String translateField2Config(String fieldName) {
        StringBuilder result = new StringBuilder();
        char[] chars = fieldName.toCharArray();
        for(int i = 0 ; i < chars.length ; i ++ ){
            char c = chars[i];
            if(i==0){
                if(c>= 'A' && c<='Z'){
                    result.append((char)(chars[i]+32));
                }else {
                    result.append(c);
                }
            }else {
                if(c>= 'A' && c<='Z'){
                    result.append(".").append((char)(chars[i]+32));
                }else {
                    result.append(c);
                }
            }
        }
        return result.toString();
    }

    public String getLocalHost() {
        return localHost;
    }

    public void setLocalHost(String localHost) {
        this.localHost = localHost;
    }

    public Integer getLocalPort() {
        return localPort;
    }

    public void setLocalPort(Integer localPort) {
        this.localPort = localPort;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public Integer getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }
}
