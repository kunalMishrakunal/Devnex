# Divnex - Real-World Micro-Experience Platform & Digital Passport

Divnex bridges the gap between ambitious freshers and high-growth companies through verified practical micro-projects. Companies post small, scoped engineering and product tasks; students complete them with real evidence; companies review and score the deliverables; and Divnex issues cryptographic, tamper-evident Digital Experience Passports that prove genuine hands-on ability.

### User Review & Critical Decisions

> [!IMPORTANT]
> The following product configurations and design decisions reflect the user preferences confirmed in Phase 1, establishing a reliable, zero-friction foundation for immediate testing and future cloud expansion.

- **Confirmed Decision 1 (Persistence Engine & Backend Readiness)**: Powered by a local Room SQLite database with seed datasets for Students, Companies, and Admins, structured identically to cloud Firestore schemas. A dedicated Backend Readiness banner and audit screen will transparently detail the cloud service status without simulated mock fallbacks.
- **Confirmed Decision 2 (Landing Experience & Role Switching)**: A modern startup landing page with hero messaging, feature showcases, and an interactive **One-Tap Persona Switcher** allowing instant previewing as *Alex Chen (Student)*, *Acme Corp / TechNova (Company Lead)*, or *System Auditor (Admin)*.
- **Confirmed Decision 3 (Submission & Verification Standard)**: Submissions enforce production proof: GitHub repository URL, live deployment URL, structured deliverable notes, and task self-reflections. Verification is never automatic—it strictly requires company review or admin dispute sign-off before entering the verified passport.

---

### 1. Overview & Core Concept

- **What It Does**: Divnex provides an end-to-end ecosystem where companies publish scoped micro-tasks (2–10 hours of practical work), students discover and apply with their skill sets, complete assignments with real deliverables, receive ratings and constructive feedback, and build a verified Digital Experience Passport.
- **Target Audience / Personas**:
  1. *Students & Freshers*: Seeking legitimate portfolio-building experience and verifiable credentials to stand out to employers.
  2. *Companies & Tech Leads*: Wanting to evaluate junior talent through lightweight, realistic tasks rather than whiteboard puzzles or unverified resumes.
  3. *Admins & Auditors*: Ensuring quality, preventing spam, mediating disputes, and maintaining the credibility of the verification ecosystem.
- **Key Value**: Replaces the flawed "entry-level requires 3 years experience" loop with proof-of-work micro-experiences backed by verified company endorsements.

---

### 2. User Experience & Visual Design

#### Key User Flows

1. **Startup Landing & Discovery**:
   - High-impact hero section with value propositions for both students and companies.
   - Live stats ticker (Completed micro-tasks, verified skills, hiring partner rating).
   - "How It Works" 4-step interactive timeline: Discover $\rightarrow$ Build $\rightarrow$ Review $\rightarrow$ Mint Passport.
   - Persona Switcher Bar at the top to jump between Student, Company, and Admin views with one tap.

2. **Student Journey**:
   - **Task Feed**: Filter by tech stack (Kotlin, Jetpack Compose, TypeScript, React, Python), difficulty, duration, and stipend/credit.
   - **Task Detail & Application**: View deliverables checklist, company details, deadline; submit application note and primary strengths.
   - **Active Task Workspace**: Track lifecycle state (`Assigned` $\rightarrow$ `In Progress` $\rightarrow$ `Submitted` $\rightarrow$ `Revision Requested` $\rightarrow$ `Approved`).
   - **Deliverable Submission Modal**: Enter GitHub URL, Live App/Demo URL, architecture breakdown, and challenges encountered.
   - **Experience Passport**: Interactive certificate-style passport displaying completed projects, issued badges, company badges, reviewer ratings, verified timestamps, and shareable QR/copy link.

3. **Company Journey**:
   - **Company Hub**: Active listings overview, pending applicants count, submissions awaiting review, and talent pipeline.
   - **Task Creation Studio**: Multi-step wizard with title, category, description, required skills, estimated hours, deadline, and acceptance criteria.
   - **Applicant Review**: Review student profiles, previous passport achievements, and approve/reject applications.
   - **Submission Evaluation**: Inspect deliverables (direct links to GitHub/Live Demo), leave detailed rubric scores (Code Quality, Communication, Timeliness, Architecture), and either `Request Revision` with notes or `Approve & Verify`.

4. **Admin & Trust Dashboard**:
   - Global platform metrics (Active tasks, total verified hours, dispute rate).
   - Task audit feed & content moderation.
   - Dispute resolution room (student appeals or company no-response triggers).
   - Backend Readiness panel detailing cloud Firebase configuration steps.

