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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @NotEmpty(message = "name cannot be empty")
    @Column(columnDefinition = "varchar(20) not null")
    private String name;

    @NotEmpty(message = "username cannot be empty")
    @Column(columnDefinition = "varchar(20) not null unique")
    private String username;

    @NotEmpty(message = "password cannot be empty")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,20}$")
    @Column(columnDefinition = "varchar(20) not null")
    private String password;

    @Positive
    @Column(columnDefinition = "double")
    private Double balance;

    @Pattern(regexp = "^(child|parent|teenager)")
    @Column(columnDefinition = "varchar(8) check(role='child' or role='parent' or role='teenager')")
    private String role;

    @Column(columnDefinition = "double")
    private Double budget;

    @Column(columnDefinition = "DATE not null")
    private LocalDate birthDate;

    @Column(columnDefinition = "double")
    private Double weeklyLimit;

    @Column(columnDefinition = "double")
    private Double currentSpending;

    @Column(columnDefinition = "int")
    private Integer points;

    @Column(columnDefinition = "boolean")
    private Boolean isLogin;

    @Column(columnDefinition = "boolean")
    private Boolean hasTeen;

    @Column(columnDefinition = "boolean")
    private Boolean hasParent;

    @Column(columnDefinition = "int")
    private Integer purchaseId;

    @Column(columnDefinition = "int")
    private Integer rewardId;

    @Column(columnDefinition = "int")
    private Integer planId;

    @Column(columnDefinition = "int")
    private Integer parentId;

    @Column(columnDefinition = "int")
    private Integer purchHistoryId;

}
