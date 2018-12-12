package Codec;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseBody implements Serializable
{
    private String msg;
    private Object body;
}
