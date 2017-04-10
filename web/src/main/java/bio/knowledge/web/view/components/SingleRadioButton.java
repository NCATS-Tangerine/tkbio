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
package bio.knowledge.web.view.components;

import com.vaadin.ui.OptionGroup;

public class SingleRadioButton extends OptionGroup {

	private static final long serialVersionUID = -3846906946738058251L;

	public SingleRadioButton(String caption) {
		this.setMultiSelect(true);
		this.addItem(Boolean.TRUE);
		this.setItemCaption(Boolean.TRUE, caption);
	}
	
	public SingleRadioButton(String caption, boolean selectedByDefault) {
		this(caption);
		
		if (selectedByDefault) {
			this.select(Boolean.TRUE);
		}
	}
	
	public boolean isChecked() {
		return this.isSelected(Boolean.TRUE);
	}
	
	public void setChecked(boolean isChecked) {
		if (isChecked) {
			this.select(Boolean.TRUE);
		} else {
			this.unselect(Boolean.TRUE);
		}
	}
}
