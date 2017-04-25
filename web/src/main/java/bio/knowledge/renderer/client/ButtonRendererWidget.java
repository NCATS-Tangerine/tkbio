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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Button;
import com.vaadin.v7.client.widget.grid.RendererCellReference;

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
public class ButtonRendererWidget extends com.vaadin.v7.client.renderers.ButtonRenderer {
	
	private String[] highlightWords;
	// Default colour is bright red
	private String highlightColour = "#FF0000";
	
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
			if (highlightWords != null) {
				for (String word : highlightWords) {
					text = insertHtmlTags(text, word, "<span style=\"color:" + highlightColour + ";\">", "</span>");
				}
			}
			
			button.setHTML(text);
			button.setWidth("100%");
			
		} else {
			button.setVisible(false);
		}
	}
	
	/**
	 * Inserts HTML tags (or, in fact, any sort of tag) around all substrings of
	 * {@code text} which match {@code taggedText} with case insensitivity. Will
	 * also not match substrings that are between {@code leftTag} and
	 * {@code rightTag}. <br>
	 * <br>
	 * For example, insertHtmlTags("HELLO friend", "e", "(", ")") will return
	 * "H(E)LLO fri(e)nd"
	 * 
	 * @param text
	 *            The text to insert HTML tags into
	 * @param taggedText
	 *            The substring of {@code text} to place HTML tags around
	 * @param leftTag
	 *            The HTML tag to place to the left of {@code taggedText}
	 * @param rightTag
	 *            The HTML tag to place to the left of {@code taggedText}
	 * @return A string with HTML tags wrapping each occurrence of
	 *         {@code taggedText}
	 */
	private String insertHtmlTags(String text, String taggedText, String leftTag, String rightTag) {
		List<Integer> l = getIndices(text, taggedText, leftTag, rightTag);
		int taglength = leftTag.length() + rightTag.length();
		int k = 0;
		for (Integer i : l) {
			i = i + (taglength * k);
			k++;
			int e = i + taggedText.length();
			text = text.substring(0, i) + leftTag + text.substring(i, e) + rightTag + text.substring(e);
		}
		return text;
	}

	/**
	 * Gets a list of indices of occurrences of substring within text. Ignores
	 * substrings that occur between occurrences of {@code leftTag} and
	 * {@code rightTag}.
	 * 
	 * @param text
	 * @param substring
	 * @param leftTag
	 * @param rightTag
	 * @return
	 */
	private List<Integer> getIndices(String text, String substring, String leftTag, String rightTag) {
		List<Integer> l = new ArrayList<Integer>();

		text = text.toLowerCase();
		substring = substring.toLowerCase();
		for (int i = 0; i < text.length(); ) {
			int index = text.indexOf(substring, i);
			int tagIndex = text.indexOf(leftTag.toLowerCase(), i);
			
			boolean tagNotInterfering = tagIndex == -1 || tagIndex > index + substring.length();
			
			if (tagNotInterfering) {
				if (index == -1) {
					break;
				} else {
					l.add(index);
					i = index + substring.length();
				}
			} else {
				i = text.indexOf(rightTag, i) + rightTag.length();
			}
		}

		return l;
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
