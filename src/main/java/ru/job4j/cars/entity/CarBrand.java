package ru.job4j.cars.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "auto_car_brands")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CarBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private int id;

    @Column(name = "brand")
    private String name;
}
