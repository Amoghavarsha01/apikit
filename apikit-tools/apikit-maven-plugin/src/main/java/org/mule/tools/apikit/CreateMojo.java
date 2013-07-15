package org.mule.tools.apikit;

import org.apache.commons.lang.Validate;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.Scanner;
import org.sonatype.plexus.build.incremental.BuildContext;

import java.io.*;
import java.util.*;

/**
 * Goal for apikit:create
 */
@Mojo(name = "create")
public class CreateMojo
        extends AbstractMojo {
    @Component
    private BuildContext buildContext;

    /**
     * Pattern of where to find the spec .yaml files.
     */
    @Parameter
    private String[] specIncludes = new String[]{"src/main/api/**/*.yaml", "src/main/api/**/*.yml"};

    /**
     * Pattern of what to exclude searching for .yaml files.
     */
    @Parameter
    private String[] specExcludes = new String[]{};

    /**
     * Spec source directory to use as root of specInclude and specExclude patterns.
     */
    @Parameter(defaultValue = "${basedir}")
    private File specDirectory;

    /**
     * Pattern of where to find the Mule XMLs.
     */
    @Parameter
    private String[] muleXmlIncludes = new String[]{"src/main/app/**/*.xml", "src/main/resources/**/*.xml"};

    /**
     * Pattern of what to exclude searching for Mule XML files.
     */
    @Parameter
    private String[] muleXmlExcludes = new String[]{};

    /**
     * Spec source directory to use as root of muleInclude and muleExclude patterns.
     */
    @Parameter(defaultValue = "${basedir}")
    private File muleXmlDirectory;

    /**
     * Where to output the generated mule config files.
     */
    @Parameter(defaultValue = "${basedir}/src/main/app")
    private File muleXmlOutputDirectory;

    private Log log;

    List<String> getIncludedFiles(File sourceDirectory, String[] includes, String[] excludes) {
        Scanner scanner = buildContext.newScanner(sourceDirectory, true);
        scanner.setIncludes(includes);
        scanner.setExcludes(excludes);
        scanner.scan();
        String[] includedFiles = scanner.getIncludedFiles();
        for (int i = 0; i < includedFiles.length; i++) {
            includedFiles[i] = new File(scanner.getBasedir(), includedFiles[i]).getAbsolutePath();
        }

        String[] result = new String[includedFiles.length];
        System.arraycopy(includedFiles, 0, result, 0, includedFiles.length);
        return Arrays.asList(result);
    }

    public void execute()
            throws MojoExecutionException {
        Validate.notNull(muleXmlDirectory, "Error: muleXmlDirectory parameter cannot be null");
        Validate.notNull(specDirectory, "Error: specDirectory parameter cannot be null");

        log = getLog();

        List<String> specFiles = getIncludedFiles(specDirectory, specIncludes, specExcludes);
        List<String> muleXmlFiles = getIncludedFiles(muleXmlDirectory, muleXmlIncludes, muleXmlExcludes);

        log.info("Processing the following yaml files: " + specFiles);
        log.info("Processing the following xml files as mule configs: " + muleXmlFiles);

        Scaffolder scaffolder = Scaffolder.createScaffolder(log, muleXmlOutputDirectory, specFiles, muleXmlFiles);
        scaffolder.run();
    }

}