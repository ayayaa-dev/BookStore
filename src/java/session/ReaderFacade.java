package session;

import entity.Reader;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ReaderFacade extends AbstractFacade<Reader> {

    @PersistenceContext(unitName = "JPTV20BookShopPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReaderFacade() {
        super(Reader.class);
    }
    
}
