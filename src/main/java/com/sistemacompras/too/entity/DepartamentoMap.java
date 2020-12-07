package com.sistemacompras.too.entity;

public class DepartamentoMap {
    Long idDeDepartamento;
    String nombreDepartamento;
    int numeroDeVentas;
    double ventas;

    public DepartamentoMap() {
    }

    public DepartamentoMap(Long idDeDepartamento, String nombreDepartamento, int numeroDeVentas, double ventas) {
        this.idDeDepartamento = idDeDepartamento;
        this.nombreDepartamento = nombreDepartamento;
        this.numeroDeVentas = numeroDeVentas;
        this.ventas = ventas;
    }

    public double getVentas() {
        return ventas;
    }

    public void setVentas(double ventas) {
        this.ventas = ventas;
    }

    public Long getIdDeDepartamento() {
        return idDeDepartamento;
    }

    public void setIdDeDepartamento(Long idDeDepartamento) {
        this.idDeDepartamento = idDeDepartamento;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public int getNumeroDeVentas() {
        return numeroDeVentas;
    }

    public void setNumeroDeVentas(int numeroDeVentas) {
        this.numeroDeVentas = numeroDeVentas;
    }

    @Override
    public String toString() {
        return "DepartamentoMap{" +
                "idDeDepartamento=" + idDeDepartamento +
                ", nombreDepartamento='" + nombreDepartamento + '\'' +
                ", numeroDeVentas=" + numeroDeVentas +
                ", ventas=" + ventas +
                '}';
    }
}
