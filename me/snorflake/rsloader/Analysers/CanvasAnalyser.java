package me.snorflake.rsloader.Analysers;

import com.sun.org.apache.xpath.internal.compiler.OpCodes;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.FieldNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import me.snorflake.rsloader.AbstractAnalyser;
import me.snorflake.rsloader.JarUtils;
import me.snorflake.rsloader.Variables;

import java.util.ListIterator;

/**
 * Created by Snorflake on 8/11/2015.
 */
public class CanvasAnalyser extends AbstractAnalyser{

    private boolean hasbeenfound = false;
    @Override
    protected boolean canRun(ClassNode node) {
        if (!node.superName.contains("java/lang/Object"))
            return false;
        int fieldType = 0, methodType = 0;
        ListIterator<FieldNode> fnIt = node.fields.listIterator();
        while (fnIt.hasNext()) {
            FieldNode fn = fnIt.next();
            if ((fn.access & Opcodes.ACC_STATIC) == 0) {
                fieldType++;
            }
        }
        ListIterator<MethodNode> mnIt = node.methods.listIterator();
        while(mnIt.hasNext())
        {
            MethodNode mn = mnIt.next();
            if((mn.access & Opcodes.ACC_STATIC) == 0)
            {
                methodType++;
            }
        }
        return fieldType == 4 && methodType == 14;
    }

    @Override
    protected void analyse(ClassNode node) {
        if(hasbeenfound) return;
        System.out.println("Canvas class: " + node.name);
        Variables.clientClasses.put(node.name, node);
        hasbeenfound = true;
        ListIterator<FieldNode> fnIt = node.fields.listIterator();
        while (fnIt.hasNext()) {
            FieldNode fn = fnIt.next();
                    if(fn.desc.contains("Ljava/awt/Canvas"))
                    {
                        System.out.println("-- Canvas: " + fn.name);

                    }


        }
    }
}
