package com.example.avid;

import java.util.ArrayList;
import java.util.HashMap;

public class PictureSetContainer {

  private HashMap<String, ArrayList<Picture>> mPictureSetContainer;

  public PictureSetContainer()
  {
    mPictureSetContainer = new HashMap<>();
  }

  public boolean addPictureSet(String key, ArrayList<Picture> pictures)
  {
    if(!mPictureSetContainer.containsKey(key))
    {
      mPictureSetContainer.put(key, pictures);
      return true;
    }
    return false;
  }

  public boolean addPictureToSet(String key, Picture picture)
  {
    mPictureSetContainer.get(key).add(picture);
    return true;
  }

}