package br.ufrj.cc.aleph.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.ufrj.cc.aleph.controller.form.TripletsForm;

@Component
public class TripletsValidator implements Validator {

	public boolean supports( Class<?> arg0 ) {
		return TripletsForm.class.isAssignableFrom( arg0 );
	}

	public void validate( Object target, Errors errors ) {

	}

}
