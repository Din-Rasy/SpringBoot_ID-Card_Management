# Ansible Deployment & MySQL Backup Task Submission

- **Repository URL**: https://github.com/Din-Rasy/SpringBoot_ID-Card_Management.git
- **Branch**: main
- **Ansible Inventory**: `127.0.0.1 ansible_port=2222 ansible_user=root ansible_password=Hello@123 ansible_connection=ssh`
- **SSH Target**: localhost:2222

## 1. Environment Verification
- **Whoami**: root
- **Hostname**: b6dac6f0a464
- **Java Version**:
  ```
  openjdk version "21.0.11" 2026-04-21 LTS
OpenJDK Runtime Environment Temurin-21.0.11+10 (build 21.0.11+10-LTS)
OpenJDK 64-Bit Server VM Temurin-21.0.11+10 (build 21.0.11+10-LTS, mixed mode, sharing)
  ```
- **Maven Version**:
  ```
  [1mApache Maven 3.9.12[m
Maven home: /usr/share/maven
Java version: 21.0.11, vendor: Eclipse Adoptium, runtime: /opt/java/openjdk
Default locale: en, platform encoding: UTF-8
OS name: "linux", version: "6.6.87.2-microsoft-standard-wsl2", arch: "amd64", family: "unix"
  ```
- **Git Version**: git version 2.53.0

## 2. Git Status Before Pull
```

```

## 3. Git Pull Output
```
Already up to date.
```

## 4. Maven Build Output Summary
```
[[1;34mINFO[m] [1m--- [0;32mresources:3.3.1:resources[m [1m(default-resources)[m @ [36mspringboot-id-card-management[0;1m ---[m\n[[1;34mINFO[m] Copying 2 resources from src/main/resources to target/classes\n[[1;34mINFO[m] Copying 12 resources from src/main/resources to target/classes\n[[1;34mINFO[m] \n[[1;34mINFO[m] [1m--- [0;32mcompiler:3.13.0:compile[m [1m(default-compile)[m @ [36mspringboot-id-card-management[0;1m ---[m\n[[1;34mINFO[m] Recompiling the module because of [1mchanged source code[m.\n[[1;34mINFO[m] Compiling 26 source files with javac [debug parameters release 17] to target/classes\n[[1;34mINFO[m] \n[[1;34mINFO[m] [1m--- [0;32mresources:3.3.1:testResources[m [1m(default-testResources)[m @ [36mspringboot-id-card-management[0;1m ---[m\n[[1;34mINFO[m] Copying 1 resource from src/test/resources to target/test-classes\n[[1;34mINFO[m] \n[[1;34mINFO[m] [1m--- [0;32mcompiler:3.13.0:testCompile[m [1m(default-testCompile)[m @ [36mspringboot-id-card-management[0;1m ---[m\n[[1;34mINFO[m] Recompiling the module because of [1mchanged dependency[m.\n[[1;34mINFO[m] Compiling 8 source files with javac [debug parameters release 17] to target/test-classes\n[[1;34mINFO[m] \n[[1;34mINFO[m] [1m--- [0;32msurefire:3.2.5:test[m [1m(default-test)[m @ [36mspringboot-id-card-management[0;1m ---[m\n[[1;34mINFO[m] Tests are skipped.\n[[1;34mINFO[m] \n[[1;34mINFO[m] [1m--- [0;32mjar:3.4.1:jar[m [1m(default-jar)[m @ [36mspringboot-id-card-management[0;1m ---[m\n[[1;34mINFO[m] Building jar: /app/target/springboot-id-card-management-0.0.1-SNAPSHOT.jar\n[[1;34mINFO[m] \n[[1;34mINFO[m] [1m--- [0;32mspring-boot:3.3.0:repackage[m [1m(repackage)[m @ [36mspringboot-id-card-management[0;1m ---[m\n[[1;34mINFO[m] Replacing main artifact /app/target/springboot-id-card-management-0.0.1-SNAPSHOT.jar with repackaged archive, adding nested dependencies in BOOT-INF/.\n[[1;34mINFO[m] The original artifact has been renamed to /app/target/springboot-id-card-management-0.0.1-SNAPSHOT.jar.original\n[[1;34mINFO[m] [1m------------------------------------------------------------------------[m\n[[1;34mINFO[m] [1;32mBUILD SUCCESS[m\n[[1;34mINFO[m] [1m------------------------------------------------------------------------[m\n[[1;34mINFO[m] Total time:  6.689 s\n[[1;34mINFO[m] Finished at: 2026-06-18T01:39:01Z\n[[1;34mINFO[m] [1m------------------------------------------------------------------------[m
```

## 5. Maven Test Output Summary (SQLite profile)
```
        p1_0.department,\n        p1_0.email,\n        p1_0.expiry_date,\n        p1_0.full_name,\n        p1_0.issue_date,\n        p1_0.phone,\n        p1_0.photo_content_type,\n        p1_0.photo_file_name,\n        p1_0.registration_number,\n        p1_0.template_id,\n        p1_0.title,\n        p1_0.type,\n        p1_0.updated_at,\n        p1_0.uuid \n    from\n        profiles p1_0 \n    where\n        p1_0.uuid=?\n[[1;34mINFO[m] [1;32mTests run: [0;1;32m11[m, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.249 s -- in net.orderzone.idcard.controller.[1mIdCardWebIntegrationTest[m\n[[1;34mINFO[m] \n[[1;34mINFO[m] Results:\n[[1;34mINFO[m] \n[[1;34mINFO[m] [1;32mTests run: 28, Failures: 0, Errors: 0, Skipped: 0[m\n[[1;34mINFO[m] \n[[1;34mINFO[m] [1m------------------------------------------------------------------------[m\n[[1;34mINFO[m] [1;32mBUILD SUCCESS[m\n[[1;34mINFO[m] [1m------------------------------------------------------------------------[m\n[[1;34mINFO[m] Total time:  8.994 s\n[[1;34mINFO[m] Finished at: 2026-06-18T01:39:11Z\n[[1;34mINFO[m] [1m------------------------------------------------------------------------[m
```

## 6. MySQL Backup Verification
- **MySQL Backup Command Used**: `mysqldump -h mysql -uroot -pHello@123 A-DIN_Rasy-db > backups/A-DIN_Rasy-db-backup.sql`
- **Backup File Path inside Container**: `/app/backups/A-DIN_Rasy-db-backup.sql`
- **Backup File Path on Host**: `backups/A-DIN_Rasy-db-backup.sql`

### File List in backups/
```
total 8.0K
-rw-r--r-- 1 root root 4.5K Jun 18 01:39 A-DIN_Rasy-db-backup.sql
```

### First 20 Lines of SQL Backup
```sql
-- MySQL dump 10.13  Distrib 8.4.9, for Linux (x86_64)
--
-- Host: mysql    Database: A-DIN_Rasy-db
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `profiles`
--
```

## 7. Submission Checklist
- [x] Verify connection to web server container
- [x] Go to project directory (/app)
- [x] Check Git status before pull (fail on changes)
- [x] Pull latest code from main branch
- [x] Build with Maven skipping tests
- [x] SQLite configured for test profile
- [x] Run tests using SQLite database
- [x] Production DB remains MySQL
- [x] Backed up MySQL database
- [x] Verify backup exists and show contents
- [x] Commit and push Ansible and configuration files
