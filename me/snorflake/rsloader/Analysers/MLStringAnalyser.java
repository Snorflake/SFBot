package me.snorflake.rsloader.Analysers;

import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import me.snorflake.rsloader.AbstractAnalyser;

import java.util.ListIterator;

/**
 * Created by Snorflake on 8/10/2015.
 */
public class MLStringAnalyser extends AbstractAnalyser{
    @Override
    protected boolean canRun(ClassNode node) {
        ListIterator<MethodNode> methodIterator = node.methods.listIterator();
        while(methodIterator.hasNext())
        {
            MethodNode methodNode = methodIterator.next();
            if(methodNode.name.equals("<init>"))
            {
                if(methodNode.desc.equals("(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"))
                {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    protected void analyse(ClassNode node) {
        System.out.println("MLString class: " + node.name);
    }
}
