/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.Home;

import Dal.CourseDetailDAO;
import Dal.HomeDAO;
import Dal.WishlistDAO;
import Model.Account;
import Model.Category;
import Model.Course;
import Model.Enrollment;
import Model.WishlistDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
@WebServlet(name = "listCourseSeverlet", urlPatterns = {"/listCourseSeverlet"})
public class listCourseSeverlet extends HttpServlet {

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
            out.println("<title>Servlet listCourseSeverlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet listCourseSeverlet at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();
        HomeDAO dao = new HomeDAO();
        CourseDetailDAO cdDao = new CourseDetailDAO();
        String action = request.getParameter("action");
        Account acc = (Account) session.getAttribute("account");
        String cid = request.getParameter("cateid");

        try {
            ArrayList<Course> listAllCourse = dao.getAllCourses();
            ArrayList<Category> listCategory = dao.getAllCategory();
            ArrayList<Course> listCourseByCategory = dao.getCourseByCategoryId(cid);
            if (acc != null) {
                ArrayList<Enrollment> listEnrollment = cdDao.getEnrollmentByAccountId(acc.getAccount_id());
                request.setAttribute("listEnrollment", listEnrollment);

                //Lấy ra list wishList để check is active icon
                getCidFromWishlistByAccId(request, response, acc.getAccount_id());
                
            }

            //Định dạng khóa học theo giá tiền Việt Nam
            for (Course course : listCourseByCategory) {
                course.setFormattedPrice(formartPrice(course.getPrice()));
            }

            for (Course course : listAllCourse) {
                course.setFormattedPrice(formartPrice(course.getPrice()));
            }

            //list All course
            if (cid.equals("all")) {
                request.setAttribute("listAllCourse", listAllCourse);

            } else {
                // List course by category
                request.setAttribute("listCourseByCategory", listCourseByCategory);
            }

            request.setAttribute("action", action);
            request.setAttribute("listCategory", listCategory);

        } catch (Exception e) {
            e.getStackTrace();
            throw new ServletException("error", e);
        }
        request.getRequestDispatcher("Courses.jsp").forward(request, response);

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
        try {
            HttpSession session = request.getSession();
            Account acc = (Account) session.getAttribute("account");

            String search = request.getParameter("search");
            if (search == null || search.isBlank()) {
                response.sendRedirect("home");
                return;
            }
            HomeDAO dao = new HomeDAO();
            CourseDetailDAO cdDao = new CourseDetailDAO();
            ArrayList<Course> listCourseBySearch = dao.searchByName(search);
            if (acc != null) {
                ArrayList<Enrollment> listEnrollment = cdDao.getEnrollmentByAccountId(acc.getAccount_id());
                request.setAttribute("listEnrollment", listEnrollment);
            }

            //Định dạng khóa học theo giá tiền Việt Nam
            for (Course course : listCourseBySearch) {
                course.setFormattedPrice(formartPrice(course.getPrice()));
            }

            request.setAttribute("listCourseBySearch", listCourseBySearch);
            request.setAttribute("searchValue", search);

        } catch (SQLException ex) {
            Logger.getLogger(listCourseSeverlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.getRequestDispatcher("Courses.jsp").forward(request, response);
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

    public String formartPrice(int price) {
        NumberFormat formatTer = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatTer.format(price);
    }

  public void getCidFromWishlistByAccId(HttpServletRequest request, HttpServletResponse response, int acc_id)
            throws ServletException, IOException {
        WishlistDAO dao = new WishlistDAO();
        ArrayList<WishlistDTO> listWishListCoursId = dao.getCidFromWishListByAccId(acc_id);
        ArrayList<Integer> CourseIdList = new ArrayList<>();
        for (WishlistDTO wishlist : listWishListCoursId) {
            CourseIdList.add(wishlist.getCourse_id());
        }
        //response.getWriter().print(listWishListCoursId);

        request.setAttribute("CourseIdList", CourseIdList);
    }


}
