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
package bio.knowledge.web.view;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import bio.knowledge.model.datasource.Result;

//first iteration of a class that iteratively 
// builds a description from a ResultSet
// TODO: Need to generalize this "description builder" to handle more data fields?
public class DescriptionBuilder implements Consumer<Result> {
	
	private static final String DEFAULT = "";		
	private Map<String,String> details = new HashMap<String,String>() ;
	
	@Override
	public void accept(Result result) {
		String propLabel = (String)result.get("propLabel") ;
		if(propLabel!=null) {
			String[] pdPart  = propLabel.split("\\@") ; // ignore language?
			propLabel = pdPart[0];
			String valueLabel = (String)result.get("valueLabel") ;
			String value = (String)result.get("value") ;
			
			String item = details.getOrDefault(propLabel, DEFAULT) ;
			if(!item.isEmpty()) item += ";" ;
			item += valueLabel+"|"+value ;
			details.put(propLabel, item);
			
		}else if(result.containsKey("wikipedia_article_url")) {
			// Outlier case of there being a classical 
			// concept descriptor lurking in this result
			String item = details.getOrDefault("wikipedia_article_url", DEFAULT) ;
			if(!item.isEmpty()) item += ";" ;
			item += result.get("wikipedia_article_url") ;
			details.put("wikipedia_article_url", item);
		}
	}
	
	public String getArticleUrl() {
		return details.getOrDefault("wikipedia_article_url", DEFAULT);
	}
	
	public Map<String,String> getDetails() {
		return details ;
	}
}