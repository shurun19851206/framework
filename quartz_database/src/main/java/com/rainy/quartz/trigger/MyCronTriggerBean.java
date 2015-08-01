//package com.rainy.quartz.trigger;
//
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.lang.ArrayUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.quartz.JobDetail;
//import org.quartz.Trigger;
//import org.springframework.util.ReflectionUtils;
///**
// * 接口定时器Trigger
// * 
// * @author yaojz
// *
// */
//public class MyCronTriggerBean extends CronTriggerBean {
//	
//	private static final long serialVersionUID = -4015250701218611044L;
//	
//	private static final Log logger = LogFactory.getLog(MyCronTriggerBean.class);
//
//	private boolean moreThanOneWarehouse = false;
//	
//	private Trigger[] subTriggers = new Trigger[0];
//	
//	private InfTriggerDatabaseConfiguration infTriggerDatabaseConfiguration;
//	
//	private String infCode;
//
//	public void afterPropertiesSet() throws Exception {
//		super.afterPropertiesSet();
//		String targetBeanName = this.getJobDetail().getJobDataMap().getString("targetBean");
//		Object target = BeanInvokingJobDetailFactoryBean.applicationContext.getBean(targetBeanName);
//		if (!InfJobBaseServiceF.class.isInstance(target) && !InfJobBaseServiceS.class.isInstance(target) && !WmsJobBaseService.class.isInstance(target)
//				) {
//			throw new RuntimeException(targetBeanName + "没有继承InfJobBaseServiceF或InfJobBaseServiceS或WmsJobBaseService");
//		}
//		String methodName = this.getJobDetail().getJobDataMap().getString("targetMethod");
//		// 区分仓库创建Jobdetail
//		if (this.moreThanOneWarehouse) {
//			List<BasWarehouse> warehouses = infTriggerDatabaseConfiguration.getOpenTriggerWarehouse();
//			List<Trigger> triggers = new ArrayList<Trigger>();
//			for (BasWarehouse warehouse : warehouses) {
//				String triggerName = this.getName() + "_" + warehouse.getWarehouseCode();
//				String cron = infTriggerDatabaseConfiguration.getString(triggerName);
//				// 没有配置运行时间  使用默认的
//				if (cron == null) {
//					logger.warn("定时器:" + triggerName + "没有在" + InfConfigurationDomain.InfConfiguration + "表中配置定时器表达式,将使用默认的定时器表达式:" + InfConstant.TRIGGER_CRON_EXPRESSION);
//					cron = InfConstant.TRIGGER_CRON_EXPRESSION;
//				}
//				MyCronTriggerBean bean = new MyCronTriggerBean();
//				org.springframework.beans.BeanUtils.copyProperties(this, bean, new String[] { "cronExpression" });
//				bean.setCronExpression(cron);
//				bean.setName(triggerName);
//				bean.setBeanName(triggerName);
//				// 组别名称,
//				bean.setGroup(warehouse.getWarehouseCode());
//				bean.setDescription(bean.getDescription() == null ? ( bean.getInfCode()+":"+WmsConstantsUtils.getDescription(InfConstant.MapName.INF_CODES, bean.getInfCode()) + "定时器("  + warehouse.getWarehouseName()+"-"+getMethodDescription((String)this.getJobDetail().getJobDataMap().get("targetMethod")) + ")")
//						: bean.getDescription() + "(" + warehouse.getWarehouseName()+"-"+getMethodDescription((String)this.getJobDetail().getJobDataMap().get("targetMethod")) + ")");
//				JobDetail beanJobDetail = new JobDetail();
//				org.springframework.beans.BeanUtils.copyProperties(this.getJobDetail(), beanJobDetail, new String[] { "name", "jobDataMap" });
//				// jobdetail  名称.
//				String jobName = this.getJobDetail().getName() + "_" + warehouse.getWarehouseCode();
//				beanJobDetail.setName(jobName);
//				Object[] sourceArguments = (Object[]) this.getJobDetail().getJobDataMap().get("arguments");
//				Object[] arguments = new Object[0];
//				if (ArrayUtils.isEmpty(sourceArguments)) {
//					arguments = new Object[] { warehouse.getWarehouseCode() };
//				} else {
//					arguments = ArrayUtils.addAll(arguments, sourceArguments);
//					arguments = ArrayUtils.add(arguments, warehouse.getWarehouseCode());
//				}
//				beanJobDetail.getJobDataMap().put("arguments", arguments);
//				beanJobDetail.getJobDataMap().put("targetBean", targetBeanName);
//				beanJobDetail.getJobDataMap().put("targetMethod", methodName);
//				bean.setJobName(jobName);
//				bean.setJobDetail(beanJobDetail);
//				triggers.add(bean);
//			}
//			subTriggers = triggers.toArray(subTriggers);
//		} else {
//			// 不区分仓库.
//			this.setDescription(this.getDescription() == null ? (this.getInfCode()+":"+WmsConstantsUtils.getDescription(InfConstant.MapName.INF_CODES, this.getInfCode()) + "定时器"+"("+getMethodDescription((String)this.getJobDetail().getJobDataMap().get("targetMethod"))+")") : this.getDescription()+"("+getMethodDescription((String)this.getJobDetail().getJobDataMap().get("targetMethod"))+")");
//			String cronExpression = infTriggerDatabaseConfiguration.getString(this.getName(), InfConstant.TRIGGER_CRON_EXPRESSION);
//			this.setCronExpression(cronExpression);
//			Object[] arguments = (Object[]) this.getJobDetail().getJobDataMap().get("arguments");
//			if(arguments == null) {
//				arguments = new Object[0];
//			}
//			Method method = ReflectionUtils.findMethod(target.getClass(), methodName, null);
//			if(method == null) {
//				throw new RuntimeException(targetBeanName + "不存在" + methodName + "方法.");
//			}
//			Class<?>[] parmaeters = method.getParameterTypes();
//			if(parmaeters.length == arguments.length) {
//				this.getJobDataMap().put("arguments", arguments);
//			} 
//			else {
//				int len = parmaeters.length - arguments.length;
//				for(int i = 0; i< len; i++) {
//					arguments = ArrayUtils.add(arguments, null);
//				}
//			}
//			this.getJobDataMap().put("arguments", arguments);
//			this.subTriggers = new Trigger[] { this };
//		}
//	}
//
//	protected Trigger[] getSubTrigger() {
//		return subTriggers;
//	}
//
//	private String getMethodDescription(String targetMethod) {
//		if (targetMethod.equals("saveTransmit") || targetMethod.equals("saveInInterface") || targetMethod.equals("saveInterface")) {
//			return "接口到接口";
//		} else if (targetMethod.equals("saveInterfaceToBusiness")) {
//			return "接口到业务";
//		} else if (targetMethod.equals("saveInterfaceToBusinessByPriority")){
//			return "接口到业务（异常）";
//		}else if (targetMethod.equals("saveBreakPointInInterface")||targetMethod.equals("saveTransmitByPriority")) {
//			return "接口到接口（异常）";
//		}
//		return "";
//	}
//	
//	public String getInfCode() {
//		return infCode;
//	}
//
//	public void setInfCode(String infCode) {
//		this.infCode = infCode;
//	}
//
//	public InfTriggerDatabaseConfiguration getInfTriggerDatabaseConfiguration() {
//		return infTriggerDatabaseConfiguration;
//	}
//
//	public void setInfTriggerDatabaseConfiguration(InfTriggerDatabaseConfiguration infTriggerDatabaseConfiguration) {
//		this.infTriggerDatabaseConfiguration = infTriggerDatabaseConfiguration;
//	}
//
//	public boolean isMoreThanOneWarehouse() {
//		return moreThanOneWarehouse;
//	}
//
//	public void setMoreThanOneWarehouse(boolean moreThanOneWarehouse) {
//		this.moreThanOneWarehouse = moreThanOneWarehouse;
//	}
//}
