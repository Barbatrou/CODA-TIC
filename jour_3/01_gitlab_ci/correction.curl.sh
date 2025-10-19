#!/usr/bin/env bash

set -x

# Authentication credentials for admin_1
AUTH="-u admin_1:"

curl -iw "\n" $AUTH localhost:8080/api/products
curl -iw "\n" $AUTH -X POST localhost:8080/api/products -H "Content-Type: application/json" --data '{"name": "naruto", "category": "book", "price": 6.70, "quantity": 10}'
curl -iw "\n" $AUTH localhost:8080/api/products

curl -iw "\n" $AUTH localhost:8080/api/products/1
curl -iw "\n" $AUTH -X PUT localhost:8080/api/products/1 -H "Content-Type: application/json" --data '{"quantity": 20}'
curl -iw "\n" $AUTH localhost:8080/api/products/1

curl -iw "\n" $AUTH -X DELETE localhost:8080/api/products/1
curl -iw "\n" $AUTH localhost:8080/api/products/1

curl -iw "\n" $AUTH localhost:8080/api/users
curl -iw "\n" $AUTH -X POST localhost:8080/api/users -H "Content-Type: application/json" --data '{"name": "new_user_1", "permissions": ["client"]}'
curl -iw "\n" $AUTH localhost:8080/api/users

curl -iw "\n" $AUTH localhost:8080/api/users/2
curl -iw "\n" $AUTH -X PUT localhost:8080/api/users/2 -H "Content-Type: application/json" --data '{"name": "changed_user_2"}'
curl -iw "\n" $AUTH localhost:8080/api/users/2

curl -iw "\n" $AUTH -X DELETE localhost:8080/api/users/2
curl -iw "\n" $AUTH localhost:8080/api/users/2
