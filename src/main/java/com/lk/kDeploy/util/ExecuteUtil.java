package com.lk.kDeploy.util;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Execute helper using apache commons exed
 *
 *  add this dependency to your pom.xml:
   <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-exec</artifactId>
            <version>1.2</version>
        </dependency>

 * @author wf, lk
 *
 */
public class ExecuteUtil {

    protected static Logger LOGGER = LoggerFactory.getLogger(ExecuteUtil.class);

    protected final static boolean debug = true;

    /**
     * LogOutputStream
     * http://stackoverflow.com/questions/7340452/process-output-from
     * -apache-commons-exec
     * 
     * @author wf
     * 
     */
    public static class ExecResult extends LogOutputStream {
        private int exitCode;
        /**
         * @return the exitCode
         */
        public int getExitCode() {
            return exitCode;
        }

        /**
         * @param exitCode the exitCode to set
         */
        public void setExitCode(int exitCode) {
            this.exitCode = exitCode;
        }

        private final List<String> lines = new LinkedList<String>();

        @Override
        protected void processLine(String line, int level) {
            lines.add(line);
        }

        public List<String> getLines() {
            return lines;
        }
    }

    /**
     * execute the given command
     * @param cmd - the command 
     * @param exitValue - the expected exit Value
     * @return the output as lines and exit Code
     * @throws Exception
     */
    public static ExecResult execCmd(String cmd, int exitValue) throws Exception {
        if (debug)
            LOGGER.info("running {}", cmd);
        CommandLine commandLine = CommandLine.parse(cmd);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValue(exitValue);
        ExecResult result =new ExecResult();
        executor.setStreamHandler(new PumpStreamHandler(result));
        result.setExitCode(executor.execute(commandLine));
        return result;
    }

}
