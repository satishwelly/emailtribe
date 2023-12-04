package com.airnz.emailtribe.service;

import com.airnz.emailtribe.domain.Email;
import com.airnz.emailtribe.exception.BadRequestException;
import com.airnz.emailtribe.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*
This test class includes tests for two endpoints getAllEmails and getEmailById from EmailServiceImpl
 */

@ExtendWith(MockitoExtension.class)
public class GetEmailTest {
    private String emailAddress;
    private Long id;
    private Email email = new Email();
    private List<String> recipients = new ArrayList<>();
    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setup() {
        emailAddress = "markbenson@gmail.com";
        id = 1L;
        email.setFromAddress(emailAddress);
        email.setEmailSubject("Account Activated");
        email.setEmailBody("Your account has been activated");
        recipients.add("tommy@hotmail.com");
        email.setToAddresses(recipients);
        email.setId(1L);
    }

    @Test
    void getEmails_shouldReturnAllEmails() {

        List<Email> emails = this.emailService.getEmails(emailAddress);
        String emailSubject = "Account Activation";
        String emailBody = "Please activate your account!";

        assertEquals(3, emails.size());
        assertEquals(1L, emails.get(0).getId());
        assertEquals(emailSubject, emails.get(0).getEmailSubject());
        assertEquals(emailBody, emails.get(0).getEmailBody());

    }

    @Test
    void getEmail_shouldReturnEmail() {

        String emailSubject = "Account Activation";
        String emailBody = "Please activate your account!";

        Email email = this.emailService.getEmail(emailAddress, id);
        int numberOfToAddresses = email.getToAddresses().size();
        assertEquals(1L, email.getId());
        assertEquals(emailAddress, email.getFromAddress());
        assertEquals(emailSubject, email.getEmailSubject());
        assertEquals(emailBody, email.getEmailBody());
        assertEquals(3, numberOfToAddresses);
        assertEquals("brucebanner@gmail.com", email.getToAddresses().get(0));
        assertEquals("tombrody@yahoo.com", email.getToAddresses().get(1));
        assertEquals("sid3423@gmail.com", email.getToAddresses().get(2));
    }

    @Test()
    void getEmails_EmptyEmailAddress_EmailsNotFound() {

        assertThrows(BadRequestException.class, () -> emailService.getEmails(""));
    }

    @Test()
    void getEmails_InvalidEmailAddress_Error400() {

        assertThrows(BadRequestException.class, () -> emailService.getEmails("eesddsa"));
    }

    @Test()
    void getEmail_EmptyEmailAddress_EmailNotFound() {

        assertThrows(BadRequestException.class, () -> emailService.getEmail("", id));
    }

    @Test()
    void getEmail_EmptyEmailId_EmailNotFound() {

        assertThrows(NotFoundException.class, () -> emailService.getEmail(emailAddress, null));
    }

    //This test accepts a valid email address and email id but no emails exist for this address yet
    @Test()
    void getEmail_ValidEmailAddressAndEMailIdButNoEmailExists_EmailNotFound() {

        assertThrows(NotFoundException.class, () -> emailService.getEmail("markben@gmail.com", id));
    }
}
