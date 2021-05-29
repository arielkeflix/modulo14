package es.ariel.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ariel.app.models.dao.ICuadroDao;

import es.ariel.app.models.entity.Cuadro;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
@Service
public class CuadroService implements ICuadroService {

	@Autowired
	private ICuadroDao cuadroDao;
	@Autowired
	@Qualifier("root")
	private IUploadFileService uploadFileService; 

	@Override
	@Transactional
	public void save(Cuadro cuadro) {
		cuadroDao.save(cuadro);

	}

	@Override
	@Transactional(readOnly = true)
	public Cuadro findOne(Long id) {
		return cuadroDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		Cuadro cuadro = findOne(id);
		
		if ( cuadro.getFoto().length() > 0) { // si tiene foto																		
			uploadFileService.delete(cuadro.getFoto());			
		}
		cuadroDao.deleteById(id);

	}
	
	
	@Transactional(readOnly = true)
	@Override
	public List<Cuadro> buscarPorTienda(Long tiendaId) {
		
		return  (List<Cuadro>) cuadroDao.findByTiendaId(tiendaId);
	}

	@Override
	public void deleteAll(Long tiendaId) {
		List <Cuadro> cuadros = buscarPorTienda( tiendaId);
		for(Cuadro c:cuadros) {
			delete(c.getId());
		}
				
	}	


	@Override
	@Transactional(readOnly = true) 
	public Page<Cuadro> findAll(Pageable page) {		
		
		return cuadroDao.findAll(page);
	}
	@Override
	@Transactional(readOnly = true) 
	public Page<Cuadro> buscarPorTienda(Long tienda_id, Pageable page) {		
		
		return cuadroDao.findByTiendaId(tienda_id, page);
	}

}
