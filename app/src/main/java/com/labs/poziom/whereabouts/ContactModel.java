package com.labs.poziom.whereabouts;

import java.math.BigInteger;
import java.security.SecureRandom;

public class ContactModel {
    private String mName;
    private String mId;
    private String mPhone;
    private String mComment;

    public ContactModel(String name, String id, String phone) {
        mName = name;
        mId = id;
        mPhone = phone;
    }

    public ContactModel(String name, String phone) {
        mName = name;
        mPhone = phone;
        mId = "";
    }

    public ContactModel() {}

    @Override
    public boolean equals(Object obj) {
        if (this == obj)    return true;
        if (obj == null)    return false;
        if (getClass() != obj.getClass())   return false;
        ContactModel other = (ContactModel) obj;
        return mPhone.equals(other.mPhone);
    }

    @Override
    public String toString() {
        return "{Name: " + mName + ", Phone: " + mPhone + "}";
    }

    static String generateRecordId() {
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(new BigInteger(36, 0, random).toString(Character.MAX_RADIX));
        while(stringBuilder.length() > 6) {
            stringBuilder.deleteCharAt(random.nextInt(stringBuilder.length()));
        }
        for (int i = 0; i < stringBuilder.length(); i++) {
            char ch = stringBuilder.charAt(i);
            if (Character.isLetter(ch) && Character.isLowerCase(ch) && random.nextFloat() < 0.5) {
                stringBuilder.setCharAt(i, Character.toUpperCase(ch));
            }
        }
        return stringBuilder.toString();
    }

    public void setName(String name) { mName = name; }
    public void setId(String id) { mId = id; }
    public void setPhone(String phone) { mPhone = phone; }
    public void setComment(String comment) { mComment = comment; }

    public String getName() { return mName; }
    public String getId() { return mId; }
    public String getPhone() { return mPhone; }
    public String getComment() { return mComment; }
}
