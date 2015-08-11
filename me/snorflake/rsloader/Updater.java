package me.snorflake.rsloader;

import jdk.internal.org.objectweb.asm.tree.ClassNode;
import me.snorflake.rsloader.Analysers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.JarFile;

/**
 * Created by Snorflake on 8/10/2015.
 */
public class Updater {
    public static HashMap<String, ClassNode> CLASSES = new HashMap<>();
    private ArrayList<AbstractAnalyser> analysers = new ArrayList<AbstractAnalyser>();
    public Updater(JarFile jar)
    {
        try
        {
           // JarFile jar = new JarFile("Runescape.jar");
            CLASSES = JarUtils.parseJar(jar);
            System.out.println("[DEVELOPER] " + CLASSES.values().size() + " Classes parsed\n");
            this.loadAnalysers();
            this.runAnalysers();

        } catch( Exception e)
        {
            e.printStackTrace();
        }
    }
    private void loadAnalysers()
    {
        this.analysers.add(new NodeAnalyser());
        this.analysers.add(new StringStorageAnalyser());
        this.analysers.add(new ItemDefinitionAnalyser());
        this.analysers.add(new CanvasAnalyser());
    }

    private void runAnalysers()
    {
        for(ClassNode node : CLASSES.values())
        {
            for(AbstractAnalyser analyser : this.analysers)
            {
                analyser.run(node);
            }
        }
    }

}
