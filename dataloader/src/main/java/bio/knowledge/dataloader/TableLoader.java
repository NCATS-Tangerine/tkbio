/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-17 Scripps Institute (USA) - Dr. Benjamin Good
 *                       STAR Informatics / Delphinai Corporation (Canada) - Dr. Richard Bruskiewich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *-------------------------------------------------------------------------------
 */
package bio.knowledge.dataloader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lance Hannestead
 * @author Richard Bruskiewich
 */
public abstract class TableLoader {
	
	protected static Logger _logger = LoggerFactory.getLogger(TableLoader.class);
	
	//private static Boolean purge = false ;
	//static public void setPurgeOn() { purge = true ; }
	
	static private String libraryPath = "" ;
	
	static public void setLibraryPath(String path) {
		libraryPath = path ;
	}
	
	static public String getLibraryPath() {
		return libraryPath ;
	}
	
	static private String neo4jShellPath = "" ;
	
	/**
	 * @param neo4jShellPath
	 */
	public static void setNeo4jShellPath(String path) {
		neo4jShellPath = path;
	}

	static public String getNeo4jShellPath() {
		return neo4jShellPath ;
	}
	
	private final String fileName ;
	
	protected TableLoader(String fileName) {
		this.fileName = fileName ;
	}
	
	public String getFileName() {
		return fileName;
	}

	public String getDataFilePath() {
		return libraryPath+File.separator+fileName;
	}

	private List<String> queries = new ArrayList<String>();
	
	/**
	 * A method to be used by base classes to add the queries to be executed (in the order added) when loadData() is called.
	 * The string ";\n" automatically gets appended to the end of the query string.
	 * @param query
	 * A String containing the CQL query to be added, without ";\n" at the end.
	 */
	protected void addQuery(String query) {
		this.queries.add(query + ";\n");
	}

	/**
	 * Returns the output and error messages of a process.
	 * @param process
	 * @throws IOException
	 */
	private String readOutput(Process process) throws IOException {
		
		String output = "";
		
		BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		BufferedReader errorReader  = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		
		String line;
		
		while ((line = outputReader.readLine()) != null) {
			output += line + "\n";
		}
		outputReader.close();
		
		while ((line = errorReader.readLine()) != null) {
			output += line + "\n";
		}
		errorReader.close();
		
		return output;
	}
	
	public Long loadData() throws IOException {
//		We are counting warnings and errors. Often times neo4j returns a warning for something that should be an error, though not
//		everything it warns you about is an error. I will leave this up to the user to decide whether the warning returned is important.
//		Note: neo4j doesn't return an error code for warnings. This is why I'm searching the output for occurrences of the text "WARNING".
		Long problemCount = 0L;
		
		Process process;
		for (String query : this.queries) {
			query = query.replace("dataFilePath", this.getDataFilePath());
			System.out.println("Executing query: " + query);
			String[] command = {neo4jShellPath,  "-c", query};
			process = Runtime.getRuntime().exec(command);

			String output = readOutput(process);
			System.out.println(output + "\n");
			
			if (output.contains("ERROR")) {
				problemCount++;
			}
			
			if (output.contains("WARNING")) {
				problemCount++;
			}
		}
		
		return problemCount;
	}

	public Long loadData2() throws IOException {

		Process process = Runtime.getRuntime().exec(neo4jShellPath);

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

		for (String query : this.queries) {
			query = query.replace("dataFilePath", this.getDataFilePath());
			writer.write(query);
		}

//		writer.write("quit");
		writer.close();

//		Only needed for debugging
		String output = readOutput(process);
		
		System.out.println(output);

//		forciblyDestroy() ?
		process.destroy();
		
		while (process.isAlive()) { }
		
		System.out.println(process.exitValue());
		
		if (process.exitValue() != 0) {
			throw new RuntimeException("Sucksssssssss");
		}
		
		System.out.println();
		System.out.println();
		System.out.println();

//		Return number of lines? WITH row RETURN COUNT(row)
		return 0L;
	}

}
