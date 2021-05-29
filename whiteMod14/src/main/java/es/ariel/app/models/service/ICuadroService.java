package es.ariel.app.models.service;

import java.util.List;

import es.ariel.app.models.entity.Cuadro;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface ICuadroService {

	public Cuadro findOne(Long id);

	public Page<Cuadro> findAll(Pageable page);

	public void deleteAll(Long tiendaId);

	public void delete(Long id);

	public List<Cuadro> buscarPorTienda(Long tiendaId);

	public Page<Cuadro> buscarPorTienda(Long tienda_id, Pageable page);

	void save(Cuadro cuadro);
}
