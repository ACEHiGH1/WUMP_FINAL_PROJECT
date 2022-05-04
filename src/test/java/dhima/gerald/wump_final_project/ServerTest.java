package dhima.gerald.wump_final_project;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {
private int encryptionNumber = 5;
private String encryptedPassword = "Gerald";

@org.junit.jupiter.api.Test
    void testDecryptPassword(){
    assertEquals(decryptPassword(encryptedPassword),Server.decryptPassword(encryptedPassword));
}

    public String decryptPassword(String password){ //public only for JUnit test purposes.
        //returns the encrypted password according to a simple algorithm

        String decryptedPassword = "";
        for (int i = 0; i < password.length(); i++){
            decryptedPassword += (char)(password.charAt(i) - encryptionNumber);
        }
        return decryptedPassword;
    }
}