package es.ariel.app.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;


@Service("static")
public class UploadFileServiceImplStatic implements IUploadFileService {

	private final static String UPLOADS = "uploads";

	@Override
	public Resource load(String fileName) throws MalformedURLException {
		Path pathFoto = getPath(fileName);

		UrlResource recurso = null;

		recurso = new UrlResource(pathFoto.toUri());
		if (!recurso.exists() && !recurso.isReadable()) {
			throw new RuntimeException("error al cargar imagen: " + pathFoto.toString());
		}

		return recurso;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path rootPath = getPath(uniqueFileName);

		Files.copy(file.getInputStream(), rootPath);

		return uniqueFileName;
	}

	@Override
	public boolean delete(String fileName) {
		Path rootPath = getPath(fileName);
		File archivo = rootPath.toFile();
		if (archivo.exists() && archivo.canRead()) {
			if (archivo.delete()) {
				return true;
			}
		}
		return false;
	}

	public Path getPath(String fileName) {
		Path directorioRecursos = Paths.get("src//main//resources//static//uploads");
		String rootPath = directorioRecursos.toFile().getAbsolutePath();
		return Paths.get(rootPath + "//" + fileName);	
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS).toFile());

	}

	@Override
	public void init() throws IOException {
		Files.createDirectory(Paths.get(UPLOADS));

	}

}
