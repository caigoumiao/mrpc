package client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// todo 基于注解进行服务自动注入
/**
 * @author larry miao
 * @date 2018-12-14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface MRpcInjection
{
}