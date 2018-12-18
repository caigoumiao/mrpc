package client.config;

import client.MRpcInjection;
import client.ServiceImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author larry miao
 * @date 2018-12-18
 */
@Component
public class ClientPostProcessor implements BeanPostProcessor
{
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private ServiceImporter serviceImporter;

    public ClientPostProcessor(ServiceImporter _serviceImporter)
    {
        this.serviceImporter = _serviceImporter;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean , String beanName) throws BeansException
    {
        log.info("Bean[" + beanName + "] initialized ...");
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields)
        {
            // 当此字段被 @MRpcInjection 注解时，为此字段自动注入服务代理类
            if (field.getAnnotation(MRpcInjection.class) != null)
            {
                try
                {
                    field.setAccessible(true);
                    Class<?> serviceClass = field.getType();
                    field.set(bean , serviceImporter.importService(serviceClass));
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
