package client.config;

import client.MRpcInjection;
import client.ServiceImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author larry miao
 * @date 2018-12-18
 */
@Component
public class ClientPostProcessor implements BeanPostProcessor, ApplicationContextAware
{
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private ServiceImporter serviceImporter;
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean , String beanName) throws BeansException
    {
        log.info("Bean[" + beanName + "] initialized ...");

        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields)
        {
            MRpcInjection injectAnnotation = field.getAnnotation(MRpcInjection.class);
            // 当字段被 @MRpcInjection 注解时，为此字段自动注入服务代理类
            if (injectAnnotation != null)
            {
                // todo 解析注解上设置的信息
                injectAnnotation.timeOut();
                try
                {
                    //
                    if (serviceImporter == null)
                    {
                        serviceImporter = applicationContext.getBean(ServiceImporter.class);
                    }

                    field.setAccessible(true);
                    Class<?> serviceClass = field.getType();
                    Object serviceProxy = serviceImporter.importService(serviceClass);
                    field.set(bean , serviceProxy);

                    log.info("Field[" + serviceClass.getName() + "] is being assigned to " + serviceProxy.getClass().getName());
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
