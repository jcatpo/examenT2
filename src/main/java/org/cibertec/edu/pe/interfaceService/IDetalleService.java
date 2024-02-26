package org.cibertec.edu.pe.interfaceService;

import java.util.List;
import java.util.Optional;

import org.cibertec.edu.pe.modelo.Detalle;


public interface IDetalleService {
	public List<Detalle> listado();
	public Optional<Detalle> buscar(int idDetalle);	
	public Detalle guardar(Detalle detalle);
	public void Suprimir(int idDetalle);
	public void actualizarDetalles(List<Detalle> carrito);	
}
