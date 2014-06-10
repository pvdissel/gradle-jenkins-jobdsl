package nl.ikoodi.continuousdelivery

import nl.ikoodi.jenkins.JobConfig
import javaposse.jobdsl.dsl.JobParent

class AbstractPipeline {
    private final JobConfig config

    def jobs = [:]
    private JobParent parent

    def AbstractPipeline(JobConfig config) {
        this.config = config
    }

    def propertyMissing(String name, value) {
        jobs[name] = value
    }

    def propertyMissing(String name) {
        jobs[name]
    }

    void each(Closure closure) {
        jobs.each closure
    }

    JobParent build(JobParent parent) {
        this.parent = parent
    }
}
