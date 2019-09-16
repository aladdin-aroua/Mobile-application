package projet.uqam.mobileproject.Views.authentification;

import android.text.TextUtils;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class RegisteringActivityTest {



    public RegisteringActivity registerActivity =  new RegisteringActivity();



    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertTrue(EmailValidator.isValidEmail("name@email.com"));
    }

    @Test
    public void emailValidator_CorrectEmailSubDomain_ReturnsTrue() {
        assertTrue(EmailValidator.isValidEmail("name@email.co.uk"));
    }

    @Test
    public void emailValidator_InvalidEmailNoTld_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail("name@email"));
    }

    @Test
    public void emailValidator_InvalidEmailDoubleDot_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail("name@email..com"));
    }

    @Test
    public void emailValidator_InvalidEmailNoUsername_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail("@email.com"));
    }

    @Test
    public void emailValidator_EmptyString_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail(""));
    }

    @Test
    public void emailValidator_NullEmail_ReturnsFalse() {
        assertFalse(EmailValidator.isValidEmail(null));
    }

    @Test
    public void isValidPassword_eightcharsTest( ) {
        assertFalse(registerActivity.isValidPassword("T4sq_"));
    }

    @Test
    public void isValidPassword_digitAndUpperCaseTest( ) {
        assertFalse(registerActivity.isValidPassword("gregorymarjames-law"));
    }

    @Test
    public void isValidPassword_NoLowerCaseTest( ) {
        assertFalse(registerActivity.isValidPassword("ABCASWF2!"));
    }

    @Test
    public void isValidPassword_ValidPassowrdTest( ) {
        assertTrue(registerActivity.isValidPassword("J@vaC0deG##ks"));
    }

}