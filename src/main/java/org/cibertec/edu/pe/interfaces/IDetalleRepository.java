package org.cibertec.edu.pe.interfaces;

import org.cibertec.edu.pe.modelo.Detalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDetalleRepository extends JpaRepository<Detalle, Integer> {

}
