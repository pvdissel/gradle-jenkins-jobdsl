/*
 * DOES NOT WORK (YET)
 * 
 * This is an experiment to make JobDSL' more reusable
 * and standardized in the form of pipelines
 */
import nl.ikoodi.continuousdelivery.MyGenericPipeline
import nl.ikoodi.jenkins.JobConfig

import static nl.ikoodi.jenkins.ApplicationType.SERVICE
import static nl.ikoodi.jenkins.ScmType.GIT

//@Grab(group = '', module = '', version = '')

def config = new JobConfig(
        app: 'AppKey'
        , appType: SERVICE
        , repoType: GIT
        , repoUrl: 'git@repo:project/repo.git'
        , operationalExcellence: ['team@domain']
)

// Fetch config from a some landscape configuration service
//def config = new LandScapeConfigFactory().get('AppKey', SERVICE)

def pipe = new MyGenericPipeline(config)

//pipe.ci.apply(new MavenPublishTestNgReport())
//pipe.ci.apply(new DisableJob())
pipe.it
pipe.deploy

// Customizing all jobs
pipe.each { job ->
    // Use JobDSL directly
    job.logRotator(
            -1,  // daysToKeepInt
            7,   // numToKeepInt
            -1,  // artifactDaysToKeepInt
            -1   // artifactNumToKeepInt
    )
    job.jdk(config.jdkName)
    job.configure buildWrappers()

    // Or ..
//    job.apply(new DisableJob())
//    job.apply(new MavenPublishTestNgReport())
}

pipe.build(this)

