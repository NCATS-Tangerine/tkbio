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
package bio.knowledge.graph.jsonmodels;

import java.lang.reflect.Type;

public class Layout extends AbstractElementData {

	private static final long serialVersionUID = 219802557599746361L;
	// node cuis
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String layoutName) {
		this.name = layoutName;
	}
	
	public Layout getLayout() {
		return this;
	}
	
	public void setLayout(Layout layout) {
		this.name = layout.name;
		update();
	}
	
	@Override
	public void update() {
		put("name", this.name);
	}
	
	public Layout(String layoutName) {
		if (layoutName.toLowerCase().equals("manual")) {
			// if Manual selected from client side
			layoutName="preset";
		}
		setName(layoutName);
		update();
	}
	
	public Layout() {
		setName("preset");
		update();
	}
	
	@Override
	public Type[] getActualTypeArguments() {
		return new Type[] {"".getClass(), "".getClass()};
	}

	@Override
	public Type getRawType() {
		return this.getRawType();
	}
	
}
