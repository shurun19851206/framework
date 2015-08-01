//package com.rainy.quartz.config;
//
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.beans.factory.config.RuntimeBeanReference;
//import org.springframework.beans.factory.config.TypedStringValue;
//import org.springframework.beans.factory.support.AbstractBeanDefinition;
//import org.springframework.beans.factory.support.ManagedList;
//import org.springframework.beans.factory.support.RootBeanDefinition;
//import org.springframework.beans.factory.xml.BeanDefinitionParser;
//import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
//import org.springframework.beans.factory.xml.ParserContext;
//import org.springframework.util.StringUtils;
//import org.w3c.dom.Element;
//
//import com.rainy.quartz.trigger.BeanInvokingJobDetailFactoryBean;
//import com.rainy.quartz.trigger.MyCronTriggerBean;
//
//public class QuartzBeanDefinitionParser implements BeanDefinitionParser, QuartzBeanDefinition {
//
//    private static final Map<String, IParseHandler> PARSE_HANDLER = new ConcurrentHashMap<String, IParseHandler>();
//    private static final Set<String> TRIGGERS = new HashSet<String>();
//    
//    public BeanDefinition parse(Element element, ParserContext parserContext) {
//        return null;
//    }
//    
//    private static final IParseHandler parseHandlerBySysType = new IParseHandler() {
//        
//        public void handle(Element element, ParserContext parserContext) {
//            String triggerId = element.getAttribute(BeanDefinitionParserDelegate.ID_ATTRIBUTE);
//            if(!StringUtils.hasLength(triggerId)) {
//                // id不存在  异常
//                throw new ParseException("job 配置错误. id节点必须配置.");
//            }
//            // 是否并发运行
//            boolean concurrent = "true".equals(element.getAttribute("concurrent"));
//            // 业务类  spring bean name
//            String serviceBeanName = element.getAttribute("serviceBeanName");
//            if(!StringUtils.hasLength(serviceBeanName)) {
//                // 业务 beanName 没有配置  异常
//                throw new ParseException("job 配置错误. serviceBeanName节点必须配置.");
//            }
//            // 多仓库
//            boolean multiWarehouse = "true".equals(element.getAttribute("multiWarehouse"));
//            // 优先级为0
//            // job detail
//            String jobBeanName = triggerId + "XX";
//            RootBeanDefinition jobDetailDefinition = createJobDetailDefinition(concurrent, serviceBeanName, "XX", null);
//            parserContext.getRegistry().registerBeanDefinition(jobBeanName, jobDetailDefinition);
//            // job Trigger
//            String infCode = element.getAttribute("infCode");
//            RootBeanDefinition trigger = createTriggerDefinition(jobBeanName, infCode, multiWarehouse);
//            parserContext.getRegistry().registerBeanDefinition(triggerId, trigger);
//            TRIGGERS.add(triggerId);
//            
//            // 多优先级运行
//            if("true".equals(element.getAttribute("multiPriority"))) {
//                // 优先级不为0的
//                // job detail
//                String jobDetailByPriorityName = triggerId + "XX";
//                RootBeanDefinition jobDetailBeanDefinitionByPriority = createJobDetailDefinition(concurrent, serviceBeanName, "XX", "XX");
//                parserContext.getRegistry().registerBeanDefinition(jobDetailByPriorityName, jobDetailBeanDefinitionByPriority);
//                // job Trigger
//                String triggerByPriorityName = triggerId + "XX"; 
//                RootBeanDefinition triggerByPriority = createTriggerDefinition(jobDetailByPriorityName, infCode, Boolean.FALSE);
//                parserContext.getRegistry().registerBeanDefinition(triggerByPriorityName, triggerByPriority);
//                TRIGGERS.add(triggerByPriorityName);
//            }
//        }
//
//        public String getTypeFlag() {
//            return WmsJobBaseService.class.getName();
//        }
//    };
//    
//    private static RootBeanDefinition createJobDetailDefinition(boolean concurrent, String serviceBeanName, String targetMethod,  Integer priority) {
//        RootBeanDefinition jobDetailDefinition = new RootBeanDefinition();
//        jobDetailDefinition.setBeanClass(BeanInvokingJobDetailFactoryBean.class);
//        jobDetailDefinition.setLazyInit(Boolean.FALSE);
//        jobDetailDefinition.getPropertyValues().addPropertyValue("concurrent", concurrent);
//        jobDetailDefinition.getPropertyValues().addPropertyValue("targetBean", serviceBeanName);
//        jobDetailDefinition.getPropertyValues().addPropertyValue("targetMethod", targetMethod);
//        ManagedList managedList = null;
//        if(priority != null) {
//            managedList = new ManagedList();
//            managedList.add(new TypedStringValue(String.valueOf(priority), Integer.class));
//        }
//        jobDetailDefinition.getPropertyValues().addPropertyValue("arguments", managedList);
//        return jobDetailDefinition;
//    }
//    
//    private static RootBeanDefinition createTriggerDefinition(String jobBeanName, String infCode, boolean multiWarehouse) {
//        RootBeanDefinition trigger = new RootBeanDefinition();
//        trigger.setBeanClass(MyCronTriggerBean.class);
//        trigger.setLazyInit(Boolean.FALSE);
//        trigger.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_NAME);
//        trigger.getPropertyValues().addPropertyValue("jobDetail", new RuntimeBeanReference(jobBeanName));
//        trigger.getPropertyValues().addPropertyValue("infCode", infCode);
//        trigger.getPropertyValues().addPropertyValue("moreThanOneWarehouse", multiWarehouse);
//        return trigger;
//    }
//
//}
