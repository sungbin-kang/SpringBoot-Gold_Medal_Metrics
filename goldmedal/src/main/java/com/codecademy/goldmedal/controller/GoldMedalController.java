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
@RequestMapping("/countries")
public class GoldMedalController {
	// TODO: declare references to your repositories
	private final GoldMedalRepository goldmedalRepository;
	private final CountryRepository countryRepository;

	// TODO: update your constructor to include your repositories
	public GoldMedalController(final GoldMedalRepository goldmedalRepository, final CountryRepository countryRepository) {
		this.goldmedalRepository = goldmedalRepository;
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

		Sort sort;

		if (ascendingOrder) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}

		switch (sortBy) {
		case "year":
			medalsList = goldmedalRepository.findByCountry(countryName, sort);
			break;

		case "season":
			medalsList = goldmedalRepository.findByCountry(countryName, sort);
			break;

		case "city":
			medalsList = goldmedalRepository.findByCountry(countryName, sort);
			break;
			
		case "name":
			medalsList = goldmedalRepository.findByCountry(countryName, sort);
			break;
			
		case "event":
			medalsList = goldmedalRepository.findByCountry(countryName, sort);
			break;
			
		default:
			medalsList = new ArrayList<>();
			break;
		}

		return new CountryMedalsListResponse(medalsList);
	}

	private CountryDetailsResponse getCountryDetailsResponse(String countryName) {

		var countryOptional = countryRepository.findOptionalByName(countryName);
				if (countryOptional.isEmpty()) {
					return new CountryDetailsResponse(countryName);
				}

		var country = countryOptional.get();
		Integer goldMedalCount = (int) goldmedalRepository.countByCountry(countryName);
				
				var summerWins = goldmedalRepository.findByCountry(countryName, Sort.by("year").ascending()); // get the collection of wins at the Summer Olympics, sorted by year in ascending order

				var numberSummerWins = summerWins.size() > 0 ? summerWins.size() : null;
				var totalSummerEvents = goldmedalRepository.countBySeason("Summer"); // get the total number of events at the Summer Olympics
						var percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != null ? (float) summerWins.size() / totalSummerEvents : null;
				var yearFirstSummerWin = summerWins.size() > 0 ? summerWins.get(0).getYear() : null;

				var winterWins = goldmedalRepository.findBySeason("Winter"); // TODO: get the collection of wins at the Winter Olympics
						var numberWinterWins = winterWins.size() > 0 ? winterWins.size() : null;
						var totalWinterEvents = goldmedalRepository.findByCountry(countryName, Sort.by("year").ascending()).size(); // TODO: get the total number of events at the Winter Olympics, sorted by year in ascending order
								var percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != null ? (float) winterWins.size() / totalWinterEvents : null;
						var yearFirstWinterWin = winterWins.size() > 0 ? winterWins.get(0).getYear() : null;

						Integer numberEventsWonByFemaleAthletes = (int) goldmedalRepository.countByGenderAndFindByCountry("Women", countryName); // TODO: get the number of wins by female athletes
								Integer numberEventsWonByMaleAthletes = (int) goldmedalRepository.countByGenderAndFindByCountry("Men", countryName); // TODO: get the number of wins by male athletes

								return new CountryDetailsResponse(
										countryName,
										country.getGdp(),
										country.getPopulation(),
										goldMedalCount,
										numberSummerWins,
										percentageTotalSummerWins,
										yearFirstSummerWin,
										numberWinterWins,
										percentageTotalWinterWins,
										yearFirstWinterWin,
										numberEventsWonByFemaleAthletes,
										numberEventsWonByMaleAthletes);
	}

	private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {

		List<Country> countries;

		Sort sort;

		if (ascendingOrder) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}

		switch (sortBy) {
		case "name":
			countries = countryRepository.findAll(sort); // TODO: list of countries sorted by name in the given order
			break;
		case "gdp":
			countries = countryRepository.findAll(sort); // TODO: list of countries sorted by gdp in the given order
			break;
		case "population":
			countries =  countryRepository.findAll(sort);// TODO: list of countries sorted by population in the given order
			break;
		case "medals":
		default:
			countries = countryRepository.findAll(sort); // TODO: list of countries in any order you choose; for sorting by medal count, additional logic below will handle that
			break;
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
			var goldMedalCount = (int) goldmedalRepository.countByCountry(country.getName()); // TODO: get count of medals for the given country
					countrySummaries.add(new CountrySummary(country, goldMedalCount));
		}
		return countrySummaries;
	}
}
