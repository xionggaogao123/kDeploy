package com.lk.kDeploy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.exec.ExecuteWatchdog;

/**
 * <code>{@link GbkCommonExecs}</code>
 *
 * windows开发环境测试
 *
 * @author Honwhy
 */
public class GbkCommonExecs extends AbstractCommonExecs {

    /**
     * @param bin
     * @param arguments
     */
    public GbkCommonExecs(String bin, List<String> arguments) {
        super(bin, arguments);
    }

    /* (non-Javadoc)
     * @see com.bingosoft.proxy.helper.AbstractCommonExecs#supportWatchdog()
     */
    @Override
    public boolean supportWatchdog() {
        // TODO implement AbstractCommonExecs.supportWatchdog
        return false;
    }

    /* (non-Javadoc)
     * @see com.bingosoft.proxy.helper.AbstractCommonExecs#getWatchdog()
     */
    @Override
    public ExecuteWatchdog getWatchdog() {
        // TODO implement AbstractCommonExecs.getWatchdog
        return null;
    }

    public String getEncoding() {
        return "GBK";
    }
    public static void main(String[] args) {
        String bin = "ping";
        String arg1 = "127.0.0.1";
        List<String> arguments = new ArrayList<String>();
        arguments.add(arg1);
        AbstractCommonExecs executable = new GbkCommonExecs(bin, arguments);
        executable.exec();
    }

}