package dk.quarantaine.api.application.data;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dk.quarantaine.api.application.helpers.Oauth2Helper;
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

    /**
     * Saves the token in the database
     * @param oauthTokenResponseDTO Token to save
     * @param username user who generated the token
     * @return whether or not it was sucessful
     * @throws ObjectExistsException thrown if the access_token is already existing on the database
     */
    public boolean save(OauthTokenResponseDTO oauthTokenResponseDTO,String username) throws ObjectExistsException{
        Connection con;
        
        try{  
            //Class.forName("com.mysql.jdbc.Driver");  

            con=DriverManager.getConnection( 
                String.format("jdbc:mysql://%s:%s/%s", mysqlServerAddress, mysqlServerPort,mysqlDatabaseName),mysqlDatabaseUser,mysqlDatabasePassword);  


            PreparedStatement stmt = con.prepareStatement("INSERT INTO `oauth2token`(`username`, `access_token`, `refresh_token`, `token_type`, `valid_to`) value (?,?,?,?,NOW() + INTERVAL ? SECOND);");
            stmt.setString(1,username);

            stmt.setBytes(2, Oauth2Helper.convertUUIDToBinary(UUID.fromString(oauthTokenResponseDTO.getAccess_token())));
            stmt.setBytes(3, Oauth2Helper.convertUUIDToBinary(UUID.fromString(oauthTokenResponseDTO.getRefresh_token())));
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

    /**
     * Gets the username by an access token
     * @param accessToken the access token to get for
     * @return The username associated with the token.
     */
    public String getUserByToken(String accessToken){
        return null;
    }
    
}
