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
package bio.knowledge.renderer;

import bio.knowledge.renderer.client.ButtonRendererState;

/**
 * This class looks and functions a lot like Vaadin's ButtonRenderer, with a few
 * extra features. First, it handles text-overflow with ellipses rather than
 * simply cutting the text off. Secondly, it allows for highlighting substrings
 * of the text. This is used to highlight search terms when searching up
 * concepts.
 * 
 * @see bio.knowledge.renderer.client.ButtonRendererConnector
 * @see bio.knowledge.renderer.client.ButtonRendererState
 * @see bio.knowledge.renderer.client.ButtonRendererWidget
 * 
 * @author Lance Hannestad
 */
public class ButtonRenderer extends com.vaadin.ui.renderers.ButtonRenderer {

	private static final long serialVersionUID = 3478709185409867558L;
	
	/**
	 * 
	 */
	public ButtonRenderer() {
		
	}
	
	/**
	 * 
	 * @param listener
	 */
	public ButtonRenderer(RendererClickListener listener) {
		super(listener);
	}

	/**
	 * Sets the parameters for highlighting special terms in the button's
	 * caption.
	 * 
	 * @param highlightTerms
	 *            An array of words to be highlighted.
	 * @param highlightColour
	 *            The colour to use for highlighting. The default colour is
	 *            #FF0000.
	 */
	public void setHighlightProperties(String[] highlightTerms, String highlightColour) {
		getState().highlightTerms = highlightTerms;
		if (highlightColour != null) {
			getState().highlightColour = highlightColour;
		}
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see com.vaadin.server.AbstractClientConnector#getState()
	 */
	public ButtonRendererState getState() {
		return (ButtonRendererState) super.getState();
	}
}
