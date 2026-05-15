package BadmintonCourtBooking_Project;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

/*
=============================================
BADMINTON COURT BOOKING SYSTEM
=============================================

CREATE DATABASE badmintondb;
USE badmintondb;

CREATE TABLE courts(
court_no INT PRIMARY KEY,
hourly_rate DOUBLE,
status VARCHAR(20)
);

CREATE TABLE bookings(
id INT PRIMARY KEY AUTO_INCREMENT,
customer_name VARCHAR(100),
court_no INT,
hours INT,
bill DOUBLE
);

INSERT INTO courts VALUES
(1,300,'Available'),
(2,300,'Available'),
(3,400,'Available'),
(4,400,'Available'),
(5,500,'Available');
*/

class DBConnection {

public static Connection getConnection(){
Connection con=null;
try{
Class.forName("com.mysql.cj.jdbc.Driver");
con=DriverManager.getConnection(
"jdbc:mysql://localhost:3306/badmintondb",
"root",
"your_password"
);
}
catch(Exception e){
JOptionPane.showMessageDialog(null,e);
}
return con;
}

}


public class BadmintonCourtBooking_Project extends JFrame implements ActionListener{

JButton bookBtn,viewBtn,cancelBtn,statusBtn,revenueBtn;

BadmintonCourtBooking_Project(){

setTitle("Badminton Court Booking System");

JLabel title=new JLabel("Badminton Court Booking System");
title.setBounds(70,30,260,30);

bookBtn=new JButton("Book Court");
viewBtn=new JButton("View Bookings");
cancelBtn=new JButton("Cancel Booking");
statusBtn=new JButton("Court Status");
revenueBtn=new JButton("Revenue Report");

bookBtn.setBounds(100,80,180,35);
viewBtn.setBounds(100,130,180,35);
cancelBtn.setBounds(100,180,180,35);
statusBtn.setBounds(100,230,180,35);
revenueBtn.setBounds(100,280,180,35);

bookBtn.addActionListener(this);
viewBtn.addActionListener(this);
cancelBtn.addActionListener(this);
statusBtn.addActionListener(this);
revenueBtn.addActionListener(this);

add(title);
add(bookBtn);
add(viewBtn);
add(cancelBtn);
add(statusBtn);
add(revenueBtn);

setSize(400,430);
setLayout(null);
setVisible(true);
setLocationRelativeTo(null);
setDefaultCloseOperation(EXIT_ON_CLOSE);
}

public void actionPerformed(ActionEvent e){

if(e.getSource()==bookBtn)
new BookCourt();

if(e.getSource()==viewBtn)
new ViewBookings();

if(e.getSource()==cancelBtn)
new CancelBooking();

if(e.getSource()==statusBtn)
new CourtStatus();

if(e.getSource()==revenueBtn)
new RevenueReport();
}

public static void main(String args[]){
new BadmintonCourtBooking_Project();
}

}


class BookCourt extends JFrame implements ActionListener{

JTextField nameField,hoursField;
JComboBox courtBox;
JButton bookBtn;

BookCourt(){

setTitle("Book Court");

JLabel l1=new JLabel("Customer Name:");
JLabel l2=new JLabel("Court Number:");
JLabel l3=new JLabel("Hours:");

nameField=new JTextField();
hoursField=new JTextField();

courtBox=new JComboBox(
new String[]{"1","2","3","4","5"}
);

bookBtn=new JButton("Book");
bookBtn.addActionListener(this);

l1.setBounds(50,50,100,30);
nameField.setBounds(160,50,150,30);

l2.setBounds(50,110,100,30);
courtBox.setBounds(160,110,150,30);

l3.setBounds(50,170,100,30);
hoursField.setBounds(160,170,150,30);

bookBtn.setBounds(130,250,100,40);

add(l1); add(nameField);
add(l2); add(courtBox);
add(l3); add(hoursField);
add(bookBtn);

setSize(420,380);
setLayout(null);
setVisible(true);
setLocationRelativeTo(null);
}

public void actionPerformed(ActionEvent e){

try{
Connection con=DBConnection.getConnection();

int court=Integer.parseInt(
courtBox.getSelectedItem().toString());

PreparedStatement chk=
con.prepareStatement(
"select status,hourly_rate from courts where court_no=?"
);
chk.setInt(1,court);
ResultSet rs=chk.executeQuery();
rs.next();

if(rs.getString(1).equals("Booked")){
JOptionPane.showMessageDialog(this,"Court Already Booked");
return;
}

double rate=rs.getDouble(2);
int hours=Integer.parseInt(hoursField.getText());
double bill=rate*hours;

PreparedStatement ps=
con.prepareStatement(
"insert into bookings(customer_name,court_no,hours,bill) values(?,?,?,?)"
);

ps.setString(1,nameField.getText());
ps.setInt(2,court);
ps.setInt(3,hours);
ps.setDouble(4,bill);
ps.executeUpdate();

PreparedStatement up=
con.prepareStatement(
"update courts set status='Booked' where court_no=?"
);
up.setInt(1,court);
up.executeUpdate();

JOptionPane.showMessageDialog(
this,
"Booking Confirmed\nBill = "+bill
);

con.close();
}
catch(Exception ex){
JOptionPane.showMessageDialog(this,ex);
}

}

}


