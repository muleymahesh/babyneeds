package com.maks.model;

import android.widget.ImageView;

public class Card {
    private String title;
  //  private String line2;
    private ImageView imageView;

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Card() {

    }
    public Card(String title) {
     this.title=title;
    }
   /*  public Card(String line1, String line2) {
      //  this.line1 = line1;
        //this.line2 = line2;
    }*/

   public String getTitle() {
        return title;
    }

   /* public String getLine2() {
        return line2;
    }*/

}