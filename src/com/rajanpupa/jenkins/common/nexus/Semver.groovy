package com.rajanpupa.jenkins.common.nexus

//import groovy.transform.Sortable

//@Sortable
public class Semver implements Comparable<Semver>, Serializable {
    Integer major
    Integer minor
    Integer patch

    public Semver(String semvVer) {
        def majorMinorPatch = semvVer.split('\\.')
        
        if (majorMinorPatch.length != 3) 
            throw new IllegalArgumentException("SemVer must be of form 'X.Y.Z'. Instead found ${semvVer} -> ${majorMinorPatch}");
        
        this.major = Integer.parseInt(majorMinorPatch[0])
        this.minor = Integer.parseInt(majorMinorPatch[1])
        this.patch = Integer.parseInt(majorMinorPatch[2])

        // echo ("-- Major version " + this.major)
    }

    @Override
    int compareTo(Semver o) {
        if (this.major > o.major) {
            return 1
        }
        if (this.major < o.major) {
            return -1
        }
        if (this.minor > o.minor) {
            return 1
        }
        if (this.minor < o.minor) {
            return -1
        }
        if (this.patch > o.patch) {
            return 1
        }
        if (this.patch < o.patch) {
            return -1
        }
        return 0
    }

    @Override
    String toString() {
        return "${this.major}.${this.minor}.${this.patch}"
    }

    def previewBumpMajor() {
        return "${this.major + 1}.${0}.${0}"
    }

    def previewBumpMinor() {
        return "${this.major}.${this.minor + 1}.${0}"
    }

    def previewBumpPatch() {
        return "${this.major}.${this.minor}.${this.patch + 1}"
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