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
    private final String LEVEL_TAG = "Level";
    private final String PLATFORMS_TAG = "Platforms";
    private final String IMAGES_TAG= "Images";
    private final String DIMENSIONS_TAG = "Dimensions";
    private final String WIDTH_TAG = "Width";
    private final String HEIGHT_TAG = "Height";
    private final String RENDERIDX_TAG = "RenderIdx";
    private final String X_TAG = "X";
    private final String Y_TAG = "Y";
    private Document _doc;


    public void SetLevelFile(String file)
    {
        _levelFile = file;
        _doc = SetUpDocument();
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

    public String[] GetImageNames()
    {

        ArrayList<String> imagesArr = new ArrayList<>();
        if(_doc != null)
        {
            XPath xpath = XPathFactory.newInstance().newXPath();
            try {
                NodeList imageNodes = (NodeList)xpath.compile("/"+LEVEL_TAG+"/"+IMAGES_TAG).evaluate(_doc,XPathConstants.NODESET);
                if(imageNodes != null && imageNodes.getLength() > 0)
                {
                    Node imageNode = imageNodes.item(0);
                    if(imageNode!= null && imageNode.hasChildNodes())
                    {
                        NodeList children = imageNode.getChildNodes();
                        for(int i=0; i< children.getLength(); i++)
                        {
                            Node imageChild = children.item(i);
                            int renderIdx = GetImageRenderIndex(imageChild);
                            if(renderIdx > -1)
                            {
                                imagesArr.add(renderIdx,imageChild.getNodeName());
                            }
                        }
                    }
                }
            }
            catch(Exception ex)
            {

            }
        }
        String[] returnArray = new String[imagesArr.size()];
        return imagesArr.toArray(returnArray);
    }

    private int GetImageRenderIndex(Node imageNode)
    {
        if(imageNode.hasChildNodes())
        {
            NodeList children = imageNode.getChildNodes();
            for(int i=0; i<children.getLength(); i++)
            {
                Node imageChild = children.item(i);
                if(imageChild.getNodeName().equalsIgnoreCase(RENDERIDX_TAG))
                {
                    return Integer.parseInt(imageChild.getTextContent());
                }
            }
        }

        return -1;
    }

    public ArrayList<Platform> GetPlatforms()
    {
        ArrayList<Platform> platforms = new ArrayList<>();

        if(_doc != null)
        {
            XPath xpath = XPathFactory.newInstance().newXPath();
            try
            {
                NodeList platformNodes = (NodeList)xpath.compile("/Level/Platforms/Platform").evaluate(_doc, XPathConstants.NODESET);
                if(platformNodes != null && platformNodes.getLength() > 0)
                {
                    for(int i=0; i<platformNodes.getLength(); i++)
                    {
                        Node platformNode = platformNodes.item(i);
                        Platform platform = BuildPlatformObject(platformNode);
                        if(platform != null)
                        {
                            platforms.add(platform);
                        }
                    }
                }

            }catch(Exception ex){}
        }


        return platforms;
    }

    private Platform BuildPlatformObject(Node platformNode)
    {
        if(platformNode.hasChildNodes())
        {
            float xLoc,yLoc,width,height;
            xLoc = yLoc = width = height = 0;
            NodeList platformAttributes = platformNode.getChildNodes();
            for(int i=0; i<platformAttributes.getLength(); i++)
            {
                Node attr = platformAttributes.item(i);
                if(attr.getNodeName().equalsIgnoreCase(X_TAG))
                {
                    xLoc = Float.parseFloat(attr.getTextContent());
                }
                else if(attr.getNodeName().equalsIgnoreCase(Y_TAG))
                {
                    yLoc = Float.parseFloat(attr.getTextContent());
                }
                else if(attr.getNodeName().equalsIgnoreCase(WIDTH_TAG))
                {
                    width = Float.parseFloat(attr.getTextContent());
                }
                else if(attr.getNodeName().equalsIgnoreCase(HEIGHT_TAG))
                {
                    height = Float.parseFloat(attr.getTextContent());
                }
            }
            return new Platform(xLoc,yLoc,width,height);
        }

        return null;
    }

    public int[] GetLevelDimensions()
    {
        int[] dimensions = new int[2];

        if(_doc != null)
        {
            XPath xpath = XPathFactory.newInstance().newXPath();
            try
            {
                NodeList dimensionNodes = (NodeList)xpath.compile(("/"+LEVEL_TAG+"/"+DIMENSIONS_TAG)).evaluate(_doc, XPathConstants.NODESET);
                if(dimensionNodes!= null && dimensionNodes.getLength() > 0)
                {
                    Node dimNode = dimensionNodes.item(0);

                    if(dimNode != null && dimNode.hasChildNodes())
                    {
                        NodeList children = dimNode.getChildNodes();
                        for(int i=0; i < children.getLength(); i++)
                        {
                            Node child = children.item(i);
                            if(child.getNodeName().equalsIgnoreCase(WIDTH_TAG))
                            {
                                dimensions[0]  = Integer.parseInt(child.getTextContent());
                            }
                            else if(child.getNodeName().equalsIgnoreCase(HEIGHT_TAG))
                            {
                                dimensions[1] = Integer.parseInt(child.getTextContent());
                            }
                        }
                    }
                }
            }
            catch(Exception ex){}

        }

        return dimensions;
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
