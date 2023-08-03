package com.example.animelistinanotherworld.list;

import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.exception.JikanQueryException;
import net.sandrohc.jikan.model.anime.Anime;
import net.sandrohc.jikan.model.anime.AnimeOrderBy;
import net.sandrohc.jikan.model.enums.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AnimeListService {

    private final AnimeListRepository animeListRepository;


    public AnimeListService(AnimeListRepository animeListRepository) {
        this.animeListRepository = animeListRepository;
    }


    //public Page<animelist> getAnimeListByUserId(Integer userId, Pageable pageable) {
    //    return animeListRepository.findByUser_Id(userId,pageable);
    //}


}
