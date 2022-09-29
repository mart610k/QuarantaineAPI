package dk.quarantaine.api.application.data;


import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.Connection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dk.quarantaine.commons.dto.RegisterUserDTO;
import dk.quarantaine.commons.exceptions.ObjectExistsException;
import lombok.extern.java.Log;


@Service
@Log
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

    public boolean registerUser(RegisterUserDTO user) throws ObjectExistsException {
        Connection con;
        
        try{  
            //Class.forName("com.mysql.jdbc.Driver");  
            con=DriverManager.getConnection( 
                String.format("jdbc:mysql://%s:%s/%s", mysqlServerAddress, mysqlServerPort,mysqlDatabaseName),mysqlDatabaseUser,mysqlDatabasePassword);  
            //here sonoo is database name, root is username and password  
            PreparedStatement stmt = con.prepareStatement("INSERT INTO `user`(`username`,`password`,`name`,`phonenumber`) VALUE (?,?,?,?);");
            stmt.setString(1,user.getUsername());
            stmt.setString(2,user.getPassword());
            stmt.setString(3,user.getName());
            stmt.setString(4,user.getPhoneNumber());
            stmt.execute();
            stmt.close();


            
            

        }
        catch(SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException){
            throw new ObjectExistsException("User with the name \""+user.getUsername()+"\" already exists");
        }
        
        
        catch(Exception e){ 
            System.out.println("----------------------------");
            log.warning(e.getLocalizedMessage());
            //log.warning(e.getCause().getClass().getName());
            e.printStackTrace();
            System.out.println("----------------------------");

            return false;
        }
        
        //com.mysql.cj.jdbc.exceptions.SQLError.createSQLException

        return true;
    }
    
}
