package com.rajanpupa.jenkins.common.nexus

import com.rajanpupa.jenkins.common.nexus.Semver
import groovy.json.JsonSlurperClassic
import java.util.List
import java.util.ArrayList;

class Nexus implements Serializable {
	final String GROUP_ID = 'com.rajanpupa.common' 
	
	def jenkins;

	String snapshotRepo
	String releaseRepo
	String username
	String password
	String nexusHost = 'nexus.rajanpupa.com'
	String nexusSearchPath = 'service/rest/v1/search'
	final Semver DEFAULT_VERSION = new Semver('1.0.0')
	//Map servicesConfig
	String projectName
	
	Nexus(String snapshotRepo, String releaseRepo, String username, String password, String projectName) {
		this.snapshotRepo = snapshotRepo
		this.releaseRepo = releaseRepo
		this.username = username
		this.password = password
		//this.servicesConfig = servicesConfig
		this.projectName = projectName
	}
	
	def setJenkins(jenkins){
		this.jenkins = jenkins;
	}
	
	def log(str){
		if(this.jenkins != null){
			this.jenkins.echo(str);
		}
	}

	def getLastRelease(service) {
		//def allReleaseArtifacts = getAllReleaseArtifacts(service)
		
		// def vs = allReleaseArtifacts.collect { it.version }.findAll {
		// 	isSemanticVersion(it)
		// };

        def vs = ['1.0.0', '10.0.0', '2.0.0', '9.0.0'];
		
		log(" vs = ${vs}")
		
		if(vs.isEmpty()) {
			return this.DEFAULT_VERSION.toString()
		}

        //def semvers = [];
        List<Semver> semvers = new ArrayList<>();
		for(String version: vs){
			semvers.add(new Semver(version));
		}
        //Collections.sort(semvers);//.sort(false);
        semvers.sort(false);
		
		log(" vs = ${vs}")
        log(" sorted = ${sorted}")
		
		def lastVersion = vs[vs.size()-1]
		
		log(" lastreleased = ${lastVersion}")
		
		return lastVersion;
		
	}

	def getConnection(nexusRequest) {
		def connection = new URL(nexusRequest).openConnection(Proxy.NO_PROXY)
		def base64Auth = (this.username + ":" + this.password).getBytes().encodeBase64().toString()
		connection.setRequestProperty("Authorization", "Basic ${base64Auth}")
		return connection
	}

	/**
	 * Get all the release artifacts for a given service.
	 * @param service
	 * @return Array of map objects containing artifact
	 */
	def getAllReleaseArtifacts(service) {
		//def projects = this.servicesConfig
		def nexusRequest = "http://www.${nexusHost}/${nexusSearchPath}?repository=${releaseRepo}&group=${this.GROUP_ID}&name=${this.projectName}"
		def connection = getConnection(nexusRequest)
		def response = connection.getInputStream().getText()
		def data = new JsonSlurperClassic().parseText(response)
		def artifacts = data.items
		while (data.continuationToken != null) {
			def continuationToken = data.continuationToken
			def nexusContinuationRequest = "http://www.${nexusHost}/${nexusSearchPath}?repository=${releaseRepo}&continuationToken=$continuationToken"
			def continuationConnection = getConnection(nexusContinuationRequest)
			response = continuationConnection.getInputStream().getText()
			data = new JsonSlurperClassic().parseText(response)
			artifacts += data.items
		}
		return artifacts
	}

	def getAllReleaseArtifacts() {
		return getAllArtifacts(this.releaseRepo)
	}

	def getAllSnapshotArtifacts() {
		return getAllArtifacts(this.snapshotRepo)
	}

	def getAllArtifacts(repo) {
		def nexusRequest = "http://www.${nexusHost}/service/rest/beta/components?repository=$repo"
		def connection = getConnection(nexusRequest)
		def response = connection.getInputStream().getText()

		def data = new JsonSlurperClassic().parseText(response)
		def artifacts = data.items
		while (data.continuationToken != null) {
			def continuationToken = data.continuationToken
			def nexusContinuationRequest = "http://www.${nexusHost}/service/rest/beta/components?repository=$repo&continuationToken=$continuationToken"
			connection = getConnection(nexusContinuationRequest)
			response = connection.getInputStream().getText()
			data = new JsonSlurperClassic().parseText(response)
			artifacts += data.items
		}
		return artifacts
	}
	
	static boolean isSemanticVersion(version) {
		return version =~ /^(0|[1-9]+0*)\.(0|[1-9]+0*)\.(0|[1-9]+0*)$/
	}
}