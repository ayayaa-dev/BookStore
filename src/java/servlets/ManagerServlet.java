package servlets;

import entity.Author;
import entity.Book;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import jsontools.AuthorJsonBuilder;
import jsontools.BookJsonBuilder;
import session.AuthorFacade;
import session.BookFacade;

@WebServlet(name = "ManagerServlet", urlPatterns = {
    "/createNewAuthor",
    "/getListAuthors",
    "/getAuthor",
    "/updateAuthor",
    "/createNewBook",
    "/getListBooks",
    "/getListCovers",
    "/getEditBook",
    "/updateBook",
  
})
@MultipartConfig
public class ManagerServlet extends HttpServlet {
    
    @EJB private AuthorFacade authorFacade;
    @EJB private BookFacade bookFacade;
    
    private final String uploadDir = "D:\\UploadDir\\JPTV20BookShop";
//    private final String uploadDir = "/opt/UploadDir/JPTV20BookShop";
    
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
         JsonObjectBuilder job = Json.createObjectBuilder();
         String path = request.getServletPath();
        switch (path) {
            case "/createNewAuthor":
                JsonReader jsonReader = Json.createReader(request.getReader());
                JsonObject jsonObject = jsonReader.readObject();
                String firstname = jsonObject.getString("firstname","");
                String lastname = jsonObject.getString("lastname","");
                String birthYear = jsonObject.getString("birthYear","");
                if("".equals(firstname) || "".equals(lastname) || "".equals(birthYear)){
                    job.add("info", "Заполните все поля")
                       .add("firstname",firstname)
                       .add("lastname",lastname)
                       .add("birthYear",birthYear);
                    try (PrintWriter out = response.getWriter()) {
                        out.println(job.build().toString());
                    }
                    break;
                }
                
                Author newAuthor = new Author();
                newAuthor.setFirstname(firstname);
                newAuthor.setLastname(lastname);
                newAuthor.setBirthYear(Integer.parseInt(birthYear));
                authorFacade.create(newAuthor);
                job.add("info", "Создан автор "+newAuthor.getFirstname()+" "+newAuthor.getLastname())
                   .add("status",true);
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                break;
            case "/getListAuthors":
                List<Author> listAuthors = authorFacade.findAll();
                AuthorJsonBuilder ajb = new AuthorJsonBuilder();
                job.add("status",true);
                job.add("info","Создан массив авторов");
                job.add("authors",ajb.getAuthorsJsonArray(listAuthors));
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                break;
            case "/getAuthor":
                jsonReader = Json.createReader(request.getReader());
                jsonObject = jsonReader.readObject();
                String authorId = jsonObject.getString("authorId","");
                Author author = authorFacade.find(Long.parseLong(authorId));
                ajb = new AuthorJsonBuilder();
                job.add("info", "Редактируем автора: "+author.getFirstname()+" "+author.getLastname());
                job.add("status", true);
                job.add("author", ajb.getAuthorJsonObject(author));
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                break;
            case "/updateAuthor":
                jsonReader = Json.createReader(request.getReader());
                jsonObject = jsonReader.readObject();
                authorId = jsonObject.getString("authorId","");
                firstname = jsonObject.getString("firstname","");
                lastname = jsonObject.getString("lastname","");
                birthYear = jsonObject.getString("birthYear","");
                Author updateAuthor = authorFacade.find(Long.parseLong(authorId));
                updateAuthor.setBirthYear(Integer.parseInt(birthYear));
                updateAuthor.setLastname(lastname);
                updateAuthor.setFirstname(firstname);
                authorFacade.edit(updateAuthor);
                job.add("info", "Автор изменен");
                job.add("status", true);
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                break;
            case "/createNewBook":
                String bookName = request.getParameter("bookName");
                String publishedYear = request.getParameter("publishedYear");
                String[] selectAuthors = request.getParameterValues("selectAuthors");
                String price = request.getParameter("price");
                if("".equals(bookName) || "".equals(publishedYear)
                        || selectAuthors.length == 0 || "".equals(price)){
                    job.add("info", "Заполните все поля!");
                    job.add("status", false);
                    try (PrintWriter out = response.getWriter()) {
                        out.println(job.build().toString());
                    }
                }
                Book book = new Book();
                book.setBookName(bookName);
                book.setPublishedYear(Integer.parseInt(publishedYear));
                List<Author> authors = new ArrayList<>();
                for(int i=0; i < selectAuthors.length; i++){
                    String jsonAuthorId = selectAuthors[i];
                    authors.add(authorFacade.find(Long.parseLong(jsonAuthorId)));
                }
                book.setAuthor(authors);
                book.setPrice(price);
                String coverFileName = request.getParameter("coverFileName");
                if(coverFileName == null || "".equals(coverFileName)){
                    book.setCover(getPathToCover(request.getPart("cover")));
                }else{
                    book.setCover(getPathToCover(coverFileName));
                }
                bookFacade.create(book);
                job.add("info", "Книга добавлена!");
                    job.add("status", true);
                    try (PrintWriter out = response.getWriter()) {
                        out.println(job.build().toString());
                    }
                break;
            case "/getListBooks":
                List<Book> listBooks = bookFacade.findAll();
                BookJsonBuilder bjb = new BookJsonBuilder();
                job.add("status",true);
                job.add("info","Создан массив книг");
                job.add("books",bjb.getBooksJsonArray(listBooks));
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                break;
            case "/getListCovers":
                String[] coversFileName = getCoversFileName();
                JsonArrayBuilder jab = Json.createArrayBuilder();
                for (int i = 0; i < coversFileName.length; i++) {
                    jab.add(coversFileName[i]);
                }
                
                job.add("status",true);
                job.add("info","Создан массив авторов");
                job.add("covers",jab.build());
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                break;
            case "/getEditBook":
                jsonReader = Json.createReader(request.getReader());
                jsonObject = jsonReader.readObject();
                String editBookId = jsonObject.getString("editBookId","");
                Book editBook = bookFacade.find(Long.parseLong(editBookId));
                bjb = new BookJsonBuilder();
                job.add("status",true);
                job.add("info","Редактируем книгу: "+editBook.getBookName());
                job.add("editBook",bjb.getBookJsonObject(editBook));
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                break;
            case "/updateBook":
                
                break;
        }
    }
    private String getPathToCover(Part part) throws IOException {
        
        String pathToCover = uploadDir + File.separator + getFileName(part);
        File file = new File(pathToCover);
        file.mkdirs();
        try(InputStream fileContent = part.getInputStream()){
            Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return pathToCover;
    }
    private String getPathToCover(String coverFileName){
        File uploadDirFolder = new File(uploadDir);
        File[] listOfFiles = uploadDirFolder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if(listOfFiles[i].isFile()){
                if(coverFileName.equals(listOfFiles[i].getName())){
                    return listOfFiles[i].getPath();
                }
            }
        }
        return "";
    }
    private String[] getCoversFileName(){
        Set<String> setPathToCover = new HashSet<>();
        File uploadDirFolder = new File(uploadDir);
        File[] listOfFiles = uploadDirFolder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if(listOfFiles[i].isFile()){
                setPathToCover.add(listOfFiles[i].getName());
            }
        }
        return setPathToCover.toArray(new String[setPathToCover.size()]);
    }
    private String getFileName(Part part){
        final String partHeader = part.getHeader("content-disposition");
        for (String content : part.getHeader("content-disposition").split(";")){
            if(content.trim().startsWith("filename")){
                return content
                        .substring(content.indexOf('=')+1)
                        .trim()
                        .replace("\"",""); 
            }
        }
        return null;
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