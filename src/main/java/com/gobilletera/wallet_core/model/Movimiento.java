package com.gobilletera.wallet_core.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Table("movimientos")
public class Movimiento {

    @PrimaryKey
    private UUID id;

    @Column("dni_origen")
    private String dniOrigen;
    @Column("dni_destino")
    private String dniDestino;
    private BigDecimal monto;
    private String mensaje;
    @Column("codigo_transferencia")
    private String codigoTransferencia;
    @Column("fecha_hora")
    private LocalDateTime fechaHora;

    private String contacto;

    public Movimiento() {
        this.id = UUID.randomUUID();
    }

    public Movimiento(String dniOrigen, String dniDestino, BigDecimal monto, String mensaje,
            String codigoTransferencia) {
        this.id = UUID.randomUUID();
        this.dniOrigen = dniOrigen;
        this.dniDestino = dniDestino;
        this.monto = monto;
        this.mensaje = mensaje;
        this.codigoTransferencia = codigoTransferencia;
        this.fechaHora = LocalDateTime.now();
        this.contacto = contacto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDniOrigen() {
        return dniOrigen;
    }

    public void setDniOrigen(String dniOrigen) {
        this.dniOrigen = dniOrigen;
    }

    public String getDniDestino() {
        return dniDestino;
    }

    public void setDniDestino(String dniDestino) {
        this.dniDestino = dniDestino;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCodigoTransferencia() {
        return codigoTransferencia;
    }

    public void setCodigoTransferencia(String codigoTransferencia) {
        this.codigoTransferencia = codigoTransferencia;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

}
