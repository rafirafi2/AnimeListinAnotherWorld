package com.example.animelistinanotherworld.user;

import com.example.animelistinanotherworld.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "userdata")
public class userData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //@Column(name = "profileImage")
    //private String profileImage;

    @Lob
    @Column(name = "profileImage", nullable = true)
    private Blob profileImage;


    @Column(name = "totalCount", columnDefinition = "INT(11) DEFAULT 0")
    private int totalCount;
    @Column(name = "totalWatching")
    private int totalWatching;
    @Column(name = "totalPlanning")
    private int totalPlanning;
    @Column(name = "totalWatched")
    private int totalWatched;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, updatable = false) // Specify the name of the column in the database that links to the User table's primary key
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
