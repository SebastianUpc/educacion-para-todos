package pe.edu.upc.educacionparatodos.dtos;

public class ColegiosPorDistritoDTO {
    private String distrito;
    private Long cantidad;

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }
}
