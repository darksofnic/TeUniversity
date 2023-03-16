/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package te.university;

/**
 *
 * @author steam
 */
public class courseData {
        
           private String courseID=" ";
           private String courseName=" ";
           private String courseDay = " ";
           private String courseTime = " ";
           private String courseRoom = " ";
           private int courseCredit=0;
            
           public  courseData(String id,String name,String day,String time,String room,int credit){
                setCourseID(id);
                setCourseName(name);
                setCourseDay(day);
                setCourseTime(time);
                setCourseRoom(room);
                setCourseCredit(credit);
            
            }
            
       public void setCourseID(String courseID){
            this.courseID = courseID;
        }
       public void setCourseName(String courseName){
            this.courseName = courseName;
        }
        public void setCourseDay(String courseDay){
            this.courseDay = courseDay;
        }
        public void setCourseTime(String courseTime){
            this.courseTime = courseTime;
        }
        public void setCourseRoom(String courseRoom){
            this.courseRoom = courseRoom;
        }
        public void setCourseCredit(int courseCredit){
            this.courseCredit = courseCredit;
        }
        
        public String getCourseID(){return courseID;}
        public String getCourseName(){return courseName;}
        public String getCourseDay(){return courseDay;}
        public String getCourseTime(){return courseTime;}
        public String getCourseRoom(){return courseRoom;}
        public int getCourseCredit(){return courseCredit;}
}
