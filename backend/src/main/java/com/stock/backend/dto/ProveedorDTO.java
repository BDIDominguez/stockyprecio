package com.stock.backend.dto;

import com.stock.backend.entity.Proveedor;
import jakarta.validation.constraints.*;

public record ProveedorDTO(

        Long id,

        @NotNull(message = "El Codigo es obligatorio")
        @Size(message = "El código no puede superar 50 caracteres")
        Long codigo,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 200, message = "El nombre no puede superar 200 caracteres")
        String nombre,

        @Size(max = 100, message = "El contacto no puede superar 100 caracteres")
        String contacto,

        @Size(max = 50, message = "El teléfono no puede superar 50 caracteres")
        String telefono,

        @Email(message = "El email debe ser válido")
        @Size(max = 100, message = "El email no puede superar 100 caracteres")
        String email,

        @Size(max = 500, message = "La dirección no puede superar 500 caracteres")
        String direccion,

        Boolean activo
) {

    // Constructor vacío (simulado)
    public ProveedorDTO() {
        this(null, null, null, null, null, null, null, true);
    }

    // Conversión de Entidad → DTO
    public static ProveedorDTO fromEntity(Proveedor proveedor) {
        if (proveedor == null) return null;
        return new ProveedorDTO(
                proveedor.getId(),
                proveedor.getCodigo(),
                proveedor.getNombre(),
                proveedor.getContacto(),
                proveedor.getTelefono(),
                proveedor.getEmail(),
                proveedor.getDireccion(),
                proveedor.getActivo()
        );
    }

    // Conversión de DTO → Entidad
    public Proveedor toEntity() {
        return Proveedor.builder()
                .id(this.id)
                .codigo(this.codigo)
                .nombre(this.nombre)
                .contacto(this.contacto)
                .telefono(this.telefono)
                .email(this.email)
                .direccion(this.direccion)
                .activo(this.activo != null ? this.activo : true)
                .build();
    }
}
