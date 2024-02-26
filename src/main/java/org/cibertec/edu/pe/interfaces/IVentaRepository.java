package org.cibertec.edu.pe.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.cibertec.edu.pe.modelo.Venta;

@Repository
public interface IVentaRepository extends JpaRepository<Venta, Integer> {

}
