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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.Button;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.widget.grid.RendererCellReference;

/**
 * The client side widget, corresponding to the server side
 * {@link bio.knowledge.renderer.ButtonRenderer}. This contains the logic that
 * renders the widget for the client. <br>
 * <br>
 * The GWT button's in this widget have a CSS style that makes them look the
 * same as Vaadin NativeButton's. That CSS style also allows for ellipses when
 * the buttons are too long for their grid cell. <br>
 * <br>
 * This widget also highlights specified terms. <br>
 * <br>
 * If changes are made to this class,
 * {@link bio.knowledge.renderer.ButtonRendererWidgetset.gwt.xml} will have to
 * be recompiled with the Vaadin widgetset compiler.
 * 
 * 
 * @see bio.knowledge.renderer.ButtonRenderer
 * @see bio.knowledge.renderer.client.ButtonRendererConnector
 * @see bio.knowledge.renderer.client.ButtonRendererState
 * 
 * @author Lance Hannestad
 */
public class ButtonRendererWidget extends com.vaadin.client.renderers.ButtonRenderer {
	
	private String[] highlightWords;
	// Default colour is bright red
	private String highlightColour = "#FF0000";
	
	private String regexFlags = "gi";
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see com.vaadin.client.renderers.ButtonRenderer#createWidget()
	 */
	public Button createWidget() {
		Button b = GWT.create(Button.class);
		b.addClickHandler(this);
		b.setStylePrimaryName("grid-button-style");
		return b;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see com.vaadin.client.renderers.ButtonRenderer#render(com.vaadin.client.widget.grid.RendererCellReference, java.lang.String, com.google.gwt.user.client.ui.Button)
	 */
	public void render(RendererCellReference cell, String text, Button button) {
		if (text != null) {
			
			text = WidgetUtil.escapeHTML(text);
			
			if (highlightWords != null) {
				for (String word : highlightWords) {
					RegExp regExp = RegExp.compile(word, regexFlags);
					text = regExp.replace(text, "<span style=\"color:" + highlightColour + ";\">$&</span>");
				}
			}
			
			button.setHTML(text);
			button.setWidth("100%");
			
		} else {
			button.setVisible(false);
		}
	}
	
	/**
	 * 
	 * @param words
	 */
	public void setWordHighlights(String[] words) {
		this.highlightWords = words;
	}
	
	/**
	 * 
	 * @param colour
	 */
	public void setHighlightColour(String colour) {
		this.highlightColour = colour;
	}
}
