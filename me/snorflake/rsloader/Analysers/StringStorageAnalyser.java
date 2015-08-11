package me.snorflake.rsloader.Analysers;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;
import me.snorflake.rsloader.AbstractAnalyser;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.ListIterator;

/**
 * Created by Snorflake on 8/11/2015.
 */
public class StringStorageAnalyser extends AbstractAnalyser{
    @Override
    protected boolean canRun(ClassNode node) {
        int count = 0;
        ListIterator<FieldNode> fnIt = node.fields.listIterator();
        while(fnIt.hasNext())
        {
            FieldNode fn = fnIt.next();

            if(fn.desc.equals("Ljava/lang/String;"))
                count++;
        }
        return count > 20;
    }

    public static HashMap<String, String> StringStorageValues = new HashMap<String, String>();

    private void storeStrings(ClassNode node)
    {
        ListIterator<MethodNode> mnIt = node.methods.listIterator();
        while(mnIt.hasNext())
        {
            MethodNode mn = mnIt.next();
            if(mn.name.equals("<clinit>"))
            {
                ListIterator<AbstractInsnNode> ainIt = mn.instructions.iterator();
                while(ainIt.hasNext())
                {
                    AbstractInsnNode ain = ainIt.next();
                    if(ain.getOpcode() == Opcodes.LDC)
                    {
                        String text = (String) ((LdcInsnNode)ain).cst;
                        if(text == null)
                            continue;
                        if(text.length() < 4)
                            continue;
                        AbstractInsnNode next = ainIt.next();
                        if(next.getOpcode() == Opcodes.PUTSTATIC)
                        {
                            FieldInsnNode fin = (FieldInsnNode) next;
                            String[] theValues = new String[]{text};
                            for(int i = 0;i<theValues.length;i++)
                            {
                                if(!StringStorageValues.containsKey(theValues[i]))
                                {
                                    StringStorageValues.put(theValues[i],fin.name);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static String getStringStorageField(String lookupString)
    {
        if(StringStorageValues.containsKey(lookupString))
            return StringStorageValues.get(lookupString);
        return "";
    }

    @Override
    protected void analyse(ClassNode node) {
        System.out.println("StringStorage class: " + node.name);
    }
}
