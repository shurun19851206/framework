package com.rainy.jta.common;

import org.springframework.util.Assert;

/**
  * ClassName: DataSourceSwitcher
  * Description: TODO
  * @date 2015年4月23日 下午12:51:00
  *
 */
public class DataSourceSwitcher {

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	/**
	  * <p>
	  * Title: setDataSource <br>
	  * Description: TODO  <br>
	  * @param locationCode    设定文件  <br>
	  * void    返回类型  <br>
	  * @throws
	 */
	public static void setDataSource(String locationCode) {
		Assert.notNull(locationCode, "dataSource cannot be null");
		contextHolder.set(DatasourceProperties.getPropertiesStr(locationCode));
	}

	/**
	  * <p>
	  * Title: setMaster <br>
	  * Description: TODO  <br>    设定文件  <br>
	  * void    返回类型  <br>
	  * @throws
	 */
	public static void setMaster() {
		clearDataSource();
	}

	/**
	  * <p>
	  * Title: setSlave <br>
	  * Description: TODO  <br>
	  * @param locationCode    String
	  * void    返回类型  <br>
	  * @throws
	 */
	public static void setSlave(String locationCode) {
		setDataSource(locationCode);
	}

	/**
	  * <p>
	  * Title: getDataSource <br>
	  * Description: TODO  <br>
	  * @return    设定文件  <br>
	  * String    返回类型  <br>
	  * @throws
	 */
	public static String getDataSource() {
		return (String) contextHolder.get();
	}

	/**
	  * <p>
	  * Title: clearDataSource <br>
	  * Description: TODO  <br>    设定文件  <br>
	  * void    返回类型  <br>
	  * @throws
	 */
	public static void clearDataSource() {
		contextHolder.remove();
	}
}
