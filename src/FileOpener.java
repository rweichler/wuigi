import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Used to open, prompt to open, save, prompt to save and read files.
 * @author Reed Weichler
 *
 */
public class FileOpener{
	private JFileChooser chooser;
	private Component component;
	private String ext;
	
	private BufferedReader reader;
	
	/**
	 * Creates a new FileOpener with the parent Component specified
	 * @param component component to be used when displaying the JFileChooser
	 */
	public FileOpener(Component component){
		chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileFilter(new CustomFilter("wuigi", "Wuigi Level"));
		this.component = component;
		ext = "wuigi";
		
	}
	
	/**
	 * Prompts the user to save a file by opening a JFileChooser
	 * @return the File that is chosen, null if none is chosen
	 */
	public File saveFile(){
		if(chooser == null || component == null)return null;
		File file;
		int returnVal = chooser.showSaveDialog(chooser);
		if(returnVal != JFileChooser.APPROVE_OPTION) return null;
		file = chooser.getSelectedFile();
		String name = file.getPath();
		String ext = '.' + this.ext;
		if(!name.substring(name.length()-ext.length(), name.length()).equals(ext)){
			name += ext;
			file = new File(name);
		}
		return file;
	}
	
	/**
	 * Prompts the user to open a file by opening a JFileChooser
	 * @return the File that is chosen, null if none is chosen
	 */
	public File openFile(){
		if(chooser == null || component == null)return null;
		File file;
		int returnVal = chooser.showOpenDialog(component);
		if(returnVal != JFileChooser.APPROVE_OPTION) return null;
		file = chooser.getSelectedFile();
		try{
			file.createNewFile();
			return file;
		}catch(Exception ex){
			System.err.println("COULDN'T READ THE SHIT");
			return null;
		}
	}
	/**
	 * Opens a new FileReader to read the File specified by the path
	 * @param path path to the File
	 * @return true if could be read, false if not
	 */
	public boolean readFile(String path){
		return readFile(new File(path));
	}
	/**
	 * Opens a new FileReader to read the File specified
	 * @param file File to read
	 * @return true if could be read, false if not
	 */
	public boolean readFile(File file){
		try{
			reader = new BufferedReader(new FileReader(file));
			return true;
		}catch(FileNotFoundException e){
			reader = null;
			return false;
		}

	}
	
	/**
	 * gets the next line from the FileReader after calling readFile
	 * @return the next line if it can be read, null if not
	 */
	public String readLine(){
		if(reader == null)return null;
		String str;
		try {
			str = reader.readLine();
		} catch (IOException e) {
			return null;
		}
		return str;
	}
	
	private class CustomFilter extends FileFilter{
		
		private String ext, description;
		
		public CustomFilter(String ext, String description){
			this.ext = '.' + ext;
			this.description = description;
		}
		public boolean accept(File file) {
			if(file.isDirectory())
				return true;
			String name = file.getName();
			int length = name.length();
			if(name.substring(length-ext.length(), length).equals(ext)){
				return true;
			}
			return false;
		}

		public String getDescription() {
			return description + " *" + ext;
		}
		
	}
	
}
