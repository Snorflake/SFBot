package me.snorflake.rsloader;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import sun.management.MethodInfo;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Snorflake on 8/10/2015.
 */
public class JarUtils {

    public static HashMap<String, ClassNode> parseJar(JarFile jar)
    {
        HashMap<String, ClassNode> classes = new HashMap<>();
        try
        {
            Enumeration<?> enumeration = jar.entries();

            while(enumeration.hasMoreElements())
            {
                JarEntry entry = (JarEntry)enumeration.nextElement();
                if(entry.getName().endsWith(".class"))
                {
                    ClassReader classReader = new ClassReader(jar.getInputStream(entry));
                    ClassNode classNode = new ClassNode();
                    classReader.accept(classNode, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                    classes.put(classNode.name, classNode);
                }
            }
            jar.close();
            return classes;
        }catch(Exception e)
        {
            return null;
        }
    }
    public static void setSuper(ClassNode node, String superClass)
    {
        ListIterator<?> mlIt = node.methods.listIterator();
        while(mlIt.hasNext())
        {
            MethodNode mn = (MethodNode)mlIt.next();
            ListIterator<?> iIt = mn.instructions.iterator();
            while(iIt.hasNext())
            {
                AbstractInsnNode ain = (AbstractInsnNode)iIt.next();
                if(ain.getOpcode() == Opcodes.INVOKESPECIAL)
                {
                    MethodInsnNode min = (MethodInsnNode) ain;
                    min.owner = superClass;
                    break;
                }
            }
        }
        node.superName = superClass;
    }
}
