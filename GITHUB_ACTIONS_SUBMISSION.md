# GitHub Actions CI/CD Pipeline Submission

This document contains the submission details for **Task 5: GitHub Actions CI/CD Integration** of the Spring Boot ID Card Management System.

---

## 1. Project Information
* **Repository URL:** [https://github.com/Din-Rasy/SpringBoot_ID-Card_Management.git](https://github.com/Din-Rasy/SpringBoot_ID-Card_Management.git)
* **Branch:** `main`
* **Workflow File Path:** `.github/workflows/ci-cd.yml`

---

## 2. CI/CD Architecture & Runner Details

### Workflow Trigger
* **On Push to `main`:** The workflow automatically runs on every commit pushed to the `main` branch.
* **Manual Run (`workflow_dispatch`):** Allows triggering the workflow manually from the GitHub Actions tab.

### Self-Hosted Runner Explanation
* **Runner Name:** `Rasy-PC`
* **Runner OS:** Windows
* **Configured Label:** `self-hosted`
* **Shell Environment:** `powershell`

### Why Self-Hosted Runner is Required
1. **Access to Local Services:** The workflow must deploy to a local Docker Compose setup (containers `web-app` and `mysql-db`) exposed on `localhost`. A GitHub-hosted runner cannot directly access local services on a private developer computer.
2. **Ansible Playbook Deployment:** The deployment phase relies on running `ansible-playbook`, which interacts with the local containerized environment.
3. **Optimized Build Speed:** The runner can cache maven packages locally in `C:\Users\Rasy\.m2` to significantly speed up compilation and execution across subsequent runs.

---

## 3. Workflow Commands

### 1. Build Command
Builds the package while skipping unit tests (tests are isolated in a subsequent step):
```powershell
mvn clean package -DskipTests
```

### 2. SQLite Test Command
Runs all JUnit tests under the test profile using SQLite as the isolated in-memory test database (keeping MySQL untouched for production):
```powershell
mvn clean test "-Dspring.profiles.active=test"
```

### 3. Ansible Deploy Command
Runs the deployment playbook against the web server container:
```powershell
ansible-playbook -i inventory.ini playbook.yml
```
*(Implemented as a wrapper `ansible-playbook.cmd` in the repository root that redirects to WSL Ubuntu).*

---

## 4. Email Notification Setup

### Behavior On Failure
If any build or test step fails, the workflow uses `dawidd6/action-send-mail@v3` to send an email notification:
* **To:** Developer who committed the error (`${{ github.event.head_commit.author.email }}`)
* **CC:** `srengty@gmail.com`

### Fallback Step
If the required GitHub Secrets are missing, the workflow prints a warning to `github-actions-build-output.txt` instead of failing the job.

### Required GitHub Secrets
To configure email notifications, the following secrets must be added to your GitHub repository under **Settings > Secrets and variables > Actions**:
1. `SMTP_SERVER` (e.g., `smtp.gmail.com`)
2. `SMTP_PORT` (e.g., `465` or `587`)
3. `SMTP_USERNAME` (your SMTP email address)
4. `SMTP_PASSWORD` (Gmail App Password if using Gmail)
5. `SMTP_FROM` (sender email address)

---

## 5. How to Run & Download Artifacts

### Running the Workflow Manually
1. Go to your GitHub Repository page.
2. Click on the **Actions** tab.
3. Select the **Spring Boot CI/CD with Ansible** workflow from the left sidebar.
4. Click the **Run workflow** dropdown on the right side.
5. Select the `main` branch and click the **Run workflow** button.

### Downloading the Build Output Artifact
1. Go to the **Actions** tab.
2. Click on the completed run.
3. Scroll down to the **Artifacts** section at the bottom of the page.
4. Click on **github-actions-build-output** (contains `github-actions-build-output.txt`) or **maven-test-reports** to download the zip archives.

---

## 6. Submission Screenshot Checklist
Ensure the following screenshots are captured and included in the submission PDF:
* [ ] **Self-Hosted Runner Status:** Showing runner `Rasy-PC` is active and online in the GitHub Repository Settings.
* [ ] **GitHub Actions Run:** Showing a successful run of the pipeline.
* [ ] **Maven Build Phase:** Successful logs of `mvn clean package -DskipTests`.
* [ ] **SQLite Test Phase:** Successful logs of `mvn clean test` running 28 tests with SQLite configuration.
* [ ] **Ansible Deploy Phase:** Logs of `ansible-playbook` successfully connecting to `host.docker.internal` on port 2222 and deploying.
* [ ] **GitHub Actions Artifacts:** Displaying uploaded `github-actions-build-output` and `maven-test-reports`.
* [ ] **Email Notification Step:** Showing the email step (either success/failure step or warning execution logs).

---

## 7. Final Checklist

| Task Requirement | Status |
|---|---|
| Trigger on Push & Manual dispatch | **Done** |
| Run on Windows Self-hosted Runner (`runs-on: self-hosted`) | **Done** |
| Use PowerShell as default shell | **Done** |
| Use Java 21 & Maven 3.9+ | **Done** |
| Maven Clean Package - Skip Tests | **Done** |
| Maven Clean Test with SQLite database | **Done** |
| Run Ansible Playbook only on success | **Done** |
| Redirect build, test, and deploy outputs to `github-actions-build-output.txt` | **Done** |
| Upload build output and test reports as workflow artifacts | **Done** |
| Failures trigger email notification to author + CC `srengty@gmail.com` | **Done** |
| Warning fallback step when email secrets are missing | **Done** |
| `ansible-playbook.cmd` wrapper script created for Windows-WSL execution | **Done** |
