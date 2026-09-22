package pe.edu.upc.educacionparatodos.dtos;

public class EvaluacionesPorEstudianteDTO {

    private String nombre;
    private Long cantidad;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }
}
