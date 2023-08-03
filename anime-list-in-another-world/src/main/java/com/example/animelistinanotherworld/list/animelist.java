package com.example.animelistinanotherworld.list;

import com.example.animelistinanotherworld.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "animelist")
public class animelist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "anime_id", nullable = false)
    private int animeId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id") // Specify the name of the column in the database that links to the User table's primary key
    private User user;

    @Column(name = "rating", nullable = false)
    private int rating;


}
