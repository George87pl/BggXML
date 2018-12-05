package com.gmail.gpolomicz.bggxml;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class ParseString {
    private static final String TAG = "GPDEB";

    private ArrayList<BGSearchEntry> informationArrayList;

    public ParseString() {
        this.informationArrayList = new ArrayList<>();
    }

    public ArrayList<BGSearchEntry> getInformationArrayList() {
        return informationArrayList;
    }

    public String getLink(int position) {
        return informationArrayList.get(position).getLink();
    }

    public boolean parseXML(String xmlData) {
        boolean status = true;
        BGSearchEntry currentRecord = null;
        boolean inItem = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT && informationArrayList.size() < 50) {

                Log.d(TAG, "parseXML: " + informationArrayList.size());

                String tagName = xpp.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
//                        Log.d(TAG, "parse: Starting tag for  " + tagName);
                        if ("item".equalsIgnoreCase(tagName)) {
                            inItem = true;
                            currentRecord = new BGSearchEntry();
                            currentRecord.setId(Integer.parseInt(xpp.getAttributeValue(null, "id")));
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
//                        Log.d(TAG, "parse: Ending tag for " + tagName);
                        if (inItem) {
                            if ("item".equalsIgnoreCase(tagName)) {
                                informationArrayList.add(currentRecord);
                                inItem = false;

                            } else if ("name".equalsIgnoreCase(tagName)) {
                                currentRecord.setTitle(xpp.getAttributeValue(null, "value"));

                            } else if ("yearpublished".equalsIgnoreCase(tagName)) {
//                                String date= textValue.substring(0, textValue.length() - 9);
                                currentRecord.setPubDate(xpp.getAttributeValue(null, "value"));
                            }
                        }
                        break;
                    default:
                        // Nothing else to do
                }
                eventType = xpp.next();
            }

//            for (BGSearchEntry info : informationArrayList) {
//                Log.d(TAG, "*************");
//                Log.d(TAG, info.toString());
//            }

        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public boolean parseXML(String xmlData, Integer id) {
        boolean status = true;
        boolean inItem = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
//                        Log.d(TAG, "parse: Starting tag for  " + tagName);
                        if ("item".equalsIgnoreCase(tagName)) {
                            inItem = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
//                        Log.d(TAG, "parse: Ending tag for " + tagName);
                        if (inItem) {
                            if ("item".equalsIgnoreCase(tagName)) {
                                inItem = false;
                            } else if ("thumbnail".equalsIgnoreCase(tagName)) {
                               informationArrayList.get(id).setImage(textValue);
                            }
                        }
                        break;
                    default:
                        // Nothing else to do
                }
                eventType = xpp.next();
            }

        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }
}
