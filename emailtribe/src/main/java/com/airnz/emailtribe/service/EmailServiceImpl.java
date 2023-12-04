package com.airnz.emailtribe.service;

import com.airnz.emailtribe.domain.Email;
import com.airnz.emailtribe.exception.BadRequestException;
import com.airnz.emailtribe.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger log = Logger.getLogger(EmailServiceImpl.class.getName());
    private Map<String, List<Email>> emailsForEmailAddress = new HashMap<>();

    public List<Email> getEmails(String emailAddress) {

        if (!IsEmailAddressFormatValid(emailAddress)) {
            System.out.println(IsEmailAddressFormatValid(emailAddress));
            log.info("Email address format is invalid for: " + emailAddress);
            throw new BadRequestException("Email address format is invalid", HttpStatus.BAD_REQUEST);
        }

        List<Email> emails;

        if (emailAddress.equals(getFromAddress())) {
            Map<String, List<Email>> emailsMap = getEmailsForEmailAddress(emailAddress);
            emails = emailsMap.get(emailAddress);
            if (emails.isEmpty()) {
                throw new NotFoundException("No emails found under " + emailAddress, HttpStatus.NOT_FOUND);
            }
        } else {
            throw new NotFoundException("Email address not found " + emailAddress, HttpStatus.NOT_FOUND);
        }
        return emails;
    }

    public Email getEmail(String emailAddress, Long id) {

        if (!IsEmailAddressFormatValid(emailAddress)) {
            log.info("Email address format is invalid for: " + emailAddress);
            throw new BadRequestException("Email address format is invalid", HttpStatus.BAD_REQUEST);
        }

        if (id == null) {
            throw new NotFoundException("Email id cannot be empty", HttpStatus.NOT_FOUND);
        }

        Map<String, List<Email>> emailsForEmailAddress = getEmailsForEmailAddress(emailAddress);
        List<Email> emails = emailsForEmailAddress.get(emailAddress);
        Optional<Email> emailObj = emails.stream().filter(email -> Objects.equals(email.getId(), id))
                .findFirst();
        return emailObj.orElseThrow(() -> new NotFoundException("No email found", HttpStatus.NOT_FOUND));
    }

    public void draftEmail(String emailAddress, Email email) {

        if (!IsEmailAddressFormatValid(emailAddress)) {
            log.info("Email address format is invalid for: " + emailAddress);
            throw new BadRequestException("Email address format is invalid", HttpStatus.BAD_REQUEST);
        }

        if (email.getId() == null || email.getId() == 0) {

            throw new NotFoundException("Email id cannot be empty", HttpStatus.NOT_FOUND);
        }
        log.info("Draft email begins.");
        log.info("email id is " + email.getId());
        Email draft = new Email();
        List<Email> emails = new ArrayList<>();
        List<Email> existingDrafts = new ArrayList<>();

        draft.setId(email.getId());
        if (email.getEmailSubject() != null) {
            draft.setEmailSubject(email.getEmailSubject());
        }

        if (email.getEmailBody() != null) {
            draft.setEmailBody(email.getEmailBody());
        }

        draft.setFromAddress(emailAddress);
        draft.setToAddresses(email.getToAddresses());

        //Check if there are existing draft emails for this email address
        if (emailsForEmailAddress.containsKey(emailAddress)) {
            existingDrafts = emailsForEmailAddress.get(emailAddress);
        }

        //Check if email id already exists in one of the drafts
        if (!existingDrafts.isEmpty()) {
            for (Email e : existingDrafts) {
                if (Objects.equals(e.getId(), email.getId())) {
                    throw new BadRequestException("This email ID already exists. Please use another email id and then save the draft", HttpStatus.BAD_REQUEST);
                }
            }
            //Added the new draft email to the list of old drafts
            emails.add(draft);
        }

        //Add the list of emails to the map with the key as email address
        emailsForEmailAddress.put(emailAddress, emails);
    }

    public void updateDraftEmail(String emailAddress, Email email) {

        if (!IsEmailAddressFormatValid(emailAddress)) {
            log.info("Email address format is invalid for: " + emailAddress);
            throw new BadRequestException("Email address format is invalid", HttpStatus.BAD_REQUEST);
        }

        if (email.getId() == null || email.getId() == 0) {
            throw new NotFoundException("Email id cannot be empty", HttpStatus.NOT_FOUND);
        }
        log.info("Updating draft email begins.");
        //Get the email ID
        Long emailId = email.getId();
        List<Email> emails = new ArrayList<>();
        //Check if there are existing draft emails for this email address
        if (emailsForEmailAddress.containsKey(emailAddress)) {
            emails = emailsForEmailAddress.get(emailAddress);
        }

        //Check if the draft email id matches the one sent in the request
        //If true, get the old draft email and update it with the new content
        //Otherwise return the new draft email
        Email emailToBeUpdated = emails.stream().filter(e -> Objects.equals(e.getId(), emailId))
                .findFirst().orElse(email);

        if (email.getEmailBody() != null) {
            emailToBeUpdated.setEmailBody(email.getEmailBody());
        }

        if (email.getEmailSubject() != null) {
            emailToBeUpdated.setEmailSubject(email.getEmailSubject());
        }

        if (!email.getToAddresses().isEmpty()) {
            emailToBeUpdated.getToAddresses().addAll(email.getToAddresses());
        }

        if (email.getFromAddress() != null) {
            emailToBeUpdated.setFromAddress(email.getFromAddress());
        }

        //This draft email is added to the old list of emails
        emails.add(emailToBeUpdated);
        emailsForEmailAddress.put(emailAddress, emails);
    }

    public void sendEmail(String emailAddress, Email email) {

        if (!IsEmailAddressFormatValid(emailAddress)) {
            log.info("Email address format is invalid for: " + emailAddress);
            throw new BadRequestException("Email address format is invalid", HttpStatus.BAD_REQUEST);
        }

        if (email.getId() == null) {
            throw new NotFoundException("Email id cannot be empty", HttpStatus.NOT_FOUND);
        }
        Email emailToSend = getEmail(email);

        log.info("Email sent with id " + emailToSend.getId() + "from email address " + emailAddress);
        //Remove the draft email from the Hashmap after it is sent
        List<Email> emails = emailsForEmailAddress.get(emailAddress);
        Optional<Email> emailObj = emails.stream().filter(e -> Objects.equals(e.getId(), email.getId()))
                .findFirst();
        if (emailObj.isPresent()) {
            emailsForEmailAddress.remove(emailAddress);
        }
    }

    private static Email getEmail(Email email) {
        Email emailToSend = new Email();
        if (!email.getToAddresses().isEmpty()) {
            emailToSend.setToAddresses(email.getToAddresses());
        } else {
            throw new BadRequestException("Recipient address cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (email.getToAddresses() != null) {
            emailToSend.setFromAddress(email.getFromAddress());
        } else {
            throw new BadRequestException("From email address cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (email.getEmailSubject() != null) {
            emailToSend.setEmailSubject(email.getEmailSubject());
        }

        if (email.getEmailBody() != null) {
            emailToSend.setEmailBody(email.getEmailBody());
        }

        if (email.getId() != null) {
            emailToSend.setId(email.getId());
        } else {
            throw new BadRequestException("Email id cannot be empty", HttpStatus.BAD_REQUEST);
        }
        return emailToSend;
    }

    private Map<String, List<Email>> getEmailsForEmailAddress(String emailAddress) {

        //This will usually be fetched from database but has been hardcoded below
        if (!emailAddress.equals(getFromAddress())) {
            throw new NotFoundException("There are no emails under this address", HttpStatus.NOT_FOUND);
        }
        log.info("All emails fetched under email address: " + emailAddress);
        List<Email> emails = new ArrayList<>();

        Email email1 = new Email();
        email1.setId(1L);
        email1.setEmailSubject("Account Activation");
        email1.setEmailBody("Please activate your account!");
        email1.setFromAddress(getFromAddress());
        email1.setToAddresses(getToAddresses());
        emails.add(email1);

        Email email2 = new Email();
        email2.setId(2L);
        email2.setEmailSubject("Purchase complete");
        email2.setEmailBody("Congratulations on the the purchase!");
        email2.setFromAddress(getFromAddress());
        email2.setToAddresses(getToAddresses());
        emails.add(email2);

        Email email3 = new Email();
        email3.setId(3L);
        email3.setEmailSubject("Customer Feedback");
        email3.setEmailBody("Please leave a feedback!");
        email3.setFromAddress(getFromAddress());
        email3.setToAddresses(getToAddresses());
        emails.add(email3);

        emailsForEmailAddress.put(getFromAddress(), emails);

        return emailsForEmailAddress;
    }

    private List<String> getToAddresses() {

        //This will usually be fetched from database but has been hardcoded below
        return List.of("brucebanner@gmail.com", "tombrody@yahoo.com", "sid3423@gmail.com");
    }

    private String getFromAddress() {
        //This will usually be fetched from database but has been hardcoded below
        return "markbenson@gmail.com";
    }

    private boolean IsEmailAddressFormatValid(String emailAddress) {

    /*
    Email Address part:
    Can contain numeric values from 0 to 9.
    Both uppercase and lowercase letters from a to z are allowed.
    Allowed are underscore “_”, hyphen “-“, and dot “.”
    Dot isn’t allowed at the start and end of the local part.
    Consecutive dots aren’t allowed.
    For the local part, a maximum of 64 characters are allowed.

    Domain name rules:
    Allowed numeric values from 0 to 9.
    Can contain both uppercase and lowercase letters from a to z.
    Hyphen “-” and dot “.” aren’t allowed at the start and end of the domain part.
    No consecutive dots.
    */
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
