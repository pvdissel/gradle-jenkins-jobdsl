package nl.ikoodi.continuousdelivery

import nl.ikoodi.jenkins.JobConfig
import javaposse.jobdsl.dsl.Job

class MyGenericPipeline extends AbstractPipeline {
    Job ci
    Job it
    Job deploy

    MyGenericPipeline(JobConfig config) {
        super(config)
    }


}
