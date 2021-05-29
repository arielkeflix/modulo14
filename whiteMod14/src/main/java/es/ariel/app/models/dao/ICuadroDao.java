package es.ariel.app.models.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import es.ariel.app.models.entity.Cuadro;

public interface ICuadroDao extends PagingAndSortingRepository<Cuadro, Long>{

	public List<Cuadro> findByTiendaId(Long tienda_id);
	public Page<Cuadro> findByTiendaId(Long tienda_id, Pageable page);

}
