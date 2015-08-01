package com.rainy.jta.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

/**
  * ClassName: DatasourceProperties
  * Description: TODO
  * @date 2015年4月21日 下午5:30:02
 */
public class DatasourceProperties {
	private static Logger log = (Logger) LogFactory.getLog(DatasourceProperties.class);
	private static Properties prop = null;
	
	static {
		prop = new Properties();
		try {
			prop.load(DatasourceProperties.class.getClassLoader().getResourceAsStream("filterdatasource.properties"));
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(),e);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
	}
	
	/**
	  * <p>
	  * Title: getPropertiesStr <br>
	  * Description: TODO  <br>
	  * @param keyName String
	  * @return    设定文件  <br>
	  * String    返回类型  <br>
	  * @throws
	 */
	public static String getPropertiesStr (String keyName) {
		String propStr = prop.getProperty(keyName);
		return StringUtils.isNotBlank(propStr) ? propStr : null;
	}

}
