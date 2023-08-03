package com.example.animelistinanotherworld.list;

import com.example.animelistinanotherworld.user.User;
import com.example.animelistinanotherworld.user.userData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnimeListRepository extends JpaRepository<animelist, Integer> {

    //animelist findByUser_Id(Integer userId);


    @Modifying
    @Query("DELETE FROM animelist a WHERE a.animeId = :animeId AND a.user.id = :userId")
    void deleteByAnimeIdAndUser(@Param("animeId") String animeId, @Param("userId") Integer userId);


    List<animelist> findByUser_Id(Integer userId);

    Page<animelist> findByUser_Id(Integer userId, Pageable pageable);

}
