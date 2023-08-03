package com.example.animelistinanotherworld.list;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sandrohc.jikan.model.anime.Anime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimeListDetails {

    private Anime anime;
    private String watchStatus;
    private int animeId;

    private int malId;


}
