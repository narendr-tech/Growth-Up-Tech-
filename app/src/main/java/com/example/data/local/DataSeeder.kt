package com.example.data.local

import com.example.data.local.dao.AppDao
import com.example.data.local.entity.BlogEntity
import com.example.data.local.entity.CouponEntity
import com.example.data.local.entity.CourseEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.first

object DataSeeder {

    suspend fun seedInitialData(appDao: AppDao) {
        val existingCourses = appDao.getAllCourses().first()
        if (existingCourses.isNotEmpty()) return

        // 1. Seed Courses
        val courses = listOf(
            CourseEntity(
                id = 1,
                title = "Complete Modern Full-Stack Web Development 2026",
                category = "Web Development",
                description = "Master HTML5, CSS3, Modern ES2026 JavaScript, Node.js, Express, MongoDB, and RESTful APIs from scratch with real production projects.",
                price = 3999.0,
                discountPrice = 999.0,
                duration = "32 Hours",
                difficulty = "Beginner to Advanced",
                lessonsCount = 10,
                instructorName = "Vikram Sharma",
                instructorRole = "Staff Software Engineer & Tech Educator",
                rating = 4.9,
                reviewsCount = 428,
                whatYouWillLearn = "Build full-stack modern web applications;Master modern asynchronous JavaScript and Promises;Design and implement REST APIs with Express and Node.js;Connect and model databases with MongoDB and PostgreSQL;Deploy applications to production with CI/CD and Docker",
                requirements = "Basic computer knowledge and a text editor (VS Code recommended);No prior coding experience required - starts from fundamentals",
                curriculumJson = """
                    [
                        {"title": "1. Modern Web Architecture & Tools", "duration": "45 min", "previewCode": "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n  <meta charset=\"UTF-8\" />\n  <title>Growth Up Tech</title>\n</head>\n<body>\n  <h1>Welcome to Full-Stack</h1>\n</body>\n</html>"},
                        {"title": "2. HTML5 Semantic Elements & Accessibility", "duration": "50 min", "previewCode": "<main role=\"main\">\n  <article class=\"post\">\n    <header><h2>Semantic Web</h2></header>\n    <p>Accessible and SEO friendly structure.</p>\n  </article>\n</main>"},
                        {"title": "3. Advanced CSS3 Grid & Flexbox Systems", "duration": "65 min", "previewCode": ".container {\n  display: grid;\n  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));\n  gap: 1.5rem;\n}"},
                        {"title": "4. Modern JavaScript Fundamentals & ESNext", "duration": "80 min", "previewCode": "const fetchUser = async (id) => {\n  const res = await fetch('/api/users/' + id);\n  const { name, role } = await res.json();\n  return { name, role };\n};"},
                        {"title": "5. Asynchronous JS, Promises & Event Loop", "duration": "70 min", "previewCode": "const delay = (ms) => new Promise(res => setTimeout(res, ms));\nawait delay(1000);"},
                        {"title": "6. Node.js Architecture & Express Server Setup", "duration": "90 min", "previewCode": "import express from 'express';\nconst app = express();\napp.use(express.json());\napp.get('/api/health', (req, res) => res.json({ status: 'healthy' }));"},
                        {"title": "7. RESTful API Design & Middleware Protection", "duration": "85 min", "previewCode": "export const authMiddleware = (req, res, next) => {\n  const token = req.headers.authorization;\n  if (!token) return res.status(401).json({ error: 'Unauthorized' });\n  next();\n};"},
                        {"title": "8. Database Schema Design with MongoDB / PostgreSQL", "duration": "95 min", "previewCode": "const UserSchema = new Schema({\n  email: { type: String, required: true, unique: true },\n  createdAt: { type: Date, default: Date.now }\n});"},
                        {"title": "9. Authentication, JWT Tokens & Password Hashing", "duration": "100 min", "previewCode": "const hash = await bcrypt.hash(password, 12);\nconst token = jwt.sign({ id: user._id }, SECRET, { expiresIn: '7d' });"},
                        {"title": "10. Production Deployment, Docker & Monitoring", "duration": "75 min", "previewCode": "FROM node:20-alpine\nWORKDIR /app\nCOPY package*.json ./\nRUN npm ci --only=production\nCMD [\"node\", \"server.js\"]"}
                    ]
                """.trimIndent(),
                isFeatured = true,
                enrolled = true,
                completedLessons = "0,1",
                certificateIssued = false
            ),
            CourseEntity(
                id = 2,
                title = "Advanced JavaScript & TypeScript Mastery",
                category = "JavaScript",
                description = "Deep dive into JS internals: execution contexts, closures, prototypes, event loop, memory management, and enterprise TypeScript typing.",
                price = 2999.0,
                discountPrice = 799.0,
                duration = "22 Hours",
                difficulty = "Intermediate to Advanced",
                lessonsCount = 8,
                instructorName = "Priya Nambiar",
                instructorRole = "Principal Frontend Architect",
                rating = 4.95,
                reviewsCount = 312,
                whatYouWillLearn = "Deep understanding of the V8 JavaScript engine and call stack;Master Closures, Currying, and Functional Programming;TypeScript Generics, Utility Types, and Strict Discriminated Unions;Write clean, maintainable, production-ready asynchronous code",
                requirements = "Basic understanding of JavaScript variables, loops, and functions",
                curriculumJson = """
                    [
                        {"title": "1. Execution Context & Call Stack Deep Dive", "duration": "45 min", "previewCode": "console.log(a); var a = 10; // Hoisting internals"},
                        {"title": "2. Scope Chain, Lexical Environments & Closures", "duration": "60 min", "previewCode": "function counter() {\n  let count = 0;\n  return () => ++count;\n}"},
                        {"title": "3. Prototypes, Prototypal Inheritance & Classes", "duration": "55 min", "previewCode": "Object.setPrototypeOf(child, parent);"},
                        {"title": "4. The V8 Event Loop, Microtasks & Macrotasks", "duration": "70 min", "previewCode": "queueMicrotask(() => console.log('Microtask'));"},
                        {"title": "5. TypeScript Essentials: Types vs Interfaces", "duration": "65 min", "previewCode": "type Result<T> = { success: true; data: T } | { success: false; error: string };"},
                        {"title": "6. Advanced Generics & Conditional Types", "duration": "80 min", "previewCode": "type NonNullable<T> = T extends null | undefined ? never : T;"},
                        {"title": "7. Design Patterns in Modern TypeScript", "duration": "75 min", "previewCode": "class Singleton { private static instance: Singleton; }"},
                        {"title": "8. Building an Open-Source Utility Library", "duration": "90 min", "previewCode": "export function debounce<T extends (...args: any[]) => any>(fn: T, delay: number) {}"}
                    ]
                """.trimIndent(),
                isFeatured = true,
                enrolled = false
            ),
            CourseEntity(
                id = 3,
                title = "Backend Engineering with Node.js & Microservices",
                category = "Backend",
                description = "Build resilient backend architectures. Cover caching with Redis, message queues with RabbitMQ, database transactions, and rate limiting.",
                price = 4499.0,
                discountPrice = 1299.0,
                duration = "26 Hours",
                difficulty = "Intermediate",
                lessonsCount = 8,
                instructorName = "Arjun Mehta",
                instructorRole = "Lead Infrastructure Engineer",
                rating = 4.88,
                reviewsCount = 210,
                whatYouWillLearn = "Design distributed microservice architectures;Implement Redis caching layers for 10x throughput;Handle distributed transactions and idempotent APIs;Set up API Gateway, rate limiting, and observability",
                requirements = "Intermediate JavaScript and familiarity with basic REST APIs",
                curriculumJson = """
                    [
                        {"title": "1. Monolith to Microservices Strategy", "duration": "40 min", "previewCode": "// Gateway routing"},
                        {"title": "2. High Performance Caching with Redis", "duration": "65 min", "previewCode": "await redisClient.setEx('user:' + id, 3600, JSON.stringify(userData));"},
                        {"title": "3. Message Queues & Event-Driven Architecture", "duration": "80 min", "previewCode": "channel.sendToQueue('order_created', Buffer.from(JSON.stringify(order)));"},
                        {"title": "4. Relational Database Transactions & Locks", "duration": "70 min", "previewCode": "await db.transaction(async trx => { ... });"},
                        {"title": "5. Zero Trust Security & API Gateways", "duration": "60 min", "previewCode": "app.use(rateLimit({ windowMs: 15 * 60 * 1000, max: 100 }));"},
                        {"title": "6. Logging, Metrics & Prometheus Monitoring", "duration": "55 min", "previewCode": "const httpRequestDurationMicroseconds = new Prometheus.Histogram();"},
                        {"title": "7. Microservice CI/CD & Blue-Green Deploys", "duration": "65 min", "previewCode": "kubectl rollout restart deployment/order-service"},
                        {"title": "8. Production Incident Drill & Chaos Engineering", "duration": "75 min", "previewCode": "// Resilience testing"}
                    ]
                """.trimIndent(),
                isFeatured = true,
                enrolled = false
            ),
            CourseEntity(
                id = 4,
                title = "HTML5 & CSS3 Masterclass: Pixel-Perfect UI",
                category = "HTML",
                description = "Become a layout wizard. Master modern CSS Grid, Flexbox, responsive typography, custom properties, animations, and clean semantic markup.",
                price = 1999.0,
                discountPrice = 499.0,
                duration = "14 Hours",
                difficulty = "Beginner",
                lessonsCount = 6,
                instructorName = "Sneha Patel",
                instructorRole = "Design Systems Lead",
                rating = 4.92,
                reviewsCount = 540,
                whatYouWillLearn = "Semantic HTML5 structure for accessibility and SEO;Deep mastery of Flexbox and CSS Grid layout techniques;Responsive design without relying on bloated frameworks;CSS animations, transitions, and micro-interactions",
                requirements = "No prior coding experience required",
                curriculumJson = """
                    [
                        {"title": "1. Semantic HTML5 & Modern Document Structure", "duration": "40 min", "previewCode": "<article><header><h2>CSS Master</h2></header></article>"},
                        {"title": "2. The Box Model, Specificity & Cascade", "duration": "45 min", "previewCode": "* { box-sizing: border-box; margin: 0; }"},
                        {"title": "3. Flexbox Layouts for Components", "duration": "60 min", "previewCode": ".navbar { display: flex; justify-content: space-between; align-items: center; }"},
                        {"title": "4. CSS Grid for Full-Page Layouts", "duration": "75 min", "previewCode": ".dashboard { display: grid; grid-template-columns: 240px 1fr; }"},
                        {"title": "5. CSS Custom Properties & Dynamic Theming", "duration": "50 min", "previewCode": ":root { --primary-accent: #00D2FF; }"},
                        {"title": "6. High-Performance CSS Animations", "duration": "55 min", "previewCode": "@keyframes pulse { 0% { transform: scale(1); } 50% { transform: scale(1.05); } }"}
                    ]
                """.trimIndent(),
                isFeatured = false,
                enrolled = false
            )
        )
        appDao.insertCourses(courses)

        // 2. Seed Digital Products
        val products = listOf(
            ProductEntity(
                id = 1,
                title = "SaaS Launchpad - Modern Fullstack Web Template",
                category = "Website templates",
                description = "Complete production-ready SaaS starter code with authentication, billing gateway integration, dark/light theme, user dashboard, and admin analytics.",
                price = 2499.0,
                discountPrice = 799.0,
                format = "ZIP",
                fileSize = "42.5 MB",
                features = "React 19 & Next.js 15 App Router;Tailwind CSS v4 & Lucide Icons;Authentication (JWT & OAuth ready);Mock Payment Gateway Checkout;Responsive Admin Dashboard;Fully commented codebase",
                previewCode = """
                    // SaaS Launchpad Quick Config
                    export const siteConfig = {
                      name: "Growth Up SaaS",
                      plans: [
                        { name: "Starter", price: 499, interval: "month" },
                        { name: "Pro", price: 1499, interval: "month" }
                      ]
                    };
                """.trimIndent(),
                isFeatured = true,
                purchased = true,
                downloadCount = 1420
            ),
            ProductEntity(
                id = 2,
                title = "100+ JavaScript Real-World Project Source Codes",
                category = "JavaScript projects",
                description = "Comprehensive bundle of 100 practical JavaScript projects ranging from calculators, games, weather apps, to API dashboards with clean code and explanations.",
                price = 1499.0,
                discountPrice = 499.0,
                format = "ZIP",
                fileSize = "68.2 MB",
                features = "100 complete standalone projects;Pure Vanilla JS, HTML5, Modern CSS;Async/Await API project examples;Clean beginner-friendly directory structure;Step-by-step README for each project",
                previewCode = """
                    // Project #42: Real-time Weather Fetcher
                    async function fetchWeather(city) {
                      const res = await fetch('https://api.example.com/weather?city=' + city);
                      return await res.json();
                    }
                """.trimIndent(),
                isFeatured = true,
                purchased = false,
                downloadCount = 2890
            ),
            ProductEntity(
                id = 3,
                title = "TechStartup Cyberpunk & Dark UI Kit (Figma + CSS)",
                category = "UI kits",
                description = "Over 180+ sleek, high-tech UI components designed in Figma with matching CSS/Tailwind utility classes. Perfect for crypto, SaaS, AI, and developer tools.",
                price = 1299.0,
                discountPrice = 399.0,
                format = "FIGMA + ZIP",
                fileSize = "32.0 MB",
                features = "180+ responsive UI components;Auto-layout v5 with tokenized variables;Light and Dark mode styles;Matching Tailwind HTML snippets;Commercial usage license included",
                previewCode = """
                    <div class="tech-card bg-slate-900 border border-cyan-500/30 rounded-xl p-6 shadow-cyan-500/10">
                      <span class="badge bg-emerald-500/20 text-emerald-400">ACTIVE</span>
                      <h3 class="text-xl font-bold text-white mt-2">Cyber UI Token</h3>
                    </div>
                """.trimIndent(),
                isFeatured = true,
                purchased = false,
                downloadCount = 890
            ),
            ProductEntity(
                id = 4,
                title = "The Full-Stack & System Design Hand-Written Study Notes",
                category = "Coding notes",
                description = "240+ pages of high-yield hand-written diagrams, cheat sheets, and algorithmic patterns covering Frontend, Backend, Databases, and System Design.",
                price = 699.0,
                discountPrice = 199.0,
                format = "PDF",
                fileSize = "18.4 MB",
                features = "240+ illustrated visual PDF pages;Covers Data Structures & Algorithms;System Design diagrams (Load balancing, Caching, Sharding);SQL vs NoSQL trade-offs;Interview cheat sheets",
                previewCode = """
                    System Design Architecture Note:
                    [Client] -> [Cloudflare CDN] -> [Load Balancer (Nginx)]
                                                      |--> [App Server 1] -> [Redis Cache]
                                                      |--> [App Server 2] -> [PostgreSQL DB]
                """.trimIndent(),
                isFeatured = false,
                purchased = false,
                downloadCount = 3510
            ),
            ProductEntity(
                id = 5,
                title = "Production Ready Animated HTML/CSS Components Pack",
                category = "Website components",
                description = "45+ copy-paste interactive UI components including glassmorphic navigation bars, glowing gradient buttons, animated pricing cards, and interactive accordions.",
                price = 799.0,
                discountPrice = 249.0,
                format = "JSON + ZIP",
                fileSize = "8.6 MB",
                features = "45+ copy-paste modular components;Zero external dependencies (pure CSS);Cross-browser tested;Includes accessible ARIA labels;Dark mode optimized",
                previewCode = """
                    .glow-btn {
                      background: linear-gradient(135deg, #00D2FF 0%, #10B981 100%);
                      box-shadow: 0 0 20px rgba(0, 210, 255, 0.4);
                      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
                    }
                """.trimIndent(),
                isFeatured = false,
                purchased = false,
                downloadCount = 1120
            )
        )
        appDao.insertProducts(products)

        // 3. Seed Coupons
        val coupons = listOf(
            CouponEntity("GROWTH50", discountPercentage = 50, minPurchase = 499.0, expiryDate = "31 Dec 2026", description = "50% OFF on orders over ₹499"),
            CouponEntity("WELCOME20", discountPercentage = 20, minPurchase = 0.0, expiryDate = "31 Dec 2026", description = "20% OFF for new learners"),
            CouponEntity("CODING100", flatDiscount = 100.0, minPurchase = 299.0, expiryDate = "31 Dec 2026", description = "₹100 Flat discount"),
            CouponEntity("TECHFREE", discountPercentage = 100, minPurchase = 0.0, expiryDate = "31 Dec 2026", description = "Demo 100% Free pass")
        )
        appDao.insertCoupons(coupons)

        // 4. Seed User Profile
        val initialUser = UserEntity(
            id = 1,
            fullName = "Narendra Masram",
            email = "narendramasram50k@gmail.com",
            headline = "Aspiring Full-Stack Software Engineer",
            bio = "Passionate about learning modern web development, creating impactful digital products, and building high-performance tech systems.",
            githubUrl = "https://github.com/growthuptech",
            isLoggedIn = true
        )
        appDao.insertOrUpdateUser(initialUser)

        // 5. Seed Blogs
        val blogs = listOf(
            BlogEntity(
                id = 1,
                title = "Mastering Modern CSS Grid and Subgrid in 2026",
                category = "CSS",
                readTime = "6 min read",
                date = "Sept 2026",
                author = "Growth Up Tech Team",
                summary = "Learn how CSS Subgrid simplifies card alignment, fluid multi-column dashboards, and nested layout hierarchies without extra markup.",
                content = """
                    CSS Grid has evolved dramatically. With universal browser support for subgrid, nesting complex grids while preserving parent column alignments has never been easier.
                    
                    ### Why Subgrid Matters
                    In classic CSS Grid, child elements could not inherit the column tracks of their ancestors. With `grid-template-rows: subgrid;`, cards in different columns automatically align their headings, descriptions, and footer buttons regardless of varying text lengths.
                    
                    ```css
                    .card-grid {
                      display: grid;
                      grid-template-columns: repeat(3, 1fr);
                      gap: 2rem;
                    }
                    .card {
                      grid-row: span 3;
                      display: grid;
                      grid-template-rows: subgrid;
                    }
                    ```
                    
                    Stay disciplined with your layout variables and use subgrid for predictable, bulletproof card grids.
                """.trimIndent(),
                tags = "CSS,Web Development,Frontend"
            ),
            BlogEntity(
                id = 2,
                title = "Node.js Performance Optimization & Asynchronous Queues",
                category = "Backend",
                readTime = "8 min read",
                date = "Sept 2026",
                author = "Arjun Mehta",
                summary = "Preventing event loop blockages, managing CPU-bound tasks with worker threads, and scaling Node.js applications under heavy traffic.",
                content = """
                    Because Node.js runs on a single-threaded event loop, long-running computational tasks will block every incoming HTTP request.
                    
                    ### Key Best Practices:
                    1. **Never block the event loop**: Avoid sync file operations or heavy JSON parsing on the main thread.
                    2. **Offload compute tasks**: Use Node worker threads or an external worker queue (like BullMQ + Redis).
                    3. **Cluster mode**: Utilize all available CPU cores using PM2 or Docker container replicas.
                    
                    ```javascript
                    import { Worker } from 'worker_threads';
                    
                    function runHeavyTask(workerData) {
                      return new Promise((resolve, reject) => {
                        const worker = new Worker('./worker.js', { workerData });
                        worker.on('message', resolve);
                        worker.on('error', reject);
                      });
                    }
                    ```
                """.trimIndent(),
                tags = "Node.js,Backend,Architecture"
            ),
            BlogEntity(
                id = 3,
                title = "The Ethical Developer: Building Real Digital Businesses without Gimmicks",
                category = "Career guidance",
                readTime = "5 min read",
                date = "Aug 2026",
                author = "Vikram Sharma",
                summary = "Why genuine technical craftsmanship, valuable open-source tools, and transparent pricing always outcompete get-rich-quick claims.",
                content = """
                    At Growth Up Tech, our philosophy is anchored in our motto: **Learn. Build. Grow.**
                    
                    We firmly reject get-rich-quick promises or false earning claims. True career independence and digital success come from:
                    - Deep foundational coding skills
                    - Developing practical, reusable software that solves real user pain
                    - Continuous learning and ethical transparency
                    
                    When you focus on creating genuine value—whether through clean code templates, well-crafted learning materials, or reliable web services—growth follows naturally.
                """.trimIndent(),
                tags = "Career,Ethics,Growth"
            )
        )
        appDao.insertBlogs(blogs)
    }
}
