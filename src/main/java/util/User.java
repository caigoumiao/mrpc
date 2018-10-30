package util;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable
{

    private static final long serialVersionUID = 6058720907182472146L;
    private String name;
    private int age;
}
