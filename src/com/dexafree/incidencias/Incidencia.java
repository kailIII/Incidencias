package com.dexafree.incidencias;

/**
 * Created by Carlos on 18/05/13.
 */
public class Incidencia { 

    private String tipo;
    private String autonomia;
    private String matricula;
    private String provincia;
    private String causa;
    private String poblacion;
    private String fechahora;
    private String hora;
    private String nivel;
    private String carretera;
    private String pkInicio;
    private String pkFin;
    private String sentido;
    private String hacia;
    private String refIncidencia;
    private double x;
    private double y;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAutonomia() {
        return autonomia;
    }

    public void setAutonomia(String autonomia) {
        this.autonomia = autonomia;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public String getCausa() {
        if (causa == null){
            return "No especificada";
        }
        return causa;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getPoblacion() {
        return poblacion;
    }
    public void setFechahora(String fechahora) {
        this.fechahora = fechahora;
    }

    public String getFechahora() {
        return fechahora;
    }



    public String getHora() {
        String fh = this.fechahora;
        String fhs = fh.trim();
        String hora = fhs.substring(10,12);
        return hora;

    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getCarretera() {
        return carretera;
    }
    public void setCarretera(String carretera) {
        this.carretera = carretera;
    }

    public String getPkInicio() {
        return pkInicio;
    }
    public void setPkInicio(String pkInicio) {
        this.pkInicio = pkInicio;
    }

    public String getPkFin() {
        return pkFin;
    }
    public void setPkFin(String pkFin) {
        this.pkFin = pkFin;
    }

    public String getSentido() {
        return sentido;
    }
    public void setSentido(String sentido) {
        this.sentido = sentido;
    }

    public String getRefIncidencia() {
        return refIncidencia;
    }
    public void setRefIncidencia(String refIncidencia) {
        this.refIncidencia = refIncidencia;
    }

    public String getHacia() {
        return hacia;
    }
    public void setHacia(String hacia) {
        this.hacia = hacia;
    }

    public void setX(double x){
        this.x = x;
    }

    public double getX(){
        return x;
    }

    public void setY(double y){
        this.y = y;
    }

    public double getY(){
        return y;
    }

}
