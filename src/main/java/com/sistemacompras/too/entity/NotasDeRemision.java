package com.sistemacompras.too.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class NotasDeRemision {
	//Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idNotaRemision;
    private Integer estado;

	@ManyToOne
	@JoinColumn(name = "idOrdenDeCompra")
	private OrdenDeCompra idOrdenDeCompra;

	@ManyToOne
	@JoinColumn(name = "idEmpleado")
	private Empleado idEmpleado;

	//Constructor sin parametros

	public NotasDeRemision() {
	}

	//Constructor con parametros

	public NotasDeRemision(Long idNotaRemision, Integer estado, OrdenDeCompra idOrdenDeCompra, Empleado idEmpleado) {
		this.idNotaRemision = idNotaRemision;
		this.estado = estado;
		this.idOrdenDeCompra = idOrdenDeCompra;
		this.idEmpleado = idEmpleado;
	}


	//Getter y Setter

	public Long getIdNotaRemision() {
		return idNotaRemision;
	}

	public void setIdNotaRemision(Long idNotaRemision) {
		this.idNotaRemision = idNotaRemision;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public OrdenDeCompra getIdOrdenDeCompra() {
		return idOrdenDeCompra;
	}

	public void setIdOrdenDeCompra(OrdenDeCompra idOrdenDeCompra) {
		this.idOrdenDeCompra = idOrdenDeCompra;
	}

	public Empleado getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Empleado idEmpleado) {
		this.idEmpleado = idEmpleado;
	}


	//ToString


	@Override
	public String toString() {
		return "NotasDeRemision{" +
				"idNotaRemision=" + idNotaRemision +
				", estado=" + estado +
				", idOrdenDeCompra=" + idOrdenDeCompra +
				", idEmpleado=" + idEmpleado +
				'}';
	}
}