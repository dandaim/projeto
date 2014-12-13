package br.ufrj.cc.aleph.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import br.ufrj.cc.aleph.controller.form.TripletsForm;

@Component
public class TripletsValidator implements Validator {

	public boolean supports( Class<?> arg0 ) {
		return TripletsForm.class.isAssignableFrom( arg0 );
	}

	public void validate( Object target, Errors errors ) {

		TripletsForm form = ( TripletsForm ) target;

		ValidationUtils.rejectIfEmptyOrWhitespace( errors, "email",
				"field.required" );
		ValidationUtils.rejectIfEmptyOrWhitespace( errors, "name",
				"field.required" );

		if ( form.getExamples().getSize() == 0 ) {

			errors.rejectValue( "examples", "field.required" );
		}
	}
}
