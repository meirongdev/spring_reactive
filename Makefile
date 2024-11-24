# 定义变量
APP_NAME = webflux-helloworld-app
DOCKER_IMAGE = $(APP_NAME)
DOCKER_TAG = latest
JAR_FILE = target/webflux-0.0.1-SNAPSHOT.jar
PROFILE = webflux-helloworld

# 默认目标
.PHONY: all
all: build docker-build docker-run

# 构建项目
.PHONY: build
build:
	mvn clean package -P$(PROFILE) -DskipTests

# 构建 Docker 镜像
.PHONY: docker-build
docker-build:
	docker build -t $(DOCKER_IMAGE):$(DOCKER_TAG) .

# 运行 Docker 容器
.PHONY: docker-run
docker-run:
	docker run -d --name $(APP_NAME) -p 8080:8080 -p 9010:9010 --cpus="2" --memory="2g" -v $(PWD)/logs:/app/logs $(DOCKER_IMAGE):$(DOCKER_TAG)

# 停止并删除 Docker 容器
.PHONY: docker-clean
docker-clean:
	docker stop $(APP_NAME) || true
	docker rm $(APP_NAME) || true

# 清理构建文件
.PHONY: clean
clean:
	mvn clean