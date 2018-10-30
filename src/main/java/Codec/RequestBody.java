package Codec;

import lombok.Data;

@Data
public class RequestBody
{
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] args;
}
