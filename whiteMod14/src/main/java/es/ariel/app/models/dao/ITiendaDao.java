package es.ariel.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import es.ariel.app.models.entity.Tienda;

public interface ITiendaDao extends PagingAndSortingRepository<Tienda, Long>{

}