class ViewBookings extends JFrame{

JTextArea area;

ViewBookings(){
setTitle("View Bookings");

area=new JTextArea();
area.setEditable(false);

JScrollPane sp=new JScrollPane(area);
sp.setBounds(20,20,450,300);
add(sp);

loadBookings();

setSize(520,400);
setLayout(null);
setVisible(true);
setLocationRelativeTo(null);
}

void loadBookings(){
try{
Connection con=DBConnection.getConnection();
Statement st=con.createStatement();
ResultSet rs=st.executeQuery(
"select * from bookings"
);

area.append("ID\tName\tCourt\tHours\tBill\n");
area.append("----------------------------------\n");

while(rs.next()){
area.append(
rs.getInt(1)+"\t"+
rs.getString(2)+"\t"+
rs.getInt(3)+"\t"+
rs.getInt(4)+"\t"+
rs.getDouble(5)+"\n"
);
}
con.close();
}
catch(Exception e){
JOptionPane.showMessageDialog(this,e);
}
}

}


class CancelBooking extends JFrame implements ActionListener{

JTextField courtField;
JButton cancelBtn;

CancelBooking(){
setTitle("Cancel Booking");

JLabel l1=new JLabel("Court No:");
l1.setBounds(50,70,100,30);

courtField=new JTextField();
courtField.setBounds(150,70,150,30);

cancelBtn=new JButton("Cancel");
cancelBtn.setBounds(120,170,100,40);
cancelBtn.addActionListener(this);

add(l1);
add(courtField);
add(cancelBtn);

setSize(400,300);
setLayout(null);
setVisible(true);
setLocationRelativeTo(null);
}

public void actionPerformed(ActionEvent e){
try{
Connection con=DBConnection.getConnection();

int court=Integer.parseInt(courtField.getText());

PreparedStatement p=
con.prepareStatement(
"delete from bookings where court_no=?"
);
p.setInt(1,court);
int result=p.executeUpdate();

PreparedStatement p2=
con.prepareStatement(
"update courts set status='Available' where court_no=?"
);
p2.setInt(1,court);
p2.executeUpdate();

JOptionPane.showMessageDialog(this,
result+" Booking Cancelled");

con.close();
}
catch(Exception ex){
JOptionPane.showMessageDialog(this,ex);
}
}

}


class CourtStatus extends JFrame{

JTextArea area;

CourtStatus(){
setTitle("Court Status");

area=new JTextArea();
JScrollPane sp=new JScrollPane(area);
sp.setBounds(20,20,430,300);
add(sp);

loadCourts();

setSize(500,400);
setLayout(null);
setVisible(true);
setLocationRelativeTo(null);
}

void loadCourts(){
try{
Connection con=DBConnection.getConnection();
Statement st=con.createStatement();
ResultSet rs=st.executeQuery("select * from courts");

while(rs.next()){
area.append(
"Court: "+rs.getInt(1)+
"  Rate: "+rs.getDouble(2)+
"  Status: "+rs.getString(3)+"\n"
);
}

con.close();
}
catch(Exception e){
JOptionPane.showMessageDialog(this,e);
}
}

}


class RevenueReport extends JFrame{

RevenueReport(){
setTitle("Revenue Report");

JTextArea area=new JTextArea();
area.setBounds(30,30,300,150);
add(area);

try{
Connection con=DBConnection.getConnection();
Statement st=con.createStatement();
ResultSet rs=st.executeQuery(
"select sum(bill) from bookings"
);

if(rs.next()){
area.append(
"Total Revenue = "+rs.getDouble(1)
);
}
con.close();
}
catch(Exception e){
area.setText(e.toString());
}

setSize(400,280);
setLayout(null);
setVisible(true);
setLocationRelativeTo(null);
}

}
