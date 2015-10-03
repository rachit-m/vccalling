package com.housing.vccalling.backend;

import java.io.IOException;

import javax.servlet.http.*;



import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;



public class MyServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Hello Working");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String name = req.getParameter("name");
        resp.setContentType("text/plain");
        if (name == null) {
            resp.getWriter().println("Please enter a name");
        }


        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = "Hello" + name;

        try {
            resp.getWriter().println("Hello World");

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("rachit.iitkgp@gmail.com", "VCCalling Admin"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("rachit.iitkgp@gmail.com", "Mr. Developer"));
            msg.setSubject("Please verify that your phone number is");
            msg.setText(msgBody);
            Transport.send(msg);

        } catch (AddressException e) {
            // ...
        } catch (MessagingException e) {
            // ...
        }

        resp.getWriter().println("Hello " + name);
    }
}
