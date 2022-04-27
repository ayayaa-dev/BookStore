/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Role;
import entity.User;
import entity.UserRoles;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Melnikov
 */
@Stateless
public class UserRolesFacade extends AbstractFacade<UserRoles> {
    
    @EJB private RoleFacade roleFacade;
    
    @PersistenceContext(unitName = "JPTV20BookShopPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserRolesFacade() {
        super(UserRoles.class);
    }
    public Role getRoleForUser(User user){
        try {
            List<String> listRoleNamesForUser = 
                  em.createQuery("SELECT ur.role.roleName FROM UserRoles ur WHERE ur.user = :user")
                    .setParameter("user", user)
                    .getResultList();
            if(listRoleNamesForUser.contains("ADMINISTRATOR")){
                Role role = roleFacade.getRoleByName("ADMINISTRATOR");
                return role;
            }else if(listRoleNamesForUser.contains("MANAGER")){
                return roleFacade.getRoleByName("MANAGER");
            }else if(listRoleNamesForUser.contains("USER")){
                return roleFacade.getRoleByName("USER");
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
