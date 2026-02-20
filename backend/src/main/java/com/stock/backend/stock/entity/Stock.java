package com.stock.backend.stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stocks")
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
        private Long sucursal;
        private Double cantidad;
        private Double reserva;


        public void  sumar(Double cantidad){
                this.cantidad = this.cantidad + cantidad;
        }

        public void  restar(Double cantidad){
                this.cantidad = this.cantidad + cantidad;
        }
        public void actualizar(Stock datos){
                if (datos.getCantidad() != null){
                        this.cantidad = datos.getCantidad();
                }
                if (datos.getReserva() != null){
                        this.reserva = datos.getReserva();
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
