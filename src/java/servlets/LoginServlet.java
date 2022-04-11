/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entity.Reader;
import entity.Role;
import entity.User;
import entity.UserRoles;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import session.ReaderFacade;
import session.RoleFacade;
import session.UserFacade;
import session.UserRolesFacade;
import tools.PasswordProtected;

/**
 *
 * @author Melnikov
 */
@WebServlet(name = "LoginServlet", urlPatterns = {
    "/login",
    "/logout",
    "/registration",
})
public class LoginServlet extends HttpServlet {
    @EJB private UserFacade userFacade;
    @EJB private ReaderFacade readerFacade;
    @EJB private RoleFacade roleFacade;
    @EJB private UserRolesFacade userRolesFacade;
    
    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        if(userFacade.count()>0) return;
        Reader reader = new Reader();
        reader.setFirstname("Juri");
        reader.setLastname("Melnikov");
        reader.setPhone("56656545656");
        readerFacade.create(reader);
        User user = new User();
        user.setLogin("admin");
        PasswordProtected passwordProtected = new PasswordProtected();
        String salt = passwordProtected.getSalt();
        user.setSalt(salt);
        String password = passwordProtected.getProtectedPassword("12345", salt);
        user.setPassword(password);
        user.setReader(reader);
        userFacade.create(user);
        Role role = new Role();
        role.setRoleName("USER");
        roleFacade.create(role);
        UserRoles ur = new UserRoles();
        ur.setRole(role);
        ur.setUser(user);
        userRolesFacade.create(ur);
        role = new Role();
        role.setRoleName("MANAGER");
        roleFacade.create(role);
        ur = new UserRoles();
        ur.setRole(role);
        ur.setUser(user);
        userRolesFacade.create(ur);
        role = new Role();
        role.setRoleName("ADMINISTRATOR");
        roleFacade.create(role);
        ur = new UserRoles();
        ur.setRole(role);
        ur.setUser(user);
        userRolesFacade.create(ur);
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
         request.setCharacterEncoding("UTF-8");
         HttpSession session = null;
         String path = request.getServletPath();
        switch (path) {
            case "/login":
                JsonReader jsonReader = Json.createReader(request.getReader());
                JsonObject jsonObject = jsonReader.readObject();
                String login = jsonObject.getString("login","");
                String password = jsonObject.getString("password","");
                User authUser = userFacade.findByLogin(login);
                if(authUser == null){
                    String json = "{\"info\": \"Нет такого пользователя\"}";
                    try (PrintWriter out = response.getWriter()) {
                        out.println(json);
                    }
                    break;
                }
                PasswordProtected pp = new PasswordProtected();
                password = pp.getProtectedPassword(password, authUser.getSalt());
                if(!password.equals(authUser.getPassword())){
                    String json = "{\"info\": \"Неверный пароль\"}";
                    try (PrintWriter out = response.getWriter()) {
                        out.println(json);
                    }
                    break;
                }
                session = request.getSession(true);
                session.setAttribute("authUser", authUser);
                JsonObjectBuilder job = Json.createObjectBuilder();
                job.add("info", "Вы вошли как "+authUser.getLogin());
                String json = job.build().toString();
                //String json = "{\"info\": \"Вы вошли как "+authUser.getLogin()+"\"}";
                try (PrintWriter out = response.getWriter()) {
                        out.println(json);
                }
                break;
            case "/logout":
                session = request.getSession(false);
                if(session != null){
                    session.invalidate();
                    request.setAttribute("info", "Вы вышли");
                }
                request.getRequestDispatcher("/listAccounts").forward(request, response);
                break;
           
            case "/registration":
               
                break;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
