package com.xebialabs.deployit.plugin.chef;

import com.google.common.util.concurrent.ListenableFuture;
import com.xebialabs.deployit.booter.local.LocalBooter;
import com.xebialabs.deployit.itest.ItestWizard;
import com.xebialabs.deployit.itest.cloudhost.ItestHostLauncher;
import com.xebialabs.deployit.plugin.api.reflect.Type;
import com.xebialabs.deployit.plugin.api.udm.Container;
import com.xebialabs.deployit.plugin.api.udm.Deployed;
import com.xebialabs.deployit.plugin.api.udm.DeployedApplication;
import com.xebialabs.deployit.plugin.overthere.Host;
import com.xebialabs.deployit.test.deployment.DeployitTester;
import com.xebialabs.deployit.test.support.ItestTopology;
import com.xebialabs.overcast.host.CloudHost;
import com.xebialabs.overthere.OverthereConnection;
import com.xebialabs.overthere.OverthereFile;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class ChefItestBase {

    private static final Logger LOG = LoggerFactory.getLogger(ChefItestBase.class);

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    static {
        LocalBooter.bootWithoutGlobalContext();
    }

    protected final DeployitTester tester;
    protected final String description;
    protected final ItestTopology topology;
    protected final Container container;
    protected final ItestWizard wizard;

    public ChefItestBase(String description, ItestTopology topology, Container container) {
        this.description = description;
        this.topology = topology;
        this.container = container;
        this.tester = new DeployitTester();
        this.wizard = new ChefItestWizard(tester);
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> getTargets() {
        List<Object[]> constructorArgsList = new ArrayList<Object[]>();
        Map<String, ItestTopology> topologies = ItestTopology.load();

        for (ItestTopology topology : topologies.values()) {
            if (ItestTopology.isItestEnabled(topology.getId(), topology.isEnabledByDefault())) {
                for (Container container : topology.getTargets()) {
                    constructorArgsList.add(new Object[]{"Test on " + container, topology, container});
                }
            }
        }

        if (constructorArgsList.isEmpty()) {
            LOG.error("No topologies found");
        }
        return constructorArgsList;
    }


    @Before
    public void takeCareOfVagrantImages() {
        ItestHostLauncher launcher = ItestHostLauncher.getInstance();

        ListenableFuture<CloudHost> future = launcher.launch(topology.getId());
        try {
            CloudHost ch = future.get();
            topology.registerIp(ch.getHostName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void assertInitial(final DeployedApplication app) {
        wizard.assertInitial(app);
    }

    public DeployedApplication newDeployedArtifact(String name, String version, Deployed<?, ?>... deployeds) {
        return ItestWizard.newDeployedApplication(name, version, deployeds);
    }

    protected void assertFileExists(Host host, String... paths) {
        assertFileExists(host, true, paths);
    }

    protected void assertFileExists(Host host, boolean exists, String... paths) {
        OverthereConnection conn = host.getConnection();
        try {
            for (String path : paths) {
                OverthereFile file = conn.getFile(path);
                assertThat(file.exists(), is(exists));
            }
        } finally {
            conn.close();
        }
    }

    public Host getHost() {
        return (Host)topology.findFirstMatchingCi(Type.valueOf("overthere.SshHost"));
    }
}
