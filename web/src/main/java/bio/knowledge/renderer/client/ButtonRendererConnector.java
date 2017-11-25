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
package bio.knowledge.renderer.client;

import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;

/**
 * This class connects the server side
 * {@link bio.knowledge.renderer.ButtonRenderer} component with the client side
 * {@link bio.knowledge.renderer.client.ButtonRendererWidget} widget. It also
 * handles communication of state changes from the server side component to the
 * client side widget.
 * 
 * @see bio.knowledge.renderer.ButtonRenderer
 * @see bio.knowledge.renderer.client.ButtonRendererState
 * @see bio.knowledge.renderer.client.ButtonRendererWidget
 * 
 * @author Lance Hannestad
 */
@Connect(bio.knowledge.renderer.ButtonRenderer.class)
public class ButtonRendererConnector extends  com.vaadin.client.connectors.ButtonRendererConnector {

	private static final long serialVersionUID = -1788410400017478115L;

	@Override
	/*
	 * (non-Javadoc)
	 * @see com.vaadin.client.connectors.ButtonRendererConnector#getRenderer()
	 */
	public ButtonRendererWidget getRenderer() {
		return (ButtonRendererWidget) super.getRenderer();
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see com.vaadin.client.ui.AbstractConnector#onStateChanged(com.vaadin.client.communication.StateChangeEvent)
	 */
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		
		getRenderer().setWordHighlights(getState().highlightTerms);
		getRenderer().setHighlightColour(getState().highlightColour);
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see com.vaadin.client.ui.AbstractConnector#getState()
	 */
	public ButtonRendererState getState() {
		return (ButtonRendererState) super.getState();
	}
}
