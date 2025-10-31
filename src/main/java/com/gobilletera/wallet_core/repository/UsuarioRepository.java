package com.gobilletera.wallet_core.repository;

import com.gobilletera.wallet_core.model.Usuario;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CassandraRepository<Usuario, String> {

}
