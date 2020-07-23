package com.neo.qiaoqiaochat;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;

/**
 * @program: qiaoqiaochat-server
 * @description: 1
 * @author: neo lin
 * @create: 2020/07/23 17:36
 */
public class DocMain {

    public static void main(String[] args) {
        DocsConfig config = new DocsConfig();
        config.setProjectPath("D:\\person\\git\\qiaoqiaochat-server\\qiaoqiaochat-web"); // root project path
        config.setProjectName("qiaoqiaochat"); // project name
        config.setApiVersion("V1.0");       // api version
        config.setDocsPath("doc"); // api docs target path
        config.setAutoGenerate(Boolean.TRUE);  // auto generate
        Docs.buildHtmlDocs(config); // execute to generate
    }
}