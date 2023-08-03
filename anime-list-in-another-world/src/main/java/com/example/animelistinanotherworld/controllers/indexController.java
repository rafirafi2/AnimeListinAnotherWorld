package com.example.animelistinanotherworld.controllers;

import com.example.animelistinanotherworld.animeSpotlightDetails;
import com.example.animelistinanotherworld.searchForm;
import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.exception.JikanQueryException;
import net.sandrohc.jikan.model.anime.Anime;
import net.sandrohc.jikan.query.random.RandomAnimeQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Controller
public class indexController {

    //gets login form page
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView getIndexPage(@ModelAttribute searchForm searchForm, Model model, ModelAndView modelAndView) throws JikanQueryException {
        //html page name return
        model.addAttribute("searchForm", searchForm);

        Anime randomAnime1 = getRandomAnime();
        Anime randomAnime2 = getRandomAnime();
        Anime randomAnime3 = getRandomAnime();
        Anime randomAnime4 = getRandomAnime();


        List<animeSpotlightDetails> animeSpotlightDetailsList = new ArrayList<>();

        animeSpotlightDetails animeSpotlightDetails = new animeSpotlightDetails();
        animeSpotlightDetails.setAnime(randomAnime1);
        animeSpotlightDetails.setMalId(randomAnime1.malId);
        animeSpotlightDetailsList.add(animeSpotlightDetails);

        animeSpotlightDetails animeSpotlightDetails2 = new animeSpotlightDetails();
        animeSpotlightDetails2.setAnime(randomAnime2);
        animeSpotlightDetails2.setMalId(randomAnime2.malId);
        animeSpotlightDetailsList.add(animeSpotlightDetails2);

        animeSpotlightDetails animeSpotlightDetails3 = new animeSpotlightDetails();
        animeSpotlightDetails3.setAnime(randomAnime3);
        animeSpotlightDetails3.setMalId(randomAnime3.malId);
        animeSpotlightDetailsList.add(animeSpotlightDetails3);

        animeSpotlightDetails animeSpotlightDetails4 = new animeSpotlightDetails();
        animeSpotlightDetails4.setAnime(randomAnime4);
        animeSpotlightDetails4.setMalId(randomAnime4.malId);
        animeSpotlightDetailsList.add(animeSpotlightDetails4);


        return new ModelAndView("index","animeSpotlightDetailsList",animeSpotlightDetailsList);
    }

    private Anime getRandomAnime() throws JikanQueryException {

        Jikan jikan = new Jikan();
        RandomAnimeQuery randomAnimeQuery = new RandomAnimeQuery(jikan);

        Mono<Anime> randomAnimeMono = randomAnimeQuery.execute();

        return randomAnimeMono.block();

    }

    @ExceptionHandler(JikanQueryException.class)
    public ModelAndView handleJikanQueryException(JikanQueryException ex) {
        return new ModelAndView("apiError");
    }

}
