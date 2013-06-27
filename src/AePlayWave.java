import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * This class is used to play sounds in-game. This creates a new Thread to play the sound.
 * @author Reed Weichler and the people who wrote the book Java Methods A&AB
 *
 */
public class AePlayWave{
private SourceDataLine line = null;
private byte[] audioBytes;
	private int numBytes;
	private AudioFormat audioFormat;
	private DataLine.Info info;

	/**
	 * Opens the file specified by the path and loads it to be memory so it can be played
	 * @param path
	 */
	public AePlayWave(String path){
		File soundFile = new File(path);
		AudioInputStream audioInputStream = null;
		try{
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		}catch (Exception ex){
			//System.out.println("*** Cannot find " + fileName + " ***");
			//System.exit(1);
		}
		if(audioInputStream == null)return;
		audioFormat = audioInputStream.getFormat();
		info = new DataLine.Info(SourceDataLine.class,
								 audioFormat);
		try{
			line = (SourceDataLine)AudioSystem.getLine(info);
			line.open(audioFormat);
		}catch (LineUnavailableException ex){
			//System.out.println("*** Audio line unavailable ***");
			//System.exit(1);
		}

		line.start();

		audioBytes = new byte[(int)soundFile.length()];

		try{
			numBytes = audioInputStream.read(audioBytes, 0, audioBytes.length);
		}catch (IOException ex){
			//System.out.println("*** Cannot read " + fileName + " ***");
			//System.exit(1);
		}
	}
	
	/**
	 * Plays the sound
	 */
	public void start(){
		new Thread(){
			public void run(){
				try{
					line = (SourceDataLine)AudioSystem.getLine(info);
					line.open(audioFormat);
				}catch (LineUnavailableException ex){
					//System.out.println("*** Audio line unavailable ***");
					//System.exit(1);
				}
				line.start();
				line.write(audioBytes, 0, numBytes);
			}
		}.start();
	}
}
