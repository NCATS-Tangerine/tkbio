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
package bio.knowledge.web;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;

/**
 * Utility class for filedownloader
 * 
 * @author Chandan Mishra
 *
 */

public class OnDemandFileDownloader extends FileDownloader {

	/**
	 * Provide both the {@link StreamSource} and the filename in an on-demand
	 * way.
	 */
	public interface OnDemandStreamResource extends StreamSource {
		String getFilename();
	}

	private static final long serialVersionUID = 1L;
	private final OnDemandStreamResource onDemandStreamResource;
	private final Callable<Void> executeUponCompletion;
	
	public OnDemandFileDownloader(OnDemandStreamResource onDemandStreamResource) {
		this(onDemandStreamResource, null);
	}

	public OnDemandFileDownloader(OnDemandStreamResource onDemandStreamResource, Callable<Void> executeUponCompletion) {
		super(new StreamResource(onDemandStreamResource, ""));
		this.onDemandStreamResource = onDemandStreamResource;
		this.executeUponCompletion = executeUponCompletion;
	}

	@Override
	public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse response, String path)
			throws IOException {
		getResource().setFilename(onDemandStreamResource.getFilename());

		if (super.handleConnectorRequest(request, response, path)) {
			if (executeUponCompletion != null) {
				try {
					executeUponCompletion.call();
				} catch (Exception e) {
					// The calling code is responsible for passing in a
					// Callable<Void> object
					// that will not result in an exception when called. We
					// re-throw the error
					// and crash the program if something goes wrong.
					throw new RuntimeException(e.getMessage(), e.getCause());
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private StreamResource getResource() {
		return (StreamResource) this.getResource("dl");
	}

}
