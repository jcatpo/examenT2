package org.cibertec.edu.pe.interfaceService;

import java.util.List;
import java.util.Optional;


import org.cibertec.edu.pe.modelo.Producto;


public interface IProductoService {
	
	public Optional<Producto> buscar(int idProducto);	
	
	public void Suprimir(int idProducto);	
}
