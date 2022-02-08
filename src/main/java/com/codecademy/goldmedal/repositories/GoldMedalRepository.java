package com.codecademy.goldmedal.repositories;

import java.util.List;

import com.codecademy.goldmedal.model.GoldMedal;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GoldMedalRepository extends PagingAndSortingRepository<GoldMedal, Integer> {

    List<GoldMedal> findByCountry(String country, Sort sort);

    List<GoldMedal> findByCountryAndSeason(String country, String season, Sort sort);

    Integer countByCountry(String country);

    Integer countBySeason(String season);

    Integer countByCountryAndGender(String country, String gender);

}
