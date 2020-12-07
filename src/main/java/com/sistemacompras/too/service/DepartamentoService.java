package com.sistemacompras.too.service;

import com.sistemacompras.too.entity.DepartamentoMap;
import com.sistemacompras.too.entity.RequisicionDeArticulo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.sistemacompras.too.entity.Departamento;
import com.sistemacompras.too.repository.DepartamentoRepository;

@Service
public class DepartamentoService {
    //Inyeccion de dependencias
    @Autowired
    private DepartamentoRepository repository;
    @Autowired
    private DepartamentoService departamentoService;

    //Muestra todos los departamentos
    public List<Departamento> listAll() {
        return repository.findAll();
    }
    //Guarda un departamento
    public void save(Departamento departamento){
        repository.save(departamento);
    }
    //Obtiene un departamento por su id
    public Departamento get(Long id){
        return repository.findById(id).get();
    }
    //Elimina un departamento por su id
    public void delete(Long id){
        repository.deleteById(id);
    }

    //Devuelve una lista de objetos de tipo DepartamentoMap con el id y el nombre del departamento
    public List<DepartamentoMap> listDepartamentMap() {

        List<Departamento> listDepartamentosAll = departamentoService.listAll();
        List<DepartamentoMap> listDepartamentoMap = new ArrayList();


        for (Departamento departamento : listDepartamentosAll) {

            DepartamentoMap departamentoMap = new DepartamentoMap();
                departamentoMap.setIdDeDepartamento(departamento.getIdDepartamento());
                departamentoMap.setNombreDepartamento(departamento.getNombreDepartamento());
            listDepartamentoMap.add(departamentoMap);
        }
        return listDepartamentoMap;
    }

}
