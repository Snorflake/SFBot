package me.snorflake.rsloader.Analysers;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;
import me.snorflake.rsloader.AbstractAnalyser;

import java.util.ListIterator;

/**
 * Created by Snorflake on 8/10/2015.
 */
public class NodeAnalyser extends AbstractAnalyser{
    @Override
    protected boolean canRun(ClassNode node) {
        if(!node.superName.contains("java/lang/Object"))
            return false;
        int nodeType = 0, longType = 0, fieldType = 0;
        ListIterator<FieldNode> fnIt = node.fields.listIterator();
        while(fnIt.hasNext())
        {
            FieldNode fn = fnIt.next();
            if((fn.access & Opcodes.ACC_STATIC) == 0)
            {
                if(fn.desc.equals(String.format("L%s;", node.name)))
                    nodeType++;
                else if(fn.desc.equals("J"))
                    longType++;
                fieldType++;
            }
        }
        return nodeType == 2 && longType == 1 && fieldType == 3;
    }

    @Override
    protected void analyse(ClassNode node) {
        System.out.println("Node class: " + node.name);
        ListIterator<FieldNode> fnIt = node.fields.listIterator();
        String previousName = "";
        ListIterator<MethodNode> mnIt = node.methods.listIterator();
        methodIterator: while(mnIt.hasNext())
        {
            MethodNode mn = mnIt.next();
            if((mn.access & Opcodes.ACC_STATIC) == 0)
            {
                if(mn.desc.equals("()Z"))
                {
                    ListIterator<AbstractInsnNode> ainIt = mn.instructions.iterator();
                    while(ainIt.hasNext())
                    {
                        AbstractInsnNode ain = ainIt.next();
                        if(ain instanceof FieldInsnNode)
                        {
                            previousName = ((FieldInsnNode)ain).name;
                            break methodIterator;
                        }
                    }
                }
            }
        }
        System.out.println("--Previous Field: " + previousName);
        while(fnIt.hasNext())
        {
            FieldNode fn = fnIt.next();
            if((fn.access & Opcodes.ACC_STATIC) == 0)
            {
                if(fn.desc.equals("J"))
                    System.out.println("--UID Field: " + fn.name);


            }

        }
        if(previousName.length() > 0)
        {
            fnIt = node.fields.listIterator();
            while(fnIt.hasNext())
            {
                FieldNode fn = fnIt.next();
                if((fn.access & Opcodes.ACC_STATIC) == 0)
                {
                    if(fn.desc.equals(String.format("L%s;", node.name)) && !fn.name.equals(previousName))
                        System.out.println("--Next field: " + fn.name);
                }
            }
        }
    }
}
