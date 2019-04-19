import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.tools.attach.VirtualMachine;

import java.io.File;
import java.util.Optional;

public class AgentLoader {
    private static Logger LOGGER = LoggerFactory.getLogger(AgentLoader.class);

    public static void run() {
        String agentFilePath = "C:\\Users\\Mariusz_Kiwilsza\\Desktop\\projects\\training\\books-queue\\java-agent-1.0-SNAPSHOT.jar";
        String applicationName = "QueueApplication";

        Optional<String> jvmProcessOpt = Optional.ofNullable(VirtualMachine.list()
                .stream()
                .filter(jvm -> {
                    LOGGER.info("jvm: ", jvm.displayName());
                    return jvm.displayName().contains(applicationName);
                })
                .findFirst().get().id());

        if (!jvmProcessOpt.isPresent()) {
            LOGGER.error("Application not found");
            return;
        }

        File agentFile = new File(agentFilePath);

        try {
            String jvmPid = jvmProcessOpt.get();
            LOGGER.info("Attaching to target JVM with PID: " + jvmPid);

            VirtualMachine jvm = VirtualMachine.attach(jvmPid);
            jvm.loadAgent(agentFile.getAbsolutePath());
            jvm.detach();
            LOGGER.info("Attached to target JVM and loaded Java agent successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}