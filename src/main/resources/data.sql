INSERT INTO roles (nombre_rol, descripcion_rol, fecha_creacion_rol, actualizado_en_rol)
SELECT 'ADMIN', 'Administrador del sistema', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nombre_rol = 'ADMIN');

INSERT INTO usuarios (nombre, apellido, email, password_hash, rol_id, estado, fecha_registro, updated_at)
SELECT 'Admin', 'Sistema', 'admin@educacion.edu.pe', '$2b$10$h9cLq0DvqbbsCow0j4fQOOciFmMBOHTDYOR0xXv9RbNUWHN3Mbn92', r.id, 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM roles r
WHERE r.nombre_rol = 'ADMIN'
  AND NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'admin@educacion.edu.pe');
