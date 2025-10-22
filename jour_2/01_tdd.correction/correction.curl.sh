#!/usr/bin/env bash

set -x

curl -iw "\n" localhost:8080/api/products
curl -iw "\n" -X POST localhost:8080/api/products -H "Content-Type: application/json" --data '{"name": "naruto", "category": "book", "price": 6.70, "quantity": 10}'
curl -iw "\n" localhost:8080/api/products

curl -iw "\n" localhost:8080/api/products/1
curl -iw "\n" -X PUT localhost:8080/api/products/1 -H "Content-Type: application/json" --data '{"quantity": 20}'
curl -iw "\n" localhost:8080/api/products/1

curl -iw "\n" -X DELETE localhost:8080/api/products/1
curl -iw "\n" localhost:8080/api/products/1

curl -iw "\n" localhost:8080/api/users
curl -iw "\n" -X POST localhost:8080/api/users -H "Content-Type: application/json" --data '{"name": "new_user_1", "permissions": ["client"]}'
curl -iw "\n" localhost:8080/api/users

curl -iw "\n" localhost:8080/api/users/1
curl -iw "\n" -X PUT localhost:8080/api/users/1 -H "Content-Type: application/json" --data '{"name": "changed_user_1"}'
curl -iw "\n" localhost:8080/api/users/1

curl -iw "\n" -X DELETE localhost:8080/api/users/1
curl -iw "\n" localhost:8080/api/users/1
