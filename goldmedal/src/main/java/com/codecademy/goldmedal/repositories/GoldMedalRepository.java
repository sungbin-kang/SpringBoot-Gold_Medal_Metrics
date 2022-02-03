package com.codecademy.goldmedal.repositories;

import java.util.List;
import java.util.Optional;

import com.codecademy.goldmedal.model.GoldMedal;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GoldMedalRepository extends CrudRepository<GoldMedal, Integer> {

    List<GoldMedal> findByCountry(String country, Sort sort);

    List<GoldMedal> findBySeason(String season);

    long countByCountry(String country);

    long countBySeason(String season);

    @Query(value = "SELECT COUNT(*) FROM GoldMedal gm WHERE gm.season = :season ORDER BY year ASC")
    long countBySeasonOrderByYearAsc(@Param("season") String season);

    @Query(value = "SELECT COUNT(*) FROM GoldMedal gm WHERE gm.country = :country AND gm.gender = :gender")
    long countByGenderAndFindByCountry(String gender, String country);
    
//     @Query("SELECT c FROM Client c WHERE c.name = :name and c.age = :age")
// List<Client> findByName(@Param("name") String name, @Param("age") int age);
}
