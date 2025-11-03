package example;

import example.model.GrammyNomination;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class GrammyNominationDao {
    @PersistenceContext(unitName = "GrammyPU")
    private EntityManager em;

    public void save(GrammyNomination nomination) {
        em.persist(nomination);
    }
}
