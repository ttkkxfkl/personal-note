# Personal Note System
个人笔记系统

## 项目介绍
一个功能完整的个人笔记管理系统，参考主流笔记软件的核心功能设计，支持笔记本分类、标签管理、全文搜索、收藏等功能。采用前后端分离架构，界面简洁美观，操作流畅。

## 技术栈
- **后端**: SpringBoot 2.7.x + Spring Data JPA + H2/MySQL
- **前端**: Vue 3 + Element Plus + Vue Router + Pinia
- **构建工具**: Maven + Vite
- **数据库**: H2（开发）/ MySQL（生产）

## 功能特性
- ✅ **笔记管理**: 创建、编辑、删除、查看笔记，支持Markdown
- ✅ **笔记本分类**: 多层级笔记本管理，支持颜色标识
- ✅ **标签系统**: 多标签支持，灵活分类
- ✅ **全文搜索**: 快速搜索笔记内容
- ✅ **收藏功能**: 重要笔记一键收藏
- ✅ **回收站**: 误删保护，支持恢复
- ✅ **响应式设计**: 完美适配桌面和移动设备
- ✅ **暗色主题**: 支持明暗主题切换

## 项目结构
```
personal-note/
├── backend/              # SpringBoot后端
│   ├── src/main/java/
│   │   └── com/personalnote/
│   │       ├── controller/    # 控制器层
│   │       ├── service/       # 服务层  
│   │       ├── repository/    # 数据访问层
│   │       ├── entity/        # 实体类
│   │       ├── dto/           # 数据传输对象
│   │       └── common/        # 通用类
│   └── pom.xml
├── frontend/             # Vue前端
│   ├── src/
│   │   ├── views/            # 页面组件
│   │   ├── components/       # 通用组件
│   │   ├── api/              # API接口
│   │   ├── router/           # 路由配置
│   │   └── assets/           # 静态资源
│   └── package.json
├── start.sh              # 完整启动脚本
├── demo.sh               # 快速演示脚本
└── README.md
```

## 快速开始

### 环境要求
- Java 11+
- Node.js 16+
- Maven 3.6+

### 方式一：快速演示（推荐）
```bash
# 克隆项目后直接运行
./demo.sh
```

### 方式二：分别启动

#### 后端启动
```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### 前端启动  
```bash
cd frontend
npm install
npm run dev
```

## 访问地址
- **前端应用**: http://localhost:3000
- **后端API**: http://localhost:8080/api  
- **API文档**: http://localhost:8080/swagger-ui.html
- **H2控制台**: http://localhost:8080/api/h2-console

## 数据库配置

### 开发环境（默认）
- 使用H2内存数据库
- 无需额外配置
- 数据重启后清空

### 生产环境
1. 创建MySQL数据库：
```sql
CREATE DATABASE personal_note CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改配置文件 `backend/src/main/resources/application.yml`：
```yaml
spring:
  profiles:
    active: prod
  datasource:
    url: jdbc:mysql://localhost:3306/personal_note
    username: your_username  
    password: your_password
```

## API接口

### 笔记本管理
- `GET /api/notebooks` - 获取笔记本列表
- `POST /api/notebooks` - 创建笔记本
- `PUT /api/notebooks/{id}` - 更新笔记本
- `DELETE /api/notebooks/{id}` - 删除笔记本

### 笔记管理
- `GET /api/notes` - 获取笔记列表
- `POST /api/notes` - 创建笔记
- `PUT /api/notes/{id}` - 更新笔记
- `DELETE /api/notes/{id}` - 删除笔记

### 标签管理
- `GET /api/tags` - 获取标签列表
- `POST /api/tags` - 创建标签

## 开发指南

### 后端开发
```bash
cd backend
mvn clean compile      # 编译
mvn test               # 运行测试
mvn spring-boot:run    # 启动应用
```

### 前端开发  
```bash
cd frontend
npm run dev            # 开发模式
npm run build          # 构建生产版本
npm run preview        # 预览构建结果
```

## 部署说明

### Docker部署（推荐）
```bash
# 构建镜像
docker build -t personal-note .

# 运行容器
docker run -p 8080:8080 -p 3000:3000 personal-note
```

### 传统部署
1. 构建前端：`cd frontend && npm run build`
2. 构建后端：`cd backend && mvn clean package`
3. 部署JAR文件到服务器
4. 配置Nginx代理前端静态文件

## 贡献指南
1. Fork项目
2. 创建功能分支：`git checkout -b feature/AmazingFeature`
3. 提交更改：`git commit -m 'Add some AmazingFeature'`
4. 推送分支：`git push origin feature/AmazingFeature`
5. 提交Pull Request

## 许可证
MIT License

## 作者
个人笔记系统开发团队

---

💡 **提示**: 这是一个功能完整的笔记系统，可以直接用于个人笔记管理，也可以作为学习SpringBoot + Vue开发的参考项目。
