package com.sistemacompras.too.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class MovimientoEmpresa {
	//Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idMovimiento;
    private String nombreProducto;
    private String movimiento;
    private Date fechaMovimiento;
    private Integer cantidadEntrada;
    private Integer cantidadSalida;
    private double precioCompra;
    private double total;

	//Constructor sin parametros

	public MovimientoEmpresa() {
	}

	//Constructor con parametros

	public MovimientoEmpresa(Long idMovimiento, String nombreProducto, String movimiento, Date fechaMovimiento, Integer cantidadEntrada, Integer cantidadSalida, double precioCompra, double total) {
		this.idMovimiento = idMovimiento;
		this.nombreProducto = nombreProducto;
		this.movimiento = movimiento;
		this.fechaMovimiento = fechaMovimiento;
		this.cantidadEntrada = cantidadEntrada;
		this.cantidadSalida = cantidadSalida;
		this.precioCompra = precioCompra;
		this.total = total;
	}


	//Getter y Setter

	public Long getIdMovimiento() {
		return idMovimiento;
	}

	public void setIdMovimiento(Long idMovimiento) {
		this.idMovimiento = idMovimiento;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public String getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(String movimiento) {
		this.movimiento = movimiento;
	}

	public Date getFechaMovimiento() {
		return fechaMovimiento;
	}

	public void setFechaMovimiento(Date fechaMovimiento) {
		this.fechaMovimiento = fechaMovimiento;
	}

	public Integer getCantidadEntrada() {
		return cantidadEntrada;
	}

	public void setCantidadEntrada(Integer cantidadEntrada) {
		this.cantidadEntrada = cantidadEntrada;
	}

	public Integer getCantidadSalida() {
		return cantidadSalida;
	}

	public void setCantidadSalida(Integer cantidadSalida) {
		this.cantidadSalida = cantidadSalida;
	}

	public double getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(double precioCompra) {
		this.precioCompra = precioCompra;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}


	//ToString

	@Override
	public String toString() {
		return "MovimientoEmpresa{" +
				"idMovimiento=" + idMovimiento +
				", nombreProducto='" + nombreProducto + '\'' +
				", movimiento='" + movimiento + '\'' +
				", fechaMovimiento=" + fechaMovimiento +
				", cantidadEntrada=" + cantidadEntrada +
				", cantidadSalida=" + cantidadSalida +
				", precioCompra=" + precioCompra +
				", total=" + total +
				'}';
	}
}