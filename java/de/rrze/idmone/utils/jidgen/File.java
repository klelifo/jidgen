package de.rrze.idmone.utils.jidgen;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * This class was written for easy handling
 * of file I/O operations within the jidgen
 * project.
 *  
 * @author unrza249
 */
public class File {
	
	/**
	 *  The class logger
	 */
	private static final Log logger = LogFactory.getLog(File.class);
	
	/**
	 * The complete path to the file.
	 */
	private String file;
	
	/**
	 * The file reader for this object's file
	 */
	private BufferedReader reader;
	
	
	/**
	 * Default constructor
	 */
	public File() {
	}
	
	/**
	 * Constructor with file location
	 * 
	 * @param file
	 * 			the location of the file to process
	 */
	public File(String file) {
		this.file = file;
		this.reader = File.openFile(this.file);
	}

/*	
	private boolean init() {
		if (this.file == null) {
			logger.fatal(Messages.getString("File.INIT_FAILED"));
			return false;
		}
		else {
			if (this.reader == null) {
				this.reader = File.openFile(this.file);
			}
			
			if (this.reader == null) {
				logger.fatal(Messages.getString("File.INIT_FAILED"));
				return false;
			}
		}
		
		// if we made it here everything went fine ;)
		return true;
	}
*/
	
	/**
	 * Tries to get a buffered reader for the specified file
	 * 
	 * @param file
	 * 			the file we want to have a reader for
	 * @return	the buffered reader for the specified file or null on error
	 */
	private static BufferedReader openFile(String file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			return br;
		}
		catch (FileNotFoundException e) {
			logger.fatal(Messages.getString("File.FILE_NOT_FOUND") + file);
			System.exit(200);
		}

		return null;
	}
	
	/**
	 * Reads one line from the file into a string and
	 * returns it
	 * 
	 * @return the next line of the file or null on eof/error
	 */
	public String getLine() {
		String line = null;
		try {
			line = this.reader.readLine();
		}
		catch (IOException e) {
			logger.fatal(e.toString());
			System.exit(201);
		}
		return line;
	}
	
	/**
	 * Reads the whole file into a string and returns it.
	 * Lines are separated by '\n'.
	 * 
	 * @return the whole file content as a string
	 */
	public String getContents() {
		String file = "";
		while ((file += this.getLine() + "\n") != null) {
			// just loop
		}
		return file;
	}
	
	/**
	 * Closes the file reader
	 */
	public void close() {
		try {
			this.reader.close();
		}
		catch (IOException e) {
			logger.fatal(e.toString());
			System.exit(202);
		}
	}
	
}
