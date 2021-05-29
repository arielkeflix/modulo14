package es.ariel.app;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Configuramos un recurso estatico "extra" aparte del "resources/static" que viene "pre-configurado"
@Configuration
public class MvcConfig implements WebMvcConfigurer{

	private final Logger log = LoggerFactory.getLogger(getClass());
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		WebMvcConfigurer.super.addResourceHandlers(registry);
		/***  para colocar carpeta de imagenes" uploads" en el root de la app   ***********************
		 * camabiando el "@Qualifier de tiendaController, Cuadro Service y clase raiz a "root"*/
		
		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();//agrego al arch upload la ruta raiz
		registry.addResourceHandler("/uploads/**").addResourceLocations(resourcePath); //creo una ruta "virtual" que puedo agregar el  arch foto
		log.info(resourcePath);			
		/**************************************************************************************************************/	
		
		/****************  para colocarla carpeta de imagenes" uploads" en un una carpeta externa***********************
		 * camabiando el "@Qualifier de tiendaController, Cuadro Service y clase raiz a "externo"
		 
		registry.addResourceHandler("/uploads/**")
		.addResourceLocations("file:/C:/Programacion/spring5/uploads/");
		*********************************************************************************************************************/
		
		/*para dentro de la carpeta static camabiando el "@Qualifier de tiendaController, Cuadro Service y clase raiz a "static"
		 * 
		 * */
			

	}

	
}
