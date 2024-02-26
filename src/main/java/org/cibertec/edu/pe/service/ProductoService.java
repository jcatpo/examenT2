package org.cibertec.edu.pe.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.cibertec.edu.pe.interfaceService.IProductoService;
import org.cibertec.edu.pe.interfaces.IProductoRepository;
import org.cibertec.edu.pe.modelo.Detalle;
import org.cibertec.edu.pe.modelo.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoService implements IProductoService{
	@Autowired
	private IProductoRepository data;
	
	@Override
	public Optional<Producto> buscar(int idProducto){		
		return data.findById(idProducto);
	}
	
	@Transactional
	@Override
	public void Suprimir(int idProducto) {
		data.deleteById(idProducto);		
	}
}
