package com.housing.vccalling.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Guestbook {
    @Id public String book;
}