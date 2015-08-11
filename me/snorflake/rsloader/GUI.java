package me.snorflake.rsloader;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import me.snorflake.rsloader.injection.ClientInjector;
import me.snorflake.rsloader.reflection.Loader;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Snorflake on 8/10/2015.
 */
public class GUI extends JFrame implements AppletStub{

    private static final long serialVersionUID = 1L;
    public static JLabel label;
    public static JButton button;

    GUI()
    {
        createForm();
    }

    private void createForm()
    {
        label = new JLabel("Initializing...");
        try{
            for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
            {
                if("Nimbus".equals(info.getName()))
                {
                    changeLabelText(label, "LookAndFeel set to Nimbus");
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                } else
                {
                    changeLabelText(label, "LookAndFeel set to system default");
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        setVisible(true);
        setSize(775, 540);
        setTitle("Runescape loader by Aria");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);
        label = new JLabel("Loading Client...");
        label.setForeground(Color.GRAY.brighter());
        label.setFont(new Font("Consolas", Font.BOLD, 21));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.TOP);
        getContentPane().add(label);
        parseWebsite(Variables.getLink());
        //downloadJar(Variables.getLink() + Variables.getParameters().get("archive"));
        getContentPane().remove(label);
        loadClasses();
        try {
            new Updater(new JarFile("Runescape.jar"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Variables.LOADER = new RSClassLoader(new File("Runescape.jar"));
        ClientInjector.injectClient("Runescape.jar");
        //Loader.loadCache();
    }

    private void loadClasses()
    {
        changeLabelText(label, "Loading classes....");
        ClassLoader loader;
        try{
            loader = new URLClassLoader(new URL[] {new File("Runescape.jar").toURI().toURL()});
            Class<?> client = loader.loadClass("client"/*Rs2Applet*/);
            startApplet(client);
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void startApplet(Class<?> c)
    {
        changeLabelText(label, "Starting applet");
        try{
            Variables.setApplet((Applet) c.newInstance());
            Variables.getApplet().setStub(this);
            Variables.getApplet().init();
            Variables.getApplet().start();
            final Applet app = Variables.getApplet();
            getContentPane().add(app, BorderLayout.CENTER);
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    private void parseWebsite(final String url)
    {
        final String[] VALUES = { "document.write", "<param name=\"", "\">'",
                "'", "\\(", "\\)", "\"", " ", ";", "value" };
        changeLabelText(label, "Parsing website");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new URL(url).openStream()));
            String line;
            for (String s : Variables.getParameters().values()) {
                System.out.println(s);
            }
            while ((line = in.readLine()) != null) {
                if (line.contains("app") && line.contains("write")) {
                    Variables.getParameters().put("<app", "");
                    Variables.getParameters().put("let ", "");
                } else if (line.contains("document.write")) {
                    for (String s : VALUES) {
                        line = line.replaceAll(s, "");
                    }
                    String[] split = line.split("=");
                    if (split.length == 1) {
                        Variables.getParameters().put(split[0], "");
                    } else if (split.length == 2) {
                        Variables.getParameters().put(split[0], split[1]);
                    } else if (split.length == 3) {
                        Variables.getParameters().put(split[0],
                                split[1] + split[2]);
                    }
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadJar(final String url) {
        changeLabelText(label, "Downloading jar");
        try {
            BufferedInputStream in = new BufferedInputStream(
                    new URL(url).openStream());
            FileOutputStream fos = new FileOutputStream("Runescape.jar");
            BufferedOutputStream out = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            int x;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                out.write(data, 0, x);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void changeLabelText(final JLabel l, final String text)
    {
        System.out.println(String.format("[INFORMATION] %s", text));
        try{
            EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    l.setText(text);
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public URL getDocumentBase() {
        try
        {
            return new URL(Variables.getLink());
        } catch(Exception e)
        {
            return null;
        }
    }

    @Override
    public URL getCodeBase() {
        try {
            return new URL(Variables.getLink());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    public String getParameter(String name) {
        return Variables.getParameters().get(name);
    }

    @Override
    public AppletContext getAppletContext() {
        return null;
    }

    @Override
    public void appletResize(int width, int height) {

    }
}
