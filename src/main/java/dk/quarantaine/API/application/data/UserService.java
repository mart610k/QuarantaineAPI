package dk.quarantaine.api.application.data;


import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.Connection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dk.quarantaine.commons.dto.RegisterUserDTO;


@Service
public class UserService {

    @Value("${mysql.server.address}")
    String mysqlServerAddress;

    @Value("${mysql.server.port}")
    String mysqlServerPort;

    @Value("${mysql.database.name}")
    String mysqlDatabaseName;

    @Value("${mysql.database.user}")
    String mysqlDatabaseUser;

    @Value("${mysql.database.password}")
    String mysqlDatabasePassword;

    public boolean registerUser(RegisterUserDTO user) {
        try{  
            //Class.forName("com.mysql.jdbc.Driver");  
            Connection con=DriverManager.getConnection( 
                String.format("jdbc:mysql://%s:%s/%s", mysqlServerAddress, mysqlServerPort,mysqlDatabaseName),mysqlDatabaseUser,mysqlDatabasePassword);  
            //here sonoo is database name, root is username and password  
            PreparedStatement stmt = con.prepareStatement("INSERT INTO `user`(`username`,`password`,`name`,`phonenumber`) VALUE (?,?,?,?);");
            stmt.setString(1,user.getUsername());
            stmt.setString(2,user.getPassword());
            stmt.setString(3,user.getName());
            stmt.setString(4,user.getPhoneNumber());
            stmt.execute();
            
            
        }catch(Exception e){ 
            return false;
        }  

        return true;
    }
    
}
