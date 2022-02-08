package com.codecademy.goldmedal.controller;

import com.codecademy.goldmedal.model.*;
import com.codecademy.goldmedal.repositories.*;
import org.apache.commons.text.WordUtils;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/countries")
public class GoldMedalController {
    // TODO: declare references to your repositories
    private final GoldMedalRepository goldMedalRepository;
    private final CountryRepository countryRepository;

    // TODO: update your constructor to include your repositories
    public GoldMedalController(final GoldMedalRepository goldMedalRepository, final CountryRepository countryRepository) {
        this.goldMedalRepository = goldMedalRepository;
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public CountriesResponse getCountries(@RequestParam String sort_by, @RequestParam String ascending) {
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
    }

    @GetMapping("/{country}")
    public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
        String countryName = WordUtils.capitalizeFully(country);
        return getCountryDetailsResponse(countryName);
    }

    @GetMapping("/{country}/medals")
    public CountryMedalsListResponse getCountryMedalsList(@PathVariable String country, @RequestParam String sort_by, @RequestParam String ascending) {
        String countryName = WordUtils.capitalizeFully(country);
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
    }

    private CountryMedalsListResponse getCountryMedalsListResponse(String countryName, String sortBy, boolean ascendingOrder) {
        List<GoldMedal> medalsList;

        if (sortBy.equals("year") || sortBy.equals("season") || sortBy.equals("city") || sortBy.equals("name") || sortBy.equals("event")) {
            Sort sort = ascendingOrder ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

            medalsList = goldMedalRepository.findByCountry(countryName, sort);
        } else {
            
            medalsList = new ArrayList<>();
        }

        return new CountryMedalsListResponse(medalsList);
    }

    private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
        var countryOptional = countryRepository.findOptionalByName(countryName); // TODO: get the country; this repository method should return a java.util.Optional
        if (countryOptional.isEmpty()) {
            return new CountryDetailsResponse(countryName);
        }

        var country = countryOptional.get();
        var goldMedalCount = goldMedalRepository.countByCountry(countryName); // TODO: get the medal count

        var summerWins = goldMedalRepository.findByCountryAndSeason(countryName, "Summer", Sort.by("year").ascending()); // TODO: get the collection of wins at the Summer Olympics, sorted by year in ascending order
        var numberSummerWins = summerWins.size() > 0 ? summerWins.size() : null;
        var totalSummerEvents = goldMedalRepository.countBySeason("Summer"); // TODO: get the total number of events at the Summer Olympics
        var percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != null ? (float) summerWins.size() / totalSummerEvents : null;
        var yearFirstSummerWin = summerWins.size() > 0 ? summerWins.get(0).getYear() : null;

        var winterWins = goldMedalRepository.findByCountryAndSeason(countryName, "Winter", Sort.by("year").ascending()); // TODO: get the collection of wins at the Winter Olympics
        var numberWinterWins = winterWins.size() > 0 ? winterWins.size() : null;
        var totalWinterEvents = goldMedalRepository.countBySeason("Winter"); // TODO: get the total number of events at the Winter Olympics, sorted by year in ascending order
        var percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != null ? (float) winterWins.size() / totalWinterEvents : null;
        var yearFirstWinterWin = winterWins.size() > 0 ? winterWins.get(0).getYear() : null;

        var numberEventsWonByFemaleAthletes = goldMedalRepository.countByCountryAndGender(countryName, "Women"); // TODO: get the number of wins by female athletes
        var numberEventsWonByMaleAthletes = goldMedalRepository.countByCountryAndGender(countryName, "Men"); // TODO: get the number of wins by male athletes

        return new CountryDetailsResponse(
                countryName,                // String
                country.getGdp(),           // BigDecimal
                country.getPopulation(),    // Integer
                goldMedalCount,             // Integer
                numberSummerWins,           // Integer
                percentageTotalSummerWins,  // Float
                yearFirstSummerWin,         // Integer
                numberWinterWins,           // Integer
                percentageTotalWinterWins,  // Float
                yearFirstWinterWin,         // Integer
                numberEventsWonByFemaleAthletes,    // Integer
                numberEventsWonByMaleAthletes);     // Integer
    }

    private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
        List<Country> countries;

        Sort sort;

        if (sortBy.equals("name") || sortBy.equals("gdp") || sortBy.equals("population")) {
            sort = ascendingOrder ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            countries = countryRepository.findAll(sort); 
        } else {
            countries = countryRepository.findAll(); 
        }

        var countrySummaries = getCountrySummariesWithMedalCount(countries);

        if (sortBy.equalsIgnoreCase("medals")) {
            countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
        }

        return countrySummaries;
    }

    private List<CountrySummary> sortByMedalCount(List<CountrySummary> countrySummaries, boolean ascendingOrder) {
        return countrySummaries.stream()
                .sorted((t1, t2) -> ascendingOrder ?
                        t1.getMedals() - t2.getMedals() :
                        t2.getMedals() - t1.getMedals())
                .collect(Collectors.toList());
    }

    private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
        List<CountrySummary> countrySummaries = new ArrayList<>();
        for (var country : countries) {
            var goldMedalCount = (int) goldMedalRepository.countByCountry(country.getName()); // TODO: get count of medals for the given country
            countrySummaries.add(new CountrySummary(country, goldMedalCount));
        }
        return countrySummaries;
    }
}
