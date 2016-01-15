package com.xebialabs.deployit.plugin.chef.cookbook;

import static com.xebialabs.platform.test.TestUtils.createArtifact;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.IOException;
import java.util.List;
import org.junit.Test;
import com.xebialabs.deployit.deployment.planner.DeltaSpecificationBuilder;
import com.xebialabs.deployit.plugin.api.flow.Step;
import com.xebialabs.deployit.plugin.api.udm.Container;
import com.xebialabs.deployit.plugin.api.udm.Deployed;
import com.xebialabs.deployit.plugin.api.udm.DeployedApplication;
import com.xebialabs.deployit.plugin.api.udm.base.BaseDeployableArtifact;
import com.xebialabs.deployit.plugin.chef.ChefItestBase;
import com.xebialabs.deployit.test.support.ItestTopology;

public class ChefSoloCookbookItest extends ChefItestBase {

    private static final String COOKBOOK_FILE = "test-book.tar.gz";

    public ChefSoloCookbookItest(String description, ItestTopology topology, Container container) {
        super(description, topology, container);
    }

    @Test
    public void shouldDeploySingleRecipe() throws IOException {
        Deployed<?, ?> deployed = getDeployedArtifact(COOKBOOK_FILE);
        deployed.setProperty("recipe", "test-book::default");
        assertThat(getSteps(deployed).size(), equalTo(2));

        DeployedApplication deployedManifest = newDeployedArtifact("chefCookbook", "1.0", deployed);
        assertInitial(deployedManifest);

        assertFileExists(getHost(), "/tmp/chef-solo-cookbook-test-default");
    }

    @Test
    public void shouldDeployMultipleRecipe() throws IOException {
        Deployed<?, ?> deployed = getDeployedArtifact(COOKBOOK_FILE);
        deployed.setProperty("recipe", "test-book::default,test-book::test");
        assertThat(getSteps(deployed).size(), equalTo(2));

        DeployedApplication deployedManifest = newDeployedArtifact("chefCookbook", "1.0", deployed);
        assertInitial(deployedManifest);

        assertFileExists(getHost(), "/tmp/chef-solo-cookbook-test-default", "/tmp/chef-solo-cookbook-test");
    }

    private Deployed<?, ?> getDeployedArtifact(final String manifestFile)
            throws IOException {
        BaseDeployableArtifact artifact = createArtifact("chefSoloCookbook", "1.0", manifestFile, "chef.CookbookSpec", tempFolder.newFolder());
        return wizard.deployed(artifact, container, "chef.Cookbook");
    }

    private List<Step> getSteps(Deployed<?, ?> deployed) {
        DeltaSpecificationBuilder specBuilder = createDeltaSpecBuilder(deployed);
        specBuilder.create(deployed);
        return tester.resolvePlan(specBuilder.build());
    }
}
