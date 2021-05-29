package es.ariel.app.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "tiendas")

public class Tienda implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	private String nombre;

	@NotNull
	@Column(name = "cant_max_cuadros")
	private Integer cantMaxCuadros;
	
	@OneToMany(mappedBy = "tienda" ,fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)   
	private List<Cuadro> cuadros;		
	

	public Tienda() {

	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public boolean estaVacia() {
		if  (cuadros.size()==0) {
			return true;
		}
		return false;
	}
	public Integer getEspacio() {
		return cantMaxCuadros- this.cuadros.size();
	}

	public Integer getCantMaxCuadros() {
		return cantMaxCuadros;
	}

	public void setCantMaxCuadros(Integer cantMaxCuadros) {
		this.cantMaxCuadros = cantMaxCuadros;
	}

	public Long getId() {
		return id;
	}

	private static final long serialVersionUID = 1L;

}
