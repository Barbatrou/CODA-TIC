#!/usr/bin/env bash

set -x

# Authentication credentials for admin_1
AUTH="-u admin_1:"

if [[ `curl -s -o /dev/null -w "%{http_code}" $AUTH localhost:8080/api/products` != 200 ]]; then exit 1; fi
if [[ `curl -s -o /dev/null -w "%{http_code}" $AUTH -X POST localhost:8080/api/products -H "Content-Type: application/json" --data '{"name": "naruto", "category": "book", "price": 6.70, "quantity": 10}'` != 201 ]]; then exit 1; fi

if [[ `curl -s -o /dev/null -w "%{http_code}" $AUTH localhost:8080/api/products/1` != 200 ]]; then exit 1; fi
if [[ `curl -s -o /dev/null -w "%{http_code}" $AUTH -X PUT localhost:8080/api/products/1 -H "Content-Type: application/json" --data '{"quantity": 20}'` != 200 ]]; then exit 1; fi

if [[ `curl -s -o /dev/null -w "%{http_code}" $AUTH -X DELETE localhost:8080/api/products/1` != 204 ]]; then exit 1; fi
if [[ `curl -s -o /dev/null -w "%{http_code}" $AUTH localhost:8080/api/products/1` != 404 ]]; then exit 1; fi

if [[ `curl -s -o /dev/null -w "%{http_code}" $AUTH localhost:8080/api/users` != 200 ]]; then exit 1; fi
if [[ `curl -s -o /dev/null -w "%{http_code}" $AUTH -X POST localhost:8080/api/users -H "Content-Type: application/json" --data '{"name": "new_user_1", "permissions": ["client"]}'` != 201 ]]; then exit 1; fi

if [[ `curl -s -o /dev/null -w "%{http_code}" $AUTH localhost:8080/api/users/2` != 200 ]]; then exit 1; fi
if [[ `curl -s -o /dev/null -w "%{http_code}" $AUTH -X PUT localhost:8080/api/users/2 -H "Content-Type: application/json" --data '{"name": "changed_user_2"}'` != 200 ]]; then exit 1; fi

if [[ `curl -s -o /dev/null -w "%{http_code}" $AUTH -X DELETE localhost:8080/api/users/2` != 204 ]]; then exit 1; fi
if [[ `curl -s -o /dev/null -w "%{http_code}" $AUTH localhost:8080/api/users/2` != 404 ]]; then exit 1; fi

