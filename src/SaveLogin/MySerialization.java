package SaveLogin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MySerialization {

	public static byte[] objToBytes(Serializable o) {
		byte[] serializableObject = null;
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream so = new ObjectOutputStream(bo);
			so.writeObject(o);
			so.flush();
			serializableObject = bo.toByteArray();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return serializableObject;
	}
	
	public static Object bytesToObj(byte[] deserializeObject) {
		Object obj = null;
		try {
			ByteArrayInputStream bi = new ByteArrayInputStream(deserializeObject);
		    ObjectInputStream si = new ObjectInputStream(bi);
		    obj = (Object) si.readObject();	
		}catch(Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
}
