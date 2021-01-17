package guru.sfg.brewery;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.naming.ldap.LdapName;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordEncodingTests {

    static final String PASSWORD = "password";

    @Test
    void testBCrypt() {
        PasswordEncoder bcrypt = new BCryptPasswordEncoder();
        System.out.println("testBCrypt: " + bcrypt.encode("guru"));
        String encodedPwd = bcrypt.encode("guru");
        assertTrue(bcrypt.matches("guru", encodedPwd));
    }

    @Test
    void testSha256() {
        PasswordEncoder sha256 = new StandardPasswordEncoder();
        System.out.println("testSha256: " + sha256.encode(PASSWORD));
        String encodedPwd = sha256.encode(PASSWORD);
        assertTrue(sha256.matches(PASSWORD, encodedPwd));
    }

    @Test
    void testLdap() {
        // PasswordEncoder ldap = new LdapShaPasswordEncoder();
        // System.out.println("testLdap: " + ldap.encode("tiger"));
        PasswordEncoder bcrypt15 = new BCryptPasswordEncoder(15);
        System.out.println("bcrypt15: " + bcrypt15.encode("tiger"));
        String encodedPwd = bcrypt15.encode("tiger");
        assertTrue(bcrypt15.matches("tiger", encodedPwd));
    }
}
