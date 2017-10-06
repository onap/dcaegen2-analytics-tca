Repository DCAE Analytics Framework Platform
---------------------------------------------

Maven GroupId:
--------------
com.att.ecomp.dcae.analytics

Maven Parent ArtifactId:
----------------
dcae-analytics

Maven Children Artifacts:
------------------------
1. dcae-analytics-test: Common test code for all DCAE Analytics Modules
2. dcae-analytics-model: Contains models (e.g. Common Event Format) which are common to DCAE Analytics
3. dcae-analytics-common: Contains Components common to all DCAE Analytics Modules - contains high level abstractions
4. dcae-analytics-dmaap: DMaaP(Data Movement as a Platform) MR (Message Router)API using AAF(Authentication and Authorization Framework)
5. dcae-analytics-tca: DCAE Analytics TCA (THRESHOLD CROSSING ALERT) Core
6. dcae-analytics-cdap-common: Common code for all cdap modules
7. dcae-analytics-cdap-tca: CDAP Flowlet implementation for TCA
8. dcae-analytics-cdap-plugins: CDAP Plugins
9. dcae-analytics-cdap-it: Cucumber and CDAP Pipeline integration tests

Deployment in Wind River Lab:
-----------------------------
# create namespace
curl -X PUT http://10.12.25.124:11015/v3/namespaces/cdap_tca_hi_lo

# load artifact
curl -X POST --data-binary @/c/usr/tmp/dcae-analytics-cdap-tca-2.0.0-SNAPSHOT.jar http://10.12.25.124:11015/v3/namespaces/cdap_tca_hi_lo/artifacts/dcae-analytics-cdap-tca

# create app
curl -X PUT -d @/c/usr/docs/ONAP/tca_app_config.json http://10.12.25.124:11015/v3/namespaces/cdap_tca_hi_lo/apps/dcae-tca

# load preferences
curl -X PUT -d @/c/usr/docs/ONAP/tca_app_preferences.json http://10.12.25.124:11015/v3/namespaces/cdap_tca_hi_lo/apps/dcae-tca/preferences

# start program
curl -X POST http://10.12.25.124:11015/v3/namespaces/cdap_tca_hi_lo/apps/dcae-tca/workers/TCADMaaPMRPublisherWorker/start
curl -X POST http://10.12.25.124:11015/v3/namespaces/cdap_tca_hi_lo/apps/dcae-tca/workers/TCADMaaPMRSubscriberWorker/start
curl -X POST http://10.12.25.124:11015/v3/namespaces/cdap_tca_hi_lo/apps/dcae-tca/flows/TCAVESCollectorFlow/start

# check status
curl http://10.12.25.124:11015/v3/namespaces/cdap_tca_hi_lo/apps/dcae-tca/workers/TCADMaaPMRPublisherWorker/status
curl http://10.12.25.124:11015/v3/namespaces/cdap_tca_hi_lo/apps/dcae-tca/workers/TCADMaaPMRSubscriberWorker/status
curl http://10.12.25.124:11015/v3/namespaces/cdap_tca_hi_lo/apps/dcae-tca/flows/TCAVESCollectorFlow/status

# Delete namespace (and all its content)
# curl -X DELETE http://10.12.25.124:11015/v3/unrecoverable/namespaces/cdap_tca_hi_lo

# Delete artifact
# curl -X DELETE http://10.12.25.124:11015/v3/namespaces/cdap_tca_hi_lo/artifacts/dcae-analytics-cdap-tca /versions/2.0.0.SNAPSHOT
