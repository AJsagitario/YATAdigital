package com.gobilletera.wallet_core.model;

import java.math.BigDecimal;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("usuarios")
public class Usuario {

    @PrimaryKey
    private String dni;

    private String nombre;
    private BigDecimal saldo;
    private String numeroTarjeta;
    private String ccv;
    private String fechaVencimiento;

    public Usuario(String dni, String nombre) {
        this.dni = dni;
        this.nombre = nombre;
        this.saldo = BigDecimal.valueOf(500.00);
        this.numeroTarjeta = generarNumeroTarjeta();
        this.ccv = generarCcv();
        this.fechaVencimiento = generarFechaVencimiento();
    }

    private String generarNumeroTarjeta() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }

    private String generarCcv() {
        int ccv = (int) (Math.random() * 900) + 100;
        return String.valueOf(ccv);
    }

    private String generarFechaVencimiento() {
        java.time.LocalDate hoy = java.time.LocalDate.now();
        java.time.LocalDate vencimiento = hoy.plusYears(3);
        return String.format("%02d/%d", vencimiento.getMonthValue(), vencimiento.getYear());
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getCcv() {
        return ccv;
    }

    public void setCcv(String ccv) {
        this.ccv = ccv;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

}
