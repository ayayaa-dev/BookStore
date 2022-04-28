package session;

import entity.Role;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class RoleFacade extends AbstractFacade<Role> {

    @PersistenceContext(unitName = "JPTV20BookShopPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RoleFacade() {
        super(Role.class);
    }

    public Role getRoleByName(String roleName) {
        try {
            Role role = (Role) em.createQuery("SELECT r FROM Role r WHERE r.roleName = :roleName")
                    .setParameter("roleName", roleName)
                    .getSingleResult();
            return role;
        } catch (Exception e) {
            return null;
        }
    }
    
}
