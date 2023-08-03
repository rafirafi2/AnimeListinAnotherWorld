package com.example.animelistinanotherworld.controllers;

import com.example.animelistinanotherworld.searchForm;
import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.exception.JikanQueryException;
import net.sandrohc.jikan.model.anime.Anime;
import net.sandrohc.jikan.model.anime.AnimeOrderBy;
import net.sandrohc.jikan.model.anime.AnimeStatus;
import net.sandrohc.jikan.model.enums.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.List;

@Controller
public class searchController {

    @GetMapping("/search")
    public String getSearchPage(Model model) {
        model.addAttribute("searchForm", new searchForm());
        return "search";
    }

    @RequestMapping(value = "/search**", method = RequestMethod.POST)
    public String getSearchPage(@ModelAttribute searchForm searchForm,@RequestParam(value = "page", defaultValue = "1") int  page,
                                @RequestParam(name = "size", defaultValue = "25") int size,
                                Model model) throws JikanQueryException {

        model.addAttribute("searchForm", new searchForm());
        //model.addAttribute("query", animeName);

        //html page name return

        String searchQuery = searchForm.getQuery();

        Jikan jikan = new Jikan();
        Flux<Anime> results = jikan.query().anime().search()
                .query(searchQuery)
                .limit(size)
                .page(page)
                .orderBy(AnimeOrderBy.MEMBERS, SortOrder.DESCENDING)
                .execute();
        System.out.println(results.log());
        List<Anime> animeList = results.collectList().block();
        Page<Anime> animePage = new PageImpl<>(animeList);
        model.addAttribute("animePage",animeList);

        // Additional pagination information
        int totalPages = jikan.query().anime().search()
                .query(searchQuery)
                .execute()
                .collectList()
                .block()
                .size() / size;
        model.addAttribute("currentPage", page - 1);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", getTotalPages(results, size));

        return "search";
    }

    // Helper method to calculate the total number of pages
    private int getTotalPages(Flux<Anime> results, int pageSize) {
        System.out.println(results.collectList().block().size() / pageSize);
        return results.collectList().block().size() / pageSize;
    }

    @ExceptionHandler(JikanQueryException.class)
    public ModelAndView handleJikanQueryException(JikanQueryException ex) {
        return new ModelAndView("apiError");
    }



}
