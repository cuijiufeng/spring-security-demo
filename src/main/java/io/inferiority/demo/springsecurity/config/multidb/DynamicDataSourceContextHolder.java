package io.inferiority.demo.springsecurity.config.multidb;
 
/**
 * @Class: DynamicDataSourceContextHolder
 * @Date: 2023/4/27 9:32
 * @author: cuijiufeng
 */
@Deprecated
public class DynamicDataSourceContextHolder {
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setDataSourceType(String dataSourceType) {
		contextHolder.set(dataSourceType);
	}
 
	public static String getDataSourceType() {
		return contextHolder.get();
	}
 
	public static void clearDataSourceType() {
		contextHolder.remove();
	}
}