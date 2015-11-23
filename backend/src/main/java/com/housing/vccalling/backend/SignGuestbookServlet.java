package com.housing.vccalling.backend;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Key;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;
import com.google.gson.Gson;

import sun.rmi.runtime.Log;


public class SignGuestbookServlet extends HttpServlet {

    // Process the http POST of the form
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Greeting greeting;
        String characters;
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();  // Find out who the user is.

        String guestbookName = req.getParameter("guestbookName");
        String content = req.getParameter("content");
        if (user != null) {
            greeting = new Greeting(guestbookName, content, user.getNickname(), user.getEmail());
        } else {
            greeting = new Greeting(guestbookName, content);
        }


        // Use Objectify to save the greeting and now() is used to make the call synchronously as we
        // will immediately get a new page using redirect and we want the data to be present.
        ObjectifyService.ofy().save().entity(greeting).now();

        characters = req.getParameter("Characters");
         characters = characters.substring(1,characters.length()-1);
        String[] split = characters.split(",");
        Votes voting_entity = new Votes(greeting,split);

        ObjectifyService.ofy().save().entity(voting_entity).now();

     resp.setContentType("text/plain");
       resp.getWriter().println("Data Registered");
        //resp.sendRedirect("/guestbook.jsp?guestbookName=" + guestbookName);
    }


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        List<Greeting> greetings;

        try {
            String guestbookName = req.getParameter("guestbookName");
            Key<Guestbook> theBook = Key.create(Guestbook.class, guestbookName);
            greetings = ObjectifyService.ofy()
                    .load()
                    .type(Greeting.class) // We want only Greetings
                    .ancestor(theBook)    // Anyone in this book
                            // .order("-date")       // Most recent first - date is indexed.
                    .limit(5)             // Only show 5 of them.
                    .list();
        }
        catch (Exception e)
        {
            greetings = ObjectifyService.ofy()
                    .load()
                    .type(Greeting.class) // We want only Greetings
                            //.ancestor(theBook)    // Anyone in this book
                            // .order("-date")       // Most recent first - date is indexed.
                    .limit(5)             // Only show 5 of them.
                    .list();
        }

        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.


        Gson gson = new Gson();
        // JSONObject obj = new JSONObject();
        Map<String, String> map = new HashMap<String, String>();

        if (greetings.isEmpty()) {
            map.put("Null","No");


        }
        else
        {

            //obj.put(guestbookName, greetings.get(0));
            for(Greeting greet:greetings)
            {
                //    System.out.println(greet.content);
                //   System.out.println(greet.id);

                map.put(greet.date.toString(),greet.content);

            }
        }
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();
        String json = gson.toJson(map);

        out.print(json);
        out.flush();

    }
}