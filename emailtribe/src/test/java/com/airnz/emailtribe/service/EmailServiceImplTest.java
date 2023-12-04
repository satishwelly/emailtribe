package com.airnz.emailtribe.service;

import com.airnz.emailtribe.domain.Email;
import com.airnz.emailtribe.exception.BadRequestException;
import com.airnz.emailtribe.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {
    private String emailAddress;
    private Email email = new Email();
    private List<String> recipients = new ArrayList<>();
    @Mock
    private Map<String, List<Email>> mockEmailsForEmailAddress = new HashMap<>();
    @InjectMocks
    private EmailServiceImpl emailService;

    /*
    This test class includes tests for three endpoints draftEmail, updateDraftEmail and sendEmail from EmailServiceImpl
     */

    @BeforeEach
    void setup() {
        emailAddress = "markbenson@gmail.com";
        email.setFromAddress(emailAddress);
        email.setEmailSubject("Account Activated");
        email.setEmailBody("Your account has been activated");
        recipients.add("tommy@hotmail.com");
        email.setToAddresses(recipients);
        email.setId(1L);
    }

    @Test
    void draftEmail_shouldSaveDraftEmail() {

        List<Email> mockedList = mock(List.class);
        mockedList.add(email);
        this.emailService.draftEmail(emailAddress, email);
        //This verifies that the draft email has been saved to the emailsForEmailAddress map in EmailServiceImpl
        verify(mockedList).add(email);
    }

    @Test
    void updateDraftEmail_shouldUpdateDraftEmail() {

        List<Email> mockedList = mock(List.class);
        mockedList.add(email);
        this.emailService.updateDraftEmail(emailAddress, email);
//        This verifies that the draft email has been edited and updated
//        in the emailsForEmailAddress map in EmailServiceImpl
        verify(mockedList).add(email);
    }

    @Test
    void sendEmail_shouldSendEmail() {

        List<Email> emails = new ArrayList<>();
        List<String> toAddresses = List.of("brucebanner@gmail.com", "tombrody@yahoo.com", "sid3423@gmail.com");

        Email email1 = new Email();
        email1.setId(1L);
        email1.setEmailSubject("Account Activation");
        email1.setEmailBody("Please activate your account!");
        email1.setFromAddress(emailAddress);
        email1.setToAddresses(toAddresses);
        emails.add(email1);

        Email email2 = new Email();
        email2.setId(2L);
        email2.setEmailSubject("Purchase complete");
        email2.setEmailBody("Congratulations on the the purchase!");
        email2.setFromAddress(emailAddress);
        email2.setToAddresses(toAddresses);
        emails.add(email2);

        Email email3 = new Email();
        email3.setId(3L);
        email3.setEmailSubject("Customer Feedback");
        email3.setEmailBody("Please leave a feedback!");
        email3.setFromAddress(emailAddress);
        email3.setToAddresses(toAddresses);
        emails.add(email3);

        when(mockEmailsForEmailAddress.get(emailAddress)).thenReturn(emails);

        this.emailService.sendEmail(emailAddress, email);
//        This verifies that the email has been sent and then removed
//        from the emailsForEmailAddress map in EmailServiceImpl
        verify(mockEmailsForEmailAddress).get(emailAddress);
        verify(mockEmailsForEmailAddress).remove(emailAddress);
    }

    @Test()
    void draftEmail_InvalidEmailAddress_Error400() {

        assertThrows(BadRequestException.class, () -> emailService.draftEmail("eesddsa", email));
    }

    @Test()
    void draftEmail_EmailIdMissingInDraft_Error400() {

        Email mockEmail = mock(Email.class);
        mockEmail.setEmailSubject("Hi");
        mockEmail.setEmailBody("How are you?");
        assertThrows(NotFoundException.class, () -> emailService.draftEmail(emailAddress, mockEmail));
    }

    @Test()
    void sendEmail_EmailAddressInvalid_Error400() {

        assertThrows(BadRequestException.class, () -> emailService.sendEmail("eesddsa", email));
    }

    @Test()
    void sendEmail_EmailIdMissing_Error400() {

        Email mockEmail = mock(Email.class);
        mockEmail.setEmailSubject("Hi");
        mockEmail.setEmailBody("How are you?");
        assertThrows(BadRequestException.class, () -> emailService.sendEmail(emailAddress, mockEmail));
    }

    @Test()
    void updateDraft_EmailAddressInvalid_Error400() {

        assertThrows(BadRequestException.class, () -> emailService.updateDraftEmail("eesddsa", email));
    }

    @Test()
    void updateDraft_EmailIdMissing_Error400() {

        Email mockEmail = mock(Email.class);
        mockEmail.setEmailSubject("Hi");
        mockEmail.setEmailBody("How are you?");
        assertThrows(NotFoundException.class, () -> emailService.updateDraftEmail(emailAddress, mockEmail));
    }
}