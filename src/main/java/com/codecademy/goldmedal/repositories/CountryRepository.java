package com.codecademy.goldmedal.repositories;

import java.util.List;
import java.util.Optional;

import com.codecademy.goldmedal.model.Country;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CountryRepository extends PagingAndSortingRepository<Country, Integer> {
    
    Optional<Country> findOptionalByName(String name);

    List<Country> findAll(Sort sort);

    List<Country> findAll();

}
