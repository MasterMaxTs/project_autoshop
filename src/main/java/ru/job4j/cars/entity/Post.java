package ru.job4j.cars.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "auto_posts")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private int id;

    @Column(name = "text")
    private String text;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "updated")
    private LocalDateTime updated;

    @Column(name = "saled")
    private LocalDateTime saled;

    @Column(name = "is_sold")
    private boolean isSold;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", fetch = FetchType.EAGER)
    private final List<PriceHistory> priceHistoryList = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "participants",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private final Set<User> participants = new HashSet<>();

    public void addPriceHistoryToList(PriceHistory priceHistory) {
        int size = priceHistoryList.size();
        if (size == 0) {
            priceHistoryList.add(priceHistory);
            return;
        }
        if (priceHistoryList.get(size - 1).getPrice() != priceHistory.getPrice()) {
            priceHistoryList.add(priceHistory);
        }
    }

    public void addParticipantToPost(User user) {
        participants.add(user);
    }

    public void removeParticipantFromPost(User user) {
        participants.remove(user);
    }
}
