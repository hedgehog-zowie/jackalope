package org.jackalope.study.app;

import com.google.common.collect.Lists;
import org.apache.commons.cli.*;
import org.jackalope.study.app.lifecycle.LifecycleAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static final String CONF_MONITOR_CLASS = "flume.monitoring.type";
    public static final String CONF_MONITOR_PREFIX = "flume.monitoring.";

    private final List<LifecycleAware> components;

    public Application() {
        this(new ArrayList<LifecycleAware>(0));
    }

    public Application(List<LifecycleAware> components) {
        this.components = components;
    }

    public static void main(String[] args) {

        try {

            Options options = new Options();

            Option option = new Option("n", "name", true, "the name of this agent");
            option.setRequired(true);
            options.addOption(option);

            option = new Option("f", "conf-file", true, "specify a conf file");
            option.setRequired(true);
            options.addOption(option);

            option = new Option(null, "no-reload-conf", false, "do not reload " +
                    "conf file if changed");
            options.addOption(option);

            option = new Option("h", "help", false, "display help text");
            options.addOption(option);

            CommandLineParser parser = new GnuParser();
            CommandLine commandLine = parser.parse(options, args);

            File configurationFile = new File(commandLine.getOptionValue('f'));
            String agentName = commandLine.getOptionValue('n');
            boolean reload = !commandLine.hasOption("no-reload-conf");

            if (commandLine.hasOption('h')) {
                new HelpFormatter().printHelp("flume-ng agent", options, true);
                return;
            }
      /*
       * The following is to ensure that by default the agent
       * will fail on startup if the file does not exist.
       */
            if (!configurationFile.exists()) {
                String path = configurationFile.getPath();
                try {
                    path = configurationFile.getCanonicalPath();
                } catch (IOException ex) {
                    logger.error("Failed to read canonical path for file: " + path, ex);
                }
                throw new ParseException(
                        "The specified configuration file does not exist: " + path);
            }
            List<LifecycleAware> components = Lists.newArrayList();
            Application application;
            PropertiesFileConfigurationProvider configurationProvider =
                    new PropertiesFileConfigurationProvider(agentName, configurationFile);
            application = new Application();
            application.handleConfigurationEvent(configurationProvider.getConfiguration());
            application.start();

            final Application appReference = application;
            Runtime.getRuntime().addShutdownHook(new Thread("agent-shutdown-hook") {
                @Override
                public void run() {
                    appReference.stop();
                }
            });

        } catch (Exception e) {
            logger.error("A fatal error occurred while running. Exception follows.",
                    e);
        }
    }

//    @Subscribe
//    public synchronized void handleConfigurationEvent(MaterializedConfiguration conf) {
//        stopAllComponents();
//        startAllComponents(conf);
//    }
}