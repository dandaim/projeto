package br.ufrj.cc.aleph.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import br.ufrj.cc.aleph.controller.form.BeaconForm;

@Component
public class BeaconValidator implements Validator {
	
	public boolean supports( Class<?> arg0 ) {		
		return BeaconForm.class.isAssignableFrom( arg0 );
	}
	
	public void validate( Object target, Errors errors ) {
		
		BeaconForm form = (BeaconForm) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace( errors, "email", "field.required" );
		ValidationUtils.rejectIfEmptyOrWhitespace( errors, "name", "field.required" );
		
		if( form.getArqb().getSize() == 0 ) {
			errors.rejectValue( "arqb", "field.required" );
		}
		
		Long size = 0L;
		
		for( CommonsMultipartFile file : form.getArqpos() ) {
			size += file.getSize();
		}
		
		if( size == 0 ) {
			errors.rejectValue( "arqpos", "field.required" );
		}
		
	}

}
