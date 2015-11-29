package com.xebialabs.deployit.plugin.chef.cookbook;

import com.xebialabs.deployit.plugin.api.udm.Deployed;
import com.xebialabs.deployit.plugin.api.udm.DeployedApplication;
import com.xebialabs.deployit.plugin.api.udm.base.BaseDeployableArtifact;
import com.xebialabs.deployit.plugin.chef.ChefItestBase;
import com.xebialabs.deployit.plugin.api.udm.Container;
import com.xebialabs.deployit.test.support.ItestTopology;
import org.junit.Test;

import java.io.IOException;

import static com.xebialabs.platform.test.TestUtils.createArtifact;

public class ChefSoloCookbookItest extends ChefItestBase {

    private static final String COOKBOOK_FILE = "test-book.tar.gz";

    public ChefSoloCookbookItest(String description, ItestTopology topology, Container container) {
        super(description, topology, container);
    }

    @Test
    public void shouldDeployCheckCookbookFile() throws IOException {

        DeployedApplication deployedManifest = getDeployedArtifact(COOKBOOK_FILE);

        assertInitial(deployedManifest);
        assertFileExists(getHost(), "/tmp/chef-solo-cookbook-test");
    }

    private DeployedApplication getDeployedArtifact(String manifestFile) throws IOException {
        BaseDeployableArtifact artifact = createArtifact("chefSoloCookbook", "1.0", manifestFile, "chef.CookbookSpec", tempFolder.newFolder());
        Deployed<?, ?> d = wizard.deployed(artifact, container, "chef.Cookbook");
        d.setProperty("recipe", "test-book::test");
        return newDeployedArtifact("chefCookbook", "1.0", d);
    }
}
