package Codec;

import lombok.Data;

@Data
public class RpcRequest
{
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] args;
}
