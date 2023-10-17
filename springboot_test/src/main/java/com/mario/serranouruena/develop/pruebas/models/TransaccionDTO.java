package com.mario.serranouruena.develop.pruebas.models;

import java.math.BigDecimal;

public class TransaccionDTO {
    private Long cuentaOrigenId;
    private Long cuentaDetinoId;
    private BigDecimal monto;
    private Long bancoId;

    public TransaccionDTO() {
    }

    public Long getCuentaOrigenId() {
        return cuentaOrigenId;
    }

    public void setCuentaOrigenId(Long cuentaOrigenId) {
        this.cuentaOrigenId = cuentaOrigenId;
    }

    public Long getCuentaDetinoId() {
        return cuentaDetinoId;
    }

    public void setCuentaDetinoId(Long cuentaDetinoId) {
        this.cuentaDetinoId = cuentaDetinoId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Long getBancoId() {
        return bancoId;
    }

    public void setBancoId(Long bancoId) {
        this.bancoId = bancoId;
    }
}
