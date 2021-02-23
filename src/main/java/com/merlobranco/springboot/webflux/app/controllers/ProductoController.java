package com.merlobranco.springboot.webflux.app.controllers;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.merlobranco.springboot.webflux.app.models.dao.ProductoDao;
import com.merlobranco.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@Controller
public class ProductoController {
	
	private static final Logger log = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoDao dao;
	
	@GetMapping({"/listar", "/"})
	public String listar(Model model) {
		
		// There is no need for subscribing, Thymeleaf will do it for us
		// The thymeleaf template is the observer subscribed to the observable
		Flux<Producto> productos = dao.findAll().map(producto-> {
			producto.setNombre(producto.getNombre().toUpperCase());
			return producto;
		});
		
		productos.subscribe(p -> log.info(p.getNombre()));
		
		model.addAttribute("titulo", "Listado de productos");
		model.addAttribute("productos", productos);
		
		return "listar";
	}
	
	@GetMapping("/listar-datadriver")
	public String listarDataDriver(Model model) {
		Flux<Producto> productos = dao.findAll().map(producto-> {
			producto.setNombre(producto.getNombre().toUpperCase());
			return producto;
		}).delayElements(Duration.ofSeconds(1));
		
		productos.subscribe(p -> log.info(p.getNombre()));
		
		model.addAttribute("titulo", "Listado de productos");
		// We don´t have to wait until all the flow is returned or solved
		model.addAttribute("productos", new ReactiveDataDriverContextVariable(productos, 1));
		
		return "listar";
	}
	
	@GetMapping("/listar-full")
	public String listarFull(Model model) {
		Flux<Producto> productos = dao.findAll().map(producto-> {
			producto.setNombre(producto.getNombre().toUpperCase());
			return producto;
		}).repeat(5000);
		
		
		model.addAttribute("titulo", "Listado de productos");
		model.addAttribute("productos", productos);
		
		return "listar";
	}
	
	@GetMapping("/listar-chunked")
	public String listarChunked(Model model) {
		Flux<Producto> productos = dao.findAll().map(producto-> {
			producto.setNombre(producto.getNombre().toUpperCase());
			return producto;
		}).repeat(5000);
		
		
		model.addAttribute("titulo", "Listado de productos");
		model.addAttribute("productos", productos);
		
		return "listar-chunked";
	}
	
	

}
