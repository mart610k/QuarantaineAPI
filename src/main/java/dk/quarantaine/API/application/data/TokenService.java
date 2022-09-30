package dk.quarantaine.api.application.data;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dk.quarantaine.commons.dto.OauthTokenResponseDTO;
import dk.quarantaine.commons.exceptions.ObjectExistsException;
import lombok.extern.java.Log;

@Service
@Log
public class TokenService {
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

    @Value("${access.token.validity.seconds:3600}")
    int tokenValiditySeconds;

    public boolean save(OauthTokenResponseDTO oauthTokenResponseDTO,String username) throws ObjectExistsException{
        Connection con;
        
        try{  
            //Class.forName("com.mysql.jdbc.Driver");  

            con=DriverManager.getConnection( 
                String.format("jdbc:mysql://%s:%s/%s", mysqlServerAddress, mysqlServerPort,mysqlDatabaseName),mysqlDatabaseUser,mysqlDatabasePassword);  


                PreparedStatement stmt = con.prepareStatement("INSERT INTO `oauth2token`(`username`, `access_token`, `refresh_token`, `token_type`, `valid_to`) value (?,UUID_TO_BIN(?),UUID_TO_BIN(?),?,NOW() + INTERVAL ? SECOND);");
            stmt.setString(1,username);
            stmt.setString(2,oauthTokenResponseDTO.getAccess_token());
            stmt.setString(3,oauthTokenResponseDTO.getRefresh_token());
            stmt.setString(4,oauthTokenResponseDTO.getToken_type());
            stmt.setInt(5,tokenValiditySeconds);
    
            stmt.execute();
            stmt.close();

        }
        catch(SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException){
            throw new ObjectExistsException("Access Token already exists");
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

    public String getUserByToken(String token){
        return null;
    }
    
}
