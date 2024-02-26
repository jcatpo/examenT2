package org.cibertec.edu.pe.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.cibertec.edu.pe.modelo.Producto;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Integer> {
	
}
