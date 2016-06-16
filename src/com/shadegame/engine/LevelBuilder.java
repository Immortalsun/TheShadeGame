package com.shadegame.engine;

/**
 * Created by Maashes on 5/2/2016.
 */
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

import com.shadegame.engine.utils.PlatformGroup;
import com.shadegame.engine.utils.PlatformHelper;
import com.shadegame.engine.utils.Platform;
import org.w3c.dom.*;

public class LevelBuilder
{
    private String _levelFile;
    private final String LEVEL_TAG = "Level";
    private final String PLATFORMS_TAG = "Platforms";
    private final String PLATFORM_TAG = "Platform";
    private final String IMAGES_TAG= "Images";
    private final String DIMENSIONS_TAG = "Dimensions";
    private final String IMAGENAME_TAG = "ImageName";
    private final String GROUPS_TAG = "Groups";
    private final String NAME_TAG = "Name";
    private final String GROUP_TAG = "Group";
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

    public ArrayList<Platform> GetPlatforms(ArrayList<PlatformGroup> groups)
    {
        ArrayList<Platform> platforms = new ArrayList<>();

        if(_doc != null)
        {
            XPath xpath = XPathFactory.newInstance().newXPath();
            try
            {
                NodeList platformNodes = (NodeList)xpath.compile("/" + LEVEL_TAG + "/" + PLATFORMS_TAG + "/" + PLATFORM_TAG).evaluate(_doc, XPathConstants.NODESET);
                if(platformNodes != null && platformNodes.getLength() > 0)
                {
                    for(int i=0; i<platformNodes.getLength(); i++)
                    {
                        Node platformNode = platformNodes.item(i);
                        Platform platform = BuildPlatformObject(platformNode);
                        if(platform != null)
                        {
                            boolean addToGroup = false;
                            for(int k=0; k< groups.size(); k++)
                            {
                                if(IsPlatformPartOfGroup(groups.get(k),platform))
                                {
                                    groups.get(k).AddPlatformChild(platform);
                                    addToGroup = true;
                                }
                            }

                            if(!addToGroup)
                            {
                                platforms.add(platform);
                            }
                        }
                    }

                    for(int j=0; j < groups.size(); j++)
                    {
                        PlatformGroup group = groups.get(j);
                        group.BuildFullPlatform();
                        platforms.add(group);
                    }
                }

            }catch(Exception ex){}
        }
        return platforms;
    }

    public boolean IsPlatformPartOfGroup(PlatformGroup group, Platform platform)
    {
        return (platform.XLoc >= group.XLoc && platform.XLoc+platform.Width <= group.XLoc+group.Width) &&
                (platform.YLoc >= group.YLoc && platform.YLoc+platform.Height <= group.YLoc+group.Height);
    }

    public ArrayList<PlatformGroup> GetGroups()
    {
        ArrayList<PlatformGroup> groups = new ArrayList<>();
        if(_doc != null)
        {
            XPath xPath = XPathFactory.newInstance().newXPath();
            try
            {
                NodeList groupNodes = (NodeList)xPath.compile("/" + LEVEL_TAG + "/" + GROUPS_TAG + "/" + GROUP_TAG).evaluate(_doc, XPathConstants.NODESET);
                if(groupNodes != null && groupNodes.getLength() > 0)
                {
                    for(int i=0; i < groupNodes.getLength(); i++)
                    {
                        Node groupNode = groupNodes.item(i);
                        PlatformGroup group = BuildGroupObject(groupNode);
                        if(group != null)
                        {
                            groups.add(group);
                        }
                    }
                }
            }catch(Exception ex){}
        }
        return groups;
    }

    private Platform BuildPlatformObject(Node platformNode)
    {
        if(platformNode.hasChildNodes())
        {
            float xLoc,yLoc,width,height;
            xLoc = yLoc = width = height = 0;
            String imgName = "";
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
                else if(attr.getNodeName().equalsIgnoreCase(IMAGENAME_TAG))
                {
                    imgName = attr.getTextContent();
                }
            }
            Platform retPlat = new Platform(xLoc,yLoc,width,height);
            retPlat.SetPlatformImage(imgName);
            retPlat.SetPlatformType(PlatformHelper.GetPlatformType(imgName));
            return retPlat;
        }

        return null;
    }

    private PlatformGroup BuildGroupObject(Node groupNode)
    {

        if(groupNode.hasChildNodes())
        {
            float xLoc,yLoc,width,height;
            xLoc = yLoc = width = height = 0;
            String name = "";
            NodeList groupAttributes = groupNode.getChildNodes();
            for(int i=0; i<groupAttributes.getLength(); i++)
            {
                Node attr = groupAttributes.item(i);
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
                else if(attr.getNodeName().equalsIgnoreCase(NAME_TAG))
                {
                    name = attr.getTextContent();
                }
            }
            PlatformGroup retGroup = new PlatformGroup(xLoc,yLoc,width,height,name);

            return retGroup;
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

}
