package com.lambdaschool.journalguidedproject;

import android.net.Uri;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JournalEntry implements Serializable {
    public static final String TAG        = "JournalEntry";
    public static final int    INVALID_ID = -1;

    private String date, entryText, image;
    private int dayRating, id;

    public JournalEntry(String date, String entryText, String image, int dayRating, int id) {
        this.date = date;
        this.entryText = entryText;
        this.image = image;
        this.dayRating = dayRating;
        this.id = id;
    }

    public JournalEntry(int id) {
        this.id = id;
        this.entryText = "";
        this.image = "";

        initializeDate();
    }

    public JournalEntry(int id, String entryText) {
        this.id = id;
        this.entryText = entryText;
        this.dayRating = 3;
        this.image = "";

        initializeDate();
    }

    public JournalEntry(String entryText, long epochTimeSeconds, int rating) {
        this.entryText = entryText;
        this.dayRating = rating;
        Date       date       = new Date(epochTimeSeconds * 1000);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        this.setDate(dateFormat.format(date));

        this.id = INVALID_ID;
        this.image = "";
    }

    public JournalEntry(String csvString) {
        String[] values = csvString.split(",");
        // check to see if we have the right string
        if (values.length == 5) {
            // handle missing numbers or strings in the number position
            try {
                this.id = Integer.parseInt(values[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            this.date = values[1];
            try {
                this.dayRating = Integer.parseInt(values[2]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            // allows us to replace commas in the entry text with a unique character and
            // preserve entry structure
            this.entryText = values[3].replace("~@", ",");
            // placeholder for image will maintain csv's structure even with no provided image
            this.image = values[4].equals("unused") ? "" : values[4];
        }
    }

    boolean areEqual(JournalEntry a) {
        return this.id == a.id && this.getDayRating() == a.getDayRating();
    }

    // converting our object into a csv string that we can handle in a constructor
    String toCsvString() {
        return String.format("%d,%s,%d,%s,%s",
                             id,
                             date,
                             dayRating,
                             entryText.replace(",", "~@"),
                             image == "" ? "unused" : image);
    }

    private void initializeDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date       date       = new Date();

        this.setDate(dateFormat.format(date));
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEntryText() {
        return entryText;
    }

    public void setEntryText(String entryText) {
        this.entryText = entryText;
    }

    public Uri getImage() {
        if (!image.equals("")) {
            return Uri.parse(image);
        } else {
            return null;
        }
    }

    public void setImage(Uri imageUri) {
        this.image = imageUri.toString();
    }

    public int getDayRating() {
        return dayRating;
    }

    public void setDayRating(int dayRating) {
        this.dayRating = dayRating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
