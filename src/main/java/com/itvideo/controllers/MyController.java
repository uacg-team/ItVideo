package com.itvideo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itvideo.model.Product;


@Controller
public class MyController {
	
	@RequestMapping(value="/home", method=RequestMethod.GET)
	public String sayHi(Model viewModel) {
		Product product = new Product("domati",5);
		viewModel.addAttribute("Text","Hello");
		viewModel.addAttribute("product",product);
		
		return "home";
	}
	@RequestMapping(value="/homea", method=RequestMethod.GET)
	public String sayHai(Model viewModel) {
		Product product = new Product("domati",5);
		viewModel.addAttribute("Text","Hello");
		viewModel.addAttribute("product",product);
		
		return "home";
	}
	
}
