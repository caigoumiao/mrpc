package util;

import com.esotericsoftware.kryo.Kryo;

public final class KryoUtil
{
    public static void main(String[] args)
    {
        Kryo kryo = new Kryo();
        kryo.register(Class.class);
    }
}
