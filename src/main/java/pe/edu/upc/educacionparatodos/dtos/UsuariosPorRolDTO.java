package pe.edu.upc.educacionparatodos.dtos;

public class UsuariosPorRolDTO {
    private String nombreRol;
    private Long cantidad;

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }
}
