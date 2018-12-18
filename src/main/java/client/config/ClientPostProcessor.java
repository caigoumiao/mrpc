package client.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author larry miao
 * @date 2018-12-18
 */
@Component
public class ClientPostProcessor implements BeanPostProcessor
{
    private Logger log= LoggerFactory.getLogger(this.getClass());

    @Override
    public Object postProcessAfterInitialization(Object bean , String beanName) throws BeansException
    {
        log.info("After client bean initialization");
        return bean;
    }
}
