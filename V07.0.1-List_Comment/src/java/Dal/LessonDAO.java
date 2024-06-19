/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Model.Course;
import Model.Enrollment;
import Model.Lesson;
import Model.Payment;
import Model.Module;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Tuan Anh(Gia Truong)
 */
public class LessonDAO {
    
    Connection con = null; // Kết nối với sql server
    PreparedStatement ps = null; // Ném câu lệnh query sang sql server
    ResultSet rs = null; // Nhận kết quả trả về

    // Chèn bill lên db
    public void insertBillPayment(Payment payment) {
        
        String sql = "insert into Payment\n"
                + "values (?,?,?,?,?)";
        try {
            con = new DBContext().getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, payment.getAccountid());
            ps.setInt(2, payment.getCourseid());
            ps.setDate(3, payment.getPaymentDate());
            ps.setString(4, payment.getPaymentmethodid());
            ps.setInt(5, payment.getAmount());
            ps.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();  // In chi tiết lỗi ra console

        }
        
    }

    //khi thanh toán xong thì phải insert thông tin vào enrollment
    public void insertEnrollment(Enrollment enrollment) {
        
        String sql = "insert into Enrollment\n"
                + "values (?,?,?,?)";
        try {
            con = new DBContext().getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, enrollment.getAccountid());
            ps.setInt(2, enrollment.getCourseid());
            ps.setDate(3, enrollment.getEnrollmentdate());
            ps.setInt(4, enrollment.getProgress());
            
            ps.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();  // In chi tiết lỗi ra console

        }
        
    }

    //Lấy course theo course ID
    public ArrayList<Lesson> getListModulByCidd(int courseId) throws SQLException {
        ArrayList<Lesson> list = new ArrayList<>();
        String sql = "SELECT\n"
                + "    l.LessonId,\n"
                + "    m.ModuleName,\n"
                + "    l.LessonName,\n"
                + "    l.LessonContent,\n"
                + "    l.LessonVideo,\n"
                + "    c.CourseName,\n"
                + "    p.FullName,\n"
                + "    p.Avatar,\n"
                + "	c.CourseId\n"
                + "FROM [dbo].[Lesson] l\n"
                + "JOIN [dbo].[Module] m ON l.ModuleId = m.ModuleId\n"
                + "JOIN [dbo].[Course] c ON c.CourseId = m.CourseId\n"
                + "JOIN [dbo].[Teaching] teach ON teach.CourseId = c.CourseId\n"
                + "JOIN [dbo].[Profile] p ON p.ProfileId = teach.MentorId\n"
                + "WHERE c.CourseId = ?";
        try {
            con = new DBContext().getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, courseId);
            
            rs = ps.executeQuery();
            while (rs.next()) {
                int lesson_id = rs.getInt(1);
                String modulname = rs.getString(2);
                String lesson_name = rs.getString(3);
                String lesson_content = rs.getString(4);
                String lesson_video = rs.getString(5);
                String course_name = rs.getString(6);
                String mentor_name = rs.getString(7);
                String Avatar = rs.getString(8);
                int course_id = rs.getInt(9);
                list.add(new Lesson(lesson_id, modulname, lesson_name, lesson_content, lesson_video, course_name, mentor_name, Avatar, course_id));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi");
        }
        
        return list;
    }
    
    public Lesson getlessonByCid(int courseId, int lessonid) throws SQLException {
        
        String sql = "SELECT\n"
                + "    l.LessonId,\n"
                + "    m.ModuleName,\n"
                + "    l.LessonName,\n"
                + "    l.LessonContent,\n"
                + "    l.LessonVideo,\n"
                + "    c.CourseName,\n"
                + "    p.FullName,\n"
                + "    p.Avatar,\n"
                + "	c.CourseId\n"
                + "FROM [dbo].[Lesson] l\n"
                + "JOIN [dbo].[Module] m ON l.ModuleId = m.ModuleId\n"
                + "JOIN [dbo].[Course] c ON c.CourseId = m.CourseId\n"
                + "JOIN [dbo].[Teaching] teach ON teach.CourseId = c.CourseId\n"
                + "JOIN [dbo].[Profile] p ON p.ProfileId = teach.MentorId\n"
                + "WHERE c.CourseId = ? and l.LessonId = ?";
        try {
            con = new DBContext().getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, courseId);
            ps.setInt(2, lessonid);
            
            rs = ps.executeQuery();
            while (rs.next()) {
                int lesson_id = rs.getInt(1);
                String modulname = rs.getString(2);
                String lesson_name = rs.getString(3);
                String lesson_content = rs.getString(4);
                String lesson_video = rs.getString(5);
                String course_name = rs.getString(6);
                String mentor_name = rs.getString(7);
                String Avatar = rs.getString(8);
                int course_id = rs.getInt(9);
                
                return new Lesson(lesson_id, modulname, lesson_name, lesson_content, lesson_video, course_name, mentor_name, Avatar, course_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi");
        }
        
        return null;
    }

    //Lấy course theo course ID
    public ArrayList<Module> getListModulByCid(int courseId) throws SQLException {
        ArrayList<Module> list = new ArrayList<>();
        String sql = "SELECT  [ModuleId]\n"
                + "      ,[ModuleName]\n"
                + "      ,[CourseId]\n"
                + "  FROM [dbo].[Module] \n"
                + "WHERE CourseId = ?";
        try {
            con = new DBContext().getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, courseId);
            
            rs = ps.executeQuery();
            while (rs.next()) {
                int moduleid = rs.getInt(1);
                String modulename = rs.getString(2);
                int course_id = rs.getInt(3);
                list.add(new Module(moduleid, modulename, courseId));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi");
        }
        
        return list;
    }
    
    
        //Lấy ra list enrollment để kiểm tra người dùng đã mua khóa học này hay chưa
    public ArrayList<Enrollment> getEnrollmentByAccountId(int courseId) throws SQLException {
        ArrayList<Enrollment> list = new ArrayList<>();
        String sql = " SELECT [EnrollmentId]\n"
                + "      ,[AccountId]\n"
                + "      ,[CourseId]\n"
                + "      ,[EnrollmentDate]\n"
                + "      ,[Progress]\n"
                + "  FROM [dbo].[Enrollment]\n"
                + "  WHERE [AccountId] = ?";
        try {
            con = new DBContext().getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, courseId);
            rs = ps.executeQuery();
            while (rs.next()) {
                
                int enrollId = rs.getInt(1);
                int accId = rs.getInt(2);
                int courseid = rs.getInt(3);
                Date enrollmentDate = rs.getDate(4);
                int progress = rs.getInt(5);
                
                list.add(new Enrollment(enrollId, accId, courseid, enrollmentDate, progress));
            }
        } catch (Exception e) {
            e.printStackTrace();  // In chi tiết lỗi ra console

        }
        
        return list;
    }
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        LessonDAO dao = new LessonDAO();
        System.out.println(dao.getlessonByCid(2,2));
    }
}
