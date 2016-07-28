/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.geemvc.main;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import java.io.File;

/**
 * Created by Michael on 27.07.2016.
 */
public class TomcatBoot {
    protected static final String DEFAULT_WEBAPP_DIR = "src/main/webapp";
    protected static final String DEFAULT_SERVER_PORT = "8080";
    protected static final String DEFAULT_CONTEXT_PATH = "/";

    public static void main(String[] args) throws ServletException, LifecycleException {

        File targetDir = new File("target");

        if (!targetDir.exists())
            throw new IllegalStateException("Target directory '" + targetDir.getAbsolutePath() + "' does not exist");

        Tomcat tomcat = new Tomcat();

        // ------------------------------------------------------------
        // Server Port
        // ------------------------------------------------------------

        String serverPort = System.getenv("port");
        if (serverPort == null || serverPort.isEmpty()) {
            serverPort = DEFAULT_SERVER_PORT;
        }

        tomcat.setPort(Integer.valueOf(serverPort));

        File baseDir = new File(targetDir.getAbsolutePath(), "tomcat." + serverPort);

        if (!baseDir.exists())
            baseDir.mkdirs();

        File webappsDir = new File(baseDir.getAbsolutePath(), "webapps");

        if (!webappsDir.exists())
            webappsDir.mkdirs();


        tomcat.setBaseDir(baseDir.getAbsolutePath());
//        tomcat.getHost().setAppBase(targetDir.getAbsolutePath());
        tomcat.getHost().setAutoDeploy(true);
        tomcat.getHost().setDeployOnStartup(true);

        // ------------------------------------------------------------
        // Context-Path
        // ------------------------------------------------------------

        String ctxPath = System.getenv("context-path");
        if (ctxPath == null || ctxPath.isEmpty()) {
            ctxPath = DEFAULT_CONTEXT_PATH;
        }

        // ------------------------------------------------------------
        // Webapp-Directory
        // ------------------------------------------------------------

        String webappDir = System.getenv("webapp-dir");
        if (webappDir == null || webappDir.isEmpty()) {
            webappDir = DEFAULT_WEBAPP_DIR;
        }

        File f = new File(DEFAULT_WEBAPP_DIR);

        if (!f.exists()) {
            throw new IllegalStateException("Webapp directory '" + f.getAbsolutePath() + "' does not exist");
        }

        // ------------------------------------------------------------
        // War-File
        // ------------------------------------------------------------
        File warFile = findWarFile();

        if (warFile == null) {
            throw new IllegalStateException("Unable find target war-file to launch");
        }

        StandardContext ctx = (StandardContext) tomcat.addWebapp("", warFile.getAbsolutePath());

//        WebResourceRoot resources = new StandardRoot(ctx);
//        resources.addPreResources(new DirResourceSet(resources, DEFAULT_WEBINF_CLASSES_PATH, f2.getAbsolutePath(), DEFAULT_CONTEXT_PATH));
//        ctx.setResources(resources);

        tomcat.start();
        tomcat.getServer().await();
    }

    protected static File findWarFile() {
        File targetDir = new File("target");
        File[] targetFiles = targetDir.listFiles(f -> f.isFile() && f.getName().endsWith(".war"));

        return targetFiles == null || targetFiles.length == 0 ? null : targetFiles[0];
    }
}
