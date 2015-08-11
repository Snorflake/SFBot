package me.snorflake.rsloader.Analysers;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import me.snorflake.rsloader.AbstractAnalyser;

import java.util.ListIterator;

/**
 * Created by Snorflake on 8/11/2015.
 */
public class ItemAnalyser extends AbstractAnalyser{
    @Override
    protected boolean canRun(ClassNode node) {
        ListIterator<MethodNode> mnIt = node.methods.listIterator();
        while(mnIt.hasNext())
        {
            MethodNode mn = mnIt.next();
            if((mn.access & Opcodes.ACC_STATIC) != 0)
                continue;
            if(mn.name.equals("<init>"))
            {
                if(mn.instructions.get(3).getOpcode() == Opcodes.RETURN)
                    return true;
            }
        }
        return false;
    }

    @Override
    protected void analyse(ClassNode node) {
        System.out.println("Item class: " + node.name);
    }
}
