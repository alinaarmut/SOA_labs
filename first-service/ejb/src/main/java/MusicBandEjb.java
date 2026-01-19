import external.MusicBandBusinessRemote;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import org.example.model.MusicBand;
import org.example.model.MusicGenre;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Stateless(name = "MusicBandEjb")
public class MusicBandEjb implements MusicBandBusinessRemote {

    @PersistenceContext(unitName = "MusicBandPU")
    private EntityManager em;

    @Override
    public List<MusicBand> getAll() {
        TypedQuery<MusicBand> query = em.createQuery(
                "SELECT m FROM MusicBand m", MusicBand.class);
        return query.getResultList();
    }

    @Override
    public MusicBand add(MusicBand band) {
        if (band.getCreationDate() == null) {
            band.setCreationDate(LocalDate.now());
        }

        if (band.getName() == null || band.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Название группы обязательно");
        }
        if (band.getCoordinates() == null) {
            throw new IllegalArgumentException("Координаты обязательны");
        }
        if (band.getNumberOfParticipants() != null && band.getNumberOfParticipants() <= 0) {
            throw new IllegalArgumentException("Количество участников должно быть больше 0");
        }

        em.persist(band);
        em.flush();
        return band;
    }

    @Override
    public MusicBand getById(Integer id) {
        return em.find(MusicBand.class, id.longValue());
    }

    @Override
    public boolean deleteById(Integer id) {
        MusicBand band = em.find(MusicBand.class, id.longValue());
        if (band != null) {
            em.remove(band);
            return true;
        }
        return false;
    }

    @Override
    public MusicBand update(long id, MusicBand updatedBand) {
        MusicBand existing = em.find(MusicBand.class, id);
        if (existing == null) {
            throw new IllegalArgumentException("Группа с id=" + id + " не найдена");
        }

        existing.setName(updatedBand.getName());
        existing.setCoordinates(updatedBand.getCoordinates());
        existing.setNumberOfParticipants(updatedBand.getNumberOfParticipants());
        existing.setGenre(updatedBand.getGenre());
        existing.setFrontMan(updatedBand.getFrontMan());

        return em.merge(existing);
    }

    @Override
    public Map<String, Long> groupByGenre() {
        List<MusicBand> bands = getAll();
        return bands.stream()
                .collect(Collectors.groupingBy(
                        b -> b.getGenre() != null ? b.getGenre().name() : "Не указан",
                        Collectors.counting()
                ));
    }

    @Override
    public int countByFrontman(String frontMan) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(m) FROM MusicBand m WHERE m.frontMan.name = :name",
                Long.class);
        query.setParameter("name", frontMan);
        return query.getSingleResult().intValue();
    }

    @Override
    public List<MusicBand> searchByNamePrefix(String prefix) {
        TypedQuery<MusicBand> query = em.createQuery(
                "SELECT m FROM MusicBand m WHERE LOWER(m.name) LIKE LOWER(:prefix)",
                MusicBand.class);
        query.setParameter("prefix", prefix + "%");
        return query.getResultList();
    }

    @Override
    public List<MusicBand> findByGenre(MusicGenre genre) {
        TypedQuery<MusicBand> query = em.createQuery(
                "SELECT m FROM MusicBand m WHERE m.genre = :genre",
                MusicBand.class);
        query.setParameter("genre", genre);
        return query.getResultList();
    }

    @Override
    public String test(String name) {
        return "EJB тест: Привет, " + name + "!";
    }
}