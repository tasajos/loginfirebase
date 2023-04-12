package com.chakuy.mtp11.models;

public class mtpcontroller {

    String apellido,cantidad,color,detalle,nombre,pedido,precio,tipo,ubicacion,fecha;
    public mtpcontroller (){ }


    public mtpcontroller(String apellido, String cantidad, String color, String detalle, String nombre, String pedido, String precio, String tipo, String ubicacion,String fecha) {
        this.apellido = apellido;
        this.cantidad = cantidad;
        this.color = color;
        this.detalle = detalle;
        this.nombre = nombre;
        this.pedido = pedido;
        this.precio = precio;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
    }


    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    public String getfecha() {
        return fecha;
    }

    public void setfecha(String fecha) {
        this.fecha = fecha;
    }


}
