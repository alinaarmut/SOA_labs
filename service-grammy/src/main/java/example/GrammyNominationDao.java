package example;

import example.model.GrammyNomination;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class GrammyNominationDao {
    @PersistenceContext
    private EntityManager em;

    public void save(GrammyNomination nomination) {
        em.persist(nomination);
    }
}
