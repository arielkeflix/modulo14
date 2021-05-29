package es.ariel.app.models.service;

import java.util.List;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import es.ariel.app.models.entity.Tienda;

public interface ITiendaService {

public void save(Tienda tienda);
	
	public Tienda findOne(Long id);
	
	public Page<Tienda> findAll(Pageable page);
	
	public void delete(Long id);		

	public List<Tienda> findAll();
}
