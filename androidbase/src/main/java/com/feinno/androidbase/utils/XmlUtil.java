package com.feinno.androidbase.utils;

import android.util.Xml;

import com.feinno.androidbase.utils.log.LogFeinno;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONML;
import org.json.JSONObject;
import org.json.XML;
import org.simpleframework.xml.core.Persister;
import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;

/**
 * Created by ganshoucong on 2016/2/23.
 */
public class XmlUtil {

    /**
    *
    * xml转对象使用的是simplexml
    * 使用方法：
    * 假如有以下XML
    *<body>
        <generalinfo version="1.0.0">
            <result>addsubscribe</result>
        </generalinfo>
        <pa_uuid>12520040234567891111@as1.pa.rcs.chinamobile.com</pa_uuid>
        <member>
            <name>1</name>
        </member>
        <member>
            <name>2</name>
        </member>
     </body>
    *则定义java类如下：
    * @Root(name="body")
        public class Body {
            @Element(name="generalinfo")
            private Generalinfo generalinfo;
            @Element(name="pa_uuid")
            private String p_uuid;
            @ElementList(inline=true,required=false)
            private List<Member> members;
        }
    *
    *   @Root(name="generalinfo")
        public class Generalinfo {
            @Element(name="result")
                private String result;
            @Attribute(name="version")
                private String version;
        }
    *
    *   @Root(name="member")
        public class Member {
            @Element(name="name")
            private String name;
        }
    * @Root 表示根节点
    * @Element 表示这是一个节点
    * @Attribute 表示这是一个属性
    * @ElementList(inline=true,required=false)表示这是一个数组，
    *每个注解都有一个required，设置为false表示这个字段不是必须的，默认为true
    *
    *
    *
    *
     */
    public static <T> T xmlToObj(String xml,Class<T> classOfT) throws Exception {
        return new Persister().read(classOfT, xml, false);
//        JSONObject jsonObj = null;
//        jsonObj = XML.toJSONObject(xml);
//        Gson gson = new Gson();
//        T t = gson.fromJson(jsonObj.toString(), classOfT);
//        return t;
    }

    public static <T> String objToXml(T obj){
        String xml = "";
        try{

            Gson gson = new Gson();
            String s = gson.toJson(obj);
            JSONObject jsonObj = new JSONObject(s);
            xml = XML.toString(jsonObj);
        }catch (Throwable e){
            LogFeinno.e("RF_XmlUtil","objToXml ",e);
        }
        return xml;
    }

    public static String xmlToJson(String xml)throws Exception {
        JSONObject jsonObj = null;
        jsonObj = XML.toJSONObject(xml);
        return jsonObj.toString();
    }
}
