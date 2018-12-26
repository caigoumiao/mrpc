package client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author larry miao
 * @date 2018-12-14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface MRpcInjection
{
    // todo 对应着为指定的服务设置timeout
    long timeOut() default 2000;
}
