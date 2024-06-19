/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.User;

import Dal.EnrollmentDAO;
import Dal.WishlistDAO;
import Model.Account;
import Model.Course;
import Model.EnrollmentDTO;
import Model.Profile;
import Model.WishlistDTO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import javax.mail.Session;

/**
 *
 * @author tuong
 */
public class courseServlet extends HttpServlet {

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
            out.println("<title>Servlet courseServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet courseServlet at " + request.getContextPath() + "</h1>");
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
        WishlistDAO wishlistDAO = new WishlistDAO();
       
        String cid_str = request.getParameter("cid");
        String accid_str  = request.getParameter("accid");
        if (cid_str != null && !cid_str.isBlank() && accid_str!=null && !accid_str.isBlank()) {
            int cid_int = Integer.parseInt(cid_str);
            int accd_int = Integer.parseInt(accid_str);
            wishlistDAO.insetWishList(accd_int, cid_int);
           
        }
        EnrollmentDAO enrollDAO = new EnrollmentDAO();
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        Profile my_profile = (Profile) session.getAttribute("profile");
        Account my_account = (Account) session.getAttribute("account");
        
        ArrayList<EnrollmentDTO> course_list = enrollDAO.getCourseByAccId(my_account.getAccount_id());
        request.setAttribute("course_list", course_list);
        //out.print(course_list.get(0).getEnrollment_date());
        
         ArrayList<WishlistDTO> wish_list = wishlistDAO.getWishListByAccId(my_account.getAccount_id());
        request.setAttribute("wish_list", wish_list);
         
        request.getRequestDispatcher("MyCourses.jsp").forward(request, response);
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
