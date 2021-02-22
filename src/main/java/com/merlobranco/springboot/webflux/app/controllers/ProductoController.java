package com.merlobranco.springboot.webflux.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.merlobranco.springboot.webflux.app.models.dao.ProductoDao;
import com.merlobranco.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@Controller
public class ProductoController {
	
	@Autowired
	private ProductoDao dao;
	
	@GetMapping({"/listar", "/"})
	public String listar(Model model) {
		
		// There is no need for subscribing, Thymeleaf will do it for us
		// The thymeleaf template is the observer subscribed to the observable
		Flux<Producto> productos = dao.findAll();
		model.addAttribute("titulo", "Listado de productos");
		model.addAttribute("productos", productos);
		
		return "listar";
	}

}
