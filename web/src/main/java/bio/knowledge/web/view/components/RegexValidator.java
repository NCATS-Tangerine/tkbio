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

import com.vaadin.v7.data.Validator;

public class RegexValidator implements Validator {

	private static final long serialVersionUID = 6137856685757881092L;
	
	private final boolean match;
	private final String regex;
	private final String errorMessage;
	
	public RegexValidator(String regex, String errorMessage) {
		this(regex, errorMessage, true);
	}
	
	public RegexValidator(String regex, String errorMessage, boolean match) {
		this.regex = regex;
		this.errorMessage = errorMessage;
		this.match = match;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		String text = (String) value;
		
		if (match) {
			if (text.matches(regex)) {
				throw new InvalidValueException(errorMessage);
			}
		} else {
			if (!text.matches(regex)) {
				throw new InvalidValueException(errorMessage);
			}
		}
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		} else if (!(other instanceof RegexValidator)) {
			return false;
		} else {
			RegexValidator otherRegexValidator = (RegexValidator) other;
			return 	this.errorMessage.equals(otherRegexValidator.errorMessage) &&
					this.regex.equals(otherRegexValidator.regex);
		}
	}
	
}
