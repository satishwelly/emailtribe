package com.airnz.emailtribe.controller;

import com.airnz.emailtribe.domain.Email;
import com.airnz.emailtribe.exception.BadRequestException;
import com.airnz.emailtribe.exception.NotFoundException;
import com.airnz.emailtribe.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "api/v1")
public class EmailController {
    private static final Logger log = Logger.getLogger(EmailController.class.getName());

    @Autowired
    private EmailService emailService;

    @GetMapping(value = "/emails/{emailAddress}")
    public ResponseEntity<?> getAllEmails(@PathVariable("emailAddress") String emailAddress) {
        log.info("Executing getAllEmails method......");
        List<Email> emails = null;
        if ((emailAddress != null)) {
            log.info("Fetching emails for the id: " + emailAddress);
            emails = emailService.getEmails(emailAddress);
            if (emails.isEmpty()) {
                throw new NotFoundException("No emails were found!", HttpStatus.NOT_FOUND);
            }
        } else {
            throw new BadRequestException("Email address cannot be empty", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(emails);
    }

    @GetMapping(value = "/emails/{emailAddress}/{id}")
    public ResponseEntity<?> getEmailById(@PathVariable("emailAddress") String emailAddress, @PathVariable("id") Long id) {
        log.info("Executing getEmailById method......");
        Email email = null;
        if ((emailAddress != null) || (id != null)) {
            log.info("Fetching email with id " + id + "for the email address: " + emailAddress);
            email = emailService.getEmail(emailAddress, id);
        } else {
            throw new NotFoundException("Email Address or Email Id was not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(email);
    }

    @PostMapping(value = "/emails/{emailAddress}/draft")
    public ResponseEntity<?> draftEmail(@PathVariable("emailAddress") String emailAddress, @RequestBody Email emailContent) {
        log.info("Executing draftEmail method......");
        if ((emailAddress != null) || (emailContent != null)) {
            log.info("Drafting email from email address " + emailAddress);
            emailService.draftEmail(emailAddress, emailContent);
        } else {
            throw new NotFoundException("Email Address or Email content was not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok("Email drafted from " + emailAddress + " and saved");
    }

    @PatchMapping(value = "emails/{emailAddress}/draft/update")
    public ResponseEntity<?> updateDraftEmail(@PathVariable("emailAddress") String emailAddress, @RequestBody Email emailContent) {
        log.info("Executing updateDraftEmail method......");
        if ((emailAddress != null) || (emailContent != null)) {
            log.info("Updating draft email from email address " + emailAddress);
            emailService.updateDraftEmail(emailAddress, emailContent);
        } else {
            throw new NotFoundException("Email Address or Email content was not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok("Draft email updated from " + emailAddress + " and saved");
    }

    @PostMapping(value = "emails/{emailAddress}/send")
    public ResponseEntity<?> sendEmail(@PathVariable("emailAddress") String emailAddress, @RequestBody Email emailContent) {
        log.info("Executing sendEmail method......");
        if ((emailAddress != null) || (emailContent != null)) {
            log.info("Sending email from email address " + emailAddress);
            emailService.sendEmail(emailAddress, emailContent);
        } else {
            throw new NotFoundException("Email Address or Email content was not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok("Email sent from " + emailAddress);
    }
}