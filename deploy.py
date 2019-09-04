import sys
import requests
import time

token = sys.argv[1]
apiBase = 'https://api.run.pivotal.io/v3'
spaceGuid = 'ca81d879-eeea-4653-8b67-9b21011a8e84'
appGuid = '3af49658-038a-44ab-8440-89d0179b8bcf'
headers = {'Authorization': 'bearer ' + token}
jarPath ='build/libs/exchange-service-0.0.1-SNAPSHOT.jar'

def check(expectedStatusCode, actualStatusCode, message, response):
    if actualStatusCode == 401:
        print('401 unauthorized, invalid token' + '\n')
        quit()

    if expectedStatusCode != actualStatusCode:
        print(message)
        print(actualStatusCode)
        print(response + '\n')
        quit()

#create a new package
body = { "type": "bits", "relationships": { "app": { "data": { "guid": appGuid } } } }
r = requests.post(apiBase + '/packages', headers = headers, json = body)
check(201, r.status_code, 'unable to create a package', r.text)
print('created package')
packageGuid = r.json()['guid']

#upload bits to package
print('uploading jar...')
files = {'bits': open(jarPath, 'rb')}
r = requests.post(apiBase + '/packages/{}/upload'.format(packageGuid), headers = headers, files = files)
check(200, r.status_code, 'unable to upload bits', r.text)
print('uploaded jar')
packageState = r.json()['state']

#wait for package status to reach AWAITING_UPLOAD
print('waiting for package to be ready...')
while packageState == 'PROCESSING_UPLOAD':
    time.sleep(5)
    r = requests.get(apiBase + '/packages/' + packageGuid, headers = headers)
    check(200, r.status_code, 'unable to check status of package', r.text)
    packageState = r.json()['state']

print('package ready')

#stage package and create build
body = { "package": { "guid": packageGuid }, "lifecycle": { "type": "buildpack", "data": { "buildpacks": [ "java_buildpack" ], "stack": "cflinuxfs2" } } }
r = requests.post(apiBase + '/builds', headers = headers, json = body)
check(201, r.status_code, 'unable to stage package', r.text)
print('staged package and created build')
rJson = r.json()
buildGuid = rJson['guid']
buildState = rJson['state']

#wait for build state to reach STAGED
print('waiting for build to be ready...')
while buildState == 'STAGING':
    time.sleep(5)
    r = requests.get(apiBase + '/builds/' + buildGuid, headers = headers)
    check(200, r.status_code, 'unable to check status of build', r.text)
    buildState = r.json()['state']

print('build ready')

#get droplet guid
r = requests.get(apiBase + '/builds/' + buildGuid, headers = headers)
dropletGuid = r.json()['droplet']['guid']

#assign droplet to app
body = { "data": { "guid": dropletGuid } }
r = requests.patch(apiBase + '/apps/{}/relationships/current_droplet'.format(appGuid), headers = headers, json = body)
check(200, r.status_code, 'unable to assign droplet to app', r.text)
print('assigned droplet')

#stop app
r = requests.post(apiBase + '/apps/{}/actions/stop'.format(appGuid), headers = headers)
check(200, r.status_code, 'could not stop app', r.text)
print('app stopped')

#start app
r = requests.post(apiBase + '/apps/{}/actions/start'.format(appGuid), headers = headers)
check(200, r.status_code, 'could not start app', r.text)
print('app started')

#remove stuff?