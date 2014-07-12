package br.ufrj.cc.aleph.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.ufrj.cc.aleph.controller.form.BeaconForm;
import br.ufrj.cc.aleph.controller.validator.BeaconValidator;
import br.ufrj.cc.aleph.service.PrologService;

@Controller
@RequestMapping( "/aleph" )
public class AlephController {

	@Autowired
	private PrologService prologService;

	@InitBinder( value = "beaconForm" )
	protected void initBinder( final WebDataBinder binder ) {

		binder.setValidator( new BeaconValidator() );
	}

	@ModelAttribute( "beaconForm" )
	public BeaconForm createForm() {
		return new BeaconForm();
	}

	@RequestMapping( method = RequestMethod.GET )
	public String showAlephIndex( final Model model ) {

		String view = "/aleph";

		return view;
	}

	@RequestMapping( value = "/request", method = RequestMethod.POST )
	public String callService(
			@ModelAttribute( "beaconForm" ) @Valid BeaconForm beaconForm,
			final BindingResult result, final Model model ) {

		String view = "/aleph";

		if ( result.hasErrors() ) {

			return view;
		}

		try {

			prologService.executeShellScript( beaconForm );

		} catch ( Exception e ) {

			System.out.println( "Erro na classe MainController" );
			System.out.println( "Erro: " + e.getMessage() );
		}

		return view;
	}

}
