package com.housing.vccalling.backend;


import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.Key;

import java.lang.String;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The @Entity tells Objectify about our entity.  We also register it in {@link OfyHelper}
 * Our primary key @Id is set automatically by the Google Datastore for us.
 *
 * We add a @Parent to tell the object about its ancestor. We are doing this to support many
 * guestbooks.  Objectify, unlike the AppEngine library requires that you specify the fields you
 * want to index using @Index.  Only indexing the fields you need can lead to substantial gains in
 * performance -- though if not indexing your data from the start will require indexing it later.
 *
 * NOTE - all the properties are PUBLIC so that can keep the code simple.
 **/
@Entity
public class Votes {

  //  public Map<String, Integer> char_votes= new HashMap<> ();
    public List<String> characters = new ArrayList<>();
    public List<Integer> votes = new ArrayList<>();
    @Id public Long id;
    @Parent Key parentkey;
    Greeting specificGreeting;

    public Votes(Greeting greeting, String array[]) {

        parentkey = greeting.theBook;
        specificGreeting = greeting;  // Creating the Ancestor key
        for(String ele:array)
        {
            characters.add(ele);
            votes.add(0);
        }


    }

    public void votenow(String character )
    {
        Integer i = 0;
        for(String ele:characters)
        {
            if(ele == character)
            votes.set(i,votes.get(i)+1);

            i++;
        }

    }

}
