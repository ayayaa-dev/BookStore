package session;

import entity.Author;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AuthorFacade extends AbstractFacade<Author> {

    @PersistenceContext(unitName = "JPTV20BookShopPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AuthorFacade() {
        super(Author.class);
    }
    
}
