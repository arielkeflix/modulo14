package es.ariel.app.controller;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.ariel.app.models.entity.Tienda;
import es.ariel.app.models.service.ICuadroService;
import es.ariel.app.models.service.ITiendaService;
import es.ariel.app.paginator.PageRender;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Controller
@SessionAttributes({ "tienda" })
public class FranquiciaController {

	@Autowired
	private ITiendaService tiendaService;
	@Autowired
	private ICuadroService cuadroService;

	@GetMapping({ "/index", "", "/" })
	public String inicio(Model model) {

		model.addAttribute("titulo", "Bienvenido a  'White necklace' ");

		return "index";
	}

	@GetMapping("/listar")
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model, SessionStatus status) {

		Pageable pageRequest = PageRequest.of(page, 4);

		Page<Tienda> tiendas = tiendaService.findAll(pageRequest);

		PageRender<Tienda> pageRender = new PageRender<>("/listar", tiendas);		

		model.addAttribute("titulo", "Listado de tiendas");
		model.addAttribute("tiendas", tiendas);

		model.addAttribute("page", pageRender);

		return "tienda/listar";
	}

	@GetMapping("/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
		Tienda tienda = new Tienda();

		if (id > 0) {
			tienda = tiendaService.findOne(id);
			if (tienda == null) {
				flash.addFlashAttribute("error", "El id del tienda no existe!"); // mensaje hacia la vista
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El id del tienda no puede ser cero!"); // mensaje hacia la vista
			return "redirect:/listar";
		}

		model.addAttribute("titulo", "Editar tienda");
		model.addAttribute("tienda", tienda);
		return "tienda/form";
	}

	@GetMapping("/form")
	public String crear(Model model) { 

		Tienda tienda = new Tienda();
		model.addAttribute("titulo", "Formulario de tienda");
		model.addAttribute("tienda", tienda);
		return "tienda/form";
	}	

	@PostMapping("/form")
	public String guardar(@Valid @ModelAttribute("tienda") Tienda tienda, BindingResult result, Model model,
			RedirectAttributes flash, SessionStatus status) { // recibe el objeto del formulario

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de tienda");
			return "tienda/form";
		}
		String mensaje = (tienda.getId() != null) ? "Tienda editada con exito" : "Tienda creada con exito";
		tiendaService.save(tienda);// guardamos el obj en BD
		status.setComplete();// elimino el objeto tienda de la sesion (@SessionAttributes("tienda"))
		flash.addFlashAttribute("success", mensaje); // mensaje hacia la vista

		return "redirect:listar";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			cuadroService.deleteAll(id);
			tiendaService.delete(id);
			flash.addFlashAttribute("success", "Tienda eliminada con exito"); // mensaje hacia la vista
		}
		return "redirect:/listar";
	}
}
