# ENAF

This is document is used to clearly define requirements and scope of enaf

### What is the problem the app solves ?

Enaf was realized as a coursework project for the Mobile Application Development
course unit. It came about after realizing that people spend unhealthy amounts of time
on their screens being unproductive(scrolling, unproductive gaming, etc.) which eventually
leads to unachieved goals and even depression.
Enaf aims to solve this problem by being both a digital habit tracker as-well-as a form of
accountability. It does this by borrowing ideas from gamification, social psychology(competition,
accountability) in order to create a framework that naturally motivates the user to have a better
digital lifestyle and help them achieve their goals.

### Target User perspective

1. "I can't stop scrolling"
2. "I can't resist the urge to open instagram reels/tiktok"
3. "I get my news and information from social media"
4. "I'm frustrated by my inability to be more productive"
5. "I feel regret after hours of scrolling"
6. "All my friends scroll too so I need to keep up with the culture"
7. "I have attempted once in a while to fix my digital habits"
8. "I sleep late in the night and wake up tired"

### How app solves these problems

1. Interrupt scrolling after a certain amount of time
2. Give each social app a limited timer
3. Remind users to prioritize useful content
4. Provide in app goals, streaks to motivate productivity
5. Give users a pat on the back each day for any small progress achieved
6. Allow users to budget the time they use the app and show digital health rating for each
7. Give users occasional words of encouragement so they keep motivated and share their progress
with other users.
8. Setting reminders and alarms to keep good habits

### App features that solve nuanced problems

1. Competitive ranking with other users via a credit score system
2. In-app reward system(coins, diamonds) used to motivate users.
3. Gamified shop where users can buy words of advice, unlock themes etc using money collected
4. Display over other apps to interrupt scrolling with relevant encouraging message
5. Use a story like theme make it more of satirical lore where the user(warrior) is the protagonist for
more engagement using funny aliases for social apps(IG, TicToc) and antagonist are tech giants
6. Use achievements and badges displayable on profiles to show off to other users.
7. Roadmap to track progress level
8. Other game aspects like focus familiar, cool animations(future features not in beta)

## Functional Requirements (FR)

- FR-1: App Usage Monitoring: The system shall track the foreground time spent on a user-defined list of "Antagonist Apps" (e.g., TikTok, Instagram).
- FR-2: The "Ambush" Overlay: The system shall trigger a full-screen "Interrupt" overlay when a user exceeds their pre-set time budget for a specific app.
- FR-4: Warrior Dashboard: The system shall provide a central hub displaying the user’s  Stats (Time saved, current streak, and Digital Health Rating).
- FR-5: Credit Score Calculation: The system shall calculate a daily "Digital Credit Score" based on successful habit adherence and failed "ambushes."
- FR-6: Currency Minting: The system shall reward users with "Diamonds" or "Coins" for adhering to their usage plans
- FR-7: The Shop: The system shall allow users to spend earned currency on literature that supports the purpose of the app.
- FR-8: Global/Friend Leaderboard: The system shall rank users based on their Credit Score, categorized by "Warrior Tiers" (e.g., Recruit, Veteran, Legend).
- FR-9: Achievement Engine: The system shall trigger notifications and badges for milestones (e.g., "3-Day Social Media Blackout").
- FR-10: Account: The user shall be able to create an account using email or social media login to access personalized features and the leaderboard.

## Non-Functional Requirements (NFR)

- NFR-1: Battery Optimization: The background tracking service must consume less than 3% of total battery over a 24-hour period.
- NFR-2: Interrupt Latency: The "Ambush" overlay must appear within 500ms of the user exceeding their time limit.
Usability & UX
- NFR-3: Onboarding Friction: A new user should be able to set up their first three tracked apps and their "Warrior Profile" in under 2 minutes.
- NFR-4: Accessibility: The app must follow standard mobile accessibility guidelines (e.g., contrast ratios for the "Satirical Lore" text to ensure readability).
- NFR-5: Data Minimization: The system shall not store the content of user notifications or messages, only the duration of app usage.
- NFR-6: Local Encryption: All personal usage stats stored on the device must be encrypted to prevent unauthorized access by other apps.
- NFR-7: Concurrent Users: The leaderboard backend should support up to 1,000 concurrent beta users without a degradation in response time.

