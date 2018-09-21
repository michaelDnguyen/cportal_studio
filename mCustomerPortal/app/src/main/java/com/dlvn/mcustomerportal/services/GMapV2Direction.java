package com.dlvn.mcustomerportal.services;

import android.content.Context;
import android.os.AsyncTask;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.utils.myLog;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class GMapV2Direction {
    //listener direction
    private OnDirectionResponseListener mDirectionListener = null;
    Context context;

    //contructor
    public GMapV2Direction(Context context) {
        this.context = context;
    }

    public interface OnDirectionResponseListener {
        public void onResponse(String status, Document doc, GMapV2Direction gd);
    }

    /**
     * get Location by google services direction
     *
     * @param start
     * @param end
     * @param mode
     * @return
     * @arthor nn.tai
     */
    public Document getDocument(LatLng start, LatLng end, String mode) {
        String url = "https://maps.googleapis.com/maps/api/directions/xml?" + "origin=" + start.latitude + ","
                + start.longitude + "&destination=" + end.latitude + "," + end.longitude
                + "&sensor=false&units=metric&mode=" + mode + "&key=" + context.getString(R.string.key_map_production);
        new RequestTask().execute(new String[]{url});
        return null;
    }

    /**
     * request direction throught url
     *
     * @author nn.tai
     */
    private class RequestTask extends AsyncTask<String, Void, Document> {

        protected Document doInBackground(String... url) {

            HttpURLConnection urlConnection = null;
            URL urlrequest;
            try {

                myLog.E("Google Direction request = " + url[0]);

//                HttpClient httpClient = new DefaultHttpClient();
//                HttpContext localContext = new BasicHttpContext();
//                HttpPost httpPost = new HttpPost(url[0]);
//                HttpResponse response = httpClient.execute(httpPost, localContext);
//                InputStream in = response.getEntity().getContent();

                urlrequest = new URL(url[0]);
                urlConnection = (HttpURLConnection) urlrequest.openConnection();
                InputStream in = urlConnection.getInputStream();

                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                return builder.parse(in);

            } catch (IOException e) {
                myLog.printTrace(e);
            } catch (ParserConfigurationException e) {
                myLog.printTrace(e);
            } catch (SAXException e) {
                myLog.printTrace(e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        protected void onPostExecute(Document doc) {
            super.onPostExecute(doc);
            if (mDirectionListener != null)
                mDirectionListener.onResponse(getStatus(doc), doc, GMapV2Direction.this);
        }

        public String getStatus(Document doc) {
            NodeList nl1 = doc.getElementsByTagName("status");
            Node node1 = nl1.item(0);
            myLog.I("GoogleDirection", "Status : " + node1.getTextContent());
            return node1.getTextContent();
        }
    }

    /**
     * set listener
     *
     * @param listener
     * @arthor nn.tai
     */
    public void setOnDirectionResponseListener(OnDirectionResponseListener listener) {
        mDirectionListener = listener;
    }

    /**
     * get duration from doc
     *
     * @param doc
     * @return
     * @arthor nn.tai
     */
    public String getDurationText(Document doc) {
        NodeList nl1 = doc.getElementsByTagName("duration");
        Node node1 = nl1.item(nl1.getLength() - 1);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "text"));
        return node2.getTextContent();
    }

    /**
     * get duration value by doc
     *
     * @param doc
     * @return
     * @arthor nn.tai
     */
    public int getDurationValue(Document doc) {
        NodeList nl1 = doc.getElementsByTagName("duration");
        Node node1 = nl1.item(nl1.getLength() - 1);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "value"));
        return Integer.parseInt(node2.getTextContent());
    }

    /**
     * get distance by doc
     *
     * @param doc
     * @return
     * @arthor nn.tai
     */
    public String getDistanceText(Document doc) {
        NodeList nl1 = doc.getElementsByTagName("distance");
        Node node1 = nl1.item(nl1.getLength() - 1);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "text"));
        return node2.getTextContent();
    }

    /**
     * get distance value by doc
     *
     * @param doc
     * @return
     * @arthor nn.tai
     */
    public int getDistanceValue(Document doc) {
        NodeList nl1 = doc.getElementsByTagName("distance");
        Node node1 = nl1.item(nl1.getLength() - 1);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "value"));
        return Integer.parseInt(node2.getTextContent());
    }

    /**
     * get start address
     *
     * @param doc
     * @return
     * @arthor nn.tai
     */
    public String getStartAddress(Document doc) {
        NodeList nl1 = doc.getElementsByTagName("start_address");
        Node node1 = nl1.item(0);
        return node1.getTextContent();
    }

    /**
     * get end address
     *
     * @param doc
     * @return
     * @arthor nn.tai
     */
    public String getEndAddress(Document doc) {
        NodeList nl1 = doc.getElementsByTagName("end_address");
        Node node1 = nl1.item(0);
        return node1.getTextContent();
    }

    /**
     * get copyright
     *
     * @param doc
     * @return
     * @arthor nn.tai
     */
    public String getCopyRights(Document doc) {
        NodeList nl1 = doc.getElementsByTagName("copyrights");
        Node node1 = nl1.item(0);
        return node1.getTextContent();
    }

    /**
     * get list Long-lat
     *
     * @param doc
     * @return
     * @arthor nn.tai
     */
    public ArrayList<LatLng> getDirection(Document doc) {
        NodeList nl1, nl2, nl3;
        ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();

        nl1 = doc.getElementsByTagName("step");
        if (nl1.getLength() > 0) {
            for (int i = 0; i < nl1.getLength(); i++) {
                Node node1 = nl1.item(i);
                nl2 = node1.getChildNodes();

                Node locationNode = nl2.item(getNodeIndex(nl2, "start_location"));
                nl3 = locationNode.getChildNodes();
                Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
                double lat = Double.parseDouble(latNode.getTextContent());
                Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                double lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));

                locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "points"));
                ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
                for (int j = 0; j < arr.size(); j++) {
                    listGeopoints.add(new LatLng(arr.get(j).latitude, arr.get(j).longitude));
                }

                locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "lat"));
                lat = Double.parseDouble(latNode.getTextContent());
                lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));
            }
        }

        return listGeopoints;
    }

    //get node index
    private int getNodeIndex(NodeList nl, String nodename) {
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeName().equals(nodename))
                return i;
        }
        return -1;
    }

    /**
     * get list long -lat of polyline direction
     *
     * @param encoded
     * @return
     * @arthor nn.tai
     */
    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }

}
