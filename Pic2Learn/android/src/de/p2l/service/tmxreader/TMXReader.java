package de.p2l.service.tmxreader;

import org.w3c.dom.*;

import java.io.InputStream;

import javax.xml.parsers.*;

import de.p2l.ui.menu.mainmenu.MainActivity;

public class TMXReader {

    private String filename;
    private boolean[][] fieldStatus;
    private int height;
    private int width;
    private static final String REG = "[^0-9]";


    public TMXReader(String filename, int width, int height){
        this.filename = filename;
        this.width = width;
        this.height = height;
    }

    public TMXReader(String filename){
        this.filename = filename;
    }

    /**
     * computes the collision map for the level stored in the file filename
     * @return filled collision map (false = not accessible)
     */
    public boolean[][] computeFieldStatus(){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            //use in App
            InputStream inStream = MainActivity.getContext().getResources().getAssets().open(filename);
            //use for Debugging/Testing outside of the app (TMXReaderTest)
            //InputStream inStream = new FileInputStream(filename);
            Document document = builder.parse(inStream);


            NodeList ndList = document.getElementsByTagName("objectgroup");

            if(fieldStatus == null) {
                NodeList ndSize = document.getElementsByTagName("map");
                NamedNodeMap sizeNode = ndSize.item(0).getAttributes();
                width = intFromCut(sizeNode.getNamedItem("width").getNodeValue().split(REG));
                height = intFromCut(sizeNode.getNamedItem("height").getNodeValue().split(REG));
                fieldStatus = new boolean[width][height];
                fillTrue();
            }


            for(int i = 0; i < ndList.getLength(); i++){
                NodeList childNodes = ndList.item(i).getChildNodes();
                NamedNodeMap nm;
                for(int j = 0; j < childNodes.getLength(); j++){
                    nm = childNodes.item(j).getAttributes();
                    if(nm != null) {
                        fillField(intFromCut(nm.getNamedItem("x").getNodeValue().split(REG)) / 16, intFromCut(nm.getNamedItem("y").getNodeValue().split(REG)) / 16,
                                intFromCut(nm.getNamedItem("width").getNodeValue().split(REG)) / 16, intFromCut(nm.getNamedItem("height").getNodeValue().split(REG)) / 16);
                    }
                }
            }
            inStream.close();
            return this.fieldStatus;
        } catch (Exception e){
            throw new RuntimeException(e.toString());
        }

    }

    /**
     * fills the array with false for the given object
     * @param x start x
     * @param y start y
     * @param width width of the object
     * @param height heigth of the object
     */
    private void fillField(int x, int y, int width, int height){
        for(int i = x; (i<fieldStatus.length) && (i<x+width); i++){
            for(int j = y; (j<fieldStatus[i].length) && (j<y+height); j++) {
                fieldStatus[i][this.height-1-j] = false;
            }
        }
    }

    /**
     * initialises the array with true
     */
    private void fillTrue(){
        for(int i = 0; i<fieldStatus.length; i++){
            for(int j = 0; j<fieldStatus[i].length; j++){
                fieldStatus[i][j] = true;
            }
        }
    }

    /**
     * computes the integer value represented of the String array
     * @param cut array with single digits
     * @return integer made of the digits
     */
    private int intFromCut(String[] cut){
        String integ = "";
        for(int i = 0; i<cut.length; i++){
            if(cut[i].length() != 0){
                integ += cut[i];
            }
        }
        return Integer.valueOf(integ);
    }


    //debugging Methods
    private int intFromCut(char[] cut){
        String integ = "";
        for(int i = 0; i<cut.length; i++){
            integ += cut[i];
        }
        System.out.println(integ);
        return Integer.valueOf(integ);
    }


    private String stringArrToString(String[] in){
        String out = "";
        for(int i = 0; i<in.length; i++){
            out += in[i]+", ";
        }
        return out;
    }
}
