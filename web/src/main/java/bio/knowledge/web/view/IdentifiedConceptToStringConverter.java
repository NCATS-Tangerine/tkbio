package bio.knowledge.web.view;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

import bio.knowledge.model.IdentifiedConcept;

/**
 * Converter class to allow for click listeners on IdentifiedConcept
 * See also: https://vaadin.com/docs/v7/framework/articles/CreatingYourOwnConverterForString.html
 *
 */

public class IdentifiedConceptToStringConverter implements Converter<String, IdentifiedConcept>{

	@Override
	public IdentifiedConcept convertToModel(String value, Class<? extends IdentifiedConcept> targetType, Locale locale)
			throws ConversionException {
		return null;
	}

	@Override
	public String convertToPresentation(IdentifiedConcept value, Class<? extends String> targetType, Locale locale)
			throws ConversionException {
		if (value == null) {
			return "";
		} else {
			return value.getName();
		}
	}

	@Override
	public Class<IdentifiedConcept> getModelType() {
		return IdentifiedConcept.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
