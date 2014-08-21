package br.ufrj.cc.aleph.controller;

import java.util.UUID;
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
import br.ufrj.cc.aleph.controller.form.BeaconForm;
import br.ufrj.cc.aleph.controller.validator.BeaconValidator;
import br.ufrj.cc.aleph.service.PrologService;

@Controller
@RequestMapping( "/home" )
public class MainController {

	private static final Logger LOGGER = Logger.getLogger( MainController.class );

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
	public String showIndex( final Model model ) {

		String view = "/home";

		return view;
	}

	@RequestMapping( value = "/request", method = RequestMethod.POST )
	public String callService( @ModelAttribute( "beaconForm" ) @Valid BeaconForm beaconForm, final BindingResult result, final Model model ) {

		String view = "/home";

		if ( result.hasErrors() ) {

			return view;
		}

		String flowId = UUID.randomUUID().toString();

		try {

			prologService.executeShellScript( beaconForm, flowId );

		} catch ( Exception e ) {

			LOGGER.error( "{" + flowId + "} -> Erro na requisição: " + e.getMessage() );

		}

		return view;
	}

}
