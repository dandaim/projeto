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

import br.ufrj.cc.aleph.controller.form.TripletsForm;
import br.ufrj.cc.aleph.controller.validator.TripletsValidator;
import br.ufrj.cc.aleph.service.RdfService;

@Controller
@RequestMapping( "/rdf" )
public class RdfController {

	@Autowired
	private RdfService rdfService;

	@InitBinder( value = "tripletsForm" )
	protected void initBinder( final WebDataBinder binder ) {

		binder.setValidator( new TripletsValidator() );
	}

	@ModelAttribute( "tripletsForm" )
	public TripletsForm createForm() {
		return new TripletsForm();
	}

	@RequestMapping( method = RequestMethod.GET )
	public String showIndex( final Model model ) {

		String view = "/rdf";

		return view;
	}

	@RequestMapping( value = "/request", method = RequestMethod.POST )
	public String callService(
			@ModelAttribute( "tripletsForm" ) @Valid TripletsForm tripletsForm,
			final BindingResult result, final Model model ) {

		String view = "/rdf";

		if ( result.hasErrors() ) {

			return view;
		}

		try {

			rdfService.generateRdfFile( tripletsForm );

		} catch ( Exception e ) {

			System.out.println( "Erro na classe MainController" );
			System.out.println( "Erro: " + e.getMessage() );
		}

		return view;
	}

}
