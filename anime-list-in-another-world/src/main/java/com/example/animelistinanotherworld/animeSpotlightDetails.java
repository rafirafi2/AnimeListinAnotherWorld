package com.example.animelistinanotherworld;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sandrohc.jikan.model.anime.Anime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class animeSpotlightDetails {

    private Anime anime;
    private int malId;

}