#### Visual Identity & Theme

- **Aesthetic Direction**: *Nordic Neo-Fintech / Modern High-Trust SaaS*. Crisp dark-mode and light-mode adaptability, deep midnight navy surface base (`#0F172A`), electric indigo primary (`#4F46E5`), cyan verified accent (`#06B6D4`), and emerald approval states (`#10B981`).
- **Typography & Scale**: Clean, tech-forward sans-serif typography with strict hierarchy:
  - Display / Hero Headings: Bold 28sp–32sp with tight line height.
  - Section Headers: Semi-bold 20sp–22sp with subtle tracking.
  - Body & Form Labels: Medium 14sp–15sp with high legibility.
  - Metadata / Badges / Tags: Semi-bold 11sp–12sp uppercase with rounded chip surfaces.
- **Surface Elevation & Components**:
  - Outlined and elevated cards with subtle 1dp borders (`SurfaceVariant`) for contrast.
  - Status badges with distinct tint pairings (e.g., Amber for `Under Review`, Blue for `In Progress`, Emerald for `Verified`, Rose for `Revision Needed`).
  - High-precision touch targets adhering strictly to 48dp minimums.

---

### 3. Key Product Decisions & Trade-Offs

- **Decision 1: Persistence Architecture (Room with Cloud-Parity Schema)**
  - *Chosen Approach*: Implement a reactive Room SQLite database using DAOs and StateFlows, with pre-populated seed tasks, company profiles, and student records.
  - *Why*: Provides immediate, fully functional end-to-end task flows on the emulator without authentication blocks, while maintaining exact schema parity with Firestore collections so cloud syncing can be enabled seamlessly.
  - *Trade-off*: Local persistence stays on-device; a Backend Readiness indicator explicitly reports cloud connectivity status so users understand whether local or cloud storage is in effect.
- **Decision 2: Strict Lifecycle State Machine**
  - *Chosen Approach*: Rigid validation rules preventing unauthorized state jumps (e.g., a task cannot move to `Approved` without a valid `Submitted` record with valid URLs).
  - *Why*: Protects the platform's core premise—that Divnex experience is authentic and verifiable.
- **Decision 3: Multi-Role Session Switcher in Top Bar**
  - *Chosen Approach*: A persistent, elegant role badge in the top app bar that lets reviewers switch between Student, Company, and Admin modes at any point, with separate local profile states.
  - *Why*: Allows rapid demonstration and testing of the entire collaborative feedback loop without having to log out, clear data, or register three distinct accounts.

---

### 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                          Divnex UI Layer                              │
│                                                                        │
│  ┌───────────────────────┐  ┌───────────────────────────────────────┐  │
│  │   Startup Landing     │  │          App Top Bar                  │  │
│  │ • Hero & Value Props  │  │ • Persona Switcher (Student/Co/Admin) │  │
│  │ • How It Works Flow   │  │ • Active User Profile Badge           │  │
│  └───────────┬───────────┘  └───────────────────┬───────────────────┘  │
│              │                                  │                      │
│  ┌───────────▼───────────┐  ┌───────────────────▼───────────────────┐  │
│  │    Student Dashboard  │  │          Company Dashboard            │  │
│  │ • Browse & Filter     │  │ • Create Tasks & Set Criteria         │  │
│  │ • Submit Deliverables │  │ • Review Applicants & Submissions     │  │
│  │ • Experience Passport │  │ • Score Rubric & Request Revisions    │  │
│  └───────────┬───────────┘  └───────────────────┬───────────────────┘  │
│              │                                  │                      │
│              │       ┌──────────────────────────▼────────────────┐     │
│              └──────►│           Admin & Dispute Hub             │     │
│                      │ • Task Auditing & Dispute Mediation       │     │
│                      │ • Cloud Backend Readiness Indicator       │     │
│                      └──────────────────────────┬────────────────┘     │
└─────────────────────────────────────────────────┼──────────────────────┘
                                                  │
                                                  ▼
┌────────────────────────────────────────────────────────────────────────┐
│                        DivnexViewModel Layer                           │
│  • Manages CurrentUserSession, SelectedFilter, TaskFormState           │
│  • Enforces State Machine Transitions (Apply, Assign, Submit, Verify)  │
│  • Computes Experience Passport statistics and verified credentials    │
└─────────────────────────────────────────────────┬──────────────────────┘
                                                  │
                                                  ▼
