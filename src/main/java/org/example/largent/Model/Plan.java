package org.example.largent.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer planId;

    @NotNull(message = "Amount Target cannot be null")
    @Positive
    @Column(columnDefinition = "double not null")
    private Double amountTarget;

    @Positive
    @Column(columnDefinition = "double")
    private Double amountToAdd;

    @NotEmpty(message = "Title cannot be empty")
    @Column(columnDefinition = "varchar(20) not null")
    private String title;

    @NotEmpty(message = "description cannot be empty")
    @Column(columnDefinition = "varchar(255) not null")
    private String description;

    @Pattern(regexp = "^(done|in progress|not started)$")
    @Column(columnDefinition = "varchar(25) check(status='done' or status='not started' or status='in progress')")
    private String status;

    @NotNull(message = "starting date cannot be empty")
    @Column(columnDefinition = "DATE not null")
    private LocalDate startingDate;

    @NotNull(message = "ending date cannot be empty")
    @Column(columnDefinition = "DATE not null")
    private LocalDate endingDate;

    @NotNull(message = "user id cannot be empty")
    @Column(columnDefinition = "int not null ")
    private Integer userId;

}
