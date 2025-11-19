package com.gobilletera.wallet_core.model;

public class TransferenciaRequest {
    private String origen;
    private String destino;
    private double monto;
    private String mensaje;

    private String pin; // pin del emisor (para validar)
    private String toAppName;

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getToAppName() {
        return toAppName;
    }

    public void setToAppName(String toAppName) {
        this.toAppName = toAppName;
    }

    // solo para externas, ej: "PlataYa"

}
