package me.snorflake.rsloader;

import jdk.internal.org.objectweb.asm.tree.ClassNode;

/**
 * Created by Snorflake on 8/10/2015.
 */
public abstract class AbstractAnalyser {

    public void run(ClassNode node)
    {
        if(this.canRun(node))
            this.analyse(node);
    }

    protected abstract boolean canRun(ClassNode node);
    protected abstract void analyse(ClassNode node);
}
