package client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// todo 基于注解进行服务自动注入
// todo 某Bean 的某字段引用此注解，则需要在Bean 初始化的时候，为该字段赋值。
/**
 * @author larry miao
 * @date 2018-12-14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface MRpcInjection
{
}
