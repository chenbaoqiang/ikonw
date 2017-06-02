package com.feinno.androidbase.utils;

import android.text.TextUtils;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by ganshoucong on 2016/3/8.
 */
public class XmlWriter {
    private XmlSerializer serializer = null;
    StringWriter writer = null;
    public XmlWriter(){
        serializer = Xml.newSerializer();
        writer = new StringWriter();
    }
    public XmlWriter begin()throws IOException{
        serializer.setOutput(writer);
        serializer.startDocument("utf-8", true);
        return this;
    }
    public XmlWriter end()throws IOException{
        serializer.endDocument();
        return this;
    }
    public XmlWriter startTag(String tag) throws IOException {
        serializer.startTag("", tag);
        return this;
    }
    public XmlWriter endTag(String tag)throws IOException{
        serializer.endTag("", tag);
        return this;
    }
    public XmlWriter text(String text)throws IOException{
        serializer.text(text);
        return this;
    }
    public XmlWriter addAttri(String attri,String value)throws IOException{
        if(TextUtils.isEmpty(value)){
            return this;
        }
        serializer.attribute("",attri,value);
        return this;
    }
    public XmlWriter addEmptyAttri(String attri,String value)throws IOException{
        serializer.attribute("",attri,value);
        return this;
    }
    public String getString(){
        return writer.toString();
    }
    public XmlWriter cdsect(String cdata) throws IOException {
        serializer.cdsect(cdata);
        return this;
    }
}
