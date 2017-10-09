/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-16 Scripps Institute (USA) - Dr. Benjamin Good
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
package bio.knowledge.datasource.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

/**
 * @author Richard
 *
 */
@Service
public class FileUtilService implements ResourceLoaderAware {
	
	private ResourceLoader loader = null ;
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ResourceLoaderAware#setResourceLoader(org.springframework.core.io.ResourceLoader)
	 */
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		loader = resourceLoader ;
	}
	
	private ResourceLoader getResourceLoader() {
		if(loader!=null)
			return loader ;
		else
			return new DefaultResourceLoader() ;
	}
	
	private String basepath = null ;
	
	public FileUtilService() {
		basepath = "WEB-INF" ;
	}
	
	public FileUtilService(String classpath) {
		basepath = "classpath:"+classpath ;
	}
	
	public static FileUtilService getDefaultClassPath() {
		return new FileUtilService("") ;
	}
	
	public File getResourceFile( String subdirectory, String filename, String filext) throws IOException {
		
		if( filext==null || filext.isEmpty())
			filext = "" ;
		else
			filext = "."+ filext ;
		
		Resource resource = getResourceLoader().getResource(basepath+"/"+subdirectory+"/"+filename+filext) ;
		return resource.getFile() ;
	}
	
	public String getResourcePath( String subdirectory, String filename, String filext) throws IOException {
		return getResourceFile( subdirectory, filename, filext).getAbsolutePath() ;
	}
	
	public enum ImageType {
	
		PNG("png"), JPG("png"), GIF("png") ;
		
		private String imgType ;
		
		ImageType(String type) {
			this.imgType = type ;
		}
		
		public String toString() {
			return imgType ;
		}
	}
	
	public InputStream getConfigInputStream(String filename) throws IOException {
		File file = getResourceFile( "config", filename, "cfg" ) ;
		InputStream is = new FileInputStream(file) ;
		return is ;
	}

	public File getDataFile(String filename) throws IOException {
		return getResourceFile( "data", filename, null ) ;
	}
	
	public File getCachedDataFile(String filename) throws IOException {
		return getResourceFile( "cache", filename, null ) ;
	}
	
	public String getDataFilePath(String filename) throws IOException {
		return getResourcePath( "data", filename, null ) ;
	}

}
