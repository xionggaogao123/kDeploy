/**
 * This file created at 2016年1月11日.
 *
 * Copyright (c) 2002-2016 Honey, Inc. All rights reserved.
 */
package com.lk.kDeploy;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <code>{@link AbstractCommonExecs}</code>
 *
 * TODO : document me
 *
 * @author Honwhy
 */
public abstract class AbstractCommonExecs {

    private Logger log = LoggerFactory.getLogger(AbstractCommonExecs.class);
    private static final String DEFAULT_ENCODING = "UTF-8";
    private String encoding = DEFAULT_ENCODING;

    private String bin;
    private List<String> arguments;
    public AbstractCommonExecs(String bin, List<String> arguments) {
        this.bin = bin;
        this.arguments = arguments;
    }

    public void exec() {
        try {
            Executor executor = getExecutor();
            CommandLine cmdLine = getCommandLine();
            log.info("Executing script {}",cmdLine.toString());
            if(supportWatchdog()) {
                executor.setWatchdog(getWatchdog());
            }
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PipedOutputStream outputStream = new PipedOutputStream();
            PipedInputStream pis = new PipedInputStream(outputStream);
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
            executor.setStreamHandler(streamHandler);
            int ret = executor.execute(cmdLine);

            BufferedReader br = new BufferedReader(new InputStreamReader(pis, getEncoding()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            pis.close();
            String stdout = sb.toString();
            //String stdout = outputStream.toString(getEncoding());
            String stderr = errorStream.toString(getEncoding());
//            er.setStderr(stderr);
            log.info("output from script {} is {}", this.bin, stdout);
            log.info("error output from script {} is {}", this.bin, stderr);
            log.info("exit code from script {} is {}", this.bin, ret);
//            er.setStdout(stdout);
//            er.setExitCode(ret);
//            return er;
        } catch (ExecuteException e) {
        	log.error("报错", e);
        } catch (IOException e) {
        	log.error("报错", e);
        }

    }

    public Executor getExecutor() {
        Executor executor = new DefaultExecutor();
        executor.setWorkingDirectory(new File(this.bin).getParentFile());
        return executor;
    }
    public CommandLine getCommandLine() {
        String fullCommand = bin + join(arguments);
        return CommandLine.parse(fullCommand);
    }
    protected String join(List<String> arguments) {
        if(arguments == null || arguments.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(String arg : arguments) {
            sb.append(" ").append(arg);
        }
        return sb.toString();
    }

    /**
     * @return the encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public abstract boolean supportWatchdog();
    public abstract ExecuteWatchdog getWatchdog();
}
