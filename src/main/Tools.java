package main;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

public class Tools {
	
	//Given a directory, returns an array of all files contained in that directory and subdirectories.
	public static File[] findAllFilesInDirectory(File pDirectory)
	{
		ArrayList<File> directoryFiles = new ArrayList<File>();
		File[] files;
		
		addDirectoryFiles(directoryFiles, pDirectory);
		
		return directoryFiles.toArray(new File[directoryFiles.size()]);
	}
	
	//Recursively adds all files (excluding folders) within the specified directory and subdirectories to the provided ArrayList of files
	private static void addDirectoryFiles(ArrayList<File> pFiles, File pDirectory)
	{
		File[] directoryFiles = pDirectory.listFiles();
		
		for(File loopFile : directoryFiles)
		{
			if(loopFile.isFile())
			{
				pFiles.add(loopFile);
			}
			
			if(loopFile.isDirectory())
			{
				addDirectoryFiles(pFiles, loopFile);
			}
		}
	}
	
	//TODO Modify this to return a string, and update accordingly every place where it's used
	public static void showExceptionInfo(Exception pException)
	{
		MetadataToSqlGui mainGUI = Main.getMainGUI();
		PrintWriter stackTraceWriter = new PrintWriter(new StringWriter());
		String errorReadout;
		String stackTrace;
		
		//TODO figure out why printStackTrace isn't providing anything useful
		
		//Write the stacktrace to a string
		pException.printStackTrace(stackTraceWriter);
		stackTrace = stackTraceWriter.toString();
		
		//Put the error info together for a single readout
		errorReadout = String.format("%s\r\n%s\r\n", pException.getMessage(), stackTrace);
		pException.printStackTrace();
		
		//Try to print the error message to the output window.  If that fails, then show the user with a message box.
		try {
			mainGUI.appendLine(errorReadout);
		} catch (BadLocationException e) {
			JOptionPane.showMessageDialog(mainGUI.getFrame(), errorReadout);
		}
	}
	
	public static void setContainerComponentsEnabled(Container pContainer, boolean pEnable)
	{
		pContainer.setEnabled(pEnable);
		
        Component[] components = pContainer.getComponents();
        for (Component component : components) {
            component.setEnabled(pEnable);
            if (component instanceof Container) {
            	setContainerComponentsEnabled((Container)component, pEnable);
            }
        }
	}
	
	public static String convertNullString(String convertString)
	{
		return convertString != null ? convertString : "";
	}
	
	//Tries to parse a provided string to an integer.  In case of failure, it returns the provided default value
	public static int TryParseInt(String parseString, int defaultValue)
	{
		try
		{
			return Integer.parseInt(parseString);
		}
		catch(NumberFormatException ex)
		{
			return defaultValue;
		}
		catch(NullPointerException ex)
		{
			return defaultValue;
		}
	}
	
	//TODO Migrate anything in here that would be useful in other applications to a separate shared tools library
}
