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

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Richard Bruskiewich
 *
 */
@SpringBootApplication
@ComponentScan(
		basePackages={
			"bio.knowledge.dataloader"
		} 
)
public class DataLoader {

	protected static Logger _logger = LoggerFactory.getLogger(DataLoader.class);
	
	private static String[] types;

	private static void processArgs( String[] args ) {

		// create Options object

		Options options = new Options();

		@SuppressWarnings("static-access")
		Option libraryOpt = OptionBuilder
		.withArgName("library")
		.withLongOpt("library")
		.hasArg()
		.withDescription("Data library 'library' where the data files are to be found.")
		.create('l');

		options.addOption(libraryOpt);

		@SuppressWarnings("static-access")
		Option neo4jShellOpt = OptionBuilder
		.withArgName("neo4jShell")
		.withLongOpt("neo4jShell")
		.hasArg()
		.withDescription("Path to neo4jshell (if not default).")
		.create('n');

		options.addOption(neo4jShellOpt);

		@SuppressWarnings("static-access")
		Option debugOpt = OptionBuilder
		.withArgName("debug")
		.withLongOpt("debug")
		.withDescription("Debugging mode")
		.create('d');

		options.addOption(debugOpt);

		@SuppressWarnings("static-access")
		Option typeOpt = OptionBuilder
		.withArgName("type")
		.withLongOpt("type")
		.hasArg()
		//.isRequired()
		.withDescription("Specific target data type(s) to be loaded.  "
				       + "Multiple data type names may be specified but should be comma delimited."
				       + "Data will be loaded in the given order of the data type names."
		)
		.create('t');

		options.addOption(typeOpt);
		
		@SuppressWarnings("static-access")
		Option purgeOpt = OptionBuilder
		.withArgName("purge")
		.withLongOpt("purge")
		.withDescription("Purge database of type-related entries before reloading.")
		.create('p');

		options.addOption(purgeOpt);
		
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
				
		CommandLineParser parser = new BasicParser();
		CommandLine line;
		try {
			// parse the command line arguments
			line = parser.parse(options, args);

			//if( line.hasOption("debug") ) TableLoader.setDebugOn() ;
			
			//if( line.hasOption("purge") ) TableLoader.setPurgeOn() ;
			
			String libraryPath  = line.getOptionValue("library");
			if(libraryPath != null)
				TableLoader.setLibraryPath(libraryPath);
			
			String neo4jShellPath  = line.getOptionValue("neo4jShell");
			if(neo4jShellPath != null)
				TableLoader.setNeo4jShellPath(neo4jShellPath);

			if ( ! line.hasOption("type") ) {

				formatter.printHelp( "DataLoader", options );
				System.exit(-1);
				
			} else {

				String typeList = line.getOptionValue("type");
				types  = typeList.split("\\,") ;
			}

		} catch ( ParseException exp) {
			// oops, something went wrong
			_logger.error("Data loading failed.  Invalid or undefined class: " + exp.getMessage());
			System.exit(-1);
		}
	}
	
	private static void processData( ApplicationContext ctx ) {
		
		_logger.info("Library Path is: "+TableLoader.getLibraryPath());
		
		for(String name : types) {
			
			_logger.info("Loading data with bean: "+name);
			
			try {
				// Load the data loader service Bean here, if available
				TableLoader loader = (TableLoader)ctx.getBean(name) ;
				
				// ... then use it to load the available data
				Long errorCode = loader.loadData();
				
				if(errorCode==0)
					_logger.info("Loading of " + name+ " data completed successfully?") ;
				else
					_logger.error("Warning: " + errorCode + " errors encountered for " + name+ " data loading?") ;

			} catch (NoSuchBeanDefinitionException nsb) {
				_logger.error("Error: there is no bean definition with the specified name?") ;
			} catch (BeansException be) {
				_logger.error("Error: a bean of specified name could not be obtained?") ;
			} catch (IOException ioe) {
				_logger.error(ioe.getLocalizedMessage()) ;
			}
		}
		
		_logger.info("Loading completed!") ;
	}

	static private int normalExit() {
		return 0 ;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		processArgs( args ) ;
		
		ApplicationContext ctx = SpringApplication.run( DataLoader.class, args );
		
        _logger.debug("Let's inspect the beans provided by Spring Boot:");
        
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
        	_logger.debug(beanName);
        }
        
		processData( ctx  ) ;
		
		// Need to explicitly exit here because the
		// SpringApplication is still running
		// thinking that it is in a web session!
		SpringApplication.exit( ctx, ()->normalExit() ) ;
	}
}
