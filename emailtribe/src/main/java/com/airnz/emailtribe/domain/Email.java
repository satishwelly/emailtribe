package com.airnz.emailtribe.domain;

import java.util.List;

public class Email {
    private Long id;
    private String fromAddress;
    private List<String> toAddresses;
    private String emailBody;
    private String emailSubject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public List<String> getToAddresses() {
        return toAddresses;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public void setToAddresses(List<String> toAddresses) {
        this.toAddresses = toAddresses;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    @Override
    public String toString() {
        return "Email{" +
                "id=" + id +
                ", fromAddress='" + fromAddress + '\'' +
                ", toAddresses=" + toAddresses +
                ", emailBody='" + emailBody + '\'' +
                ", emailSubject='" + emailSubject + '\'' +
                '}';
    }
}
