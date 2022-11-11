package ru.job4j.cars.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "auto_drivers")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "ownership_start")
    @Temporal(TemporalType.DATE)
    private Date ownershipStartTime;

    @Column(name = "ownership_end")
    @Temporal(TemporalType.DATE)
    private Date ownershipEndTime;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "user_id")
    private User user;

    public Driver(String firstName, Date ownershipStartTime, Date ownershipEndTime) {
        this.firstName = firstName;
        this.ownershipStartTime = ownershipStartTime;
        this.ownershipEndTime = ownershipEndTime;
    }
}
