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
data = '{{ "type": "bits", "relationships": {{ "app": {{ "data": {{ "guid": "{}" }} }} }} }}'.format(appGuid)
r = requests.post(apiBase + '/packages', headers = {'Authorization': 'bearer ' + token, 'Content-Type': 'application/json' }, data = data)
check(201, r.status_code, 'unable to create a package', r.text)
print('created package')
packageGuid = r.json()['guid']
print('packageGuid:' + packageGuid)

#upload bits to package
print('uploading bits...')
files = {'bits': open(jarPath, 'rb')}
r = requests.post(apiBase + '/packages/{}/upload'.format(packageGuid), headers = headers, files = files)
check(200, r.status_code, 'unable to upload bits', r.text)
print('uploaded bits')
packageState = r.json()['state']

#wait for package status to reach AWAITING_UPLOAD
print('waiting for package to be ready...')
while packageState == 'PROCESSING_UPLOAD':
    time.sleep(3)
    r = requests.get(apiBase + '/packages/' + packageGuid, headers = headers)
    check(200, r.status_code, 'unable to check status of package', r.text)
    packageState = r.json()['state']

#stage package and create build
#TODO: change this to use .format()
data = '{ "package": { "guid": "' + packageGuid + '" }, "lifecycle": { "type": "buildpack", "data": { "buildpacks": [ "java_buildpack" ], "stack": "cflinuxfs2" } } }'
r = requests.post(apiBase + '/builds', headers = {'Authorization': 'bearer ' + token, 'Content-Type': 'application/json' }, data = data)
check(201, r.status_code, 'unable to stage package', r.text)
print('staged package')
rJson = r.json()
buildGuid = rJson['guid']
buildState = rJson['state']

print('buildGuid:' + buildGuid)

#wait for build state to reach STAGED
print('waiting for build to be ready...')
while buildState == 'STAGING':
    time.sleep(3)
    r = requests.get(apiBase + '/builds/' + buildGuid, headers = headers)
    check(200, r.status_code, 'unable to check status of build', r.text)
    buildState = r.json()['state']

print(r.json()['droplet']['guid'])

#get droplet guid
r = requests.get(apiBase + '/builds/' + buildGuid, headers = headers)
dropletGuid = r.json()['droplet']['guid']

print('dropletGuid:' + dropletGuid)

#assign droplet to app
data = '{{ "data": {{ "guid": "{}" }} }}'.format(dropletGuid)
r = requests.patch(apiBase + '/apps/{}/relationships/current_droplet'.format(appGuid), headers = {'Authorization': 'bearer ' + token, 'Content-Type': 'application/json' }, data = data)
check(200, r.status_code, 'unable to assign droplet to app', r.text)
print('assigned droplet to app')

#restart app?

#remove stuff?