package com.gobilletera.wallet_core.repository;

import com.gobilletera.wallet_core.model.Movimiento;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovimientoRepository extends CassandraRepository<Movimiento, UUID> {
    @Query("SELECT * FROM movimientos WHERE dni_origen = ?0 ALLOW FILTERING")
    List<Movimiento> findByDniOrigen(String dni);

    @Query("SELECT * FROM movimientos WHERE dni_destino = ?0 ALLOW FILTERING")
    List<Movimiento> findByDniDestino(String dni);
}
