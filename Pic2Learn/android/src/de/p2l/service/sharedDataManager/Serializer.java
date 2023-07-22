package de.p2l.service.sharedDataManager;

//Adapted from: https://github.com/sudar/android-samples/blob/master/TaskManager/src/com/sudarmuthu/android/taskmanager/util/ObjectSerializer.java (05.01.2019)

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class Serializer {
    public static String serizalize(Serializable input) throws IOException {
        if (input == null) return "";
        ByteArrayOutputStream serialObj = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(serialObj);
        objStream.writeObject(input);
        objStream.close();
        return encodeBytes(serialObj.toByteArray());
    }

    public static Object deserialize(String str) throws IOException, ClassNotFoundException {
        if (str == null || str.length() == 0) return null;
        ByteArrayInputStream serialObj = new ByteArrayInputStream(decodeBytes(str));
        ObjectInputStream objStream = new ObjectInputStream(serialObj);
        return objStream.readObject();
    }

    public static String encodeBytes(byte[] bytes) {
        StringBuffer strBuf = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
        }

        return strBuf.toString();
    }

    public static byte[] decodeBytes(String str) {
        byte[] bytes = new byte[str.length() / 2 ];
        for (int i = 0; i < str.length()-1; i+=2) {
            char c = str.charAt(i);
            bytes[i/2] = (byte) ((c - 'a') << 4);
            c = str.charAt(i+1);
            bytes[i/2] += (c - 'a');
        }
        return bytes;
    }
}
