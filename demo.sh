#!/bin/bash

echo "🚀 启动个人笔记系统演示..."

# 检查是否在项目根目录
if [ ! -f "README.md" ]; then
    echo "❌ 请在项目根目录运行此脚本"
    exit 1
fi

echo "📋 系统检查通过"

# 启动后端服务
echo "🔧 启动后端服务（开发环境）..."
cd backend
echo "正在启动SpringBoot应用..."
mvn spring-boot:run -Dspring-boot.run.profiles=dev -q &
BACKEND_PID=$!

# 等待后端启动
echo "⏳ 等待后端服务启动..."
sleep 10

# 启动前端服务
echo "🎨 启动前端服务..."
cd ../frontend
echo "正在启动Vue开发服务器..."
npm run dev &
FRONTEND_PID=$!

echo ""
echo "🎉 个人笔记系统启动成功！"
echo ""
echo "📝 访问地址："
echo "   前端应用: http://localhost:3000"
echo "   后端API:  http://localhost:8080/api"
echo "   H2控制台: http://localhost:8080/api/h2-console"
echo ""
echo "💡 系统特性："
echo "   ✅ 笔记本分类管理"
echo "   ✅ 标签系统"
echo "   ✅ 搜索功能"
echo "   ✅ 响应式UI设计"
echo "   ✅ H2内存数据库（演示用）"
echo ""
echo "🛑 停止服务: 按 Ctrl+C"

# 捕获中断信号
trap 'echo ""; echo "🛑 正在停止服务..."; kill $BACKEND_PID $FRONTEND_PID 2>/dev/null; echo "✅ 服务已停止"; exit 0' INT

# 保持脚本运行
wait