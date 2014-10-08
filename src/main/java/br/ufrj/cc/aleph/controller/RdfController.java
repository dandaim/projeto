package br.ufrj.cc.aleph.controller;

import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.validation.Valid;

import org.apache.log4j.Logger;
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
import br.ufrj.cc.aleph.service.MailService;
import br.ufrj.cc.aleph.service.RdfService;

@Controller
@RequestMapping( "/rdf" )
public class RdfController {

	private static final Logger LOGGER = Logger.getLogger( RdfController.class );

	@Autowired
	private RdfService rdfService;

	@Autowired
	private MailService mailService;

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
			final BindingResult result, final Model model )
			throws AddressException, MessagingException {

		String view = "/rdf";

		if ( result.hasErrors() ) {

			mailService.processMail( "dandaim@gmail.com", "Teste", "Uhul!",
					"ufrj.cc.aleph@gmail.com" );

			return view;
		}

		String flowId = UUID.randomUUID().toString();

		try {

			rdfService.generateRdfFile( tripletsForm, flowId );

		} catch ( Exception e ) {

			LOGGER.error( "{" + flowId
					+ "} -> Erro no fluxo de geração do arquivo RDF. "
					+ e.getMessage() );
		}

		return view;
	}
}
