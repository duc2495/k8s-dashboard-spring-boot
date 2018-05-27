# Kubernetes Web UI 
Đây là giao diện người dùng web cho Kubernetes được viết bằng Java Spring Boot, cơ sở dữ liệu PostgreSQL.
# Hướng dẫn đóng gói ứng dụng thành docker image
## Điều kiện tiên quyết
Trước khi đóng gói ứng dụng yêu cầu một số điều kiện sau:
- Cài đặt [Docker](https://docs.docker.com/install/). 
- Cài đặt [Maven](https://maven.apache.org/install.html).
## Tải mã nguồn từ github 
Mở Terminer lên và chạy lệnh sau:
> git clone https://github.com/duc2495/k8s-dashboard-spring-boot.git

Sau khi tải về thành công, truy cập vào thư mục `k8s-dashboard-spring-boot` với lệnh sau:
> cd k8s-dashboard-spring-boot

## Đóng gói Web UI
Sử dụng `Dockerfile` đã được định nghĩa sẵn trong thư mục để đóng gói Web UI
1. Xuất khẩu mã nguồn thành file thực thi Jar
   > maven clean package 
2. Đóng gói image với `Dockerfile`. Sau khi xuất khẩu thành công, file `K8sClient-0.0.1-SNAPSHOT.jar`được tạo ra trong thư mục target. Thực hiện lệnh sau để đóng gói:
   > sudo docker build -t $USER/k8s-dashboard --build-arg JAR_FILE=target/K8sClient-0.0.1-SNAPSHOT.jar .
3. Đẩy lên Docker Hub với lệnh sau:
   > sudo docker push $USER/k8s-dashboard
## Đóng gói cơ sở dữ liệu PostgreSQL
Sử dụng file `k8s-client-db.sql` để tạo các bảng dữ liệu
1. Chạy 1 container postgres với lệnh sau:
   > sudo docker run  -it --name postgres-container -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=k8s-client postgres:9.6 bash
2. Sau khi vào được dòng lệnh của container thực hiện tạo các bảng dữ liệu dựa trên file `k8s-client-db.sql` 
3. Thực hiện commit container `postgres-container` thành image với tên `$USER/dashboard-postgres:9.6`
   > sudo docker commit postgres-container $USER/dashboard-postgres:9.6
4. Đẩy lên Docker Hub với lệnh sau:
   > sudo docker push $USER/dashboard-postgres:9.6
