package pe.edu.upc.educacionparatodos.exceptions;

import org.springframework.http.HttpStatus;

// Falla al comunicarse con la API de inteligencia artificial (HU-105, CA08).
// Lleva su propio status: 503 si la IA no esta configurada, 502 si respondio mal.
public class IaServiceException extends RuntimeException {

    private final HttpStatus status;

    public IaServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public IaServiceException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