┌────────────────────────────────────────────────────────────────────────┐
│                        DivnexRepository Layer                          │
│                                                                        │
│    ┌───────────────────────────────────┐  ┌─────────────────────────┐  │
│    │     Local Room SQLite Database    │  │   Backend Status /      │  │
│    │  • TaskEntity & Dao               │  │   Cloud Sync Bridge     │  │
│    │  • ApplicationEntity & Dao        │  │ • Explicit Readiness    │  │
│    │  • SubmissionEntity & Dao         │  │   Diagnostics           │  │
│    │  • Review & PassportEntity & Dao  │  │ • No Fake Cloud Flags   │  │
│    └───────────────────────────────────┘  └─────────────────────────┘  │
└────────────────────────────────────────────────────────────────────────┘
```

#### Core Entities & Schema

1. **`User`**: `id`, `name`, `email`, `role` (`STUDENT`, `COMPANY`, `ADMIN`), `avatarUrl`, `headline`, `skills`, `companyName`, `companyWebsite`.
2. **`Task`**: `id`, `companyId`, `companyName`, `title`, `description`, `category`, `requiredSkills` (comma-separated), `difficulty` (`BEGINNER`, `INTERMEDIATE`, `ADVANCED`), `estimatedHours`, `stipendOrReward`, `deadlineEpochMs`, `status` (`DRAFT`, `PUBLISHED`, `ASSIGNED`, `COMPLETED`, `ARCHIVED`), `assignedStudentId`, `assignedStudentName`, `createdAtEpochMs`.
3. **`Application`**: `id`, `taskId`, `studentId`, `studentName`, `coverNote`, `portfolioUrl`, `status` (`PENDING`, `ACCEPTED`, `REJECTED`), `appliedAtEpochMs`.
4. **`Submission`**: `id`, `taskId`, `studentId`, `githubUrl`, `liveDemoUrl`, `notes`, `status` (`SUBMITTED`, `REVISION_REQUESTED`, `APPROVED`, `REJECTED`), `revisionFeedback`, `submittedAtEpochMs`, `updatedAtEpochMs`.
5. **`ReviewRecord`**: `id`, `taskId`, `submissionId`, `companyId`, `studentId`, `overallRating` (1 to 5), `codeQualityScore`, `timelinessScore`, `communicationScore`, `writtenFeedback`, `verifiedTimestampEpochMs`, `verificationHash`.
6. **`DisputeRecord`**: `id`, `taskId`, `studentId`, `companyId`, `reason`, `status` (`OPEN`, `RESOLVED`, `DISMISSED`), `adminNotes`.

#### Task State Machine Rules

- `PUBLISHED`: Open for student applications.
- `APPLIED`: Student submits application; company can inspect candidate and accept.
- `ASSIGNED` / `IN_PROGRESS`: Student is actively working; submission form is unlocked.
- `SUBMITTED`: Student entered GitHub URL, Demo URL, and summary notes; locked for student until company review.
- `REVISION_REQUESTED`: Company requested specific fixes; student can update submission and resubmit.
- `APPROVED` / `VERIFIED`: Company scored rubric and approved; generates immutable `ReviewRecord` and appends to student's **Digital Experience Passport**.
- `DISPUTED`: If unresolved, escalates to Admin Dashboard for mediation.

---

### Step-by-Step Implementation Sequence

1. **Project Identity & Configuration**:
   - Set app name to `Divnex` in `metadata.json` and `res/values/strings.xml`.
   - Update theme colors to modern high-trust indigo/slate palette.
2. **Data Layer & Room Integration**:
   - Define Room Entities, DAOs, TypeConverters, and `DivnexDatabase`.
   - Implement `DivnexRepository` with rich preloaded seed data (realistic tasks across Android, Frontend, Backend, UI/UX).
3. **ViewModel & State Management**:
   - Implement `DivnexViewModel` with reactive state flows, role switching, task filtering, submission submission, revision requests, and rubric scoring.
4. **Landing & Navigation Framework**:
   - Modern landing page with interactive hero, feature showcase, and role switch bar.
   - Compose navigation or seamless scaffold tab switching between Landing, Student Hub, Company Studio, Admin Portal, and Passport.
5. **Role Screens & Workflows**:
   - **Student Workspace**: Browse, search, filter, apply modal, submission dialog with input validation, and real-time status tracker.
   - **Company Studio**: Task creation form with skill tags, applicant approval list, submission inspector, and review scoring sheet.
   - **Digital Experience Passport Screen**: Shareable card, verified badge stamp, skills radar/list, completed project evidence links, and employer testimonials.
   - **Admin Hub**: Platform health audit, dispute moderation, and backend readiness checklist.
6. **Polishing & Verification**:
   - Empty states, loading indicators, input error validation, and responsive tablet layout support.
   - Run `compile_applet` to verify compilation.
