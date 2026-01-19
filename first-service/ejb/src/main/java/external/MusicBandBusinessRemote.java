package external;


import jakarta.ejb.Remote;
import org.example.model.MusicBand;
import org.example.model.MusicGenre;

import java.util.List;
import java.util.Map;

// контракт сервиса
@Remote
public interface MusicBandBusinessRemote {
    List<MusicBand> getAll();
    MusicBand add(MusicBand band);
    MusicBand getById(Integer id);
    boolean deleteById(Integer id);
    MusicBand update(long id, MusicBand updatedBand);
    Map<String, Long> groupByGenre();
    int countByFrontman(String frontMan);
    List<MusicBand> searchByNamePrefix(String prefix);
    List<MusicBand> findByGenre(MusicGenre genre);
    String test(String name);
}
