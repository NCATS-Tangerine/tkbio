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

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import bio.knowledge.model.core.IdentifiedEntity;
import bio.knowledge.service.AnnotationService;
import bio.knowledge.service.AuthenticationState;
import bio.knowledge.service.ConceptMapArchiveService;
import bio.knowledge.service.ConceptService;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.StatementService;
import bio.knowledge.service.core.IdentifiedEntityService;
import bio.knowledge.service.core.MessageService;
import bio.knowledge.service.core.OntologyTermService;
import bio.knowledge.service.organization.ContactFormService;
import bio.knowledge.web.ui.DesktopUI;

/**
 * @author Richard
 *
 */
public abstract class BaseView extends VerticalLayout implements View, MessageService {
	
	private static final long serialVersionUID = -137428743692695008L;
	
	protected Logger _logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	protected KBQuery query;

	@Autowired
	Registry registry;

	@Autowired
	protected OntologyTermService ontologyTermService;

	@Autowired
	protected IdentifiedEntityService<IdentifiedEntity> identifierService;

	@Autowired
	protected ContactFormService contactFormService;

	@Autowired
	protected ConceptService conceptService ;

	@Autowired
	protected ConceptMapArchiveService conceptMapArchiveService;

	@Autowired
	protected StatementService statementService ;
	
	@Autowired
	protected AnnotationService annotationService ;
	
	@Autowired
	protected AuthenticationState authenticationState;

	
	@Autowired
	private MessageSource messageSource ;
	
	public DesktopUI getCurrentUI() {
		return (DesktopUI)(UI.getCurrent());
	}
	
	/**
	 * 
	 * @return current Locale of this user inteface
	 */
	@Override
	public Locale getLocale() {
		return UI.getCurrent().getLocale();
	}
	
	/**
	 * 
	 * @param locale current locale to which to set the user interface
	 */
	@Override
	public void setLocale(Locale locale) {
		UI.getCurrent().setLocale(locale);
	}
	
	@Override
	public String getMessage(String id) {
		
		Locale locale = getLocale() ;
		
		String msg = "" ;
		try {
			msg = messageSource.getMessage(id,null,locale) ;
		} catch (NoSuchMessageException nsme) {
			msg = messageSource.getMessage(id, null, new Locale("en")) ;
		}
		return msg ;
	}
	
	@Override
	public String getMessage(String id, String tag) throws NoSuchMessageException {
		
		if( id == null || id.isEmpty() )
			throw new NoSuchMessageException("ERROR: Null or empty getMessage id?") ;
		
		if( tag == null || tag.isEmpty() )
			throw new NoSuchMessageException("ERROR: Null or empty getMessage tag?") ;
		
		Locale locale = getLocale();
		
		String msg = "" ;
		try {
			// Resolve on level of language only for now
			//System.err.println("### Getting '"+locale.getLanguage()+"' version of message "+id);
			msg = messageSource.getMessage(id,new Object[]{tag},locale) ;
		} catch (NoSuchMessageException nsme) {
			//System.err.println("### Getting 'en' version of message "+id);
			msg = messageSource.getMessage(id, new Object[]{tag}, new Locale("en")) ;
		}
		return msg ;
		
	}
	
	public String tag( String tag, String text ) {
		return "<"+tag+">"+text+"<"+tag+"/>" ;
	}
	
	public String tag( String tag, String css_class, String text ) {
		return "<"+tag+" class=\""+css_class+"\">"+text+"<"+tag+"/>" ;
	}
	
	public Label h1(String id) {
		return new Label( tag("h1", getMessage(id) ), ContentMode.HTML ) ;
	}
	
	public Label h2(String id) {
		return new Label( tag("h2", getMessage(id) ), ContentMode.HTML ) ;
	}
	
	public Label h3(String id) {
		return new Label( tag("h3", getMessage(id) ), ContentMode.HTML ) ;
	}
	
	public String p(String id) {
		return tag("p", getMessage(id) ) ;
	}
	
	public String p( String[] ids ) {
		String text = "" ;
		for(String id : ids) {
			text += tag("p", getMessage(id) ) ;
		}
		return text ;
	}
	
	public String p( String css_class, String id ) {
		return tag("p", css_class, getMessage(id) ) ;
	}
	
	public String p(String css_class, String[] ids) {
		String text = "" ;
		for(String id : ids) {
			text += tag("p", css_class, getMessage(id) ) ;
		}
		return text ;
	}
	
	// A hack... not sure why Window access is not directly available in Vaadin...
	private Window mainWindow = null ;
	
	public Window getMainWindow() {
		if(mainWindow == null) {
			Collection<Window> windows = getCurrentUI().getWindows() ;
			Iterator<Window> witerator = windows.iterator() ;
			// blissful assumption that it is the first Window in the list?
			mainWindow = witerator.next() ;
		}
		return mainWindow ;
	}
}
