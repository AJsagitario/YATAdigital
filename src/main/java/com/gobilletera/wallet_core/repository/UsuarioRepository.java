package com.gobilletera.wallet_core.repository;

import java.util.Optional;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.gobilletera.wallet_core.model.Usuario;

@Repository
public interface UsuarioRepository extends CassandraRepository<Usuario, String> {

    @AllowFiltering
    Usuario findByContacto(String contacto);

    @AllowFiltering
    Usuario findByContactoAndPin(String contacto, String pin);

}
