#
context.addStep(steps.os_script(
    description="Run recipe %s from %s on chef-solo " % (deployed.recipe,deployed.name),
    script="chef/chef-solo-cookbook",
    order=deployed.runOrder
))