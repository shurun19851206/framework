package com.rainy.jta.common;
import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

/**
 * 
  * ClassName: DataSourceAdvice
  * Description: TODO
  * @date 2015年4月23日 下午12:49:04
  *
 */
public class DataSourceAdvice implements MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice {

	/**
	  * Title: before
	  * Description: service方法执行之前被调用  
	  * @param method Method
	  * @param args Object[]
	  * @param target Object
	  * @throws Throwable    设定文件
	  * @see org.springframework.aop.MethodBeforeAdvice#before(java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
	  * @throws Throwable Throwable
	 */
    public void before(Method method, Object[] args, Object target) throws Throwable {
        if (method.getName().endsWith("slave")) {
            DataSourceSwitcher.setSlave("slave");
        } else {
            DataSourceSwitcher.clearDataSource();
        }
    }  
  
    /**
      * Title: afterReturning
      * Description: service方法执行完之后被调用  
      * @param arg0 Object
      * @param method Method
      * @param args Object[]
      * @param target Object
      * @throws Throwable    设定文件
      * @see org.springframework.aop.AfterReturningAdvice#afterReturning(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
      * @throws Throwable Throwable
     */
    public void afterReturning(Object arg0, Method method, Object[] args, Object target) throws Throwable {  
    	DataSourceSwitcher.setMaster();
    }  
  
    /**
      * <p>
      * Title: afterThrowing <br>
      * Description: 抛出Exception之后被调用  
      * @param method Method
      * @param args Object[]
      * @param target Object
      * @param ex Exception
      * @throws Throwable    设定文件  <br>
      * void    返回类型  <br>
      * @throws Throwable Throwable
     */
    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) throws Throwable {  
        DataSourceSwitcher.setMaster();
        System.out.println("出现异常,切换到: master");
    }  
  
}
