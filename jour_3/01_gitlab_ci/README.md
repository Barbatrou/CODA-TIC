# first time setup gitlab and runner

```
docker-compose up -d gitlab
login to http://localhost
Go to User Settings > Access Tokens
Create token with api scope
echo "GITLAB_PERSONAL_ACCESS_TOKEN=glpat-your-token-here" >> .env
docker-compose up -d gitlab-runner
```

# start the gitlab and runner stack
```
docker-compose up -d
```
