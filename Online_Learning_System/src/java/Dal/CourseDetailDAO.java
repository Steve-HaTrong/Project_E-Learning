/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dal;

import Model.Category;
import Model.Course;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;

/**
 *
 * @author Tuan Anh(Gia Truong)
 */
public class CourseDetailDAO {

    Connection con = null; // Kết nối với sql server
    PreparedStatement ps = null; // Ném câu lệnh query sang sql server
    ResultSet rs = null; // Nhận kết quả trả về

    //======== LOAD DỮ LIỆU TỪ DATABASE ============
    //Lấy ra tất cả 5 khóa học liên quan với khóa học hiện tại trong database  theo category
    public ArrayList<Course> getRelateCourse(int courseId) throws SQLException {
        ArrayList<Course> list = new ArrayList<>();
        String sql = "SELECT TOP 4\n"
                + "    cr.[CourseId],\n"
                + "    cr.[CourseName],\n"
                + "    cr.[Description],\n"
                + "    cr.[Image],\n"
                + "    cr.[Price],\n"
                + "    cr.[CourseCateroryId],  \n"
                + "    cr.[CreatedBy],\n"
                + "    cr.[DateCreated],\n"
                + "    cr.[StudyTime],\n"
                + "    cr.[Status],\n"
                + "    pro.[FullName] AS MentorName  \n"
                + "FROM \n"
                + "    [Course] cr\n"
                + "JOIN \n"
                + "    [Teaching] te ON te.[CourseId] = cr.[CourseId]\n"
                + "JOIN \n"
                + "    [Profile] pro ON pro.[ProfileId] = te.[MentorId]\n"
                + "WHERE \n"
                + "    [CourseCateroryId] = (\n"
                + "        SELECT \n"
                + "            [CourseCateroryId] \n"
                + "        FROM \n"
                + "            [Course] \n"
                + "        WHERE \n"
                + "            [CourseId] = ?\n"
                + "    ) \n"
                + "    AND cr.[CourseId] != ?";
        try {
            con = new DBContext().getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, courseId);
            ps.setInt(2, courseId);

            rs = ps.executeQuery();
            while (rs.next()) {
                int course_id = rs.getInt(1);
                String course_name = rs.getString(2);
                String description = rs.getString(3);
                String image = rs.getString(4);
                int price = rs.getInt(5);
                String cate_id = rs.getString(6);  // Nếu giá trị là "IT", dùng getString để tránh lỗi
                int create_by = rs.getInt(7);
                Date date = rs.getDate(8);
                String studyTime = rs.getString(9);
                int status = rs.getInt(10);
                String instructor = rs.getString(11);
                list.add(new Course(course_id, course_name, description, instructor, image, price, cate_id, create_by, date, studyTime, status));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi");
        }

        return list;
    }

    //Lấy ra tất cả category
    public ArrayList<Category> getCategoryById(int courseId) throws SQLException {
        ArrayList<Category> list = new ArrayList<>();
        String sql = " SELECT  cate.[CourseCateroryId]\n"
                + "        ,[CategoryName]\n"
                + " FROM [dbo].[CourseCategory] cate\n"
                + " Join [dbo].[Course] cr on cr.[CourseCateroryId] = cate.[CourseCateroryId]\n"
                + " Where cr.[CourseId] = ?";
        try {
            con = new DBContext().getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, courseId);
            rs = ps.executeQuery();
            while (rs.next()) {

                String cate_id = rs.getString(1);
                String cate_name = rs.getString(2);

                list.add(new Category(cate_id, cate_name));
            }
        } catch (Exception e) {
            e.printStackTrace();  // In chi tiết lỗi ra console

        }

        return list;
    }

    //Lấy course theo course ID
    public Course getCourseById(int courseId) throws SQLException {
        String sql = "SELECT cr.[CourseId]\n"
                + "      ,[CourseName]\n"
                + "      ,[Description]\n"
                + "      ,[Image]\n"
                + "      ,[Price]\n"
                + "      ,[CourseCateroryId]\n"
                + "      ,[CreatedBy]\n"
                + "      ,[DateCreated]\n"
                + "      ,[StudyTime]\n"
                + "      ,cr.[Status]\n"
                + "	  ,pro.FullName\n"
                + "  FROM [dbo].[Course] cr\n"
                + "  join [dbo].[Teaching] te on te.CourseId = cr.CourseId\n"
                + "  join [dbo].[Profile] pro on pro.ProfileId = te.MentorId\n"
                + "  where cr.[CourseId] = ?";
        try {
            con = new DBContext().getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, courseId);

            rs = ps.executeQuery();
            while (rs.next()) {
                int course_id = rs.getInt(1);
                String course_name = rs.getString(2);
                String description = rs.getString(3);
                String image = rs.getString(4);
                int price = rs.getInt(5);
                String cate_id = rs.getString(6);
                int create_by = rs.getInt(7);
                Date date = rs.getDate(8);
                String studyTime = rs.getString(9);
                int status = rs.getInt(10);
                String instructor = rs.getString(11);

                return new Course(course_id, course_name, description, instructor, image, price, cate_id, create_by, date, studyTime, status);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi");
        }

        return null;
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        CourseDetailDAO dao = new CourseDetailDAO();
        System.out.println(dao.getCourseById(2));
    }

}
