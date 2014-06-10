package nl.ikoodi.continuousdelivery

import nl.ikoodi.jenkins.JobConfig
import javaposse.jobdsl.dsl.Job
import spock.lang.Specification

class MyGenericPipelineSpec extends Specification {

    def testSomethingUseful() {
        def pipe = new MyGenericPipeline(new JobConfig())

//        pipe.ci = job {
//
//        }
        pipe.la = "la"

        pipe.each { job ->
            println job
        }

        def jobParent = new Job()
        pipe.build(jobParent)
    }
}
