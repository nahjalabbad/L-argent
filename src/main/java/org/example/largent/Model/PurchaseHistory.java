package org.example.largent.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer purchHistoryId;

    @Column(columnDefinition = "varchar(50)")
    private String category;

    @Positive
    @Column(columnDefinition = "double")
    private Double totalAmount;

    @Column(columnDefinition = "DATE")
    private LocalDate duration;

    @Column(columnDefinition = "DATE")
    private LocalDate purchaseDate;

    @Column(columnDefinition = "int")
    private Integer userId;

    @Column(columnDefinition = "int")
    private Integer purchaseId;
}
