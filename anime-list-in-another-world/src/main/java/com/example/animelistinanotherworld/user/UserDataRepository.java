package com.example.animelistinanotherworld.user;

import com.example.animelistinanotherworld.user.userData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.awt.*;
import java.sql.Blob;


public interface UserDataRepository extends JpaRepository<userData, Integer> {
    userData findByUser_Id(Integer userId);

    Integer findTotalAnimeWatchedByUserId(Integer userId);

    @Modifying
    @Query("UPDATE userData u SET u.profileImage = :profileImage WHERE u.user.id = :userId")
    void setProfileImageByUserId(Blob profileImage, Integer userId);

    @Modifying
    @Query("UPDATE userData u SET u.totalCount = :totalCount WHERE u.user.id = :userId")
    void setAnimeWatchedByUserId(int totalCount, Integer userId);

    @Modifying
    @Query("UPDATE userData u SET u.totalWatching = :totalWatching WHERE u.user.id = :userId")
    void setWatching(int totalWatching, Integer userId);

    @Modifying
    @Query("UPDATE userData u SET u.totalPlanning = :totalPlanning WHERE u.user.id = :userId")
    void setPlanning(int totalPlanning, Integer userId);

    @Modifying
    @Query("UPDATE userData u SET u.totalWatched = :totalWatched WHERE u.user.id = :userId")
    void setWatched(int totalWatched, Integer userId);

}
