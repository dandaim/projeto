package br.ufrj.cc.aleph.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping( "/" )
public class IndexController {

	@RequestMapping( method = RequestMethod.GET )
	public String showIndex( final Model model ) {

		String view = "/index";

		return view;
	}

}
