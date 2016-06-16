/*
 *  THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS
 *  FOR A PARTICULAR PURPOSE. THIS CODE AND INFORMATION ARE NOT SUPPORTED BY XEBIALABS.
 */
package com.xebialabs.deployit.plugin.chef;

import com.xebialabs.deployit.itest.ItestWizard;
import com.xebialabs.deployit.plugin.api.deployment.specification.DeltaSpecification;
import com.xebialabs.deployit.plugin.api.flow.Step;
import com.xebialabs.deployit.plugin.api.flow.StepExitCode;
import com.xebialabs.deployit.test.deployment.DeployitTester;
import com.xebialabs.deployit.test.support.TestExecutionContext;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ChefItestWizard extends ItestWizard {

    public ChefItestWizard(DeployitTester tester) {
        super(tester);
    }

    @Override
    protected void assertPlan(final DeltaSpecification spec) {
        List<Step> steps = tester.resolvePlanWithStaging(spec);

        TestExecutionContext ctx = new TestExecutionContext(ItestWizard.class);

        try {
            assertThat(DeployitTester.executePlan(steps, ctx), is(StepExitCode.SUCCESS));
        } finally {
            ctx.destroy();
        }
    }
}
