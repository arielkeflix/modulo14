package es.ariel.app.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.ariel.app.models.entity.Cuadro;
import es.ariel.app.models.entity.Tienda;
import es.ariel.app.models.service.ICuadroService;
import es.ariel.app.models.service.ITiendaService;
import es.ariel.app.models.service.IUploadFileService;
import es.ariel.app.paginator.PageRender;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Controller
@SessionAttributes("cuadro")
@RequestMapping("/tienda")

public class TiendaController {

	@Autowired
	private ICuadroService cuadroService;
	@Autowired
	private ITiendaService tiendaService;
	@Autowired
	@Qualifier("root")
	private IUploadFileService uploadFileService;

	@GetMapping("/listar/{id}")
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, @PathVariable(value = "id") Long id,
			Model model) {

		Pageable pageRequest = PageRequest.of(page, 4);

		Page<Cuadro> cuadros = cuadroService.buscarPorTienda(id, pageRequest);

		PageRender<Cuadro> pageRender = new PageRender<>("/tienda/listar/" + id, cuadros);
		
		Tienda tienda = tiendaService.findOne(id);

		model.addAttribute("titulo", "Listado de cuadros de la tienda ' " + tienda.getNombre()
				+ " '  Resta espacio para " + tienda.getEspacio() + " cuadro/s");
		model.addAttribute("cuadros", cuadros);
		model.addAttribute("tienda", tienda);
		model.addAttribute("page", pageRender);
		return "cuadro/listar";
	}

	@GetMapping("/editarCuadro/{id}")
	public String editar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
		Cuadro cuadro = new Cuadro();

		if (id > 0) {
			cuadro = cuadroService.findOne(id);
			if (cuadro == null) {
				flash.addFlashAttribute("error", "El id del cuadro no existe!"); // mensaje hacia la vista
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El id del cuadro no puede ser cero!"); // mensaje hacia la vista
			return "redirect:/listar";
		}
		
		model.addAttribute("titulo", "Editar cuadro");
		model.addAttribute("cuadro", cuadro);
		
		return "cuadro/formCuadro";
	}

	@GetMapping("/formCuadro/{id}")
	public String crear(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Tienda tienda = tiendaService.findOne(id);
		Cuadro cuadro = new Cuadro();
		cuadro.setTienda(tienda);
		model.addAttribute("titulo", "Formulario de cuadro para la tienda de " + tienda.getNombre());
		model.addAttribute("cuadro", cuadro);

		return "cuadro/formCuadro";
	}

	@PostMapping("/formCuadro") 
	public String guardar(@Valid Cuadro cuadro, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) { 

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de cuadro para la tienda de " + cuadro.getTienda().getNombre());
			return "cuadro/formCuadro";
		}

		if (!foto.isEmpty()) {

			if (cuadro.getId() != null && // si existe cuadro
					cuadro.getId() > 0 && cuadro.getFoto().length() > 0) { // si existe foto anterior para eliminarla
																			// y cambiar por la nueva
				uploadFileService.delete(cuadro.getFoto());

			}
			String uniqueFileName = null;
			try {
				uniqueFileName = uploadFileService.copy(foto);

			} catch (Exception e) {
				e.printStackTrace();
			}
			flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFileName + "'");
			cuadro.setFoto(uniqueFileName);

			String mensaje = (cuadro.getId() != null) ? "Cuadro editado con exito" : "Cuadro creado con exito";
			if(cuadro.getAutor().isEmpty())cuadro.setAutor("anónimo");
			cuadroService.save(cuadro);// guardamos el obj en BD

			status.setComplete();// elimino el objeto cuadro de la sesion (@SessionAttributes("cuadro"))
			flash.addFlashAttribute("success", mensaje); // mensaje hacia la vista
		} else {
			model.addAttribute("titulo", "Formulario de cuadro para la tienda de " + cuadro.getTienda().getNombre());
			model.addAttribute("foto", "Falta subir archivo foto");
			return "cuadro/formCuadro";
		}
		return "redirect:listar/" + cuadro.getTienda().getId();
	}

	@GetMapping("/eliminarStock/{id}")
	public String eliminarStock(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		cuadroService.deleteAll(id);

		return "redirect:/listar/";
	}

	@GetMapping("/eliminarCuadro/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		Long idTienda = cuadroService.findOne(id).getTienda().getId();

		if (id > 0) {
			cuadroService.delete(id);
			flash.addFlashAttribute("success", "Cuadro eliminado con exito"); // mensaje hacia la vista
		}
		return "redirect:/tienda/listar/" + idTienda;
	}

	@GetMapping("/verCuadro/{id}")
	public String verCuadro(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Cuadro cuadro = new Cuadro();

		if (id > 0) {
			cuadro = cuadroService.findOne(id);
			if (cuadro == null) {
				flash.addFlashAttribute("error", "El id del cuadro no existe!"); // mensaje hacia la vista
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El id del cuadro no puede ser cero!"); // mensaje hacia la vista
			return "redirect:/listar";
		}

		model.addAttribute("titulo", cuadro.getNombre());
		model.addAttribute("enviar", "Editar cuadro");
		model.addAttribute("cuadro", cuadro);
		return "cuadro/verCuadro";
	}
}
