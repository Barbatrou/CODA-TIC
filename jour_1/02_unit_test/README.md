# Cobaye TEST app

## Build and launch

### build image
`make image` or `docker build -t coda/tic/cobaye-test:latest --target testing`

### test project
```
$ make debug
$ make shell
container-host$ /opt/build_and_test.sh
```
or
```
$ docker run --name cobaye-api -p 8080:8080 -d --rm -v ./cobaye_test:/app -v ./data.json:/var/cobaye_test.data.json coda/tic/cobaye-test:latest
```

to view tests report head to `file:///{PWD}/coda/TIC/jour_1/02_unit_test.correction/cobaye_test/build/reports/tests/test/classes/coda.tic.CobayeTest.html` on your browser

## Exercice

Ecriver les tests nécessaire dans le fichier `src/test/java/coda/tic/CoBayeTest.java` pour valider et maintenir les fonction definie dans `src/main/java/coda/tic/Cobaye.java`
Certaine fonction peuvent contenir des erreurs que vous devrez methome/cros_b/work/part-timetre en evidence à l'aide de vos tests. Proposez ensuite une implémentation pour corriger ces erreurs.