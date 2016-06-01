package com.shadegame.engine;

/**
 * Created by Maashes on 5/2/2016.
 */
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;

public class LevelBuilder
{
    private String _levelFile;


    public void SetLevelFile(String file)
    {
        _levelFile = file;
    }

    private Document SetUpDocument()
    {
        try
        {
            File levelInput = new File(_levelFile);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(levelInput);
            doc.getDocumentElement().normalize();

            return doc;
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    public String[] GetImagePaths()
    {
        String[] images = new String[2];

        Document doc = SetUpDocument();

        if(doc != null)
        {
            XPath xpath = XPathFactory.newInstance().newXPath();
            try {
                NodeList bgNode = (NodeList) xpath.compile("/Level/BackgroundImage").evaluate(doc, XPathConstants.NODESET);
                NodeList fgNode = (NodeList) xpath.compile("/Level/ForegroundImage").evaluate(doc, XPathConstants.NODESET);

                if(bgNode.item(0).getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element)bgNode.item(0);
                    images[0] = element.getTextContent();
                }

                if(fgNode.item(0).getNodeType() == Node.ELEMENT_NODE)
                {
                    images[1] = fgNode.item(0).getTextContent();
                }
            }
            catch(Exception ex)
            {

            }
        }
        return images;
    }

    public ArrayList<Platform> GetPlatforms()
    {
        ArrayList<Platform> platforms = new ArrayList<>();

        Document doc = SetUpDocument();

        if(doc != null)
        {
            XPath xpath = XPathFactory.newInstance().newXPath();
            try
            {
                NodeList platformNodes = (NodeList)xpath.compile("/Level/Platforms/Platform").evaluate(doc, XPathConstants.NODESET);
                if(platformNodes != null && platformNodes.getLength() > 0)
                {
                    for(int i=0; i<platformNodes.getLength(); i++)
                    {
                        Node platformNode = platformNodes.item(i);

                        if(platformNode.getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element platform = (Element)platformNode;

                            float xLocation = Float.parseFloat(platform.getAttribute("X"));
                            float yLocation = Float.parseFloat(platform.getAttribute("Y"));
                            float width = Float.parseFloat(platform.getAttribute("Width"));
                            float height = Float.parseFloat(platform.getAttribute("Height"));

                            platforms.add(new Platform(xLocation,yLocation,width,height));
                        }
                    }
                }

            }catch(Exception ex){}
        }


        return platforms;
    }

    public int[] GetLevelDimensions()
    {
        return null;
    }

    public class Platform
    {
        public float XLoc;
        public float YLoc;
        public float Width;
        public float Height;

        public Platform(float x, float y, float width, float height)
        {
            XLoc = x;
            YLoc = y;
            Width = width;
            Height = height;
        }
    }
}
