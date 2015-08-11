package me.snorflake.rsloader.injection;

import jdk.internal.org.objectweb.asm.tree.ClassNode;
import me.snorflake.rsloader.JarUtils;
import me.snorflake.rsloader.Updater;
import me.snorflake.rsloader.Variables;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * Created by Snorflake on 8/11/2015.
 */
public class ClientInjector {
    private static long getChecksum(String file)
    {
        try
        {
            FileInputStream fis = new FileInputStream(file);
            CRC32 crc = new CRC32();
            CheckedInputStream cis = new CheckedInputStream(fis,crc);
            byte[] buffer = new byte[(int) new File(file).length()];
            cis.read(buffer);
            return cis.getChecksum().getValue();
        }catch(Exception e)
        {
        }
        return -1;
    }
    private static void replaceCanvas()
    {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try{
            Thread.sleep(5000);
            Class<?> theClass = Variables.LOADER.loadClass("bm");
            Field cvfield = theClass.getDeclaredField("qj");
            System.out.println(cvfield.getType().getName());
            Canvas oldcv = (Canvas) cvfield.get(null);
            assert oldcv == null;
            Variables.getApplet().remove(oldcv);
            injection.wrappers.Canvas cv = new injection.wrappers.Canvas();

            cvfield.set(null,cv);

            Variables.getApplet().add(cv);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void injectClient(String clientName) {
        try {
            //replaceCanvas();
            for(ClassNode n : Updater.CLASSES.values())
            {
                System.out.printf("Class: %s%n", n.name);
                if(n.superName.contains("Canvas")) {
                    JarUtils.setSuper(n, "injection/wrappers/Canvas");
                    System.out.println("Set canvas super for: " + n.name + " to \"injection/wrappers/Canvas\"");
                }
            }
          /*  HashMap<JarEntry, byte[]> clientData = new HashMap<>();
            Variables.crcHash = getChecksum(clientName);
            System.out.println("Injecting into client : " + Variables.crcHash);
            JarFile theJar = new JarFile(clientName);
            Enumeration<?> en = theJar.entries();
            while (en.hasMoreElements()) {
                JarEntry entry = (JarEntry) en.nextElement();
                if (entry.getName().startsWith("META"))
                    continue;
                byte[] buffer = new byte[1024];
                int read;
                InputStream is = theJar.getInputStream(entry);
                byte[] allByteData = new byte[0];
                while ((read = is.read(buffer)) != -1) {
                    byte[] tempBuff = new byte[read + allByteData.length];
                    for (int i = 0; i < allByteData.length; ++i)
                        tempBuff[i] = allByteData[i];
                    for (int i = 0; i < read; ++i)
                        tempBuff[i + allByteData.length] = buffer[i];
                    allByteData = tempBuff;
                }
                if (entry.getName().endsWith(".class")) {
                    ClassReader cr = new ClassReader(allByteData);
                    ClassNode cn = new ClassNode();
                    cr.accept(cn, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                    Variables.clientClasses.put(cn.name, cn);
                }
                clientData.put(entry, allByteData);
            }
            HashMap<String, ClassNode> newNodes = new HashMap<>();
            for (ClassNode cn : Variables.clientClasses.values()) {
                Canvas.transformClass(cn);
                //MouseListener.transformClass(cn);
                //KeyListener.transformClass(cn);
                newNodes.put(cn.name, cn);
            }
            Variables.clientClasses = newNodes;
            File newJar = new File(clientName);

            FileOutputStream stream = new FileOutputStream(newJar);
            JarOutputStream out = new JarOutputStream(stream);

            for (JarEntry je : clientData.keySet()) {
                byte[] entryData = clientData.get(je);
                if (je.getName().endsWith(".class")) {
                    ClassNode cn = Variables.clientClasses.get(je.getName().substring(0, je.getName().indexOf(".class")));
                    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    cn.accept(cw);
                    entryData = cw.toByteArray();
                    JarEntry newEntry = new JarEntry(je.getName());
                    out.putNextEntry(newEntry);
                } else
                    out.putNextEntry(je);
                out.write(entryData);
            }
            out.close();
            stream.close();*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
