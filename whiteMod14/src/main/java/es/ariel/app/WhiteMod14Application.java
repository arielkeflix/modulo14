package es.ariel.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import es.ariel.app.models.service.IUploadFileService;

@SpringBootApplication
public class WhiteMod14Application implements CommandLineRunner{
	
	@Autowired
	@Qualifier("root")
	IUploadFileService uploadFileService ;
	public static void main(String[] args) {
		SpringApplication.run(WhiteMod14Application.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
	//	uploadFileService.deleteAll();
	//	uploadFileService.init();
		
	}
}
