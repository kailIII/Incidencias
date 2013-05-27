package com.dexafree.incidencias;

/**
 * Created by Carlos on 27/05/13.
 */
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ItemXMLHandler extends DefaultHandler {

    Boolean currentElement = false;
    String currentValue = "";
    Favoritos favoritoActual = null;


    public ArrayList<Favoritos> getItemsList() {
        return Favoritos.FavoritosList;
    }

    // Called when tag starts
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        currentElement = true;
        currentValue = "";
        if (localName.equals("favorito")) {
            favoritoActual = new Favoritos();
        }

    }

    // Called when tag closing
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        currentElement = false;

        /** set value */
        if (localName.equalsIgnoreCase("carretera"))
            favoritoActual.setCarretera(currentValue);
        else if (localName.equalsIgnoreCase("pkInicial"))
            favoritoActual.setPkInicial(Integer.parseInt(currentValue));
        else if (localName.equalsIgnoreCase("pkFinal"))
            favoritoActual.setPkFinal(Integer.parseInt(currentValue));
        else if (localName.equalsIgnoreCase("favorito"))
            Favoritos.FavoritosList.add(favoritoActual);
    }

    // Called to get tag characters
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (currentElement) {
            currentValue = currentValue +  new String(ch, start, length);
        }

    }

}