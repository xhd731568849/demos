package com.btzh.config;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

/**
 * 重载属性加载类，处理jar包情况下加载正确配置文件
 * @author tanzhibo
 * @date 17-10-22 下午12:34
 */
public class CustomPropertiesFactoryBean extends PropertiesFactoryBean {
	private final static String CONFIG_PROPERTIES = "config.properties";
	private final static String SUN_JAVA_COMMAND = "sun.java.command";
	private final static String COM_BTZH_MAIN = "com.btzh.Main";
	@Override
	public void setLocation(Resource location) {
		super.setLocation(reResolve(location));
	}
	
	@Override
	public void setLocations(Resource... locations) {
		super.setLocations(reResolve(locations).toArray(locations));
	}
	
	private Resource reResolve(Resource resource) {
		URI uri = null;
		try {
			uri = resource.getURI();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null != uri && CONFIG_PROPERTIES.equals(uri.getSchemeSpecificPart()) && System.getProperty(SUN_JAVA_COMMAND).contains(COM_BTZH_MAIN)) {
			URL url = CustomPropertiesFactoryBean.class.getClassLoader().getResource(CONFIG_PROPERTIES);
			if (null != url) {
				return new UrlResource(url);
			}
		}
		return resource;
	}
	
	private List<Resource> reResolve(Resource... locations) {
		List<Resource> resources = new ArrayList<>(locations.length);
		for (int i = 0; i < locations.length; i++) {
			resources.add(reResolve(locations[i]));
		}
		return resources;
	}
}