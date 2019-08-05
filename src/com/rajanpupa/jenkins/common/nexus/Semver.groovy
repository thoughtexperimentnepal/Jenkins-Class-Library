package com.rajanpupa.jenkins.common.nexus

import groovy.transform.Sortable

@Sortable
public class Semver implements Serializable {
    Integer major
    Integer minor
    Integer patch

    public Semver(String semvVer) {
        log(semvVer)
        def majorMinorPatch = semvVer.split('\\.')
        
        if (majorMinorPatch.length != 3) 
            throw new IllegalArgumentException("SemVer must be of form 'X.Y.Z'. Instead found ${semvVer} -> ${majorMinorPatch}");
        
        majorMinorPatch = majorMinorPatch.collect {
            Integer.parseInt(it)
        }
        this.major = majorMinorPatch[0]
        this.minor = majorMinorPatch[1]
        this.patch = majorMinorPatch[2]
    }

    @Override
    String toString() {
        return "${this.major}.${this.minor}.${this.patch}"
    }

    def previewBumpMajor() {
        return "${major + 1}.${0}.${0}"
    }

    def previewBumpMinor() {
        return "${major}.${minor + 1}.${0}"
    }

    def previewBumpPatch() {
        return "${major}.${minor}.${patch + 1}"
    }

    def bumpMajor() {
        this.major += 1
        this.minor = 0
        this.patch = 0
    }

    def bumpMinor() {
        this.minor += 1
        this.patch = 0
    }

    def bumpPatch() {
        this.patch += 1
    }
}