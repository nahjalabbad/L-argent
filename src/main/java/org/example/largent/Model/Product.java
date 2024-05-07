package org.example.largent.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @NotEmpty(message = "Store name cannot be empty")
    @Column(columnDefinition = "varchar(40) not null unique")
    private String storeName;

    @NotNull(message = "price cannot be null")
    @Column(columnDefinition = "double not null")
    private Double price;

    @Column(columnDefinition = "double")
    private Double total;

    @NotEmpty(message = "category cannot be null")
    @Column(columnDefinition = "varchar(50) not null ")
    private String category;

    @Column(columnDefinition = "int")
    private Integer userId;

    @Column(columnDefinition = "int")
    private Integer purchaseId;
}
