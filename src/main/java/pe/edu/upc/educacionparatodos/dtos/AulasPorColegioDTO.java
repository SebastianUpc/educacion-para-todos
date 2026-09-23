package pe.edu.upc.educacionparatodos.dtos;

public class AulasPorColegioDTO {
    private String nombreColegio;
    private Long cantidad;

    public String getNombreColegio() {
        return nombreColegio;
    }

    public void setNombreColegio(String nombreColegio) {
        this.nombreColegio = nombreColegio;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }
}
