/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.User.Manager;

import Dal.CourseDetailDAO;
import Dal.CourseManageDAO;
import Dal.HomeDAO;
import Dal.LessonManageDAO;
import Dal.ModuleDAO;
import Model.AccountDTO;
import Model.Category;
import Model.CourseManageDTO;
import Model.LessonDTO;
import Model.ModuleDTO;
import Util.HeaderSession;
import Util.ServerPath;
import Util.Validation;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.taglibs.standard.lang.jstl.ELException;

/**
 *
 * @author tuong
 */
@MultipartConfig
public class CourseManageServlet extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CourseManageServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CourseManageServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        PrintWriter o = response.getWriter();
        HeaderSession header = new HeaderSession(request, response);

        String cid = (String) request.getParameter("cid") == null ? "" : (String) request.getParameter("cid");
        String action = (String) request.getParameter("action") == null ? "" : (String) request.getParameter("action");

        HttpSession session = request.getSession();
        AccountDTO my_account = (AccountDTO) session.getAttribute("account");
        CourseManageDAO course_manage_DAO = new CourseManageDAO();

        ArrayList<CourseManageDTO> list_managed_course = course_manage_DAO.getMyManagedCourse(my_account.getAccount_id());
        request.setAttribute("list_managed_couse", list_managed_course);

////        read data lesson from database
//        LessonManageDAO dao = new LessonManageDAO();
//        ArrayList<Lesson> lessonList = null;
//        try {
//            lessonList = dao.getListlessonByCid(Integer.parseInt(cid));
//        } catch (SQLException ex) {
//            Logger.getLogger(CourseManageServlet.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NumberFormatException e) {
//        }
        switch (action) {
            case "update":
                request.setAttribute("cid", cid);
//                if (lessonList != null) {
//                    request.setAttribute("lessonList", lessonList);
//                }
                ModuleDAO module_dao = new ModuleDAO();
                LessonManageDAO lesson_manage_dao = new LessonManageDAO();
                try {
                    ArrayList<ModuleDTO> list_module = module_dao.getListModulByCid(cid);
                    ArrayList<LessonDTO> list_lesson = lesson_manage_dao.getListlessonByCid(Integer.parseInt(cid));
                    request.setAttribute("list_module", list_module);
                    request.setAttribute("list_lesson", list_lesson);
                } catch (SQLException ex) {
                    Logger.getLogger(CourseManageServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                request.getRequestDispatcher("UpdateCourse.jsp").forward(request, response);
                return;

            case "add_module":
                request.setAttribute("cid", cid);
                addModule(request, response);
                return;

            case "add_new_course":
                try {
                    addCourse(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(CourseManageServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            default:
                request.getRequestDispatcher("CourseManage.jsp").forward(request, response);
        }
    }
//    public static void main(String[] args) {
//        CourseManageDAO course_manage_DAO = new CourseManageDAO();
//        ArrayList<CourseManageDTO> list_managed_course = course_manage_DAO.getMyManagedCourse(2);
//        System.out.println(list_managed_course.get(0).getCourse_name());
//    }

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
        String action = request.getParameter("action");
        String cid = (String) request.getParameter("cid") == null ? "" : (String) request.getParameter("cid");
        PrintWriter o = response.getWriter();
        switch (action) {
            case "add_new_module":
                addNewModuleDoPost(request, response, cid);
                break;
            case "add_new_course":
                addNewCouseDoPost(request, response);
                break;
            case "delete":
                deleteCourse(cid, request, response);
                break;
            case "activate":
                activateCourse(cid, request, response);
                return;
            default:
                throw new AssertionError();
        }

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

    private void deleteCourse(String cid, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CourseManageDAO course_manage_DAO = new CourseManageDAO();
        boolean success = course_manage_DAO.deleteCourse(cid);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print("{\"success\":" + success + "}");
        out.flush();
    }

    private void activateCourse(String cid, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CourseManageDAO course_manage_DAO = new CourseManageDAO();
        boolean success = course_manage_DAO.activateCourse(cid);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print("{\"success\": " + success + "}");
        out.flush();
    }
//    public static void main(String[] args) {
//        CourseManageServlet c = new CourseManageServlet();
//        c.deleteCourse("1");
//    }

    private void addCourse(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        request.getRequestDispatcher("AddNewCourse.jsp").forward(request, response);
    }

    private void addNewCouseDoPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Part file_image_course = request.getPart("image");
        String course_name = request.getParameter("courseName") == null ? "" : request.getParameter("courseName");
        String description = request.getParameter("description") == null ? "" : request.getParameter("description");
        String price = request.getParameter("price") == null ? "0" : request.getParameter("price");
        String discount = request.getParameter("discount") == null ? "0" : request.getParameter("discount");
        String category = request.getParameter("category") == null ? "" : request.getParameter("category");

        String[] fullFields = {course_name, description, category};

        request.setAttribute("price", price);
        request.setAttribute("discount", discount);

        String image_file_name = "";
        if (file_image_course != null && file_image_course.getSize() > 0) {
            image_file_name = Validation.inputFile(request, file_image_course, "image_course");
            request.setAttribute("image", image_file_name);
        }
        if (!Validation.checkString(course_name)) {
            request.setAttribute("course_name", course_name);
            request.setAttribute("error_name", "You must input course name!");
        }
        if (!Validation.checkString(description)) {
            request.setAttribute("description", description);
            request.setAttribute("error_desciption", "You must input desciption!");
        }
        if (!Validation.checkString(category)) {
            request.setAttribute("category", category);
            request.setAttribute("error_category", "You must choose category!");
        }
        if (Validation.checkStringArray(fullFields)) {
            HttpSession session = request.getSession();
            AccountDTO my_account = (AccountDTO) session.getAttribute("account");
            CourseManageDAO course_manage_DAO = new CourseManageDAO();
            CourseManageDTO new_course = new CourseManageDTO(course_name, description, image_file_name, Float.parseFloat(price), Float.parseFloat(discount), category);
            course_manage_DAO.insertCourse(my_account.getAccount_id(), new_course);
            response.sendRedirect("course-manage");
        } else {
            request.getRequestDispatcher("AddNewCourse.jsp").forward(request, response);
        }
    }

    private void addModule(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("AddNewModule.jsp").forward(request, response);
    }

    private void addNewModuleDoPost(HttpServletRequest request, HttpServletResponse response, String cid) throws IOException, ServletException {
        String module_name = request.getParameter("module_name") == null ? "" : request.getParameter("module_name");
        String module_number = request.getParameter("module_number") == null ? "" : request.getParameter("module_number");
        
        String[] fullFields = {module_name, module_number};
        PrintWriter o = response.getWriter();
        if (!Validation.checkString(module_name)) {
            request.setAttribute("module_name", module_name);
            request.setAttribute("error_module_name", "You must input module name!");
        }
        if (!Validation.checkInt(module_number)) {
            request.setAttribute("module_number", module_number);
            request.setAttribute("error_module_number", "You must input module number!");
        }
//da nhap du fields
        if (Validation.checkStringArray(fullFields)) {
            HttpSession session = request.getSession();
            AccountDTO my_account = (AccountDTO) session.getAttribute("account");
            ModuleDAO module_DAO = new ModuleDAO();
            ModuleDTO new_module = new ModuleDTO(module_name, Integer.parseInt(module_number));
            module_DAO.insertModule(cid, new_module);
            response.sendRedirect("course-manage?cid="+cid+"&action=update");
        } else {
            request.getRequestDispatcher("AddNewModule.jsp").forward(request, response);
        }
    }

}

