package com.stock.backend.stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "stocks",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_stocks_codigo_sucursal", columnNames = {"codigo", "sucursal"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private Long codigo;

        @Column(nullable = false)
        private Long sucursal;

        @Builder.Default
        @Column(nullable = false)
        private Double cantidad = 0.00;

        @Builder.Default
        @Column(nullable = false)
        private Double reserva = 0.00;


        public void  sumar(Double cantidad){
                this.cantidad = this.cantidad + cantidad;
        }

        public void  restar(Double cantidad){
                this.cantidad = this.cantidad - cantidad;
        }

        public void actualizarReserva(Double reserva){
                if (reserva == null) {
                        return;
                }
                if (reserva < 0) {
                        throw new IllegalArgumentException("La reserva no puede ser negativa");
                }
                this.reserva = reserva;
        }

        public void actualizar(Stock datos){
                if (datos.getCantidad() != null){
                        this.cantidad = datos.getCantidad();
                }
                if (datos.getReserva() != null){
                        actualizarReserva(datos.getReserva());
                }
        }

        @Override
        public String toString() {
                return "Stock{" +
                        "codigo = " + codigo +
                        ", Sucursal = '" + sucursal + '\'' +
                        ", Cantidad = '" + cantidad + '\'' +
                        ", Reserva =" + reserva +
                        '}';
        }

}
