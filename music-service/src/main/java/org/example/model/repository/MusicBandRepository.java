package org.example.model.repository;

import org.example.model.MusicBand;
import org.example.model.MusicGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicBandRepository extends JpaRepository<MusicBand, Long> {
    // Ваш существующий метод
    List<MusicBand> findByGenre(MusicGenre genre);

    // Поиск по части имени (case-insensitive)
    List<MusicBand> findByNameContainingIgnoreCase(String name);

    // Поиск по префиксу имени
    List<MusicBand> findByNameStartingWithIgnoreCase(String prefix);

    // Подсчёт по имени фронтмена
    @Query("SELECT COUNT(b) FROM MusicBand b WHERE b.frontMan.name = :frontManName")
    long countByFrontManName(@Param("frontManName") String frontManName);

    // Поиск по жанру и количеству участников
    List<MusicBand> findByGenreAndNumberOfParticipantsGreaterThan(MusicGenre genre, Integer minParticipants);
}
