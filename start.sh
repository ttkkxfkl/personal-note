#!/bin/bash

# 个人笔记系统启动脚本

echo "🚀 正在启动个人笔记系统..."

# 检查是否存在必要的命令
check_command() {
    if ! command -v $1 &> /dev/null; then
        echo "❌ 错误: $1 未安装，请先安装 $1"
        exit 1
    fi
}

# 检查必要的依赖
echo "📋 检查系统依赖..."
check_command "java"
check_command "mvn"
check_command "npm"

# 启动后端服务
echo "🔧 启动后端服务..."
cd backend
echo "正在编译后端项目..."
mvn clean compile -q
if [ $? -eq 0 ]; then
    echo "✅ 后端编译成功"
    echo "正在启动后端服务（端口：8080）..."
    mvn spring-boot:run -Dspring-boot.run.profiles=dev &
    BACKEND_PID=$!
    echo "后端服务进程ID: $BACKEND_PID"
else
    echo "❌ 后端编译失败"
    exit 1
fi

# 等待后端启动
sleep 5

# 启动前端服务
echo "🎨 启动前端服务..."
cd ../frontend
echo "正在安装前端依赖..."
npm install --silent
if [ $? -eq 0 ]; then
    echo "✅ 前端依赖安装成功"
    echo "正在启动前端服务（端口：3000）..."
    npm run dev &
    FRONTEND_PID=$!
    echo "前端服务进程ID: $FRONTEND_PID"
else
    echo "❌ 前端依赖安装失败"
    kill $BACKEND_PID
    exit 1
fi

echo ""
echo "🎉 个人笔记系统启动成功！"
echo ""
echo "📝 服务地址："
echo "   前端访问地址: http://localhost:3000"
echo "   后端API地址:  http://localhost:8080/api"
echo "   API文档地址:  http://localhost:8080/swagger-ui.html"
echo ""
echo "💡 使用说明："
echo "   - 前端：Vue 3 + Element Plus + Vite"
echo "   - 后端：SpringBoot + JPA + H2(开发环境)"
echo "   - 数据库：开发环境使用H2内存数据库，无需额外配置"
echo ""
echo "🛑 停止服务："
echo "   按 Ctrl+C 停止服务"

# 等待用户中断
trap "echo ''; echo '🛑 正在停止服务...'; kill $BACKEND_PID $FRONTEND_PID; echo '✅ 服务已停止'; exit 0" INT

# 保持脚本运行
wait