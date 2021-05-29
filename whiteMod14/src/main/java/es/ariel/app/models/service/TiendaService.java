package es.ariel.app.models.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ariel.app.models.dao.ITiendaDao;
import es.ariel.app.models.entity.Tienda;

@Service

public class TiendaService implements ITiendaService {

	@Autowired
	private ITiendaDao tiendaDao;
	
	
	@Override
	@Transactional
	public void save(Tienda tienda) {
		tiendaDao.save( tienda);

	}

	@Override
	@Transactional(readOnly = true) 
	public Tienda findOne(Long id) {	
		return tiendaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		tiendaDao.deleteById(id);

	}

	@Override
	@Transactional(readOnly = true) 
	public List<Tienda> findAll() {		
		return (List<Tienda>) tiendaDao.findAll();
	}

	
	
	@Override
	@Transactional(readOnly = true) 
	public Page<Tienda> findAll(Pageable page) {
		System.out.println("Total paginas tiendas: " +page.getPageNumber());
		return tiendaDao.findAll(page);
	}

}
