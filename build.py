import subprocess
import requests

#TODO: check if user is logged in

out = subprocess.check_output(['cf', 'oauth-token'])

if out.split(' ')[0] != 'bearer':
    print("Could not get oauth token")
    quit()

print("Found oauth token")
token = out.split(' ')[1]

printf("Triggering build...")

