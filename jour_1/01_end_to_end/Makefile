NAME		:= cobaye_api
IMAGE		:= coda/tic/$(NAME)
TAG		:= latest
TARGET		:= testing

.DEFAULT_GOAL	:= help

export COMPOSE_DOCKER_CLI_BUILD=1
export DOCKER_BUILDKIT=1

PULL		?= --pull

## Build the application's Docker image
image:
	@docker build								\
	$(PULL) 									\
	--target $(TARGET)							\
	-t $(IMAGE):$(TAG) .

serve:	image
	@docker run 								\
	--name $(NAME)	 							\
	-p 8080:8080								\
	-v ./$(NAME):/app							\
	-v ./data.json:/var/cobaye_api.data.json	\
	--rm										\
	$(IMAGE):$(TAG)

debug:	image
	@docker run 								\
	--name $(NAME)	 							\
	-p 8080:8080								\
	-v ./$(NAME):/app							\
	-v ./data.json:/var/cobaye_api.data.json	\
	--rm										\
	-d											\
	--entrypoint "tail" $(IMAGE):$(TAG) -f /dev/null


shell:
	@docker exec -it $(NAME) /bin/bash

## This help message
help:
	@echo "Available targets:"
	@cat $(MAKEFILE_LIST) | awk '							\
	BEGIN { FS=":" }								\
	/^##/ {										\
		sub(/##[ \t]*/, "", $$0) ;						\
		help = help (help ? "\n\t" : "") $$0 ;					\
		next ;									\
	}										\
	/^[a-zA-Z0-9_-]+:/ { printf ("\033[32m" "%-25s" "\033[0m-- " help "\n", $$1 ) }	\
	/.*/ { help = "" }'

.PHONY:	image serve shell
