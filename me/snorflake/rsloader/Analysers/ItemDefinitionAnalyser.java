package me.snorflake.rsloader.Analysers;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;
import me.snorflake.rsloader.AbstractAnalyser;

import java.util.ListIterator;

/**
 * Created by Snorflake on 8/11/2015.
 */
public class ItemDefinitionAnalyser extends AbstractAnalyser{
    private boolean foundActions = false;
    @Override
    protected boolean canRun(ClassNode node) {
        ListIterator<MethodNode> mnIt = node.methods.listIterator();
        while(mnIt.hasNext())
        {
            MethodNode mn = mnIt.next();
            if(mn.name.equals("<init>"))
            {
                getActions(mn);
                //break;
            }
        }
        return foundActions;
    }

    private void getActions(MethodNode constructor)
    {
        ListIterator<AbstractInsnNode> ainIt = constructor.instructions.iterator();

        boolean gFlag = false, iFlag = false;
        String gName = StringStorageAnalyser.getStringStorageField("Take");
        String iName = StringStorageAnalyser.getStringStorageField("Drop");
        while(ainIt.hasNext())
        {
            AbstractInsnNode ain = ainIt.next();
            if(ain.getOpcode() == Opcodes.ANEWARRAY)
            {
                TypeInsnNode tin = (TypeInsnNode) ain;
                if(tin.desc.contains("java/lang/String"))
                {
                    while (ain.getNext() != null)
                    {
                        ain = ain.getNext();
                        if(ain.getOpcode() == Opcodes.GETSTATIC)
                        {
                            FieldInsnNode fin = (FieldInsnNode) ain;
                            if(fin.name.equals(gName))
                                gFlag = true;
                            else if (fin.name.equals(iName))
                                iFlag = true;

                        } else if(ain.getOpcode() == Opcodes.PUTFIELD)
                        {
                            FieldInsnNode fin = (FieldInsnNode) ain;
                            if(gFlag)
                            {
                                gFlag = false;
                                foundActions = true;
                                break;
                            } else if (iFlag)
                            {
                                iFlag = false;
                                foundActions = true;
                                break;
                            }
                        }
                    }
                }
            }
        }


    }
    @Override
    protected void analyse(ClassNode node) {
        System.out.println("ItemDefinition class: " + node.name);
    }
}
