package com.airnz.emailtribe.service;

import com.airnz.emailtribe.domain.Email;

import java.util.List;

public interface EmailService {
    public List<Email> getEmails(String emailAddress);

    public Email getEmail(String emailAddress, Long id);

    public void draftEmail(String emailAddress, Email email);
    public void updateDraftEmail (String emailAddress, Email email);

    public void sendEmail(String emailAddress, Email email);
}
