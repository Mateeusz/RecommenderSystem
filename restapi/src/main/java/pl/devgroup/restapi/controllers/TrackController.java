package pl.devgroup.restapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.devgroup.restapi.ColaborativeFiltering.Matrix;
import pl.devgroup.restapi.model.SimilarUser;
import pl.devgroup.restapi.model.TrackDetails;
import pl.devgroup.restapi.service.TrackService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
public class TrackController {

    private TrackService trackService;

    @Autowired
    private Matrix matrix;

    @Autowired
    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @RequestMapping(value = "/allTracks", method = RequestMethod.GET)
    public ModelAndView getTracks() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("trackList", trackService.getTracks());
        modelAndView.setViewName("tracks");
        return modelAndView;
    }

    @RequestMapping(value = "/getTrack/{trackId}", method = RequestMethod.GET)
    public ModelAndView getTrack(@PathVariable("trackId") String trackId, Authentication authentication) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("trackDetails", trackService.getTrackWithRating(trackId, authentication.getName()));
        modelAndView.setViewName("track");
        return modelAndView;
    }

    @RequestMapping(value = "/getTrack/{trackId}", method = RequestMethod.POST)
    public ModelAndView setPoints(@Valid @ModelAttribute TrackDetails trackDetails, Authentication authentication) throws IOException {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("trackDetails", trackDetails);
        modelAndView.addObject("message", "Thank's for your vote!");

        TrackDetails trackDetails1 = trackService.getTrackWithRating(trackDetails.getTrackId(), authentication.getName());
        trackDetails.setArtist(trackDetails1.getArtist());
        trackDetails.setTimestamp(trackDetails1.getTimestamp());
        trackDetails.setSimilars(trackDetails1.getSimilars());
        trackDetails.setTags(trackDetails1.getTags());
        trackDetails.setTitle(trackDetails1.getTitle());

        System.out.println("---------->> " + trackDetails.toString()+ "PPP->> " + authentication.getName());
        trackService.saveRating(trackDetails, authentication.getName());
        modelAndView.setViewName("track");

        return modelAndView;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView test(Authentication authentication) throws IOException {
        HashMap<CharSequence, Integer> user1Map = (HashMap<CharSequence, Integer>) matrix.getMatrix(authentication.getName());

        List<SimilarUser> list = matrix.getSimilarUsers(user1Map);

        for (SimilarUser similarUser : list ) {
            System.out.println(similarUser.toString());
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("test");

        return modelAndView;
    }




}
