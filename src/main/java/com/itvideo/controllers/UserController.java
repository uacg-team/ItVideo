package com.itvideo.controlers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

	@RequestMapping(value="/main", method=RequestMethod.GET)
	public String sayHi(Model viewModel) {
		
		return "home";
	}
}
