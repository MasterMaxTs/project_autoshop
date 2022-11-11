package ru.job4j.cars.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "auto_cars")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private int id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "brand_id")
    private CarBrand brand;

    @Column(name = "body_type")
    private String bodyType;

    @Column(name = "color")
    private String color;

    @Column(name = "model_year")
    private String modelYear;

    @Column(name = "mileage")
    private String mileage;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "engine_id")
    private Engine engine;

    @Column(name = "photo")
    private byte[] photo;

    @ManyToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "history_owners",
            joinColumns = {@JoinColumn(name = "car_id")},
            inverseJoinColumns = {@JoinColumn(name = "driver_id")}
    )
    private Set<Driver> owners = new HashSet<>();

    public Car(String color,
               String bodyType,
               String modelYear,
               String mileage,
               Engine engine,
               byte[] photo) {
        this.color = color;
        this.bodyType = bodyType;
        this.modelYear = modelYear;
        this.mileage = mileage;
        this.engine = engine;
        this.photo = photo;
    }

    public void addCarDriver(Driver driver) {
        owners.add(driver);
    }
}
