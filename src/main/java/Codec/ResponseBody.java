package Codec;

import lombok.Data;

import java.io.Serializable;

/**
 * @author larry miao
 * @date 2018-12-20
 */
// todo 返回结果类型如何确定？Object or List ?
@Data
public class ResponseBody implements Serializable
{
    /**
     * 记录请求报错信息
     */
    private String error;

    /**
     * 记录请求返回结果
     */
    private Object body;
}
