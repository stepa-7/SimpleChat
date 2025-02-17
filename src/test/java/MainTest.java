import com.example.database.DatabaseUtil;
import com.example.mysimplechat.chat.*;

import com.example.security.SecurityUtil;
import org.testng.annotations.Test;
import org.testng.Assert;


public class MainTest {
    public static void main(String[] args) {

    }

    @Test
    public void testConnection() throws Exception {
        DatabaseUtil.getConnection();
        Assert.assertTrue(true);
    }

    @Test
    public void testSaveUser() throws Exception {
        DatabaseUtil.saveUser("test", "test", "test");
        Assert.assertTrue(true);
    }

    @Test
    public void testHashPassword() {
        String password = "test";
        String hashedPassword = SecurityUtil.hashPassword(password);
        Assert.assertTrue(SecurityUtil.checkPassword(password, hashedPassword));
    }

    @Test
    public void testHashPassword2() {
        String password = "test1";
        String hashedPassword = SecurityUtil.hashPassword("test");
        Assert.assertFalse(SecurityUtil.checkPassword(password, hashedPassword));
    }
}
