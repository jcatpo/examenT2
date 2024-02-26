package org.cibertec.edu.pe.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.cibertec.edu.pe.interfaceService.IDetalleService;
import org.cibertec.edu.pe.interfaces.IDetalleRepository;
import org.cibertec.edu.pe.modelo.Detalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetalleService implements IDetalleService{
	@Autowired
	private IDetalleRepository detalleRepository;
	
	@Override
	public List<Detalle> listado() {
		return detalleRepository.findAll();
	}

	@Override
	public Optional<Detalle> buscar(int idDetalle) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Detalle guardar(Detalle detalle) {
		return detalleRepository.save(detalle);
	}

	@Override
	public void Suprimir(int idDetalle) {	
		detalleRepository.deleteById(idDetalle);
	}
	
	/*@Transactional*/
	/*public void actualizarDetalles(List<Detalle> carrito) {        	
        for (Detalle detalle : carrito) {                
				// Recalcula el subtotal (puedes ajustar esto según tu lógica)
                // Supongamos que precio es un campo en Detalle
                double subtotal = detalle.getProducto().getPrecio() * detalle.getCantidad();
                detalle.setSubtotal(subtotal);

            // Actualizar cada detalle en la base de datos
            detalleRepository.save(detalle);
        }
    }*/
	
	@Transactional
	@Override
	public void actualizarDetalles(List<Detalle> carrito) {
	    for (Detalle detalle : carrito) {
	        // Recalcula el subtotal
	        double subtotal = detalle.getProducto().getPrecio() * detalle.getCantidad();
	        detalle.setSubtotal(subtotal);

	        // Calcula el descuento (puedes ajustar esto según tu lógica)
	        double descuento = calcularDescuento(detalle);

	        // Calcula el total (puedes ajustar esto según tu lógica)
	        double total = calcularTotal(detalle, descuento);   
	       

	        // Actualizar cada detalle en la base de datos
	        detalleRepository.save(detalle);
	    }
	}

	// Método para calcular el descuento (ejemplo)
	private double calcularDescuento(Detalle detalle) {
	    // Lógica para calcular el descuento según tus reglas de negocio
	    return detalle.getSubtotal() * 0.1; // Ajusta esto según tus necesidades
	}

	// Método para calcular el total (ejemplo)
	private double calcularTotal(Detalle detalle, double descuento) {
	    // Lógica para calcular el total según tus reglas de negocio
	    return detalle.getSubtotal() - descuento; // Ajusta esto según tus necesidades
	}

}
