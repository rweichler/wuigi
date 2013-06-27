import java.io.*;
import java.util.zip.*;

/**
 * Helper class used to write levels to files.<br/>
 * <br/>
 * The four arrays stored in this are properties of a Thing. They are set using Thing.serialize().
 * @author Reed Weichler
 *
 */
public class Serializer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7925582856184491464L;
	private Class instance;
	/**
	 * ints to be written to file
	 */
	public int[] ints;
	/**
	 * doubles to be written to file
	 */
	public double[] doubles;
	/**
	 * booleans to be written to file
	 */
	public boolean[] bools;
	/**
	 * classes to be written to file
	 */
	public Class[] classes;
	
	/**
	 * 
	 * @param instance the class of the object this Serializer should represent
	 */
	public Serializer(Class instance){
		this.instance = instance;
		ints = null;
		doubles = null;
		bools = null;
		classes = null;
	}
	
	public Class getInstance(){
		return instance;
	}
	
	/**
	 * writes s to f (using GZIP compression algorithm)
	 * @param f
	 * @param s
	 * @return
	 */
	public static boolean toFile(File f, Serializable s){
		if(f == null)return false;
		FileOutputStream fout;
		GZIPOutputStream zout;
		try {
			fout = new FileOutputStream(f);
			zout = new GZIPOutputStream(new BufferedOutputStream(fout));
			ObjectOutputStream oout = new ObjectOutputStream(zout);
			oout.writeObject(s);
			oout.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * gets Serializable from file
	 * @param f
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static Serializable fromFile(File f) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		if(f == null)return null;
		FileInputStream fin = new FileInputStream(f);
		GZIPInputStream zin = new GZIPInputStream(new BufferedInputStream(fin));
		ObjectInputStream oin = new ObjectInputStream(zin);

	    return (Serializable)oin.readObject();
	}
}
