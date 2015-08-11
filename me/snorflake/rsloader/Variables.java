package me.snorflake.rsloader;

import jdk.internal.org.objectweb.asm.tree.ClassNode;
import me.snorflake.rsloader.injection.ClassHook;
import me.snorflake.rsloader.injection.FieldHook;

import java.applet.Applet;
import java.util.HashMap;

/**
 * Created by Snorflake on 8/10/2015.
 */
public class Variables {

    //private static final String LINK = "http://world76.runescape.com/"; //RS3 (Rs2Applet.class)
    private static final String LINK = "http://oldschool36.runescape.com/"; //OSRS (client.class)
    private static final HashMap<String, String> PARAMETERS = new HashMap<String, String>();
    public static long crcHash;

    private static Applet applet;

    public static String getLink()
    {
        return LINK;
    }
    public static HashMap<String, String> getParameters()
    {
        return PARAMETERS;

    }
    public static void setApplet(Applet a)
    {
        applet = a;
    }
    public static Applet getApplet()
    {
        return applet;
    }
    public static HashMap<String, ClassNode> clientClasses = new HashMap<>();
    public static HashMap<String, ClassNode> allClasses = new HashMap<>();
    public static Object clientBootClass = null;
    public static RSClassLoader LOADER;
    public static HashMap<String, ClassHook> runtimeClassHooks = new HashMap<String, ClassHook>();
    public static HashMap<String, FieldHook> staticFieldHooks = new HashMap<String, FieldHook>();
}
