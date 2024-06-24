/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Dal.MessengerDAO;
import Model.Account;
import Model.Messenger;
import Model.Profile;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
@WebServlet(name = "MessengerSeverlet", urlPatterns = {"/messenger"})
public class MessengerSeverlet extends HttpServlet {

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
            out.println("<title>Servlet MessengerSeverlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MessengerSeverlet at " + request.getContextPath() + "</h1>");
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
        MessengerDAO messDAO = new MessengerDAO();
        int receiver_id = Integer.parseInt(request.getParameter("receiver_id"));
        int sender_id = Integer.parseInt(request.getParameter("sender_id"));
        Account account_exist_session = (Account) session.getAttribute("account");
//        if (account_exist_session == null ) {
//            response.sendRedirect("join?action=login");
//            return;
//        }
        
        try {   
            
            Profile listProfile = messDAO.getProfileById(receiver_id);
            ArrayList<Messenger> listMessages = messDAO.getListMessengerBetween2User(sender_id, receiver_id);
            ArrayList<Messenger> listUser = messDAO.getUsersWhoMessaged(sender_id);
            request.setAttribute("listUser", listUser);
            request.setAttribute("listMessages", listMessages);
            request.setAttribute("listProfile", listProfile);
        } catch (SQLException ex) {
            Logger.getLogger(Messenger.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect("errorPage");
        }
        request.getRequestDispatcher("Messenger.jsp").forward(request, response);
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

    HttpSession session = request.getSession();
    Account accountInSession = (Account) session.getAttribute("account");

    // Kiểm tra nếu account không tồn tại trong session, chuyển hướng người dùng đi đăng nhập.
    if (accountInSession == null) {
       response.sendRedirect("join?action=login");
        return;
    }
    
    try {
        String receiverIdParam = request.getParameter("receiver_id");
        String messageText = request.getParameter("message_text");
        // Kiểm tra dữ liệu đầu vào
        if (receiverIdParam == null || receiverIdParam.isEmpty() || messageText == null || messageText.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Message text or receiver ID is missing.");
            request.getRequestDispatcher("/A.jsp").forward(request, response);
            return;
        }
        
        int receiver_id = Integer.parseInt(receiverIdParam);
        int sender_id = accountInSession.getAccount_id();

        // Tạo và gửi tin nhắn
        Messenger message = new Messenger(sender_id, receiver_id, messageText);
        MessengerDAO messageDAO = new MessengerDAO();
        messageDAO.insertNewMessenger(message);

         
        response.sendRedirect("messenger?sender_id=" + sender_id + "&receiver_id=" + receiver_id);          
    } catch (NumberFormatException e) {
        // Log lỗi và hiển thị trang lỗi
        request.setAttribute("errorMessage", "Invalid receiver ID.");
        request.getRequestDispatcher("/B.jsp").forward(request, response);
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

}
