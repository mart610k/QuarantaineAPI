package dk.quarantaine.api.application.data;

import java.sql.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TestService{
    

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
    
    public boolean TestConnection(){
        try{  
            //Class.forName("com.mysql.jdbc.Driver");  
            Connection con=DriverManager.getConnection( 
                String.format("jdbc:mysql://%s:%s/%s", mysqlServerAddress, mysqlServerPort,mysqlDatabaseName),mysqlDatabaseUser,mysqlDatabasePassword);  
            //here sonoo is database name, root is username and password  
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("select VERSION();");  
            while(rs.next())  
            System.out.println(rs.getString(1));
            con.close();  
        }catch(Exception e){ 
            System.out.println(e);
        }  

        return true;
    }  
    
public TestService() {
    super();
}


}
