package org.appfuse.mojo.appfuse.mojo.data;

/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;

import org.appfuse.mojo.MojoBase;
import org.appfuse.mojo.appfuse.utility.MojoUtilities;
import org.appfuse.mojo.tasks.GeneratePojoTask;

import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.Properties;


/**
 * This mojo class will create context xml entries for the hibernate dao implementations and dao
 * interfaces from a set of hbm.xml files.
 *
 * @author                        <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version                       $Id: $
 * @description                   Generate one or more hibernate context entries for the
 *                                applicationContext-persist.xml file from input hbm.xml files.*
 * @goal                          genhibernatecontext
 * @requiresDependencyResolution  compile
 */
public class GenerateHibernateContextMojo extends MojoBase
{
    /**
     * This is the package name for the hibernate dao objects.
     *
     * @parameter  expression="${appfuse.hibernate.dao.package.name}"
     *             default-value="${project.groupId}.dao.hibernate"
     */
    private String hibernateDaoPackageName;

    /**
     * This is the output file pattern for Hiberate Dao objects. The package name will be added to
     * the beginning of the pattern with . replaced with slashes.
     *
     * @parameter  expression="${appfuse.dao.output.file.pattern}"
     *             default-value="{class-name}HibernateDao.xml"
     */
    private String hibernateDaoFilePattern;

    /**
     * This is the template name used to generate the hibernate dao context objects.
     *
     * @parameter  expression="${appfusehibernate.dao.context.template.name}"
     *             default-value="/appfusedao/hibernatedaocontext.ftl"
     */
    private String hibernateDaoContextTemplateName;

    /**
     * Creates a new GenerateHibernateContextMojo object.
     */
    public GenerateHibernateContextMojo()
    {
        super();
        this.setMojoName("GenerateHibernateContextMojo");
    }

    /**
     * This method will generate a set of DAO test objects to match the input file pattern.
     *
     * @throws  MojoExecutionException  Thrown if an error is encountered in executing the mojo.
     */
    public void execute() throws MojoExecutionException
    {
        this.getLog().info("Running Mojo " + this.getMojoName());

        // Prompt for the input file pattern
        this.getLog().info("Parameters are " + this.toString());

        try
        {
            // Create a new task
            GeneratePojoTask task = new GeneratePojoTask();

            // Generate the properties list to be set to the generation task.
            Properties props = new Properties();
            props.put("modelpackagename", this.getModelPackageName());

            // Set all the properties required.
            task.setEjb3(this.isEjb3());
            task.setJdk5(this.isJdk5());
            task.setOutputDirectory(this.getOutputDirectory());
            task.setOutputFilePattern(this.getHibernateDaoFilePattern());
            task.setInputFilePattern(this.promptForInputPattern());
            task.setPackageName(this.getHibernateDaoPackageName());
            task.setModelPackageName(this.getModelPackageName());
            task.setTemplateProperties(props);
            task.setTemplateName(this.getHibernateDaoContextTemplateName());
            task.setHibernateConfigurationFile(this.getHibernateConfigurationFile());

            // Get a ouput file classpath for this project
            String outputClasspath;
            outputClasspath = MojoUtilities.getOutputClasspath(this.getMavenProject());
            task.setOuputClassDirectory(outputClasspath);
            task.execute();
        }
        catch (DependencyResolutionRequiredException ex)
        {
            throw new MojoExecutionException(ex.getMessage());
        }
        catch (PrompterException ex)
        {
            throw new MojoExecutionException(ex.getMessage());
        }
    }

    /**
     * Getter for property hibernate dao context template name.
     *
     * @return  The value of hibernate dao context template name.
     */
    public String getHibernateDaoContextTemplateName()
    {
        return this.hibernateDaoContextTemplateName;
    }

    /**
     * Setter for the hibernate dao context template name.
     *
     * @param  inHibernateDaoContextTemplateName  The value of hibernate dao context template name.
     */
    public void setHibernateDaoContextTemplateName(final String inHibernateDaoContextTemplateName)
    {
        this.hibernateDaoContextTemplateName = inHibernateDaoContextTemplateName;
    }

    /**
     * Getter for property hibernate dao file pattern.
     *
     * @return  The value of hibernate dao file pattern.
     */
    public String getHibernateDaoFilePattern()
    {
        return this.hibernateDaoFilePattern;
    }

    /**
     * Setter for the hibernate dao file pattern.
     *
     * @param  inHibernateDaoFilePattern  The value of hibernate dao file pattern.
     */
    public void setHibernateDaoFilePattern(final String inHibernateDaoFilePattern)
    {
        this.hibernateDaoFilePattern = inHibernateDaoFilePattern;
    }

    /**
     * Getter for property hibernate dao package name.
     *
     * @return  The value of hibernate dao package name.
     */
    public String getHibernateDaoPackageName()
    {
        return this.hibernateDaoPackageName;
    }

    /**
     * Setter for the hibernate dao package name.
     *
     * @param  inHibernateDaoPackageName  The value of hibernate dao package name.
     */
    public void setHibernateDaoPackageName(final String inHibernateDaoPackageName)
    {
        this.hibernateDaoPackageName = inHibernateDaoPackageName;
    }

    /**
     * This method creates a String representation of this object.
     *
     * @return  the String representation of this object
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(super.toString());
        buffer.append("GenerateHibernateContextMojo[");
        buffer.append("hibernateDaoContextTemplateName = ").append(hibernateDaoContextTemplateName);
        buffer.append("\n hibernateDaoFilePattern = ").append(hibernateDaoFilePattern);
        buffer.append("\n hibernateDaoPackageName = ").append(hibernateDaoPackageName);
        buffer.append("]");

        return buffer.toString();
    }
}